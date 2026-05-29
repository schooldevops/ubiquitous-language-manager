# 로컬 개발 환경 가이드

## 1. 필요 도구

- JDK 21
- Kotlin 지원 IDE
- Node.js LTS
- pnpm 또는 npm
- Docker
- PostgreSQL
- OpenAPI Generator CLI

## 2. 로컬 서비스

| 서비스 | 기본 포트 | 설명 |
|---|---:|---|
| Backend | 8080 | Spring Boot API 서버 |
| Frontend | 3000 | Next.js 개발 서버 |
| PostgreSQL | 5432 | 로컬 데이터베이스 |

## 3. 권장 명령

아직 프로젝트 scaffold 전 단계이므로 실제 명령은 Phase 1에서 확정한다. 현재 기준 명령은 다음과 같이 예약한다.

```bash
# OpenAPI lint
npm run openapi:lint

# Backend server stub 생성
npm run generate:backend

# Frontend axios client 생성
npm run generate:frontend

# Backend 테스트
./gradlew test

# Frontend 테스트
npm run test

# Frontend 개발 서버
npm run dev
```

## 4. 개발 순서

1. `rules.md`를 확인한다.
2. 관련 task 파일을 확인한다.
3. 관련 데이터 딕셔너리 문서를 확인하거나 작성한다.
4. OpenAPI Spec을 작성한다.
5. OpenAPI Generator로 Backend server stub과 Frontend client를 생성한다.
6. Backend를 구현한다.
7. Frontend를 구현한다.
8. 테스트와 contract 검증을 수행한다.
9. 변경 내용을 사용자에게 확인받고 다음 단계로 진행한다.

## 5. 아직 확정이 필요한 항목

- 패키지 매니저: npm 또는 pnpm
- Spring Boot 세부 버전
- Gradle Kotlin DSL 구성
- PostgreSQL 로컬 실행 방식
- OpenAPI lint 도구
- CI 도구

