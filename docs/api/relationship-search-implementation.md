# Relationship Search 구현

## 구현 범위

- `RelationshipApiController`
- `RelationshipSearchService`
- GraphSyncWorker snapshot 기반 관계 조회
- 관계 검색 contract test

## 조회 방식

MVP4 구현은 `GraphSyncWorker.buildSnapshot()` 결과와 `TermRepository` 데이터를 사용한다.

- 용어 관계: `term:{termId}` 기준 term-to-term edge 탐색
- 도메인별 용어: `TermRepository.searchDocuments()`에서 `domainName`, `Approved` 필터
- 컬럼 사용 시스템: `englishAbbreviation` 기준 매칭 후 API field 반환
- 폐기어/금지어: `TermAlias` 중 `Forbidden`, `Deprecated` 반환

## 검증

`RelationshipApiContractTest`에서 다음을 검증한다.

- 고객번호와 함께 쓰이는 고객명, 고객상태코드, 주문번호 조회
- 고객 도메인 표준 용어 조회
- `CUST_NO` 사용 시스템과 `customerNumber` API 필드 조회
- `CUST_ID` 금지어와 `CUST_NO` 권장 표현 조회
