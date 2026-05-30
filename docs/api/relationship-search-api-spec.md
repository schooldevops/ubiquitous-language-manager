# Relationship Search API Spec

## 목적

Graphify 그래프를 기반으로 용어 관계, 도메인별 용어, 컬럼 사용 시스템, 폐기어/금지어 사용 위치를 조회한다.

## OpenAPI 경로

| Method | Path | 설명 |
|---|---|---|
| GET | `/relationships/terms/{termId}` | 기준 용어와 연결된 모든 용어 조회 |
| GET | `/relationships/domains/{domainName}/terms` | 도메인별 표준 용어 조회 |
| GET | `/relationships/columns/{columnName}/systems` | 특정 컬럼을 사용하는 시스템 조회 |
| GET | `/relationships/deprecated` | Deprecated/Forbidden 표현 사용 위치 조회 |

## 대표 응답

`GET /relationships/terms/T-000001`은 고객번호와 함께 쓰이는 고객명, 고객상태코드, 주문번호를 반환한다.

`GET /relationships/columns/CUST_NO/systems`는 `CUST_NO`와 연결된 표준 용어 고객번호, API 필드 `customerNumber`, 시스템 정보를 반환한다.
