---
termId: T-000001
termNumber: TERM-000001
domainName: 고객
status: Approved
englishName: "Customer Number"
dbColumn: CUST_NO
apiField: customerNumber
codeVariable: customerNumber
---

# 고객번호

## 업무 정의

회사에서 고객을 업무적으로 식별하기 위해 사용하는 번호

## 사용 맥락

주문, 계약, 청구, 상담 등에서 고객 식별 기준으로 사용

## 기본 정보

| 항목 | 값 |
|---|---|
| 한글 표준 용어 | 고객번호 |
| 영문명 | Customer Number |
| DB 컬럼명 | CUST_NO |
| API 필드명 | customerNumber |
| 코드 변수명 | customerNumber |
| 물리 타입 | VARCHAR |
| 자릿수 | 20 |
| 소수점 | 0 |
| 소유자 | 고객도메인 데이터스튜어드 |
| 버전 | 1.0 |

## 산출물 표현

| 표현 유형 | 표현값 | 언어 | 스타일 | 표준 여부 |
|---|---|---|---|---|
| Korean | 고객번호 | ko | standard | true |
| English | Customer Number | en | title | true |
| DB_COLUMN | CUST_NO | en | UPPER_SNAKE | true |
| API_FIELD | customerNumber | en | camelCase | true |
| CODE_VARIABLE | customerNumber | en | camelCase | true |
| UI_LABEL | 고객번호 | ko | label | true |
| TEST_WORD | 고객번호 | ko | gherkin | true |

## 유사어와 금지어

- 유사어: 고객ID -> 고객번호로 변환 권장. 업무 고객 식별 번호 의미로 사용되는 경우 표준 용어는 고객번호
- 유사어: customerId -> customerNumber로 변환 권장. API 표준 필드명은 customerNumber
- 금지어: CUST_ID -> CUST_NO 사용. 기술 ID와 업무 고객번호가 혼동될 수 있음

## 관련 용어

- relatedTo: 고객명 - 고객번호는 고객명과 함께 고객 식별 정보로 사용
- relatedTo: 고객상태코드 - 고객번호는 고객상태코드와 함께 고객 상태 판단에 사용
- usedWith: 주문번호 - 고객번호는 주문번호와 함께 고객별 주문 조회에 사용

## 예시 문장

- 기획서: 고객번호를 입력하면 주문 목록을 조회한다.
- API: `GET /orders?customerNumber={customerNumber}`
- Kotlin: `val customerNumber: String`
- SQL: `WHERE CUST_NO = :custNo`
