# Document Review API Spec

## 1. 목적

기획서 본문에서 비표준 용어를 탐지하고 표준 용어, DB 컬럼, API 필드, 검증 이슈, 신규 후보를 반환하는 Review API 계약을 정의한다.

## 2. 작성 파일

| 파일 | 설명 |
|---|---|
| `openapi/openapi.yaml` | `/reviews/document` path와 Review schema 참조 추가 |
| `openapi/paths/reviews.yaml` | Review API path 정의 |
| `openapi/schemas/review.yaml` | 기획서 검토 요청/응답 schema 정의 |

## 3. 정의 API

| Method | Path | Operation ID | 설명 |
|---|---|---|---|
| POST | `/reviews/document` | `reviewDocument` | 기획서 본문 용어 검토 |

## 4. 대표 예시

입력:

```json
{
  "documentText": "고객 ID를 입력하면 주문 리스트를 조회한다.",
  "domainNames": ["고객", "주문"],
  "options": {
    "includeCandidateTerms": true,
    "includeValidationIssues": true,
    "normalizeSentences": true
  }
}
```

추천 문장:

```text
고객번호를 입력하면 주문 목록을 조회한다.
```

## 5. 응답 구성

- `detectedTerms`: 본문에서 검출된 표현
- `standardMappings`: 표준 용어, 영문명, DB 컬럼, API 필드 매핑표
- `candidateTerms`: 데이터 사전에 없는 신규 용어 후보
- `validationIssues`: Rule Engine 또는 문서 표준화 규칙에서 발생한 검증 결과
