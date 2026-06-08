# -*- coding: utf-8 -*-
"""Phase 1+4: member-ontology.md(마크다운) -> 정규화 CSV(nodes/, rels/) + graph.json.
권위 출처: 4.가(UC->Process), 5.가(Process->Function ID), 6.가(Process->Policy ID), 6.나(Policy->PolicyItem).
"""
import re, csv, json, os

BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC = os.path.join(BASE, "member-ontology.md")
NODES = os.path.join(BASE, "nodes"); RELS = os.path.join(BASE, "rels")
os.makedirs(NODES, exist_ok=True); os.makedirs(RELS, exist_ok=True)

with open(SRC, encoding="utf-8") as f:
    lines = f.read().split("\n")

def split_row(l):
    cells = [c.strip() for c in l.strip().strip("|").split(" | ")]
    return [c.replace("\\|", "|") for c in cells]

def cl(c):  # multi-value cell -> list
    return [x.strip() for x in c.split("<br>") if x.strip()]

# ---- accumulators ----
actors, usecases, states = {}, {}, {}
transitions = []
processes, functions, policies, policy_items, terms = {}, {}, {}, {}, {}
rel_has_process, rel_uses, rel_governed_by, rel_has_item = [], [], [], []
uc_seq = {}

STATE_NAME2CODE = {}  # filled after states parsed

def kv(rows):
    """항목|내용 detail table -> dict(key -> raw value cell)"""
    d = {}
    for r in rows:
        if len(r) >= 2:
            d[r[0]] = r[1]
    return d

def dispatch(sec, sub, h4id, header, rows):
    h0 = header[0]
    # 2. 용어
    if sec == 2 and h0 == "용어 ID":
        for r in rows: terms[r[0]] = {"name": r[1], "definition": r[2]}
        return
    # 3.가 액터
    if sec == 3 and sub == "가" and h0 == "액터 ID":
        for r in rows: actors[r[0]] = {"name": r[1], "desc": r[2]}
        return
    # 3.나 유즈케이스
    if sec == 3 and sub == "나" and h0 == "유즈케이스 ID":
        for r in rows:
            usecases[r[0]] = {"actorName": r[1], "name": r[2], "desc": r[3],
                              "isProc": r[4] if len(r) > 4 else ""}
        return
    # 3.라.1 상태 코드
    if sec == 3 and sub == "라" and h0 == "상태 코드":
        for r in rows:
            states[r[0]] = {"name": r[1], "definition": r[2], "followUp": r[3]}
        return
    # 3.라.2 상태 전이 기준
    if sec == 3 and sub == "라" and h0 == "현재 상태":
        for r in rows:
            transitions.append({"from": r[0], "event": r[1], "to": r[2],
                                "handler": r[3] if len(r) > 3 else ""})
        return
    # 4.가 프로세스 목록 (h4id = UC id)
    if sec == 4 and sub == "가" and h0 == "프로세스 ID":
        uc = h4id
        for r in rows:
            pid = r[0]
            uc_seq[uc] = uc_seq.get(uc, 0) + 1
            seq = uc_seq[uc]
            processes.setdefault(pid, {})
            processes[pid].update({"name": r[1], "desc": r[2], "useCaseId": uc, "seq": seq})
            rel_has_process.append((uc, pid, seq))
        return
    # 4.다 프로세스 상세 (h4id = PR id)
    if sec == 4 and sub == "다" and h0 == "항목":
        d = kv(rows); pid = d.get("프로세스 ID", h4id)
        processes.setdefault(pid, {})
        processes[pid].update({
            "name": processes[pid].get("name", d.get("프로세스명", "")),
            "actor": d.get("액터", ""),
            "entryCond": d.get("진입 조건", ""),
            "exitCond": d.get("종료 조건", ""),
            "nextNames": cl(d.get("후행 프로세스", "")),
            "prevNames": cl(d.get("선행 프로세스", "")),
        })
        return
    # 5.가 기능 목록 (h4id = PR id)
    if sec == 5 and sub == "가" and h0 == "기능 ID":
        pid = h4id
        for r in rows:
            fid = r[0]
            functions.setdefault(fid, {"name": r[1], "desc": r[2]})
            rel_uses.append((pid, fid))
        return
    # 5.나 기능 상세 (h4id = FN id)
    if sec == 5 and sub == "나" and h0 == "항목":
        d = kv(rows); fid = d.get("기능 ID", h4id)
        functions.setdefault(fid, {"name": d.get("기능명", ""), "desc": d.get("설명", "")})
        functions[fid].update({
            "name": functions[fid].get("name") or d.get("기능명", ""),
            "desc": functions[fid].get("desc") or d.get("설명", ""),
            "inputs": cl(d.get("입력 정보", "")),
            "stateActions": cl(d.get("처리 (상태-액션-결과)", "")),
            "subFunctions": cl(d.get("세부 기능 구성", "")),
            "outputs": cl(d.get("출력 정보", "")),
            "exceptions": cl(d.get("실패/예외 케이스", "")),
            "relPolicyNames": cl(d.get("관련 정책", "")),
        })
        return
    # 6.가 정책 목록 (h4id = PR id)
    if sec == 6 and sub == "가" and h0 == "정책 ID":
        pid = h4id
        for r in rows:
            polid = r[0]
            policies.setdefault(polid, {"name": r[1], "desc": r[2]})
            rel_governed_by.append((pid, polid))
        return
    # 6.나 정책 상세 (h4id = PG id) header: 정책 항목|항목 ID|설정값
    if sec == 6 and sub == "나" and h0 == "정책 항목":
        pg = h4id
        for r in rows:
            name, pol_item_id, val = r[0], r[1], (r[2] if len(r) > 2 else "")
            policy_items[pol_item_id] = {"name": name, "value": val, "policyId": pg}
            rel_has_item.append((pg, pol_item_id))
        return

# ---- walk ----
RE_H2 = re.compile(r"^## (\d+)\.")
RE_H3 = re.compile(r"^### ([가-힣])\.")
RE_H4 = re.compile(r"^#### \d+\)\s*(.*)")
RE_ID = re.compile(r"\((?:US|PR|FN|PG)-MBR-[A-Z0-9\-]+\)")

sec = sub = h4id = None
i, Nn = 0, len(lines)
while i < Nn:
    line = lines[i]
    m = RE_H2.match(line)
    if m: sec = int(m.group(1)); sub = None; h4id = None; i += 1; continue
    m = RE_H3.match(line)
    if m: sub = m.group(1); h4id = None; i += 1; continue
    m = RE_H4.match(line)
    if m:
        t = m.group(1).strip(); mid = RE_ID.search(t)
        h4id = mid.group(0).strip("()") if mid else None
        i += 1; continue
    if line.startswith("| ") and i + 1 < Nn and set(lines[i+1].replace("|", "").strip()) <= set("-") and lines[i+1].strip():
        header = split_row(line)
        rows = []
        j = i + 2
        while j < Nn and lines[j].startswith("| "):
            rows.append(split_row(lines[j])); j += 1
        dispatch(sec, sub, h4id, header, rows)
        i = j; continue
    i += 1

# ============ Phase 4 enrichment ============
STATE_NAME2CODE = {v["name"]: k for k, v in states.items()}
ACTOR_NAME2ID = {v["name"]: k for k, v in actors.items()}
PG_NAME2ID = {v["name"]: k for k, v in policies.items()}

# --- transitions -> concrete State->State rels (+ specials) ---
EVENT2PROC = {
    "회원 가입 완료": "PR-MBR-CS-001-05",
    "휴면 해제 완료": "PR-MBR-CS-002-05",
    "회원 탈퇴 최종 동의 완료": "PR-MBR-CS-003-05",
    "재가입 가능 판정 완료": "PR-MBR-CS-004-05",
    "제한 기간 종료 후 재가입 요청": "PR-MBR-CS-004-05",
}
rel_transitions = []  # fromCode,toCode,event,handler,scope,triggerProcessId
for t in transitions:
    scope = "ALL" if t["from"] in ("전체 상태",) else "NORMAL"
    trig = EVENT2PROC.get(t["event"], "")
    to_field = t["to"]
    if scope == "ALL":
        rel_transitions.append(("ALL", "", t["event"], t["handler"], scope, trig)); continue
    fcode = STATE_NAME2CODE.get(t["from"], "")
    # 다음 상태가 "기존 상태 유지" -> toCode 비움 ; "미가입 또는 정상" -> 분기 2건
    to_names = []
    if to_field == "기존 상태 유지":
        to_names = [""]
    elif "또는" in to_field:
        to_names = [s.strip() for s in to_field.split("또는")]
    else:
        to_names = [to_field]
    for tn in to_names:
        tcode = STATE_NAME2CODE.get(tn, "") if tn else ""
        rel_transitions.append((fcode, tcode, t["event"], t["handler"], scope, trig))

# --- process actor -> rel_performed_by (multi) ; uc actor ---
rel_performed_by = []  # subjectId, actorId  (subject = process or usecase)
for uc, d in usecases.items():
    aid = ACTOR_NAME2ID.get(d["actorName"].strip())
    if aid: rel_performed_by.append((uc, aid))
for pid, d in processes.items():
    for an in [x.strip() for x in d.get("actor", "").split(",") if x.strip()]:
        aid = ACTOR_NAME2ID.get(an)
        if aid: rel_performed_by.append((pid, aid))

# --- NEXT (process->process) within same UC by name ---
name2pid_by_uc = {}
for pid, d in processes.items():
    name2pid_by_uc.setdefault(d.get("useCaseId"), {})[d.get("name")] = pid
rel_next = []
for pid, d in processes.items():
    uc = d.get("useCaseId")
    for nm in d.get("nextNames", []):
        if nm == "-": continue
        tgt = name2pid_by_uc.get(uc, {}).get(nm)
        if tgt: rel_next.append((pid, tgt))

# --- REGULATED_BY (function->policy) by name match (보조) ---
rel_regulated_by = []
unmatched_fn_policy = []
for fid, d in functions.items():
    for pn in d.get("relPolicyNames", []):
        polid = PG_NAME2ID.get(pn)
        if polid: rel_regulated_by.append((fid, polid))
        else: unmatched_fn_policy.append((fid, pn))

# --- PolicyItem value typing + REFERS_STATE ---
RE_NUMUNIT = re.compile(r"^(\d+)\s*([가-힣%]+)$")
rel_refers_state = []
for piid, d in policy_items.items():
    val = d["value"]; d["valueNum"] = ""; d["valueUnit"] = ""; d["valueType"] = "text"
    m = RE_NUMUNIT.match(val)
    if m:
        d["valueNum"] = m.group(1); d["valueUnit"] = m.group(2); d["valueType"] = "scalar"
    elif "," in val:
        d["valueType"] = "enum"
    # REFERS_STATE: value mentions state name(s)
    for sname, scode in STATE_NAME2CODE.items():
        if re.search(r"(^|[,\s])" + re.escape(sname) + r"($|[,\s])", val):
            rel_refers_state.append((piid, scode))

# ============ write CSVs ============
def wcsv(path, header, rows):
    with open(path, "w", encoding="utf-8", newline="") as f:
        w = csv.writer(f); w.writerow(header)
        for r in rows: w.writerow(r)

wcsv(f"{NODES}/actors.csv", ["id", "name", "desc"],
     [[k, v["name"], v["desc"]] for k, v in actors.items()])
wcsv(f"{NODES}/usecases.csv", ["id", "name", "desc", "actorName", "isProcessDefined"],
     [[k, v["name"], v["desc"], v["actorName"], v["isProc"]] for k, v in usecases.items()])
wcsv(f"{NODES}/states.csv", ["code", "name", "definition", "followUp"],
     [[k, v["name"], v["definition"], v["followUp"]] for k, v in states.items()])
wcsv(f"{NODES}/processes.csv", ["id", "name", "desc", "useCaseId", "seq", "actor", "entryCond", "exitCond"],
     [[k, v.get("name", ""), v.get("desc", ""), v.get("useCaseId", ""), v.get("seq", ""),
       v.get("actor", ""), v.get("entryCond", ""), v.get("exitCond", "")] for k, v in processes.items()])
wcsv(f"{NODES}/functions.csv",
     ["id", "name", "desc", "inputs", "stateActions", "subFunctions", "outputs", "exceptions"],
     [[k, v.get("name", ""), v.get("desc", ""), "|".join(v.get("inputs", [])),
       "|".join(v.get("stateActions", [])), "|".join(v.get("subFunctions", [])),
       "|".join(v.get("outputs", [])), "|".join(v.get("exceptions", []))] for k, v in functions.items()])
wcsv(f"{NODES}/policies.csv", ["id", "name", "desc"],
     [[k, v["name"], v["desc"]] for k, v in policies.items()])
wcsv(f"{NODES}/policy_items.csv", ["id", "name", "value", "valueType", "valueNum", "valueUnit", "policyId"],
     [[k, v["name"], v["value"], v["valueType"], v["valueNum"], v["valueUnit"], v["policyId"]]
      for k, v in policy_items.items()])
wcsv(f"{NODES}/terms.csv", ["id", "name", "definition"],
     [[k, v["name"], v["definition"]] for k, v in terms.items()])

wcsv(f"{RELS}/rel_has_process.csv", ["useCaseId", "processId", "seq"], rel_has_process)
wcsv(f"{RELS}/rel_next.csv", ["fromProcessId", "toProcessId"], rel_next)
wcsv(f"{RELS}/rel_performed_by.csv", ["subjectId", "actorId"], rel_performed_by)
wcsv(f"{RELS}/rel_uses.csv", ["processId", "functionId"], rel_uses)
wcsv(f"{RELS}/rel_governed_by.csv", ["processId", "policyId"], rel_governed_by)
wcsv(f"{RELS}/rel_has_item.csv", ["policyId", "policyItemId"], rel_has_item)
wcsv(f"{RELS}/rel_regulated_by.csv", ["functionId", "policyId"], rel_regulated_by)
wcsv(f"{RELS}/rel_transitions.csv",
     ["fromCode", "toCode", "event", "handler", "scope", "triggerProcessId"], rel_transitions)
wcsv(f"{RELS}/rel_refers_state.csv", ["policyItemId", "stateCode"], rel_refers_state)

# ============ graph.json (for viz + python validation) ============
graph = {
    "nodes": {
        "Actor": actors, "UseCase": usecases, "State": states, "Process": processes,
        "Function": functions, "Policy": policies, "PolicyItem": policy_items, "Term": terms,
    },
    "rels": {
        "HAS_PROCESS": rel_has_process, "NEXT": rel_next, "PERFORMED_BY": rel_performed_by,
        "USES": rel_uses, "GOVERNED_BY": rel_governed_by, "HAS_ITEM": rel_has_item,
        "REGULATED_BY": rel_regulated_by, "TRANSITIONS_TO": rel_transitions,
        "REFERS_STATE": rel_refers_state,
    },
    "unmatched_fn_policy": unmatched_fn_policy,
}
with open(os.path.join(BASE, "graph.json"), "w", encoding="utf-8") as f:
    json.dump(graph, f, ensure_ascii=False, indent=1)

# ============ summary ============
print("NODES")
for lbl, d in [("Actor", actors), ("UseCase", usecases), ("State", states), ("Process", processes),
               ("Function", functions), ("Policy", policies), ("PolicyItem", policy_items), ("Term", terms)]:
    print(f"  {lbl:11} {len(d)}")
print("RELS")
for nm, r in [("HAS_PROCESS", rel_has_process), ("NEXT", rel_next), ("PERFORMED_BY", rel_performed_by),
              ("USES", rel_uses), ("GOVERNED_BY", rel_governed_by), ("HAS_ITEM", rel_has_item),
              ("REGULATED_BY", rel_regulated_by), ("TRANSITIONS_TO", rel_transitions),
              ("REFERS_STATE", rel_refers_state)]:
    print(f"  {nm:14} {len(r)}")
print(f"unmatched fn->policy names: {len(unmatched_fn_policy)}")
