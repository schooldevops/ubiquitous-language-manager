// ============================================================
// Phase 6 — 질의·Q&A 템플릿 (설계참조 · 검색 · 추론)
// ============================================================

// Q1. 유즈케이스 전체 흐름 (순서 + 액터 + 적용정책)
MATCH (u:UseCase {id:$ucId})-[h:HAS_PROCESS]->(p:Process)
OPTIONAL MATCH (p)-[:PERFORMED_BY]->(a:Actor)
OPTIONAL MATCH (p)-[:GOVERNED_BY]->(pol:Policy)
RETURN h.seq AS seq, p.name AS process,
       collect(DISTINCT a.name) AS actors,
       collect(DISTINCT pol.name) AS policies
ORDER BY seq;
// 예: $ucId='US-MBR-CS-001'

// Q2. 특정 기능을 사용하는 모든 프로세스 (재사용 추적)
MATCH (p:Process)-[:USES]->(f:Function {id:$fnId})
RETURN f.name, collect(p.id) AS usedBy;
// 예: $fnId='FN-MBR-COM-002' (본인인증 처리)

// Q3. 정책값 직답 (RAG 근거 포함)
MATCH (pol:Policy)-[:HAS_ITEM]->(pi:PolicyItem)
WHERE pi.name CONTAINS $kw
RETURN pi.id AS evidence, pol.name AS policy, pi.name AS item, pi.value AS value;
// 예: $kw='유예 기간'

// Q4. 상태 전이 경로 + 트리거 프로세스
MATCH path=(:State {code:$from})-[:TRANSITIONS_TO*1..6]->(:State {code:$to})
RETURN [n IN nodes(path) | n.code] AS states,
       [r IN relationships(path) | r.event] AS events,
       [r IN relationships(path) | r.triggerProcessId] AS triggers
LIMIT 10;
// 예: $from='MBR_ACTIVE', $to='MBR_WITHDRAWN'

// Q5. 프로세스 1-홉 서브그래프 (RAG 컨텍스트 텍스트화 대상)
MATCH (p:Process {id:$pid})
OPTIONAL MATCH (p)-[:USES]->(f:Function)
OPTIONAL MATCH (p)-[:GOVERNED_BY]->(pol:Policy)
OPTIONAL MATCH (p)-[:PERFORMED_BY]->(a:Actor)
OPTIONAL MATCH (u:UseCase)-[:HAS_PROCESS]->(p)
RETURN p, u.name AS useCase, collect(DISTINCT a.name) AS actors,
       collect(DISTINCT f.name) AS functions, collect(DISTINCT pol.name) AS policies;

// Q6. 인증수단 정책 비교 (업무별 허용 인증수단)
MATCH (pol:Policy {id:'PG-MBR-AUTH-002'})-[:HAS_ITEM]->(pi:PolicyItem)
RETURN pi.name, pi.value ORDER BY pi.id;

// Q7. 정책 영향 분석 (정책 변경 시 영향받는 프로세스·유즈케이스)
MATCH (pol:Policy {id:$polId})<-[:GOVERNED_BY]-(p:Process)<-[:HAS_PROCESS]-(u:UseCase)
RETURN pol.name, collect(DISTINCT p.name) AS processes, collect(DISTINCT u.name) AS useCases;
// 예: $polId='PG-MBR-AUTH-001'

// Q8. 액터별 책임 프로세스
MATCH (a:Actor)<-[:PERFORMED_BY]-(p:Process)
RETURN a.name, count(p) AS processCount, collect(p.name) AS processes ORDER BY processCount DESC;

// Q9. 스칼라 정책값 전수 (수치 기준값 일람 — 설정 검토)
MATCH (pi:PolicyItem) WHERE pi.valueType='scalar'
RETURN pi.policyId, pi.name, pi.valueNum, pi.valueUnit ORDER BY pi.policyId;

// Q10. 추론: 고위험 업무 인증 재사용 충돌 점검
MATCH (p:Process)-[:GOVERNED_BY]->(:Policy {id:'PG-MBR-AUTH-008'})
RETURN p.id, p.name;  // 추가인증 적용 프로세스 = 탈퇴 계열
