# Task 02 Result: 의미 기반 검색 API 및 구현

## 1. 수행 범위

- `POST /search/semantic` OpenAPI Spec 작성
- 자연어 질의, 도메인 필터, 상태 필터, 결과 개수 요청 schema 정의
- `SemanticSearchResult` 응답 schema 정의
- 유사도 점수, 표준 용어, 추천 사유, 차이 설명 응답 필드 정의
- `주문이 발생한 날짜` OpenAPI 예시 추가
- OpenAPI Generator로 server/client 타입 생성
- 용어 문서 임베딩 생성 구현
- 벡터 인덱스 저장 방식 구현
- 자연어 질의 임베딩 생성 구현
- 유사 용어 후보 검색 구현
- Rule Engine 후처리 적용
- Deprecated, Draft, Reviewing 상태별 안내 문구 적용
- 검색 품질 평가 테스트 케이스 작성

## 2. 변경 파일

| 파일 | 변경 내용 |
|---|---|
| `openapi/openapi.yaml` | `/search/semantic` path와 schema 참조 추가 |
| `openapi/paths/search.yaml` | Semantic Search API path 정의 |
| `openapi/schemas/search.yaml` | Semantic Search 요청/응답 schema 정의 |
| `docs/api/semantic-search-api-spec.md` | Semantic Search API Spec 설명 문서 추가 |
| `generated/backend` | `SemanticSearchRequest`, `SemanticSearchResponse`, `SemanticSearchResult` 서버 타입 생성 |
| `generated/frontend` | semantic search axios client와 model 생성 |
| `apps/backend/src/main/kotlin/com/aulms/search/LocalEmbeddingService.kt` | MVP용 로컬 임베딩 생성 구현 |
| `apps/backend/src/main/kotlin/com/aulms/search/SemanticVectorIndex.kt` | 용어 문서 벡터 인덱스 구현 |
| `apps/backend/src/main/kotlin/com/aulms/search/SemanticSearchService.kt` | 의미 기반 검색 구현 |
| `apps/backend/src/main/kotlin/com/aulms/search/SearchApiController.kt` | `/search/semantic` endpoint 연결 |
| `apps/backend/src/main/kotlin/com/aulms/term/TermRepository.kt` | `주문일시` 샘플 용어 추가 |
| `apps/backend/src/test/kotlin/com/aulms/search/SemanticSearchApiContractTest.kt` | `주문이 발생한 날짜` 검색 품질 테스트 추가 |
| `docs/api/semantic-search-implementation.md` | Semantic Search 구현 설명 문서 추가 |

## 3. 검증

- YAML 문법 검증 통과
- `openapi-generator-cli validate -i openapi/openapi.yaml` 통과
- `./scripts/openapi-generate-backend.sh` 실행 성공
- `./scripts/openapi-generate-frontend.sh` 실행 성공
- `gradle test` 통과
- `npm run typecheck` 통과

## 4. 완료 기준 대응

| 완료 기준 | 결과 |
|---|---|
| `주문이 발생한 날짜` 질의에 대해 주문일자 후보 제시 | 구현 |
| `주문이 발생한 날짜` 질의에 대해 주문일시 후보 제시 | 구현 |
| 후보 간 차이 설명 | `주문일자`는 날짜만, `주문일시`는 날짜와 시간을 포함한다고 설명 |
