# Task 01 Result: RAG 용어 문서 생성

## 1. 수행 범위

- 용어 문서 Markdown 템플릿 정의
- `TERM_MASTER` 정보 포함
- `TERM_EXPRESSION` 정보 포함
- `TERM_ALIAS` 정보 포함
- `TERM_RELATIONSHIP` 정보 포함
- 업무 정의와 사용 맥락 포함
- 예시 문장 포함
- Deprecated 용어 대체 안내 섹션 생성 로직 포함
- 문서 생성 배치 구현
- 문서 생성 결과 검증 테스트 작성

## 2. 변경 파일

| 파일 | 변경 내용 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/rag/RagDocumentGenerator.kt` | RAG Markdown 문서 생성기와 배치 메서드 구현 |
| `apps/backend/src/test/kotlin/com/aulms/rag/RagDocumentGeneratorTest.kt` | 고객번호 문서 내용과 파일 생성 검증 |
| `docs/rag/templates/term-document-template.md` | 용어 문서 Markdown 템플릿 추가 |
| `docs/rag/terms/T-000001-customer-number.md` | 고객번호 RAG 문서 추가 |
| `docs/rag/terms/T-000004-order-number.md` | 주문번호 RAG 문서 추가 |
| `docs/rag/rag-document-generation.md` | RAG 문서 생성 기준 문서 추가 |

## 3. 완료 기준 대응

| 완료 기준 | 결과 |
|---|---|
| 고객번호 용어 문서 생성 | `docs/rag/terms/T-000001-customer-number.md` 생성 |
| 영문명 포함 | `Customer Number` 포함 |
| DB 컬럼명 포함 | `CUST_NO` 포함 |
| API 필드명 포함 | `customerNumber` 포함 |
| 유사어 포함 | `고객ID`, `customerId` 포함 |
| 금지어 포함 | `CUST_ID` 포함 |
| 관련 용어 포함 | `고객명`, `고객상태코드`, `주문번호` 포함 |

## 4. 검증

- `gradle test` 통과
