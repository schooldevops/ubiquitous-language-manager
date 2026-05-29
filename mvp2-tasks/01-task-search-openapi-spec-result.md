# Task 01 Result: Search API OpenAPI Spec 작성

## 1. 수행 범위

- 정확 검색 API 계약 정의
- 유사어 검색 API 계약 정의
- 도메인별 검색 API 계약 정의
- 폐기어 검색 API 계약 정의
- 검색 응답 schema 정의
- `고객ID -> 고객번호` 예시 응답 정의

## 2. 변경 파일

| 파일 | 변경 내용 |
|---|---|
| `openapi/openapi.yaml` | Search API path와 schema 참조 추가 |
| `openapi/paths/search.yaml` | `/search/exact`, `/search/alias`, `/search/domain/{domainName}`, `/search/deprecated` 정의 |
| `openapi/schemas/search.yaml` | `SearchResponse`, `DeprecatedSearchResponse`, `SearchResult`, `DeprecatedSearchResult`, `MatchedExpression`, `Recommendation` 정의 |
| `docs/api/search-api-spec.md` | Search API Spec 설명 문서 추가 |
| `generated/backend` | OpenAPI Generator로 `SearchApi` server stub과 검색 model 생성 확인 |
| `generated/frontend` | OpenAPI Generator로 `SearchApi` axios client와 검색 model 생성 확인 |

## 3. 정의 API

| Method | Path | Operation ID | 설명 |
|---|---|---|---|
| GET | `/search/exact` | `exactSearch` | 정확 검색 |
| GET | `/search/alias` | `aliasSearch` | 유사어 검색 |
| GET | `/search/domain/{domainName}` | `domainSearch` | 도메인별 검색 |
| GET | `/search/deprecated` | `deprecatedSearch` | 폐기어 검색 |

## 4. 핵심 응답 구조

`SearchResult`는 검색 결과에 다음 값을 포함한다.

- `termId`
- `standardTerm`
- `englishName`
- `dbColumn`
- `apiField`
- `status`
- `matchedExpressions`
- `recommendations`

폐기어 검색은 `DeprecatedSearchResult`를 통해 폐기 표현, 폐기 사유, 대체 표준 용어, 권고 조치를 함께 반환한다.

## 5. 검증

- YAML 문법 검증 통과
- `openapi-generator-cli validate -i openapi/openapi.yaml` 통과
- `./scripts/openapi-generate-backend.sh` 실행 성공
- `./scripts/openapi-generate-frontend.sh` 실행 성공

OpenAPI Generator validate 실행 시 기존 공통 schema 일부에 대해 unused model recommendation이 표시되었으나, spec validation과 server/client 생성은 성공했다.
