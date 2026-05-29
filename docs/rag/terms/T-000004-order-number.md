---
termId: T-000004
termNumber: TERM-000004
domainName: 주문
status: Approved
englishName: "Order Number"
dbColumn: ORD_NO
apiField: orderNumber
codeVariable: orderNumber
---

# 주문번호

## 업무 정의

주문을 업무적으로 식별하기 위해 사용하는 번호

## 사용 맥락

주문 조회, 결제, 배송, 상담에서 주문 식별 기준으로 사용

## 기본 정보

| 항목 | 값 |
|---|---|
| 한글 표준 용어 | 주문번호 |
| 영문명 | Order Number |
| DB 컬럼명 | ORD_NO |
| API 필드명 | orderNumber |
| 코드 변수명 | orderNumber |
| 물리 타입 | VARCHAR |
| 자릿수 | 20 |
| 소수점 | 0 |

## 유사어와 금지어

- 유사어 또는 금지어 미정의

## 관련 용어

- usedWith: 주문일자 - 주문번호는 주문일자와 함께 주문 목록에 표시
- usedWith: 주문금액 - 주문번호는 주문금액과 함께 주문 목록에 표시
- usedWith: 주문상태코드 - 주문번호는 주문상태코드와 함께 주문 진행 상태 확인에 사용

## 예시 문장

- 기획서: 주문번호로 주문 상세를 조회한다.
- API: `orderNumber`
- SQL: `ORD_NO`
