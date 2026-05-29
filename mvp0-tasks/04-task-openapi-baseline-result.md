# Task 04 완료 결과: OpenAPI Baseline 작성

## 수행 일자

2026-05-27

## 확인한 규칙

`rules.md`를 확인했고 다음 기준을 반영했다.

- 항상 OpenAPI Spec을 생성한다.
- Backend는 OpenAPI Generator로 Spring Boot Kotlin server stub을 생성한 뒤 구현한다.
- Frontend는 OpenAPI Generator로 axios TypeScript client를 생성한 뒤 구현한다.
- 개발 대상 용어와 API 필드는 데이터 딕셔너리 표준 표현을 따른다.
- 단계 완료 후 사용자 확인과 승인을 받는다.

## 생성한 산출물

| 산출물 | 파일 |
|---|---|
| OpenAPI 루트 Spec | `openapi/openapi.yaml` |
| 공통 schema | `openapi/schemas/common.yaml` |
| enum schema | `openapi/schemas/enums.yaml` |
| OpenAPI Baseline 문서 | `docs/api/openapi-baseline.md` |
| OpenAPI 검증 규칙 | `docs/api/openapi-validation-rules.md` |

## 주요 결정 사항

- OpenAPI 버전은 `3.0.3`으로 확정했다.
- 루트 파일은 `openapi/openapi.yaml`로 확정했다.
- 공통 schema는 `openapi/schemas/common.yaml`에 둔다.
- enum schema는 `openapi/schemas/enums.yaml`에 둔다.
- path 분할 파일은 `openapi/paths/*.yaml`에 둔다.
- 인증 placeholder는 `bearerAuth`로 정의했다.
- 권한 요구사항은 `x-required-roles` 확장 필드로 명시한다.

## 정의한 공통 schema

- `ErrorResponse`
- `PageMetadata`
- `ValidationIssue`
- `ValidationSeverity`
- `TermStatus`
- `ExpressionType`
- `AliasType`
- `RelationshipType`
- `UserRole`

## 정의한 API 태그

- Term
- Expression
- Alias
- Search
- Review
- Candidate
- Governance
- Relationship
- Impact

## 완료 기준 충족 여부

| 완료 기준 | 상태 |
|---|---|
| 이후 서버 API 태스크가 OpenAPI Spec을 기준으로 개발될 수 있다 | 충족 |
| 공통 응답과 에러 포맷이 Spec에 정의되어 있다 | 충족 |

## 다음 단계

사용자 승인 후 `mvp1-tasks/01-task-database-schema-core.md`를 진행한다.

