# Document Review API Implementation

## 1. 목적

기획서 본문에서 비표준 표현을 탐지하고 표준 용어 기반 문장, 매핑표, 검증 이슈, 신규 후보를 반환한다.

## 2. 구현 파일

| 파일 | 설명 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/review/ReviewApiController.kt` | 생성된 `ReviewApi` 인터페이스 구현 |
| `apps/backend/src/main/kotlin/com/aulms/review/DocumentReviewService.kt` | 문장 분리, 용어 탐지, 표준화 문장 추천, 매핑표 생성 |
| `apps/backend/src/test/kotlin/com/aulms/review/DocumentReviewApiContractTest.kt` | Review API contract test |
| `apps/backend/src/main/kotlin/com/aulms/term/TermRepository.kt` | `주문목록` 샘플 표준 용어와 `주문 리스트` 별칭 추가 |

## 3. 처리 흐름

1. 본문을 문장 단위로 분리한다.
2. 도메인 필터에 맞는 표준 용어 표현과 별칭을 조회한다.
3. 공백, 하이픈, 언더스코어 변형을 허용해 본문 표현을 탐지한다.
4. 탐지 표현을 표준 한글 용어로 치환해 추천 문장을 생성한다.
5. 표준 용어, 영문명, DB 컬럼, API 필드, 코드 변수 매핑표를 생성한다.
6. Rule Engine 결과를 `ValidationIssue`로 포함한다.
7. 미매핑 표현은 신규 용어 후보로 반환한다.

## 4. 대표 결과

입력:

```text
고객 ID를 입력하면 주문 리스트를 조회한다.
```

추천:

```text
고객번호를 입력하면 주문 목록을 조회한다.
```

매핑:

| 입력 표현 | 표준 용어 | DB 컬럼 | API 필드 |
|---|---|---|---|
| 고객 ID | 고객번호 | CUST_NO | customerNumber |
| 주문 리스트 | 주문목록 | ORD_LIST | orderList |
