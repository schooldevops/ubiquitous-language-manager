# Term API OpenAPI Spec

## 1. 목적

본 문서는 표준 용어, 표현 매핑, 별칭, 승인, 폐기, 변경 이력 API의 OpenAPI Spec 작성 결과를 설명한다.

## 2. 작성 파일

| 파일 | 설명 |
|---|---|
| `openapi/openapi.yaml` | 루트 OpenAPI Spec, Term API path와 schema 참조 추가 |
| `openapi/paths/terms.yaml` | Term, Expression, Alias, Governance path 정의 |
| `openapi/schemas/term.yaml` | Term API 요청/응답 schema 정의 |

## 3. 정의한 API

| Method | Path | Operation ID | 설명 |
|---|---|---|---|
| GET | `/terms` | `listTerms` | 표준 용어 목록 조회 |
| POST | `/terms` | `createTerm` | 표준 용어 등록 |
| GET | `/terms/{termId}` | `getTerm` | 표준 용어 상세 조회 |
| PUT | `/terms/{termId}` | `updateTerm` | 표준 용어 수정 |
| POST | `/terms/{termId}/approve` | `approveTerm` | 표준 용어 승인 |
| POST | `/terms/{termId}/deprecate` | `deprecateTerm` | 표준 용어 폐기 |
| GET | `/terms/{termId}/history` | `listTermHistory` | 용어 변경 이력 조회 |
| GET | `/terms/{termId}/expressions` | `listTermExpressions` | 표현 매핑 목록 조회 |
| POST | `/terms/{termId}/expressions` | `createTermExpression` | 표현 매핑 등록 |
| GET | `/terms/{termId}/aliases` | `listTermAliases` | 별칭 목록 조회 |
| POST | `/terms/{termId}/aliases` | `createTermAlias` | 별칭 등록 |

## 4. 정의한 Schema

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

## 5. Validation 기준

- `koreanName`, `englishName`, `englishAbbreviation`, `businessDefinition`, `physicalType`, `owner`는 필수다.
- `englishAbbreviation`은 `^[A-Z][A-Z0-9_]*$` 패턴을 따른다.
- `digits`와 `decimalPoint`는 0 이상이어야 한다.
- `expressionValue`, `aliasName`, `reason`은 빈 문자열일 수 없다.
- 상태, 표현 유형, 별칭 유형은 공통 enum schema를 사용한다.

## 6. 권한 기준

보호 API에는 `x-required-roles`를 명시했다.

- 조회 API는 모든 등록 역할이 사용할 수 있다.
- 등록 API는 관리자, 데이터 스튜어드, 개발 리더, 아키텍트, 기획자, 개발자가 사용할 수 있다.
- 수정 API는 관리자, 데이터 스튜어드, 개발 리더, 아키텍트가 사용할 수 있다.
- 승인/폐기 API는 관리자, 데이터 스튜어드, 아키텍트가 사용할 수 있다.

## 7. OpenAPI Generator 적용 기준

다음 단계의 Backend 구현은 이 Spec을 기준으로 server stub을 생성한 뒤 진행한다.

```bash
openapi-generator-cli generate \
  -g spring \
  -i openapi/openapi.yaml \
  -o generated/backend
```

Frontend API client는 같은 Spec에서 생성한다.

```bash
openapi-generator-cli generate \
  -g typescript-axios \
  -i openapi/openapi.yaml \
  -o generated/frontend
```

