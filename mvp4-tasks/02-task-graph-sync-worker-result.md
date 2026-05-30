# Task 02 결과: Graphify 동기화 Worker 구현

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, 데이터 사전의 용어 데이터를 Graphify 그래프로 동기화하는 worker를 구현했다.

## 구현 파일

- `apps/backend/src/main/kotlin/com/aulms/graph/GraphModel.kt`
- `apps/backend/src/main/kotlin/com/aulms/graph/InMemoryGraphStore.kt`
- `apps/backend/src/main/kotlin/com/aulms/graph/GraphSyncWorker.kt`
- `apps/backend/src/test/kotlin/com/aulms/graph/GraphSyncWorkerTest.kt`

## TermRepository 확장

- `TERM_RELATIONSHIP` 대응용 `TermRelationshipRecord` 추가
- 샘플 관계 추가
  - 고객번호 `usedWith` 주문번호
  - 주문번호 `relatedTo` 주문일자
  - 주문번호 `relatedTo` 주문금액
  - 주문번호 `relatedTo` 주문상태코드

## 동기화 대상

- `TERM_MASTER`
  - `Term` 노드 생성
  - `Domain` 노드 생성
  - `Term belongsTo Domain` 관계 생성
- `TERM_EXPRESSION`
  - `Column` 후보 노드 생성
  - `APIField` 후보 노드 생성
  - `DTOField` 후보 노드 생성
  - 각 표현 노드의 `represents Term` 관계 생성
- `TERM_ALIAS`
  - `Alias` 노드 생성
  - `synonym`, `forbidden`, `deprecatedBy`, `mentions` 관계 생성
- `TERM_RELATIONSHIP`
  - 용어 간 관계 생성

## 동기화 기능

- 증분 동기화
  - `syncIncremental()`
  - `syncKey`와 `checksum` 기준 upsert
- 전체 재동기화
  - `syncFull()`
  - node/edge snapshot 교체
- 실패 재시도
  - `maxRetries` 기준 재시도
- 동기화 로그
  - `GraphSyncLog` 저장

## 문서

- `docs/graph/graph-sync-worker.md`

## 완료 기준 확인

테스트에서 다음 그래프 요소를 검증했다.

```text
term:T-000001
domain:고객
column:DICTIONARY.고객.CUST_NO
apiField:DICTIONARY.고객.customerNumber
alias:A-000001
REPRESENTS:column:DICTIONARY.고객.CUST_NO:term:T-000001
REPRESENTS:apiField:DICTIONARY.고객.customerNumber:term:T-000001
SYNONYM:alias:A-000001:term:T-000001
FORBIDDEN:alias:A-000006:term:T-000001
```

## 검증 명령

```bash
gradle test
npm run typecheck
```

## 검증 결과

- `gradle test` 성공
- `npm run typecheck` 성공
