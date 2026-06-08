// ============================================================
// Phase 5 — 무결성·정합성 검증 규칙 (Neo4j 5.x)
// 각 쿼리: 위반 항목 반환. 0건 = 통과.
// ============================================================

// R1. 액터 없는 프로세스
MATCH (p:Process) WHERE NOT (p)-[:PERFORMED_BY]->(:Actor) RETURN 'R1' AS rule, p.id;

// R2. 정책 미적용 프로세스
MATCH (p:Process) WHERE NOT (p)-[:GOVERNED_BY]->(:Policy) RETURN 'R2' AS rule, p.id;

// R3. 고립 정책 (어떤 프로세스도 적용 안 함)
MATCH (pol:Policy) WHERE NOT (:Process)-[:GOVERNED_BY]->(pol) RETURN 'R3' AS rule, pol.id;

// R4. 미가입(MBR_NONE)에서 도달 불가 상태
MATCH (s:State) WHERE s.code<>'MBR_NONE'
  AND NOT (:State {code:'MBR_NONE'})-[:TRANSITIONS_TO*]->(s)
RETURN 'R4' AS rule, s.code;

// R5. 본인인증 프로세스인데 인증정책(PG-MBR-AUTH-*) 미연결
MATCH (p:Process) WHERE p.name CONTAINS '본인인증'
  AND NOT EXISTS { (p)-[:GOVERNED_BY]->(pol:Policy) WHERE pol.id STARTS WITH 'PG-MBR-AUTH' }
RETURN 'R5' AS rule, p.id;

// R6. 기능 미사용 프로세스 (USES 없음)
MATCH (p:Process) WHERE NOT (p)-[:USES]->(:Function) RETURN 'R6' AS rule, p.id;

// R7. 정책항목 없는 정책 (HAS_ITEM 없음)
MATCH (pol:Policy) WHERE NOT (pol)-[:HAS_ITEM]->(:PolicyItem) RETURN 'R7' AS rule, pol.id;

// R8. 유예기간 정책값 일관성 (7일 단일 기대)
MATCH (pi:PolicyItem) WHERE pi.name CONTAINS '유예 기간' RETURN 'R8' AS rule, pi.id, pi.value;

// R9. isProcessDefined=Y 인데 HAS_PROCESS 없음
MATCH (u:UseCase) WHERE u.isProcessDefined='Y' AND NOT (u)-[:HAS_PROCESS]->()
RETURN 'R9' AS rule, u.id;

// R10. 추론: 탈퇴(고위험) 본인인증 프로세스가 추가인증정책(PG-MBR-AUTH-008) 적용받는지
MATCH (p:Process {id:'PR-MBR-CS-003-01'})
RETURN 'R10' AS rule, p.id,
  EXISTS { (p)-[:GOVERNED_BY]->(:Policy {id:'PG-MBR-AUTH-008'}) } AS hasHighRiskAuth;
