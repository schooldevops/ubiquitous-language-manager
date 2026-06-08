// Phase 0 — 제약·인덱스 (Neo4j 5.x)
CREATE CONSTRAINT actor_id   IF NOT EXISTS FOR (n:Actor)      REQUIRE n.id   IS UNIQUE;
CREATE CONSTRAINT uc_id      IF NOT EXISTS FOR (n:UseCase)    REQUIRE n.id   IS UNIQUE;
CREATE CONSTRAINT proc_id    IF NOT EXISTS FOR (n:Process)    REQUIRE n.id   IS UNIQUE;
CREATE CONSTRAINT fn_id      IF NOT EXISTS FOR (n:Function)   REQUIRE n.id   IS UNIQUE;
CREATE CONSTRAINT pol_id     IF NOT EXISTS FOR (n:Policy)     REQUIRE n.id   IS UNIQUE;
CREATE CONSTRAINT polid_id   IF NOT EXISTS FOR (n:PolicyItem) REQUIRE n.id   IS UNIQUE;
CREATE CONSTRAINT state_code IF NOT EXISTS FOR (n:State)      REQUIRE n.code IS UNIQUE;
CREATE CONSTRAINT term_id    IF NOT EXISTS FOR (n:Term)       REQUIRE n.id   IS UNIQUE;

CREATE INDEX proc_uc   IF NOT EXISTS FOR (n:Process)    ON (n.useCaseId);
CREATE INDEX pol_name  IF NOT EXISTS FOR (n:Policy)     ON (n.name);
CREATE INDEX polid_name IF NOT EXISTS FOR (n:PolicyItem) ON (n.name);
