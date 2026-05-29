# AI 산출물 생성 지원 구현

## 구현 범위

- `POST /ai/development-assist`
- 업무 개념 추출
- exact → alias → semantic 검색 순차 적용
- 표준 용어 매핑 생성
- 신규 용어 후보 분리
- Rule Engine 표준 위반 경고 생성
- DTO, OpenAPI Schema, SQL 예시 생성

## MVP 추출 규칙

MVP3 구현은 로컬 규칙 기반으로 동작한다.

- `고객` 포함 시 `고객번호` 추출
- `고객ID`, `customerId` 포함 시 별칭 경고 검증
- `주문` 포함 시 `주문번호` 추출
- `주문`과 `내역`, `목록`, `조회`, `API` 중 하나가 함께 있으면 `주문일자`, `주문금액`, `주문상태코드` 추출
- `배송`과 `시간대`가 함께 있으면 `고객선호배송시간대` 신규 후보 생성

## 산출물 생성

표준 용어 매핑의 물리 타입을 기준으로 Kotlin 타입과 OpenAPI 타입을 결정한다.

| 물리 타입 | Kotlin 타입 | OpenAPI 타입 |
|---|---|---|
| VARCHAR | String | string |
| DATE | LocalDate | string/date |
| TIMESTAMP | LocalDateTime | string/date-time |
| NUMERIC | BigDecimal | number |

SQL 예시는 `ORDER_HISTORY` 기준으로 생성하며, `고객번호`가 있으면 `WHERE CUST_NO = :customerNumber` 조건을 추가한다.

## 검증

`DevelopmentAssistApiContractTest`에서 다음을 검증한다.

- `고객별 주문 내역을 조회하는 API 만들어줘.` 입력 시 고객번호, 주문번호, 주문일자, 주문금액, 주문상태코드 매핑 반환
- DTO/OpenAPI Schema/SQL 예시 반환
- 데이터 사전에 없는 `고객선호배송시간대`는 후보로 분리
- `고객ID`는 `고객번호` 표준 용어 경고로 반환
