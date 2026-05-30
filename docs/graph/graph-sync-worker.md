# Graphify 동기화 Worker

## 1. 목적

`GraphSyncWorker`는 데이터 사전의 용어 데이터를 Graphify 그래프 노드와 관계로 변환한다.

MVP4에서는 관계형 DB 대신 현재 백엔드의 `TermRepository` 인메모리 데이터 사전을 원천으로 사용한다. PostgreSQL 전환 후에는 같은 변환 규칙을 `term_master`, `term_expression`, `term_alias`, `term_relationship` 조회 결과에 적용한다.

## 2. 구현 파일

| 파일 | 설명 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/graph/GraphModel.kt` | Graph node, edge, sync result, sync log 모델 |
| `apps/backend/src/main/kotlin/com/aulms/graph/InMemoryGraphStore.kt` | MVP용 graph 저장소와 sync log 저장소 |
| `apps/backend/src/main/kotlin/com/aulms/graph/GraphSyncWorker.kt` | 데이터 사전 → Graphify graph 동기화 worker |
| `apps/backend/src/test/kotlin/com/aulms/graph/GraphSyncWorkerTest.kt` | 샘플 데이터 동기화 테스트 |

## 3. 동기화 입력

| 원천 | 처리 |
|---|---|
| `TERM_MASTER` | `Term` 노드와 `Domain` 노드 생성 |
| `TERM_EXPRESSION` | `Column`, `APIField`, `DTOField` 후보 노드 생성 |
| `TERM_ALIAS` | `Alias` 노드와 `synonym`, `forbidden`, `deprecatedBy`, `mentions` 관계 생성 |
| `TERM_RELATIONSHIP` | 용어 간 `usedWith`, `relatedTo`, `deprecatedBy` 등 관계 생성 |

## 4. 노드 변환

| 데이터 | 노드 |
|---|---|
| `TERM_MASTER.term_id` | `term:{termId}` |
| `TERM_MASTER.domain_name` | `domain:{domainName}` |
| `TERM_EXPRESSION.DB_COLUMN` | `column:DICTIONARY.{domainName}.{expressionValue}` |
| `TERM_EXPRESSION.API_FIELD` | `apiField:DICTIONARY.{domainName}.{expressionValue}` |
| `TERM_EXPRESSION.CODE_VARIABLE` | `dtoField:DICTIONARY.{domainName}.{expressionValue}` |
| `TERM_ALIAS.alias_id` | `alias:{aliasId}` |

## 5. 관계 변환

| 데이터 | 관계 |
|---|---|
| `TERM_MASTER.domain_name` | `Term -> belongsTo -> Domain` |
| `TERM_EXPRESSION.DB_COLUMN` | `Column -> represents -> Term` |
| `TERM_EXPRESSION.API_FIELD` | `APIField -> represents -> Term` |
| `TERM_EXPRESSION.CODE_VARIABLE` | `DTOField -> represents -> Term` |
| `TERM_ALIAS.Synonym` | `Alias -> synonym -> Term` |
| `TERM_ALIAS.Forbidden` | `Alias -> forbidden -> Term` |
| `TERM_ALIAS.Deprecated` | `Alias -> deprecatedBy -> Term` |
| `TERM_ALIAS.NeedsContext` | `Alias -> mentions -> Term` |
| `TERM_RELATIONSHIP` | `Term -> {relationshipType} -> Term` |

## 6. 증분 동기화

`syncIncremental()`은 snapshot의 node/edge를 `syncKey`와 `checksum` 기준으로 upsert한다.

- 신규 `syncKey`: 생성
- 기존 `syncKey`, 동일 checksum: skip
- 기존 `syncKey`, 변경 checksum: update

## 7. 전체 재동기화

`syncFull()`은 그래프 저장소의 node/edge를 snapshot으로 교체한다. sync log는 유지한다.

## 8. 실패 재시도

`syncIncremental(maxRetries)`와 `syncFull(maxRetries)`는 내부 예외 발생 시 최대 `maxRetries`만큼 재시도한다. 모든 재시도가 실패하면 실패 sync log를 저장한다.

## 9. 동기화 로그

`GraphSyncLog`는 다음 정보를 저장한다.

| 필드 | 설명 |
|---|---|
| `mode` | `INCREMENTAL` 또는 `FULL` |
| `success` | 성공 여부 |
| `createdNodes` | 생성된 node 수 |
| `updatedNodes` | 갱신된 node 수 |
| `createdEdges` | 생성된 edge 수 |
| `updatedEdges` | 갱신된 edge 수 |
| `skippedItems` | 변경 없음으로 skip된 수 |
| `retryCount` | 재시도 횟수 |
| `message` | 결과 메시지 |

## 10. 고객번호 동기화 예시

| 그래프 요소 | key |
|---|---|
| Term | `term:T-000001` |
| Domain | `domain:고객` |
| Column | `column:DICTIONARY.고객.CUST_NO` |
| APIField | `apiField:DICTIONARY.고객.customerNumber` |
| Alias | `alias:A-000001` |
| Column relation | `REPRESENTS:column:DICTIONARY.고객.CUST_NO:term:T-000001` |
| APIField relation | `REPRESENTS:apiField:DICTIONARY.고객.customerNumber:term:T-000001` |
| Synonym relation | `SYNONYM:alias:A-000001:term:T-000001` |
| Forbidden relation | `FORBIDDEN:alias:A-000006:term:T-000001` |

이 구조로 고객번호, 고객ID 별칭, CUST_NO 컬럼, customerNumber API 필드를 그래프에 동기화할 수 있다.
