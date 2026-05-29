# Task 02 완료 결과: Term API OpenAPI Spec 작성

## 수행 일자

2026-05-27

## 확인한 규칙

`rules.md`를 확인했고 다음 기준을 반영했다.

- 서버 API는 항상 OpenAPI Spec을 먼저 작성한다.
- Backend는 OpenAPI Generator로 Spring Boot Kotlin server stub을 생성한 뒤 구현한다.
- Frontend는 OpenAPI Generator로 axios TypeScript client를 생성한 뒤 구현한다.
- API 필드명과 요청/응답 용어는 데이터 딕셔너리 표준 표현을 따른다.
- 단계 완료 후 사용자 확인과 승인을 받는다.

## 생성 및 변경한 산출물

| 산출물 | 파일 |
|---|---|
| 루트 OpenAPI 갱신 | `openapi/openapi.yaml` |
| Term path Spec | `openapi/paths/terms.yaml` |
| Term schema Spec | `openapi/schemas/term.yaml` |
| Term API Spec 설명 | `docs/api/term-api-spec.md` |

## 정의한 API

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

## 정의한 Schema

- `Term`
- `TermSummary`
- `TermListResponse`
- `TermCreateRequest`
- `TermUpdateRequest`
- `TermExpression`
- `TermExpressionCreateRequest`
- `TermAlias`
- `TermAliasCreateRequest`
- `TermApprovalRequest`
- `TermDeprecationRequest`
- `TermChangeHistory`
- `TermChangeHistoryListResponse`

## 완료 기준 충족 여부

| 완료 기준 | 상태 |
|---|---|
| Backend 구현자가 Spec만 보고 API를 구현할 수 있다 | 충족 |
| 서버 코드 생성 또는 타입 생성에 사용할 수 있는 수준의 OpenAPI Spec이 준비되어 있다 | 충족 |

## 비고

OpenAPI lint는 아직 프로젝트 패키지와 lint 도구가 scaffold되지 않아 실행하지 않았다. 대신 파일 수준으로 path, schema, 권한 확장 필드, 공통 ErrorResponse 연결 여부를 확인한다.

## 다음 단계

사용자 승인 후 `mvp1-tasks/03-task-term-api-implementation.md`를 진행한다.

