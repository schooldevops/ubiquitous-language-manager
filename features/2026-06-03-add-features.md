# LLM 모델활용 기능 추가

- 추가 기능은 "용어 등록" 시 한글 용어명을 입력하고 "추천" 을 클릭하면 서버에 요청하여 RAG와 DB등에 기존 용어를 검색하고, 적당한 용어가 없다면 Spring AI를 활용하여 추천 요소들을 화면에 표시하도록 작성해줘

---

# 개발 플랜: Spring AI 기반 용어 추천

## 1. 배경 / 현재 상태

추천 기능 골격은 이미 존재한다. 다만 "LLM" 부분이 가짜다.

- 엔드포인트: `POST /ai/recommend-term-draft` → `AIApiController.recommendTermDraft` → `TermRecommendationService.recommend`
  - `apps/backend/src/main/kotlin/com/aulms/ai/AIApiController.kt:17`
  - `apps/backend/src/main/kotlin/com/aulms/ai/TermRecommendationService.kt:26`
- RAG/DB 검색은 실제 동작:
  - exact / alias 검색: `SearchService`
  - semantic 검색: `SemanticSearchService` + `LocalEmbeddingService`(TF-IDF 토큰 임베딩) + `SemanticVectorIndex`
- **문제**: `TermRecommendationService.llmInferRecommendation`(:126) 은 LLM 호출이 아니라 하드코딩 사전 기반 규칙 엔진이다.
  - `translationCatalog`, `abbreviationCatalog`, `suffixCatalog`, `domainEnglishMap`(:367~) 로 영문명/약어/물리타입을 생성.
  - `build.gradle.kts:32-41` 에 Spring AI 의존성 없음. `application.yml` 파일 자체가 없음.
- 프론트엔드 추천 핸들러는 응답의 `recommendation` 객체를 그대로 폼에 채운다. 응답 스키마(`RecommendedTermDraft`)만 동일하면 프론트 변경 불필요.
  - `apps/frontend/src/app/page.tsx:225` `recommendCreateForm()`
  - `apps/frontend/src/app/page.tsx:252` `recommendCandidateForm()`

**목표**: 가짜 휴리스틱 → 실제 Spring AI(Claude) 추론으로 교체. RAG/DB 에서 적당한 용어를 못 찾을 때만 LLM 호출. LLM 실패 시 기존 휴리스틱으로 graceful degrade.

## 2. 확정된 설계 결정

| 항목 | 결정 |
|------|------|
| Chat LLM | **Anthropic Claude** (`spring-ai-anthropic-spring-boot-starter`) |
| 휴리스틱 | **폴백 유지** — LLM 우선, 키 없음/호출 실패/파싱 실패 시 기존 catalog 휴리스틱으로 degrade |
| RAG 임베딩 | **Spring AI 임베딩으로 교체** |

### 2-1. 임베딩 제약 (중요)

Anthropic 은 임베딩 API 를 제공하지 않는다(`anthropic` 스타터는 chat 전용). 따라서 임베딩은 별도 모델 필요:

- **채택: `spring-ai-transformers-spring-boot-starter`** (로컬 ONNX, all-MiniLM-L6-v2)
  - 외부 키 불필요, 인프로세스 실행, 추가 인프라 없음.
- **VectorStore: `SimpleVectorStore`** (인메모리)
  - 현재 `TermRepository` 가 인메모리이므로 pgvector/Postgres 도입은 과함(YAGNI). 데이터 영속화 도입 시 별도 과제로 분리.

환경변수: `ANTHROPIC_API_KEY` (chat). 임베딩은 키 불필요.

## 3. 목표 플로우

```
한글 용어명 입력 → "추천" 클릭 → POST /ai/recommend-term-draft
  1. RAG/DB 검색: exact + alias + semantic
       - semantic 검색 임베딩을 Spring AI EmbeddingModel + SimpleVectorStore 로 교체
  2. exact 일치 존재 → 기존 승인 용어 재사용 (LLM 호출 안 함) [기존 동작 유지]
  3. 적당한 용어 없음 → Spring AI ChatClient(Claude) 호출
       prompt = koreanName + ragMatches + graphContext
       → 구조화 JSON 으로 RecommendedTermDraft 추론
  4. LLM 실패 / 키 없음 / 파싱 실패 → 기존 catalog 휴리스틱 폴백
       + waInrnings 에 "LLM 미사용, 규칙기반 추천" 추가
  5. 응답 반환 → 프론트 폼 자동 채움 (스키마 동일, 프론트 변경 없음)
```

## 4. 변경 대상

### 4-1. 빌드 / 설정
- `apps/backend/build.gradle.kts`
  - Spring AI BOM 추가 (`org.springframework.ai:spring-ai-bom`, Boot 3.3.5 호환 1.0.x 라인).
  - `implementation("org.springframework.ai:spring-ai-anthropic-spring-boot-starter")`
  - `implementation("org.springframework.ai:spring-ai-transformers-spring-boot-starter")`
  - Spring AI 아티팩트는 Spring Milestone 저장소가 필요할 수 있음 → `settings.gradle.kts`/리포 설정 확인.
- `apps/backend/src/main/resources/application.yml` **(신규)**
  - `spring.ai.anthropic.api-key: ${ANTHROPIC_API_KEY:}`
  - `spring.ai.anthropic.chat.options.model` (예: 최신 Claude), `temperature`, `max-tokens`
  - transformers 임베딩 옵션(필요 시 모델/캐시 경로)
  - 키 미설정 시에도 부팅 가능해야 함(폴백 경로 보장) → `api-key` 기본값 빈 문자열.

### 4-2. 신규 컴포넌트
- `com.aulms.ai.LlmTermDraftGenerator` **(신규)**
  - 의존성: Spring AI `ChatClient`(또는 `ChatModel`).
  - 입력: `koreanName`, `inferredDomainName`, `ragMatches`, `graphContext`, `request` 컨텍스트.
  - 출력: `RecommendedTermDraft` (구조화 출력 / JSON 파싱).
  - 책임: 프롬프트 구성 + 호출 + 파싱. **호출 실패/파싱 실패 시 예외를 던져** 상위에서 폴백하도록.
  - 키 미설정 감지: 빈 키이면 즉시 "LLM 비활성" 신호 → 호출 자체를 스킵.

### 4-3. 기존 컴포넌트 수정
- `TermRecommendationService` (`:126` `llmInferRecommendation`)
  - exact-match 재사용 분기(:134~150) **유지**.
  - 그 외 분기: `LlmTermDraftGenerator` 우선 호출 → 성공 시 결과 사용.
  - try/catch: 실패 시 기존 규칙 로직 호출(메서드명 `heuristicInferRecommendation` 등으로 리네임, 동작 보존).
  - `warnings()` / `llmReasoning()` 에 LLM 사용 여부 반영.
- `SemanticSearchService` (`:21`) + `SemanticVectorIndex` + `LocalEmbeddingService`
  - `LocalEmbeddingService.embed/cosine` 호출부를 Spring AI `EmbeddingModel` + `SimpleVectorStore` 유사도 검색으로 교체.
  - `adjustedScore`(:39) 의 도메인 휴리스틱 가중치는 일단 유지(검색 품질 회귀 방지). 임베딩 교체 후 점수 분포 재검토.
  - `LocalEmbeddingService` 는 폴백/테스트용으로 유지하거나 단계적 제거(아래 위험 참고).

### 4-4. 프론트엔드
- **변경 없음.** 응답 스키마(`RecommendedTermDraft`, `TermRecommendationResponse`) 불변 유지가 전제.
- `recommendCreateForm`/`recommendCandidateForm` 그대로 동작.
- (선택) `warnings` 에 LLM 미사용 메시지가 올 때 UI 표시 개선은 후속 과제.

## 5. 에러 처리

- LLM 타임아웃 / 5xx / 네트워크 오류 → catch → 휴리스틱 폴백.
- API 키 미설정 → LLM 스킵 → 휴리스틱 폴백 (부팅/런타임 정상).
- 구조화 출력 파싱 실패 → catch → 휴리스틱 폴백.
- 폴백 발생 시 `warnings` 에 "LLM 미사용, 규칙기반 추천값" 명시 → 사용자가 신뢰도 판단 가능.
- 임베딩 모델 로드 실패 → 기동 로그 경고 + `LocalEmbeddingService` 폴백(가능하면).

## 6. 테스트 계획 (TDD)

- `LlmTermDraftGenerator` 단위테스트: `ChatModel` mock → 정상 JSON 파싱, 잘못된 JSON, 예외 발생 케이스.
- `TermRecommendationService` 테스트:
  - exact-match 경로 → LLM 호출 안 함 검증.
  - LLM 성공 경로 → LLM 결과 사용.
  - LLM 실패(예외) 경로 → 휴리스틱 결과로 폴백 + `warnings` 포함.
  - 키 미설정 경로 → 휴리스틱 폴백.
- `SemanticSearchService` 테스트: 임베딩 교체 후 기존 시나리오(주문일자/주문일시 등) 추천 순위 회귀 검증.
- LLM 실호출 통합테스트는 키 의존 → CI 기본 제외, 옵트인 프로파일로 분리.

## 7. 단계별 구현 순서

1. 빌드 설정: Spring AI BOM + anthropic/transformers 스타터 추가, 저장소 확인, 컴파일 통과.
2. `application.yml` 신규 작성, 키 미설정 부팅 검증.
3. 임베딩 교체: `SemanticSearchService` 를 `EmbeddingModel`+`SimpleVectorStore` 로 전환, 검색 회귀 테스트.
4. `LlmTermDraftGenerator` 신규 작성 + 단위테스트(mock).
5. `TermRecommendationService` 분기 통합(LLM 우선 → 휴리스틱 폴백) + 테스트.
6. 수동 검증: 프론트에서 "추천" 클릭 → 키 있음(LLM)/키 없음(폴백) 양쪽 동작 확인.
7. `warnings` 메시지/`llmReasoning` 문구 정리.

## 8. 위험 / 미해결

- **Spring AI 버전 호환**: Boot 3.3.5 ↔ Spring AI 1.0.x 버전 매트릭스 확인 필요. milestone 저장소 추가 여부 검증.
- **transformers ONNX 모델 다운로드**: 최초 기동 시 모델 fetch → 오프라인/CI 환경 캐싱 전략 필요.
- **인메모리 VectorStore**: 데이터 영속화 도입 시 재인덱싱 전략 별도 설계. 현 범위 밖.
- **`LocalEmbeddingService` 제거 여부**: 임베딩 교체 안정화 전까지는 유지(폴백/회귀 안전망), 안정화 후 정리.
- **비용/지연**: 매 "추천"마다 LLM 호출 → exact/semantic 충분히 매칭되면 LLM 스킵하는 임계값 검토 가능(후속 튜닝).
