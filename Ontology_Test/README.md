# 회원 온톨로지 (Property Graph)

[member-ontology.md](member-ontology.md) 정책서 → Neo4j Property Graph 온톨로지. 계획: [member-ontology-plan.md](member-ontology-plan.md).

## 구성 결과 (실측)

노드 **636** / 관계 **745** (코어+보조 전체)

| 라벨 | 건수 | | 관계 | 건수 |
|---|---|---|---|---|
| Actor | 4 | | HAS_PROCESS | 22 |
| UseCase | 13 | | NEXT | 18 |
| State | 8 | | PERFORMED_BY | 42 |
| Process | 22 | | USES | 43 |
| Function | 29 | | GOVERNED_BY | 71 |
| Policy | 44 | | HAS_ITEM | 475 |
| PolicyItem | 475 | | TRANSITIONS_TO | 18 |
| Term | 41 | | REGULATED_BY | 13 |
| | | | REFERS_STATE | 43 |

검증: [report.md](report.md) — 댕글링 0, 무결성 규칙 통과 (R4 MBR_UNKNOWN은 설계상 정상).

## 디렉토리

```
nodes/*.csv          노드 (라벨별)
rels/*.csv           관계 (타입별)
graph.json           전체 그래프 (viz/검증 소스)
schema.md            스키마 동결본
report.md            검증 리포트
integration-map.md   BSS 통합 매핑 시드
viz.html             그래프 시각화 (브라우저로 열기)
build/
  extract.py         md -> CSV+graph.json (Phase 1·4)
  validate.py        graph.json 검증 -> report.md (Phase 5)
  gen_artifacts.py   integration-map.md + viz.html (Phase 7)
  constraints.cypher 제약·인덱스 (Phase 0)
  load.cypher        노드+관계 적재 (Phase 2·3)
  validation.cypher  검증 규칙 (Phase 5)
  queries.cypher     질의 템플릿 (Phase 6)
```

## 재생성 (소스 변경 시)

```bash
python3 build/extract.py        # md -> CSV + graph.json
python3 build/validate.py       # -> report.md
python3 build/gen_artifacts.py  # -> integration-map.md + viz.html
```

## Neo4j 적재

```bash
# CSV(nodes/, rels/)를 Neo4j import 디렉토리로 복사 후
cypher-shell -f build/constraints.cypher
cypher-shell -f build/load.cypher
cypher-shell -f build/validation.cypher   # 위반 0건 기대
```

## 빠른 확인

`viz.html` 더블클릭 → 코어 그래프(120 노드/213 엣지) 인터랙티브 시각화. PolicyItem·Term 제외.
