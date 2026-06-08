# -*- coding: utf-8 -*-
"""Phase 7: integration-map.md (코드/열거 정책값 시드) + viz.html (코어 그래프 임베드)."""
import json, os, html

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
g = json.load(open(os.path.join(BASE, "graph.json"), encoding="utf-8"))
N, R = g["nodes"], g["rels"]

# ---------------- integration-map.md ----------------
KW = ("코드", "인증수단", "상태", "채널", "보관 기간", "유효시간")
seeds = []
for pid, d in N["PolicyItem"].items():
    nm, val, vt = d["name"], d["value"], d["valueType"]
    if any(k in nm for k in KW) or vt == "enum":
        seeds.append((pid, d["policyId"], nm, val))
seeds.sort()

lines = ["# 데이터 통합 매핑 (Phase 7)", "",
         "온톨로지 노드/정책값 ↔ BSS 컬럼 ↔ 채널 필드. BSS/채널 열은 실제 스키마 확보 후 채움.", "",
         "## 1. 공통 어휘 (Term 기반 정렬 기준)", "",
         "| Term ID | 용어 | 통합 식별 역할 |", "|---|---|---|"]
for tid in ("TM-MBR-014", "TM-MBR-015", "TM-MBR-008", "TM-MBR-001"):
    if tid in N["Term"]:
        t = N["Term"][tid]; lines.append(f"| {tid} | {t['name']} | {t['definition'][:40]}… |")

lines += ["", "## 2. 코드·열거 정책값 매핑 시드 (자동 추출 " + str(len(seeds)) + "건)", "",
          "| 정책항목 ID | 소속정책 | 항목명 | 온톨로지 값(원문) | BSS 컬럼 | 채널 필드 |",
          "|---|---|---|---|---|---|"]
for pid, pol, nm, val in seeds:
    v = val if len(val) <= 60 else val[:60] + "…"
    lines.append(f"| {pid} | {pol} | {nm} | {v} |  |  |")

lines += ["", "## 3. 핵심 코드 셋 (BSS 코드테이블 1:1 매핑 대상)", "",
          "- 회원상태코드: `PG-MBR-LEAVE-008` 항목값 `ACTIVE/DORMANT/LEAVE_PENDING/LEAVED/BLOCKED` ↔ 온톨로지 `:State.code(MBR_*)`",
          "- 가입제한 사유코드: `PG-MBR-JOIN-001` `DUPLICATE_CI/DORMANT_MEMBER/...`",
          "- 인증 결과코드: `PG-MBR-AUTH-006` `SUCCESS/FAIL/CANCEL/ERROR/TIMEOUT`",
          "", "> 주의: BSS 상태코드(LEAVED 등)와 온톨로지 State.code(MBR_*)는 명칭 체계가 달라 명시 매핑표 필수.",
          ""]
open(os.path.join(BASE, "integration-map.md"), "w", encoding="utf-8").write("\n".join(lines) + "\n")
print("integration-map.md:", len(seeds), "seed rows")

# ---------------- viz.html (core layer, embedded) ----------------
COLOR = {"Actor": "#e57373", "UseCase": "#64b5f6", "State": "#81c784",
         "Process": "#ffb74d", "Function": "#ba68c8", "Policy": "#4db6ac"}
SHAPE = {"Actor": "dot", "UseCase": "box", "State": "ellipse",
         "Process": "box", "Function": "diamond", "Policy": "hexagon"}
nodes, seen = [], set()
def add_node(nid, label, grp):
    if nid in seen: return
    seen.add(nid)
    nodes.append({"id": nid, "label": label, "group": grp,
                  "color": COLOR[grp], "shape": SHAPE[grp]})
for k, d in N["Actor"].items():    add_node(k, d["name"], "Actor")
for k, d in N["UseCase"].items():  add_node(k, d["name"], "UseCase")
for k, d in N["State"].items():    add_node(k, d["name"], "State")
for k, d in N["Process"].items():  add_node(k, d.get("name", k), "Process")
for k, d in N["Function"].items(): add_node(k, d.get("name", k), "Function")
for k, d in N["Policy"].items():   add_node(k, d["name"], "Policy")

edges = []
def add_edge(a, b, label, color="#999", dashes=False):
    if a in seen and b in seen:
        edges.append({"from": a, "to": b, "label": label, "arrows": "to",
                      "color": {"color": color}, "font": {"size": 9, "color": "#666"},
                      "dashes": dashes})
for u, p, s in R["HAS_PROCESS"]: add_edge(u, p, "HAS_PROCESS", "#1976d2")
for a, b in R["NEXT"]:           add_edge(a, b, "NEXT", "#f57c00")
for s, a in R["PERFORMED_BY"]:   add_edge(s, a, "PERFORMED_BY", "#c62828")
for p, f in R["USES"]:           add_edge(p, f, "USES", "#7b1fa2")
for p, pol in R["GOVERNED_BY"]:  add_edge(p, pol, "GOVERNED_BY", "#00897b", True)
for fr, to, ev, h, sc, trig in R["TRANSITIONS_TO"]:
    if sc != "ALL" and to: add_edge(fr, to, ev, "#388e3c")

data = {"nodes": nodes, "edges": edges}
tpl = """<!DOCTYPE html><html lang="ko"><head><meta charset="utf-8">
<title>회원 온톨로지 그래프</title>
<script src="https://unpkg.com/vis-network/standalone/umd/vis-network.min.js"></script>
<style>html,body{margin:0;height:100%;font-family:sans-serif}
#net{width:100%;height:100vh}#legend{position:fixed;top:8px;left:8px;background:#fff;
border:1px solid #ccc;padding:8px;font-size:12px;z-index:9;border-radius:6px}
.lg{display:inline-block;width:11px;height:11px;border-radius:50%;margin-right:4px}</style></head>
<body><div id="legend"><b>회원 온톨로지 (코어)</b><br>
<span class="lg" style="background:#e57373"></span>Actor
<span class="lg" style="background:#64b5f6"></span>UseCase
<span class="lg" style="background:#81c784"></span>State
<span class="lg" style="background:#ffb74d"></span>Process
<span class="lg" style="background:#ba68c8"></span>Function
<span class="lg" style="background:#4db6ac"></span>Policy<br>
<small>PolicyItem(475)·Term(41)은 가독성 위해 제외</small></div>
<div id="net"></div><script>
const DATA=__DATA__;
const nodes=new vis.DataSet(DATA.nodes), edges=new vis.DataSet(DATA.edges);
new vis.Network(document.getElementById('net'),{nodes,edges},{
 physics:{stabilization:true,barnesHut:{gravitationalConstant:-8000,springLength:140}},
 interaction:{hover:true,tooltipDelay:120},
 nodes:{font:{size:13}},edges:{smooth:{type:'dynamic'}}});
</script></body></html>"""
open(os.path.join(BASE, "viz.html"), "w", encoding="utf-8").write(
    tpl.replace("__DATA__", json.dumps(data, ensure_ascii=False)))
print("viz.html:", len(nodes), "nodes,", len(edges), "edges")
