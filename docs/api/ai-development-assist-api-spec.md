# AI 산출물 생성 지원 API Spec

## 목적

요구사항 입력을 받아 업무 개념 추출, 데이터 사전 매핑, 신규 용어 후보, 표준 위반 경고, 개발 산출물 예시를 반환한다.

## OpenAPI 경로

- `POST /ai/development-assist`

## 요청

| 필드 | 필수 | 설명 |
|---|---|---|
| requirementText | 예 | 사용자가 입력한 요구사항 |
| targetArtifacts | 예 | 생성할 산출물 유형. `DTO`, `OPENAPI_SCHEMA`, `SQL_EXAMPLE` |
| domainNames | 아니오 | 우선 검색할 도메인 목록 |

## 응답

| 필드 | 설명 |
|---|---|
| extractedConcepts | 요구사항에서 추출한 업무 개념 |
| termMappings | 표준 용어 매핑 결과 |
| generatedArtifacts | DTO, OpenAPI Schema, SQL 예시 |
| candidateTerms | 데이터 사전에 없는 신규 후보 |
| warnings | Rule Engine 기반 표준 위반 경고 |

## 처리 순서

1. 요구사항 텍스트에서 업무 개념 후보를 추출한다.
2. 각 개념에 대해 정확 검색을 수행한다.
3. 정확 검색 실패 시 유사어 검색을 수행한다.
4. 유사어 검색 실패 시 의미 검색을 수행한다.
5. 매핑 실패 개념은 신규 후보로 분리한다.
6. Rule Engine으로 비표준 표현과 상태 정책을 검증한다.
7. 표준 용어 매핑 기반 DTO, OpenAPI Schema, SQL 예시를 생성한다.

## 완료 기준 예시

입력:

```text
고객별 주문 내역을 조회하는 API 만들어줘.
```

반환 매핑:

| 표준 용어 | DB 컬럼 | API 필드 | 코드 변수 |
|---|---|---|---|
| 고객번호 | CUST_NO | customerNumber | customerNumber |
| 주문번호 | ORD_NO | orderNumber | orderNumber |
| 주문일자 | ORD_DT | orderDate | orderDate |
| 주문금액 | ORD_AMT | orderAmount | orderAmount |
| 주문상태코드 | ORD_STS_CD | orderStatusCode | orderStatusCode |
