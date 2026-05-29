# Task 03 완료 결과: Term API 구현

## 수행 일자

2026-05-27

## 확인한 규칙

`rules.md`를 확인했고 다음 기준을 반영했다.

- 서버 API는 OpenAPI Spec을 먼저 작성한 뒤 구현한다.
- OpenAPI Generator로 Spring Boot Kotlin server stub을 생성한다.
- 구현은 생성된 API interface와 model 타입을 기준으로 진행한다.
- 데이터 딕셔너리 표준 표현을 기준으로 API 필드명을 사용한다.
- 단계 완료 후 사용자 확인과 승인을 받는다.

## 생성 및 변경한 산출물

| 산출물 | 파일 또는 경로 |
|---|---|
| Backend server stub | `generated/backend` |
| Backend Gradle 설정 | `apps/backend/build.gradle.kts` |
| Backend 애플리케이션 진입점 | `apps/backend/src/main/kotlin/com/aulms/AulmsBackendApplication.kt` |
| Term API Controller | `apps/backend/src/main/kotlin/com/aulms/term/TermApiController.kt` |
| Term Service | `apps/backend/src/main/kotlin/com/aulms/term/TermService.kt` |
| MVP Repository | `apps/backend/src/main/kotlin/com/aulms/term/TermRepository.kt` |
| API Exception Handler | `apps/backend/src/main/kotlin/com/aulms/term/ApiExceptionHandler.kt` |
| Contract Test | `apps/backend/src/test/kotlin/com/aulms/term/TermApiContractTest.kt` |
| OpenAPI backend generation script | `scripts/openapi-generate-backend.sh` |
| 구현 설명 문서 | `docs/api/term-api-implementation.md` |

## 구현한 API

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

## 검증 결과

```text
gradle test
BUILD SUCCESSFUL
```

## 완료 기준 충족 여부

| 완료 기준 | 상태 |
|---|---|
| OpenAPI Spec에 정의된 Term API가 모두 동작한다 | 충족 |
| Spec과 다른 응답 필드가 발생하지 않는다 | contract test 기준 충족 |
| 고객번호 용어를 등록, 조회, 승인, 폐기할 수 있다 | 등록, 조회, 승인 충족. 폐기 API 구현 완료 |

## 비고

- 현재 저장소는 MVP 검증용 in-memory repository다.
- DB migration은 이미 준비되어 있으며, 이후 QueryDSL SQL 기반 repository로 교체한다.
- OpenAPI Generator 실행 중 최초 validate는 generator jar 다운로드 문제로 실패했으나, 로컬 선택 버전 설정 후 validate와 generation은 성공했다.

## 다음 단계

사용자 승인 후 `mvp1-tasks/04-task-admin-term-screens.md`를 진행한다.

