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
