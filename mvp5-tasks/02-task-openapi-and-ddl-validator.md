# Task 02: OpenAPI 및 DDL 검증기 구현

## 목표

OpenAPI YAML의 API 필드명과 DB DDL의 컬럼명을 데이터 사전 기준으로 검증한다.

## 상세 태스크

1. OpenAPI YAML 파일을 파싱한다.
2. schema properties 필드명을 추출한다.
3. request/response body 필드명을 추출한다.
4. 추출한 필드명을 `API_FIELD` 표현과 비교한다.
5. 비표준 API 필드명을 Rule Engine으로 검증한다.
6. DDL 파일을 파싱한다.
7. 테이블명과 컬럼명을 추출한다.
8. 추출한 컬럼명을 `DB_COLUMN` 표현과 비교한다.
9. 물리 타입, 자릿수, 소수점 비교를 구현한다.
10. Deprecated 컬럼명 사용을 ERROR로 반환한다.
11. Draft 용어 기반 컬럼명 사용을 ERROR로 반환한다.
12. 검증 결과를 JSON 리포트로 출력한다.
13. 단위 테스트와 fixture를 작성한다.

## 산출물

- OpenAPI validator
- DDL validator
- JSON 검증 리포트
- 테스트 fixture

## 완료 기준

- OpenAPI에서 `customerId` 필드를 발견하면 `customerNumber`를 권장한다.
- DDL에서 `CUST_ID` 컬럼을 발견하면 `CUST_NO`를 권장한다.

