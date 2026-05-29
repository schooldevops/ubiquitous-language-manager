# MVP 샘플 용어 세트

## 1. 목적

MVP 개발과 테스트에 사용할 최소 표준 용어 세트를 정의한다.

## 2. 필수 포함 용어

| TERM_ID | 표준 용어 | 영문명 | DB 컬럼 | API 필드 | 코드 변수 | 상태 |
|---|---|---|---|---|---|---|
| T-000001 | 고객번호 | Customer Number | CUST_NO | customerNumber | customerNumber | Approved |
| T-000004 | 주문번호 | Order Number | ORD_NO | orderNumber | orderNumber | Approved |
| T-000005 | 주문일자 | Order Date | ORD_DT | orderDate | orderDate | Approved |
| T-000007 | 주문금액 | Order Amount | ORD_AMT | orderAmount | orderAmount | Approved |
| T-000008 | 주문상태코드 | Order Status Code | ORD_STS_CD | orderStatusCode | orderStatusCode | Approved |

## 3. 검색 테스트 기준

### 3.1 고객번호

| 입력 표현 | 유형 | 기대 처리 |
|---|---|---|
| 고객번호 | 표준어 | 그대로 사용 |
| CUST_NO | DB 컬럼 | 고객번호로 매핑 |
| customerNumber | API 필드 | 고객번호로 매핑 |
| customerId | 유사어 | customerNumber 권장 |
| 고객ID | 유사어 | 고객번호 권장 |
| CUST_ID | 금지어 | CUST_NO 권장 |
| 회원번호 | 문맥 확인 | 고객과 회원 도메인 구분 필요 |

### 3.2 주문 관련 용어

| 입력 표현 | 기대 표준 용어 | 권장 API 필드 |
|---|---|---|
| 주문 번호 | 주문번호 | orderNumber |
| 주문 날짜 | 주문일자 | orderDate |
| 주문 금액 | 주문금액 | orderAmount |
| 주문상태 | 주문상태코드 | orderStatusCode |

## 4. 바이브코딩 테스트 입력

```text
고객별 주문 내역을 조회하는 API 만들어줘.
```

기대 업무 개념:

- 고객
- 고객번호
- 주문
- 주문번호
- 주문일자
- 주문금액
- 주문상태코드

기대 DTO:

```kotlin
data class CustomerOrderResponse(
    val customerNumber: String,
    val orderNumber: String,
    val orderDate: LocalDate,
    val orderAmount: BigDecimal,
    val orderStatusCode: String
)
```

기대 OpenAPI Schema 필드:

- `customerNumber`
- `orderNumber`
- `orderDate`
- `orderAmount`
- `orderStatusCode`

## 5. DB seed 적용 기준

MVP Phase 1에서 다음 파일을 seed 입력으로 사용한다.

- `term-master-sample.csv`
- `term-expression-sample.csv`
- `term-alias-sample.csv`
- `term-relationship-sample.csv`

