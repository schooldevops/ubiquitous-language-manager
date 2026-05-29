# Search API OpenAPI Spec

## 1. 목적

본 문서는 정확 검색, 유사어 검색, 도메인 검색, 폐기어 검색 API의 OpenAPI Spec 작성 결과를 설명한다.

## 2. 작성 파일

| 파일 | 설명 |
|---|---|
| `openapi/openapi.yaml` | Search API path와 schema 참조 추가 |
| `openapi/paths/search.yaml` | Search API path 정의 |
| `openapi/schemas/search.yaml` | 검색 응답 schema 정의 |

## 3. 정의한 API

| Method | Path | Operation ID | 설명 |
|---|---|---|---|
| GET | `/search/exact` | `exactSearch` | 한글명, 영문명, DB 컬럼명, API 필드명, 코드 변수명 정확 검색 |
| GET | `/search/alias` | `aliasSearch` | 유사어, 별칭, 문맥 확인 필요 표현 검색 |
| GET | `/search/domain/{domainName}` | `domainSearch` | 도메인별 표준 용어 검색 |
| GET | `/search/deprecated` | `deprecatedSearch` | 폐기어 또는 사용 금지 표현의 대체 표준 용어 검색 |

## 4. 정의한 Schema

- `SearchResponse`
- `DeprecatedSearchResponse`
- `SearchResult`
- `DeprecatedSearchResult`
- `MatchedExpression`
- `Recommendation`

## 5. 응답 기준

검색 결과인 `SearchResult`는 다음 필드를 반드시 포함한다.

| 필드 | 의미 |
|---|---|
| `termId` | 표준 용어 식별자 |
| `standardTerm` | 한글 표준 용어명 |
| `englishName` | 영문 표준 용어명 |
| `dbColumn` | 표준 DB 컬럼명 |
| `apiField` | 표준 API 필드명 |
| `status` | 용어 상태 |

## 6. 대표 예시

`GET /search/alias?q=고객ID`는 `고객ID`를 비표준 유사어로 판단하고 `고객번호`를 권고한다.

```json
{
  "query": "고객ID",
  "items": [
    {
      "termId": "T-000001",
      "standardTerm": "고객번호",
      "englishName": "Customer Number",
      "dbColumn": "CUST_NO",
      "apiField": "customerNumber",
      "codeVariable": "customerNumber",
      "status": "Approved",
      "matchedExpressions": [
        {
          "expressionType": "Korean",
          "expressionValue": "고객ID",
          "matchType": "Alias",
          "inputExpression": "고객ID"
        }
      ],
      "recommendations": [
        {
          "action": "UseStandardTerm",
          "recommendedTermId": "T-000001",
          "recommendedTerm": "고객번호",
          "recommendedExpression": "customerNumber",
          "reason": "고객ID는 고객번호의 유사어이며 표준 API 필드명은 customerNumber임",
          "severity": "Warning"
        }
      ]
    }
  ]
}
```

## 7. OpenAPI Generator 적용 기준

Backend server stub과 Frontend axios client는 동일한 `openapi/openapi.yaml`에서 생성한다.

```bash
openapi-generator-cli generate \
  -g kotlin-spring \
  -i openapi/openapi.yaml \
  -o generated/backend
```

```bash
openapi-generator-cli generate \
  -g typescript-axios \
  -i openapi/openapi.yaml \
  -o generated/frontend
```
