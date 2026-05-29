# Task 01: Search API OpenAPI Spec 작성

## 목표

정확 검색, 유사어 검색, 도메인 검색, 폐기어 검색 API를 OpenAPI Spec으로 정의한다.

## 범위

- Exact Search API
- Alias Search API
- Domain Search API
- Deprecated Search API
- SearchResult schema

## 상세 태스크

1. `GET /search/exact?q=` API를 정의한다.
2. `GET /search/alias?q=` API를 정의한다.
3. `GET /search/domain/{domainName}` API를 정의한다.
4. `GET /search/deprecated?q=` API를 정의한다.
5. `SearchResult` schema를 정의한다.
6. `MatchedExpression` schema를 정의한다.
7. `Recommendation` schema를 정의한다.
8. 폐기어 검색 시 대체 용어 응답 구조를 정의한다.
9. 검색 결과에 `termId`, 표준 용어, 영문명, DB 컬럼, API 필드, 상태를 포함한다.
10. OpenAPI 예시 응답에 `고객ID -> 고객번호` 케이스를 포함한다.
11. OpenAPI lint를 통과시킨다.

## 산출물

- Search API OpenAPI Spec
- 검색 응답 schema
- 검색 API 예시

## 완료 기준

- 검색 API 구현 전에 요청/응답 계약이 확정되어 있다.
- OpenAPI Spec에서 검색 API 클라이언트와 서버 타입을 생성할 수 있다.

