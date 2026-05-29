# Task 04 Result: 기획서 용어 검토 API 및 구현

## 1. 수행 범위

- `POST /reviews/document` OpenAPI Spec 작성
- 기획서 본문, 검토 옵션, 도메인 필터 요청 schema 정의
- `DocumentReviewResult`, `DetectedTerm`, `StandardMapping`, `CandidateTerm` schema 정의
- OpenAPI 예시에 `고객 ID를 입력하면 주문 리스트를 조회한다.` 포함
- OpenAPI Generator로 server/client 타입 생성
- 기획서 문장 분리, 업무 용어 후보 탐지, 정확/유사어 검색 연결 구현
- Rule Engine 검증 이슈 연결
- 표준화 문장 추천과 용어 매핑표 생성 구현
- 미매핑 표현 신규 후보 반환 구현
- contract test 작성

## 2. 변경 파일

| 파일 | 변경 내용 |
|---|---|
| `openapi/openapi.yaml` | Review API path와 schema 참조 추가 |
| `openapi/paths/reviews.yaml` | `/reviews/document` API 정의 |
| `openapi/schemas/review.yaml` | 기획서 검토 요청/응답 schema 정의 |
| `docs/api/document-review-api-spec.md` | Review API Spec 설명 문서 추가 |
| `generated/backend` | `ReviewApi`, `DocumentReviewResult` 등 서버 타입 생성 |
| `generated/frontend` | `ReviewApi` axios client와 Review model 생성 |
| `apps/backend/src/main/kotlin/com/aulms/review/ReviewApiController.kt` | `ReviewApi` 인터페이스 구현 |
| `apps/backend/src/main/kotlin/com/aulms/review/DocumentReviewService.kt` | 기획서 용어 검토 로직 구현 |
| `apps/backend/src/main/kotlin/com/aulms/term/TermRepository.kt` | `주문목록`, `주문 리스트` 샘플 데이터 추가 |
| `apps/backend/src/test/kotlin/com/aulms/review/DocumentReviewApiContractTest.kt` | Review API contract test 추가 |
| `docs/api/document-review-api-implementation.md` | 구현 설명 문서 추가 |

## 3. 검증

- YAML 문법 검증 통과
- `openapi-generator-cli validate -i openapi/openapi.yaml` 통과
- `./scripts/openapi-generate-backend.sh` 실행 성공
- `./scripts/openapi-generate-frontend.sh` 실행 성공
- `gradle test` 통과

## 4. 완료 기준 대응

| 완료 기준 | 결과 |
|---|---|
| `고객 ID를 입력하면 주문 리스트를 조회한다.` 입력 시 표준화 문장 추천 | `고객번호를 입력하면 주문 목록을 조회한다.` 반환 |
| 검토 결과에 표준 용어 포함 | `고객번호`, `주문목록` 포함 |
| 검토 결과에 DB 컬럼 포함 | `CUST_NO`, `ORD_LIST` 포함 |
| 검토 결과에 API 필드 포함 | `customerNumber`, `orderList` 포함 |
| 검토 결과에 사유 포함 | `standardMappings.reason`, `validationIssues.reason` 포함 |
