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
실무에서 사용해본 경험은 없지만 다음 이점이 유용해 보여 적용해봄:
- SQL 마이그레이션을 버전 관리할 수 있음
- 변경 이력이 히스토리 테이블(`flyway_schema_history`)로 남아 언제 무엇이 바뀌었는지 추적 가능
- `application.yml`에 설정값을 반영해두면 서버 시작 시 자동으로 마이그레이션이 적용됨
- 체크섬을 비교해 이미 적용된(변경 없는) 마이그레이션은 다시 실행하지 않음

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
