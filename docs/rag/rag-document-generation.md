# RAG Document Generation

## 1. 목적

용어별 Markdown 문서를 생성해 RAG 인덱싱 입력 데이터로 사용한다.

## 2. 산출 위치

| 경로 | 설명 |
|---|---|
| `docs/rag/templates/term-document-template.md` | 용어 문서 Markdown 템플릿 |
| `docs/rag/terms` | 용어별 RAG Markdown 문서 |

## 3. 생성 기준

- `Approved` 용어는 표준 지식 문서로 생성한다.
- `Deprecated` 용어는 대체 안내 섹션을 포함해 생성한다.
- `Draft`, `Reviewing` 후보는 후보 문서 생성 메서드에서 별도 입력으로 생성한다.
- 문서에는 `TERM_MASTER`, `TERM_EXPRESSION`, `TERM_ALIAS`, `TERM_RELATIONSHIP`에 해당하는 정보를 포함한다.

## 4. 고객번호 문서 검증 기준

고객번호 문서에는 다음 정보가 포함되어야 한다.

- 영문명: `Customer Number`
- DB 컬럼명: `CUST_NO`
- API 필드명: `customerNumber`
- 유사어: `고객ID`, `customerId`
- 금지어: `CUST_ID`
- 관련 용어: `고객명`, `고객상태코드`, `주문번호`
