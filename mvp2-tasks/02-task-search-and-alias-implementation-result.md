# Task 02 Result: 정확 검색 및 유사어 검색 구현

## 1. 수행 범위

- OpenAPI Generator로 생성된 `SearchApi` 서버 인터페이스 구현
- 정확 검색 구현
- 유사어 검색 구현
- 도메인 검색 구현
- 폐기어/금지어 검색 구현
- 검색 정규화 유틸리티 구현
- Search API contract test 작성

## 2. 변경 파일

| 파일 | 변경 내용 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/search/SearchApiController.kt` | `SearchApi` 인터페이스 구현 |
| `apps/backend/src/main/kotlin/com/aulms/search/SearchService.kt` | 정확 검색, 유사어 검색, 폐기어 검색, 도메인 검색 구현 |
| `apps/backend/src/main/kotlin/com/aulms/search/SearchNormalizer.kt` | 검색어 정규화 구현 |
| `apps/backend/src/main/kotlin/com/aulms/term/TermRepository.kt` | 검색용 용어 문서 조회 추가 |
| `apps/backend/src/test/kotlin/com/aulms/search/SearchApiContractTest.kt` | Search API contract test 추가 |
| `docs/api/search-api-implementation.md` | 구현 설명 문서 추가 |

## 3. 완료 기준 대응

| 완료 기준 | 결과 |
|---|---|
| `고객번호`로 고객번호 용어 검색 | 구현 |
| `CUST_NO`로 고객번호 용어 검색 | 구현 |
| `customerNumber`로 고객번호 용어 검색 | 구현 |
| `고객ID` 검색 시 고객번호 추천 | 구현 |
| `CUST_ID` 검색 시 금지어 사유와 `CUST_NO` 권장 | 구현 |

## 4. 검증

- `gradle test` 통과
