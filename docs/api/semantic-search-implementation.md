# Semantic Search Implementation

## 1. 목적

자연어 질의를 용어 문서 임베딩과 비교해 표준 용어 후보를 반환한다.

## 2. 구현 파일

| 파일 | 설명 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/search/LocalEmbeddingService.kt` | MVP용 로컬 토큰 임베딩 생성 |
| `apps/backend/src/main/kotlin/com/aulms/search/SemanticVectorIndex.kt` | 용어 문서 벡터 인덱스 생성 |
| `apps/backend/src/main/kotlin/com/aulms/search/SemanticSearchService.kt` | 자연어 질의 임베딩, 후보 검색, 상태 안내, Rule Engine 후처리 |
| `apps/backend/src/test/kotlin/com/aulms/search/SemanticSearchApiContractTest.kt` | 검색 품질 contract test |

## 3. MVP 검색 방식

- 외부 임베딩 API 없이 로컬 토큰 벡터를 생성한다.
- 용어명, 영문명, DB 컬럼, 업무 정의, 사용 맥락, 표현, 별칭을 하나의 문서로 구성한다.
- 질의 벡터와 문서 벡터의 cosine similarity를 계산한다.
- 날짜/일자/일시처럼 도메인에서 중요한 표현은 의미 동의어를 확장한다.
- 상태별 안내 문구와 Rule Engine 결과를 후처리로 추가한다.

## 4. 대표 케이스

질의 `주문이 발생한 날짜`는 다음 후보를 반환한다.

| 후보 | 차이 |
|---|---|
| 주문일자 | 날짜만 필요할 때 사용 |
| 주문일시 | 시분초까지 필요할 때 사용 |
