# 플로우 SaaS 부문 개발자 과제 규칙

## 기술 스택

### 백엔드
- Java 25
- Spring Boot 4.1.0
- Spring Data JPA
- Gradle (Kotlin DSL)

### 프론트엔드
- React 19
- TypeScript
- Vite 8
- pnpm

### DB
- PostgreSQL 16
- Flyway (SQL 마이그레이션)

## 설계 결정

### Flyway 도입
- SQL 마이그레이션 버전 관리
- 변경 이력을 `flyway_schema_history`로 추적
- 서버 시작 시 자동 적용, 체크섬으로 재실행 방지

### 파일 확장자 차단 API 트레이드오프

**1. 이름 형식 검증**
- 배경: 커스텀 확장자는 영문 소문자+숫자만 허용, 숫자만은 불가
- 과정: Postgres 정규식 CHECK로 구현 → ANSI SQL 이식성 문제 제기
- 결정: DB CHECK 제거, 앱 계층 `@Pattern`으로 이동
- 효과: DB 종속성 제거. 단, 앱이 유일한 쓰기 경로라는 전제 필요

**2. 대소문자 처리**
- 배경: "EXE"/"Exe" 등 대소문자 무관하게 실행될 위험
- 과정: 입력 거부 vs 정규화 저장 검토
- 결정: 거부 대신 소문자로 정규화 후 저장
- 효과: 입력 원문과 저장값이 달라질 수 있으나 보안 취지는 충족

**3. 테이블 구조**
- 배경: 고정/커스텀 간 대소문자 무시 중복을 백엔드+DB 이중 방어 필요
- 과정: 단일 테이블 통합 vs 2테이블 유지+서비스 체크 검토
- 결정: 2테이블 유지, 교차 중복은 서비스 레이어에서만 체크
- 효과: DB가 교차 중복은 못 막지만 규모(고정 7개/커스텀 최대 200개)상 허용

**4. 커스텀 확장자 200개 제한**
- 배경: 커스텀 확장자 최대 200개 등록 제한. 사용자가 1명이라는 전제가 없어 동시 요청 가능성 고려 필요
- 과정: count 체크만으로는 두 요청이 동시에 199개를 보고 둘 다 등록해 201개가 되는 race condition 발생 가능
- 결정: Postgres advisory lock(`pg_advisory_xact_lock`)으로 count 체크~등록 구간을 트랜잭션 단위 직렬화
- 효과: 동시 요청에도 200개 제한이 정확히 지켜짐, 스키마 변경이나 신규 의존성 없이 해결

**5. 고정 확장자 체크 상태 변경**
- 배경: 다중 사용자 동시 접근 시 멱등성/동시성 우려 제기
- 과정: PUT vs PATCH, 락 필요 여부 검토
- 결정: PATCH(최종 상태값 전달) + 락 없음
- 효과: 덮어쓰기 연산이라 동시 요청에도 값이 깨지지 않음, last-write-wins로 충분

**6. 에러 코드/예외 구조**
- 배경: 공통 에러와 도메인 에러를 분리한 확장 가능 구조 요청
- 과정: `BusinessException`을 인터페이스로 시도 → `@ExceptionHandler`가 `Class<? extends Throwable>`만 허용해 컴파일 불가 확인
- 결정: `ErrorCode`는 인터페이스로 공통/도메인(`CommonErrorCode`/`ExtensionErrorCode`) 분리, `BusinessException`은 추상 클래스로 유지
- 효과: 도메인 확장 가능하면서 Spring 예외 처리와 호환

**7. 테스트 의존성**
- 배경: DB 관련 검증을 위한 테스트 환경 필요
- 과정: Testcontainers 도입 여부 검토
- 결정: 미도입, 기존 로컬 Postgres 재사용
- 효과: 불필요한 의존성 추가 없이 검증 가능

## 작업 방식
- 사전 승인 후 작업 진행
- DB 데이터/테이블 임의 변경 금지
- 불필요한 의존성 추가 금지
- 민감 정보 파일(`.env`, `local.yml` 등) git 유출 금지
- 주석은 JavaDoc으로 정리
- 순서: 플랜 제시 → 승인 확인 → 구현 → 테스트 코드 작성(테스트 방식 참고) → 빌드 확인 → 컴파일/런타임 에러 확인 후 전달

## 테스트 방식
- 로컬 Postgres(`docker compose up -d`) 재사용, Testcontainers 미도입
- `@SpringBootTest` + `MockMvc`로 API 계층 포함 검증
- 카테고리: 정상값 / 경계값 / 벨리데이션 오류값 / SQL Injection / XSS

## 개발 명령어

```bash
# 백엔드 (backend/)
./gradlew build     # 빌드
./gradlew bootRun   # 애플리케이션 실행
./gradlew test      # 테스트 실행

# 프론트엔드 (frontend/)
pnpm install         # 의존성 설치
pnpm dev             # 개발 서버 실행
pnpm build           # 빌드

# 로컬 DB
docker compose up -d # 로컬 Postgres 기동 (docker-compose.yml)
```
