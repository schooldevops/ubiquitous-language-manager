<!-- aulms-artifact-review -->
## 데이터 사전 표준 검증 결과

| Severity | Count |
|---|---:|
| ERROR | 1 |
| WARNING | 1 |
| INFO | 0 |

### 위반 목록

| File | Line | Severity | Input | Recommendation | Reason |
|---|---:|---|---|---|---|
| apps/backend/src/main/kotlin/CustomerOrderResponse.kt | 4 | WARNING | customerId | customerNumber | API/코드 표준 표현은 customerNumber입니다. |
| db/customer.sql | 2 | ERROR | CUST_ID | CUST_NO | Forbidden 표현입니다. CUST_NO를 사용해야 합니다. |

Exit code: `1`
