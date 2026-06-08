# -*- coding: utf-8 -*-
"""Phase 5: Neo4j 미가동 환경에서 graph.json에 검증규칙 실행 -> report.md."""
import json, os, collections

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
g = json.load(open(os.path.join(BASE, "graph.json"), encoding="utf-8"))
NODES, RELS = g["nodes"], g["rels"]

procs = NODES["Process"]; pols = NODES["Policy"]; ucs = NODES["UseCase"]; pis = NODES["PolicyItem"]

# adjacency
performed = {s for s, a in RELS["PERFORMED_BY"]}
governed = collections.defaultdict(list)
for p, pol in RELS["GOVERNED_BY"]: governed[p].append(pol)
gov_targets = {pol for _, pol in RELS["GOVERNED_BY"]}
uses = {p for p, f in RELS["USES"]}
has_item_src = {pol for pol, pi in RELS["HAS_ITEM"]}
has_proc_src = {u for u, p, s in RELS["HAS_PROCESS"]}

# state reachability from MBR_NONE (concrete transitions)
adj = collections.defaultdict(set)
for fr, to, ev, h, sc, trig in RELS["TRANSITIONS_TO"]:
    if sc != "ALL" and to:
        adj[fr].add(to)
reach = set(); stack = ["MBR_NONE"]
while stack:
    n = stack.pop()
    for m in adj[n]:
        if m not in reach:
            reach.add(m); stack.append(m)

R = []
def add(rule, items, desc, ok_if_empty=True):
    R.append((rule, desc, items, ok_if_empty))

add("R1", [p for p in procs if p not in performed], "액터 없는 프로세스")
add("R2", [p for p in procs if not governed.get(p)], "정책 미적용 프로세스")
add("R3", [pol for pol in pols if pol not in gov_targets], "고립 정책")
add("R4", [s for s in NODES["State"] if s != "MBR_NONE" and s not in reach], "미가입에서 도달불가 상태")
add("R5", [p for p in procs if "본인인증" in procs[p].get("name", "")
           and not any(x.startswith("PG-MBR-AUTH") for x in governed.get(p, []))],
    "본인인증 프로세스인데 인증정책 미연결")
add("R6", [p for p in procs if p not in uses], "기능 미사용 프로세스")
add("R7", [pol for pol in pols if pol not in has_item_src], "정책항목 없는 정책")
add("R9", [u for u in ucs if ucs[u].get("isProc") == "Y" and u not in has_proc_src],
    "isProcessDefined=Y 인데 프로세스 없음")

# dangling ref integrity (모든 rel endpoint 가 노드에 존재?)
def keyset(lbl): return set(NODES[lbl].keys())
allids = keyset("Actor") | keyset("UseCase") | keyset("Process") | keyset("Function") | keyset("Policy") | keyset("PolicyItem")
states = keyset("State")
dangling = []
for p, f in RELS["USES"]:
    if p not in procs or f not in NODES["Function"]: dangling.append(("USES", p, f))
for p, pol in RELS["GOVERNED_BY"]:
    if p not in procs or pol not in pols: dangling.append(("GOVERNED_BY", p, pol))
for pol, pi in RELS["HAS_ITEM"]:
    if pol not in pols or pi not in pis: dangling.append(("HAS_ITEM", pol, pi))
add("R0", dangling, "댕글링 관계(노드 없는 참조)")

# R8 유예기간 값
yebos = [(pi, pis[pi]["value"]) for pi in pis if "유예 기간" in pis[pi]["name"]]
# R10 고위험 인증 추론
r10 = ("PR-MBR-CS-003-01" in procs and "PG-MBR-AUTH-008" in governed.get("PR-MBR-CS-003-01", []))

# ---- report ----
out = ["# 온톨로지 검증 리포트 (Phase 5)", "",
       "graph.json 기준 자동 검증. PASS=위반 0건.", ""]
out.append("| 규칙 | 설명 | 위반 | 판정 |")
out.append("|---|---|---|---|")
allpass = True
for rule, desc, items, ok in R:
    n = len(items); verdict = "✅ PASS" if (n == 0) == ok else "⚠️ CHECK"
    if n != 0 and ok: allpass = False
    sample = "" if n == 0 else " · " + ", ".join(map(str, items[:6])) + ("…" if n > 6 else "")
    out.append(f"| {rule} | {desc} | {n}{sample} | {verdict} |")
out += ["",
        f"**R8 유예기간 정책값**: " + ("; ".join(f"{k}={v}" for k, v in yebos) or "없음"),
        f"**R10 탈퇴 본인인증→고위험인증정책(PG-MBR-AUTH-008)**: {'적용됨 ✅' if r10 else '미적용 ⚠️ (정책 갭 후보)'}",
        "",
        f"## 종합: {'전체 통과 ✅' if allpass else '주의 항목 존재 ⚠️ (아래 해석 참조)'}",
        ]

# 해석 노트
notes = []
r2 = [p for p in procs if not governed.get(p)]
r6 = [p for p in procs if p not in uses]
r7 = [pol for pol in pols if pol not in has_item_src]
if r2: notes.append(f"- R2: {r2} — BSS 자동판정 등 정책 비귀속 프로세스일 수 있음(소스 6.가에 해당 PR 목록 없음).")
if r6: notes.append(f"- R6: {r6} — 단순 안내/결과 프로세스로 기능 비귀속 가능.")
if r7: notes.append(f"- R7: {r7} — 6.나 정책상세가 없는 정책. 소스 보강 필요 후보.")
r4 = [s for s in NODES["State"] if s != "MBR_NONE" and s not in reach]
if r4: notes.append(f"- R4: {r4} — 정상 흐름 미도달. 단, 상태확인불가는 'ALL' 오류 메타전이로만 진입하는 설계상 정상(결함 아님).")
if not r10: notes.append("- R10: 탈퇴 1차 본인인증(003-01)은 PG-MBR-AUTH-008 미연결. 추가인증은 '탈퇴 최종동의 전' 적용(정책 설계상 정상) → 003-04 단계 확인 권장.")
if notes:
    out += ["", "## 위반/주의 해석"] + notes

rep = os.path.join(BASE, "report.md")
open(rep, "w", encoding="utf-8").write("\n".join(out) + "\n")
print("\n".join(out))
