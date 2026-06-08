# 회원 온톨로지 스키마 (동결본)

Property Graph (Neo4j 5.x). 노드 키 = 소스 ID. 제약: [build/constraints.cypher](build/constraints.cypher).

## 노드 라벨 (실측 건수)

| 라벨 | 키 | 건수 | 속성 |
|---|---|---|---|
| `:Actor` | id | 4 | name, desc |
| `:UseCase` | id | 13 | name, desc, actorName, isProcessDefined |
| `:State` | code | 8 | name, definition, followUp |
| `:Process` | id | 22 | name, desc, useCaseId, seq, actor, entryCond, exitCond |
| `:Function` | id | 29 | name, desc, inputs[], stateActions[], subFunctions[], outputs[], exceptions[] |
| `:Policy` | id | 44 | name, desc |
| `:PolicyItem` | id | 475 | name, value, valueType(text/scalar/enum), valueNum, valueUnit, policyId |
| `:Term` | id | 41 | name, definition |

배열 속성은 CSV에서 `|` 구분 저장 → 적재 시 `split(x,'|')`.

## 관계 타입 (실측 건수)

| 관계 | 시작→끝 | 건수 | 속성 |
|---|---|---|---|
| `HAS_PROCESS` | UseCase→Process | 22 | seq |
| `NEXT` | Process→Process | 18 | — |
| `PERFORMED_BY` | UseCase/Process→Actor | 42 | — |
| `USES` | Process→Function | 43 | — |
| `GOVERNED_BY` | Process→Policy | 71 | — |
| `HAS_ITEM` | Policy→PolicyItem | 475 | — |
| `REGULATED_BY` | Function→Policy | 13 | source=derived(명칭매칭) |
| `TRANSITIONS_TO` | State→State | 18(+ALL 메타) | event, handler, scope, triggerProcessId |
| `REFERS_STATE` | PolicyItem→State | 43 | source=derived |

## 권위 출처 원칙

- Process↔Function = 소스 5.가 FN-ID 목록 (`USES`)
- Process↔Policy = 소스 6.가 PG-ID 목록 (`GOVERNED_BY`)
- 상세표 서술명은 `REGULATED_BY`(보조)에만 사용. 미매칭 61건은 [graph.json](graph.json) `unmatched_fn_policy`에 보존.

## 메타 전이

`전체 상태` 행 → `fromCode='ALL', scope='ALL'` (상태조회실패·중복요청). 구체 State→State 아님. 적재 시 분리 처리.
