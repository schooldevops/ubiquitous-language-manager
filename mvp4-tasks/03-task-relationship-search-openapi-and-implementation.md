# Task 03: 관계 검색 API 및 구현

## 목표

Graphify 기반 관계 검색 API를 OpenAPI Spec으로 정의하고 구현한다.

## OpenAPI 필수 조건

관계 검색 API는 구현 전에 OpenAPI Spec을 먼저 작성해야 한다.

## 상세 태스크

1. `GET /relationships/terms/{termId}` OpenAPI Spec을 작성한다.
2. `GET /relationships/domains/{domainName}/terms` OpenAPI Spec을 작성한다.
3. `GET /relationships/columns/{columnName}/systems` OpenAPI Spec을 작성한다.
4. `RelationshipSearchResult` schema를 정의한다.
5. 그래프 경로 응답 schema를 정의한다.
6. 고객번호 관련 용어 조회 예시를 Spec에 포함한다.
7. OpenAPI Spec에서 서버 타입을 생성한다.
8. 특정 용어와 연결된 모든 용어 조회를 구현한다.
9. 도메인별 표준 용어 조회를 구현한다.
10. 특정 컬럼을 사용하는 시스템 조회를 구현한다.
11. Deprecated 용어 사용 위치 조회를 구현한다.
12. contract test를 작성한다.

## 산출물

- Relationship API OpenAPI Spec
- 관계 검색 API
- 그래프 질의 로직
- contract test

## 완료 기준

- 고객번호와 함께 쓰이는 고객명, 고객상태코드, 주문번호를 조회할 수 있다.
- `CUST_NO`를 사용하는 시스템을 조회할 수 있다.

