# Local Infra 구축 계획

로컬 개발용 데이터베이스 및 기본 인프라를 `docker-compose`로 일괄 기동하기 위한 계획.

## 1. 목표

- 단일 `docker-compose.yaml`로 개발 환경 전체 기동/종료
- 구성 요소: **PostgreSQL(+pgvector)**, **Redis**, **Neo4j**
- RAG 의미 검색용 벡터 저장소를 PostgreSQL `pgvector` 확장으로 제공
- 데이터 영속화(volume), 헬스체크, 초기화 스크립트 포함
- `.env` 기반 자격증명/포트 관리, 시크릿 커밋 금지

## 2. 현황 분석

| 항목 | 현재 상태 | 영향 |
|---|---|---|
| 영속화 | JPA/Datasource 의존성 없음. 상태 in-memory | PostgreSQL 도입 시 backend 의존성/설정 추가 필요 |
| 벡터 검색 | `SemanticVectorIndex` in-memory List, 코사인 Kotlin 계산 | pgvector 또는 in-memory 유지 선택 (아래 5절) |
| 임베딩 | Spring AI transformers(ONNX) 로컬 | 외부 서비스 불필요 |
| LLM | Spring AI, 기본 `ollama` (`localhost:11434`) | Ollama는 호스트 실행 가정. 컨테이너화는 선택 |
| 그래프 | Graphify 용어 관계/영향도 분석 | Neo4j 적재 대상 |

## 3. 서비스 구성

| 서비스 | 이미지 | 포트(host:container) | 용도 |
|---|---|---|---|
| postgres | `pgvector/pgvector:pg16` | `5432:5432` | 메인 RDB + 벡터 저장소 |
| redis | `redis:7-alpine` | `6379:6379` | 캐시 / 세션 / 임시 큐 |
| neo4j | `neo4j:5-community` | `7474:7474`(HTTP), `7687:7687`(Bolt) | Graphify 관계/영향도 그래프 |

> RAG 전용 서비스는 별도 추가하지 않음. 임베딩은 backend(ONNX)에서 생성하고, 벡터는 pgvector에 저장한다. 별도 벡터 DB가 필요하면 6절 대안 참고.

## 4. 디렉토리 구조

```
infra/local/
├── plan.md                  # 본 문서
├── docker-compose.yaml      # 서비스 정의
├── .env.example             # 환경변수 템플릿 (커밋)
├── .env                     # 실제 값 (gitignore)
└── init/
    ├── postgres/
    │   └── 01-init.sql      # pgvector extension, 스키마/role 초기화
    └── neo4j/
        └── (선택) seed.cypher
```

`infra/local/.gitignore` 에 `.env` 추가.

## 5. PostgreSQL + pgvector

- 이미지 `pgvector/pgvector:pg16` 사용 시 `CREATE EXTENSION vector` 가능
- `init/postgres/01-init.sql`:
  ```sql
  CREATE EXTENSION IF NOT EXISTS vector;
  -- 애플리케이션 스키마는 backend 마이그레이션(Flyway 등)에 위임 권장
  ```
- backend 연동(후속 작업):
  - `build.gradle.kts`에 `spring-boot-starter-data-jpa`, `org.postgresql:postgresql`, (벡터 저장 시) `spring-ai-starter-vector-store-pgvector` 추가
  - `application.yml`에 `spring.datasource.*`, `spring.ai.vectorstore.pgvector.*` 추가
  - 환경변수로 주입: `POSTGRES_URL`, `POSTGRES_USER`, `POSTGRES_PASSWORD`

## 6. RAG 벡터 저장 — 결정 사항

옵션 비교(선택 필요):

| 옵션 | 장점 | 단점 |
|---|---|---|
| **A. pgvector (권장)** | 서비스 1개, 트랜잭션 일관성, 운영 단순 | 초대규모 벡터엔 한계 |
| B. Qdrant 별도 컨테이너 | 벡터 특화 성능/필터 | 서비스 추가, 동기화 부담 |
| C. in-memory 유지 | 변경 없음 | 재기동 시 인덱스 소실, 확장 불가 |

→ MVP 로컬 개발은 **옵션 A(pgvector)** 채택. Qdrant는 필요 시 6절-B로 확장.

## 7. 환경변수 (.env.example)

```dotenv
# PostgreSQL
POSTGRES_DB=aulms
POSTGRES_USER=aulms
POSTGRES_PASSWORD=changeme
POSTGRES_PORT=5432

# Redis
REDIS_PORT=6379

# Neo4j
NEO4J_AUTH=neo4j/changeme
NEO4J_HTTP_PORT=7474
NEO4J_BOLT_PORT=7687
```

## 8. 헬스체크 / 의존성

- 각 서비스 `healthcheck` 정의 (pg_isready, redis-cli ping, neo4j cypher-shell)
- backend는 컨테이너에 포함하지 않음 (호스트에서 `./gradlew bootRun`). DB만 docker로 띄움
- named volume으로 데이터 영속화: `pgdata`, `redisdata`, `neo4jdata`

## 9. 실행 절차

```bash
cd infra/local
cp .env.example .env        # 값 수정
docker compose up -d        # 기동
docker compose ps           # 상태 확인
docker compose down         # 종료 (-v 로 볼륨까지 삭제)
```

## 10. 작업 단계 (Task Breakdown)

1. `infra/local/.env.example`, `.gitignore` 작성
2. `infra/local/init/postgres/01-init.sql` 작성 (pgvector extension)
3. `infra/local/docker-compose.yaml` 작성 (postgres/redis/neo4j + volume + healthcheck)
4. `docker compose up -d` 로 기동 검증, 각 헬스체크 통과 확인
5. (후속) backend 의존성/`application.yml` 연동 — 별도 PR
6. (후속) Neo4j seed / Flyway 마이그레이션 도입

## 11. 비고

- Ollama: 컨테이너화 가능하나 GPU/모델 용량 이유로 호스트 실행 유지 권장. 필요 시 `ollama/ollama` 서비스 추가 가능
- 시크릿: `.env`는 절대 커밋 금지. 운영 환경은 별도 시크릿 관리자 사용
