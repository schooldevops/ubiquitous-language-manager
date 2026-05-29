# Search API Implementation

## 1. 목적

OpenAPI Spec으로 정의한 Search API 중 정확 검색, 유사어 검색, 도메인 검색, 폐기어 검색의 MVP 구현 결과를 정리한다.

## 2. 구현 파일

| 파일 | 설명 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/search/SearchApiController.kt` | OpenAPI Generator가 생성한 `SearchApi` 인터페이스 구현 |
| `apps/backend/src/main/kotlin/com/aulms/search/SearchService.kt` | 검색 정규화, 검색 결과 변환, 추천 정책 구현 |
| `apps/backend/src/main/kotlin/com/aulms/search/SearchNormalizer.kt` | 공백, 대소문자, 하이픈, 언더스코어 정규화 |
| `apps/backend/src/main/kotlin/com/aulms/term/TermRepository.kt` | 검색용 용어 문서 조회 메서드 추가 |
| `apps/backend/src/test/kotlin/com/aulms/search/SearchApiContractTest.kt` | Search API contract test |

## 3. 검색 기준

- 정확 검색은 한글 표준명, 영문명, DB 컬럼명, API 필드명, 코드 변수명을 대상으로 한다.
- 정규화는 공백, 하이픈, 언더스코어 제거와 소문자 변환을 적용한다.
- 유사어 검색은 `TERM_ALIAS`에 해당하는 `TermAlias` 샘플 데이터를 대상으로 한다.
- 금지어와 폐기어는 `ReplaceDeprecatedTerm` 권고와 `Error` severity로 반환한다.

## 4. 대표 검증 케이스

| 입력 | API | 기대 결과 |
|---|---|---|
| `고객번호` | `/search/exact` | `고객번호 / CUST_NO / customerNumber` |
| `CUST_NO` | `/search/exact` | `고객번호 / CUST_NO / customerNumber` |
| `customerNumber` | `/search/exact` | `고객번호 / CUST_NO / customerNumber` |
| `고객ID` | `/search/alias` | `고객번호`, `customerNumber` 권고 |
| `CUST_ID` | `/search/deprecated` | 금지 사유와 `CUST_NO` 권고 |
