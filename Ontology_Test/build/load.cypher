// ============================================================
// Phase 2/3 — 노드 + 관계 적재 (Neo4j 5.x)
// 선행: constraints.cypher 실행, CSV를 Neo4j import 디렉토리에 배치
//   nodes/*.csv, rels/*.csv  (또는 file:/// 절대경로 조정)
// 멱등: 전부 MERGE. 재실행 안전.
// ============================================================

// ---------- 노드 ----------
LOAD CSV WITH HEADERS FROM 'file:///nodes/actors.csv' AS r
MERGE (n:Actor {id:r.id}) SET n.name=r.name, n.desc=r.desc;

LOAD CSV WITH HEADERS FROM 'file:///nodes/usecases.csv' AS r
MERGE (n:UseCase {id:r.id})
SET n.name=r.name, n.desc=r.desc, n.actorName=r.actorName,
    n.isProcessDefined=r.isProcessDefined;

LOAD CSV WITH HEADERS FROM 'file:///nodes/states.csv' AS r
MERGE (n:State {code:r.code}) SET n.name=r.name, n.definition=r.definition, n.followUp=r.followUp;

LOAD CSV WITH HEADERS FROM 'file:///nodes/processes.csv' AS r
MERGE (n:Process {id:r.id})
SET n.name=r.name, n.desc=r.desc, n.useCaseId=r.useCaseId,
    n.seq=toInteger(r.seq), n.actor=r.actor, n.entryCond=r.entryCond, n.exitCond=r.exitCond;

LOAD CSV WITH HEADERS FROM 'file:///nodes/functions.csv' AS r
MERGE (n:Function {id:r.id})
SET n.name=r.name, n.desc=r.desc,
    n.inputs       = CASE WHEN r.inputs       = '' THEN [] ELSE split(r.inputs,'|')       END,
    n.stateActions = CASE WHEN r.stateActions = '' THEN [] ELSE split(r.stateActions,'|') END,
    n.subFunctions = CASE WHEN r.subFunctions = '' THEN [] ELSE split(r.subFunctions,'|') END,
    n.outputs      = CASE WHEN r.outputs      = '' THEN [] ELSE split(r.outputs,'|')      END,
    n.exceptions   = CASE WHEN r.exceptions   = '' THEN [] ELSE split(r.exceptions,'|')   END;

LOAD CSV WITH HEADERS FROM 'file:///nodes/policies.csv' AS r
MERGE (n:Policy {id:r.id}) SET n.name=r.name, n.desc=r.desc;

LOAD CSV WITH HEADERS FROM 'file:///nodes/policy_items.csv' AS r
MERGE (n:PolicyItem {id:r.id})
SET n.name=r.name, n.value=r.value, n.valueType=r.valueType,
    n.valueNum=CASE WHEN r.valueNum='' THEN null ELSE toInteger(r.valueNum) END,
    n.valueUnit=r.valueUnit, n.policyId=r.policyId;

LOAD CSV WITH HEADERS FROM 'file:///nodes/terms.csv' AS r
MERGE (n:Term {id:r.id}) SET n.name=r.name, n.definition=r.definition;

// ---------- 관계 ----------
LOAD CSV WITH HEADERS FROM 'file:///rels/rel_has_process.csv' AS r
MATCH (u:UseCase {id:r.useCaseId}),(p:Process {id:r.processId})
MERGE (u)-[x:HAS_PROCESS]->(p) SET x.seq=toInteger(r.seq);

LOAD CSV WITH HEADERS FROM 'file:///rels/rel_next.csv' AS r
MATCH (a:Process {id:r.fromProcessId}),(b:Process {id:r.toProcessId})
MERGE (a)-[:NEXT]->(b);

// PERFORMED_BY: subject = UseCase 또는 Process
LOAD CSV WITH HEADERS FROM 'file:///rels/rel_performed_by.csv' AS r
MATCH (a:Actor {id:r.actorId})
MATCH (s) WHERE (s:UseCase OR s:Process) AND s.id=r.subjectId
MERGE (s)-[:PERFORMED_BY]->(a);

LOAD CSV WITH HEADERS FROM 'file:///rels/rel_uses.csv' AS r
MATCH (p:Process {id:r.processId}),(f:Function {id:r.functionId})
MERGE (p)-[:USES]->(f);

LOAD CSV WITH HEADERS FROM 'file:///rels/rel_governed_by.csv' AS r
MATCH (p:Process {id:r.processId}),(pol:Policy {id:r.policyId})
MERGE (p)-[:GOVERNED_BY]->(pol);

LOAD CSV WITH HEADERS FROM 'file:///rels/rel_has_item.csv' AS r
MATCH (pol:Policy {id:r.policyId}),(pi:PolicyItem {id:r.policyItemId})
MERGE (pol)-[:HAS_ITEM]->(pi);

LOAD CSV WITH HEADERS FROM 'file:///rels/rel_regulated_by.csv' AS r
MATCH (f:Function {id:r.functionId}),(pol:Policy {id:r.policyId})
MERGE (f)-[x:REGULATED_BY]->(pol) SET x.source='derived';

// TRANSITIONS_TO: 구체 전이만 (scope<>'ALL' AND toCode<>'')
LOAD CSV WITH HEADERS FROM 'file:///rels/rel_transitions.csv' AS r
WITH r WHERE r.scope<>'ALL' AND r.toCode<>''
MATCH (a:State {code:r.fromCode}),(b:State {code:r.toCode})
MERGE (a)-[x:TRANSITIONS_TO {event:r.event}]->(b)
SET x.handler=r.handler, x.scope=r.scope,
    x.triggerProcessId=CASE WHEN r.triggerProcessId='' THEN null ELSE r.triggerProcessId END;

LOAD CSV WITH HEADERS FROM 'file:///rels/rel_refers_state.csv' AS r
MATCH (pi:PolicyItem {id:r.policyItemId}),(s:State {code:r.stateCode})
MERGE (pi)-[x:REFERS_STATE]->(s) SET x.source='derived';
