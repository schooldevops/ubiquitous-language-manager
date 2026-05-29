# Term API 구현

## 1. 목적

OpenAPI Spec으로 생성한 Kotlin Spring server stub을 기준으로 표준 용어 관리 API를 구현한다.

## 2. OpenAPI Generator 결과

| 항목 | 값 |
|---|---|
| Generator | `kotlin-spring` |
| 입력 Spec | `openapi/openapi.yaml` |
| 출력 위치 | `generated/backend` |
| basePackage | `com.aulms` |
| apiPackage | `com.aulms.api` |
| modelPackage | `com.aulms.model` |
| 생성 방식 | interfaceOnly |

생성 명령은 `scripts/openapi-generate-backend.sh`에 기록했다.

## 3. Backend 구현 위치

| 파일 | 설명 |
|---|---|
| `apps/backend/build.gradle.kts` | Spring Boot Kotlin 빌드 설정 |
| `apps/backend/settings.gradle.kts` | Backend Gradle 설정 |
| `apps/backend/src/main/kotlin/com/aulms/AulmsBackendApplication.kt` | Spring Boot 애플리케이션 진입점 |
| `apps/backend/src/main/kotlin/com/aulms/term/TermApiController.kt` | 생성 API interface 구현 |
| `apps/backend/src/main/kotlin/com/aulms/term/TermService.kt` | Term API application service |
| `apps/backend/src/main/kotlin/com/aulms/term/TermRepository.kt` | MVP in-memory repository |
| `apps/backend/src/main/kotlin/com/aulms/term/ApiExceptionHandler.kt` | 공통 ErrorResponse 변환 |
| `apps/backend/src/test/kotlin/com/aulms/term/TermApiContractTest.kt` | API contract test |

## 4. 구현한 API

- `GET /terms`
- `POST /terms`
- `GET /terms/{termId}`
- `PUT /terms/{termId}`
- `POST /terms/{termId}/approve`
- `POST /terms/{termId}/deprecate`
- `GET /terms/{termId}/history`
- `GET /terms/{termId}/expressions`
- `POST /terms/{termId}/expressions`
- `GET /terms/{termId}/aliases`
- `POST /terms/{termId}/aliases`

## 5. 저장소 전략

현재 구현은 MVP contract test를 위한 in-memory repository를 사용한다.

이후 DB 연동 단계에서는 다음 교체를 진행한다.

- `TermRepository`를 QueryDSL SQL 기반 repository로 교체한다.
- 기존 API controller와 service method signature는 유지한다.
- Flyway migration의 `term_*` 테이블을 실제 저장소로 사용한다.

## 6. 검증 결과

```text
gradle test
BUILD SUCCESSFUL
```

검증된 케이스:

- 고객번호 상세 조회
- 고객ID/customerId 별칭 기반 목록 검색
- 신규 용어 등록
- 신규 용어 승인

