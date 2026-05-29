"use client";

import { FormEvent, useEffect, useMemo, useState } from "react";
import { CheckCircle2, ClipboardCheck, Database, FilePenLine, Plus, Search, Tags, Upload } from "lucide-react";
import {
  CandidateStatus,
  TermStatus,
  createCandidate,
  createTerm,
  getTerm,
  getCandidate,
  listCandidates,
  listTerms,
  promoteCandidate,
  reviewCandidate,
  type Term,
  type TermCandidate,
  type TermCandidateCreateRequest,
  type TermCandidateSummary,
  type TermCreateRequest,
  type TermSummary,
} from "@/lib/term-api";

const statusOptions = [
  "",
  TermStatus.Draft,
  TermStatus.Reviewing,
  TermStatus.Approved,
  TermStatus.Deprecated,
  TermStatus.Rejected,
];

const domainOptions = ["", "고객", "주문", "결제", "상품", "계약", "청구", "상담", "공통"];

const initialForm: TermCreateRequest = {
  domainName: "고객",
  usageType: "표준항목",
  koreanName: "",
  englishName: "",
  englishAbbreviation: "",
  businessDefinition: "",
  usageContext: "",
  physicalType: "VARCHAR",
  digits: 20,
  decimalPoint: 0,
  owner: "고객도메인 데이터스튜어드",
  status: TermStatus.Draft,
};

const initialCandidateForm: TermCandidateCreateRequest = {
  koreanName: "고객선호배송시간대",
  englishName: "Customer Preferred Delivery Time Slot",
  englishAbbreviation: "CUST_PREF_DLV_TM_SLOT",
  domainName: "고객",
  businessDefinition: "고객이 선호하는 배송 시간대",
  usageContext: "배송 옵션 추천과 배송 요청 화면에서 사용",
  physicalType: "VARCHAR",
  digits: 20,
  decimalPoint: 0,
  requestedBy: "planner01",
};

export default function Home() {
  const [query, setQuery] = useState("고객ID");
  const [domainName, setDomainName] = useState("");
  const [status, setStatus] = useState<TermStatus | "">("");
  const [terms, setTerms] = useState<TermSummary[]>([]);
  const [selectedTerm, setSelectedTerm] = useState<Term | null>(null);
  const [form, setForm] = useState<TermCreateRequest>(initialForm);
  const [candidateForm, setCandidateForm] = useState<TermCandidateCreateRequest>(initialCandidateForm);
  const [candidates, setCandidates] = useState<TermCandidateSummary[]>([]);
  const [selectedCandidate, setSelectedCandidate] = useState<TermCandidate | null>(null);
  const [mode, setMode] = useState<"create" | "edit">("create");
  const [message, setMessage] = useState("샘플 데이터 또는 API 응답을 표시합니다.");
  const [candidateMessage, setCandidateMessage] = useState("미등록 표현을 후보로 등록하고 검토 후 표준 용어로 승격합니다.");

  useEffect(() => {
    void loadTerms();
    void loadCandidates();
  }, []);

  async function loadTerms(nextQuery = query) {
    const response = await listTerms(nextQuery, domainName, status || undefined);
    setTerms(response.items);
    if (response.items[0]) {
      await selectTerm(response.items[0].termId);
    }
  }

  async function selectTerm(termId: string) {
    const term = await getTerm(termId);
    setSelectedTerm(term);
    setMode("edit");
    setForm({
      domainName: term.domainName,
      usageType: term.usageType,
      koreanName: term.koreanName,
      englishName: term.englishName,
      englishAbbreviation: term.englishAbbreviation,
      businessDefinition: term.businessDefinition,
      usageContext: term.usageContext ?? "",
      physicalType: term.physicalType,
      digits: term.digits,
      decimalPoint: term.decimalPoint,
      owner: term.owner,
      status: term.status,
    });
  }

  async function submitTerm(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (mode === "create") {
      try {
        const created = await createTerm(form);
        setMessage(`${created.koreanName} 등록 완료`);
        await selectTerm(created.termId);
        await loadTerms("");
      } catch {
        setMessage("백엔드 API가 실행 중일 때 등록할 수 있습니다. 현재 화면은 샘플 데이터로 동작합니다.");
      }
      return;
    }

    setMessage("수정 API는 연결 준비가 완료되었습니다. 실제 저장은 백엔드 실행 후 적용됩니다.");
  }

  async function loadCandidates() {
    const response = await listCandidates();
    setCandidates(response.items);
    if (response.items[0]) {
      await selectCandidate(response.items[0].candidateId);
    }
  }

  async function selectCandidate(candidateId: string) {
    const candidate = await getCandidate(candidateId);
    setSelectedCandidate(candidate);
    setCandidateForm({
      koreanName: candidate.koreanName,
      englishName: candidate.englishName,
      englishAbbreviation: candidate.englishAbbreviation,
      domainName: candidate.domainName,
      businessDefinition: candidate.businessDefinition,
      usageContext: candidate.usageContext ?? "",
      physicalType: candidate.physicalType,
      digits: candidate.digits,
      decimalPoint: candidate.decimalPoint,
      requestedBy: candidate.requestedBy,
    });
  }

  async function submitCandidate(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const candidate = await createCandidate(candidateForm);
    setSelectedCandidate(candidate);
    setCandidateMessage(`${candidate.koreanName} 후보 등록 완료`);
    await loadCandidates();
  }

  async function approveCandidate() {
    if (!selectedCandidate) return;
    const candidate = await reviewCandidate(selectedCandidate.candidateId, {
      reviewer: "data.steward",
      decision: "Approve",
      reason: "중복 없음, 약어와 타입 적합",
    });
    setSelectedCandidate(candidate);
    setCandidateMessage(`${candidate.koreanName} 후보 승인 완료`);
    await loadCandidates();
  }

  async function promoteSelectedCandidate() {
    if (!selectedCandidate) return;
    const result = await promoteCandidate(selectedCandidate.candidateId, {
      approver: "data.steward",
      owner: `${selectedCandidate.domainName}도메인 데이터스튜어드`,
      reason: "표준 용어로 승인 및 개발 반영 가능",
    });
    setSelectedCandidate(result.candidate);
    setCandidateMessage(`${result.term.koreanName} 표준 용어 승격 완료`);
    await loadCandidates();
    await loadTerms("");
  }

  const standardFields = useMemo(() => {
    const expressions = selectedTerm?.expressions ?? [];
    return {
      db: expressions.find((item) => item.expressionType === "DB_COLUMN")?.expressionValue ?? "-",
      api: expressions.find((item) => item.expressionType === "API_FIELD")?.expressionValue ?? "-",
      code: expressions.find((item) => item.expressionType === "CODE_VARIABLE")?.expressionValue ?? "-",
      ui: expressions.find((item) => item.expressionType === "UI_LABEL")?.expressionValue ?? "-",
    };
  }, [selectedTerm]);

  return (
    <main className="min-h-screen">
      <header className="border-b border-[var(--line)] bg-white">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
          <div>
            <h1 className="text-xl font-semibold">AULMS 용어 관리</h1>
            <p className="mt-1 text-sm text-[var(--muted)]">표준 용어, 산출물 표현, 별칭을 한 흐름에서 관리합니다.</p>
          </div>
          <button
            type="button"
            className="inline-flex h-10 items-center gap-2 rounded-md bg-[var(--accent)] px-4 text-sm font-medium text-white hover:bg-[var(--accent-strong)]"
            onClick={() => {
              setMode("create");
              setSelectedTerm(null);
              setForm(initialForm);
            }}
          >
            <Plus size={16} />
            신규 용어
          </button>
        </div>
      </header>

      <div className="mx-auto grid max-w-7xl grid-cols-1 gap-5 px-6 py-5 xl:grid-cols-[430px_1fr]">
        <section className="rounded-md border border-[var(--line)] bg-white">
          <div className="border-b border-[var(--line)] px-4 py-3">
            <div className="flex items-center gap-2 font-semibold">
              <Search size={17} />
              용어 검색
            </div>
          </div>
          <div className="space-y-3 p-4">
            <label className="block text-sm font-medium">
              검색어
              <input
                className="mt-1 h-10 w-full rounded-md border border-[var(--line)] px-3 outline-none focus:border-[var(--accent)]"
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="고객번호, CUST_NO, customerNumber, 고객ID"
              />
            </label>
            <div className="grid grid-cols-2 gap-3">
              <label className="block text-sm font-medium">
                도메인
                <select className="mt-1 h-10 w-full rounded-md border border-[var(--line)] px-3" value={domainName} onChange={(event) => setDomainName(event.target.value)}>
                  {domainOptions.map((item) => (
                    <option key={item || "all"} value={item}>
                      {item || "전체"}
                    </option>
                  ))}
                </select>
              </label>
              <label className="block text-sm font-medium">
                상태
                <select className="mt-1 h-10 w-full rounded-md border border-[var(--line)] px-3" value={status} onChange={(event) => setStatus(event.target.value as TermStatus | "")}>
                  {statusOptions.map((item) => (
                    <option key={item || "all"} value={item}>
                      {item || "전체"}
                    </option>
                  ))}
                </select>
              </label>
            </div>
            <button
              type="button"
              className="inline-flex h-10 w-full items-center justify-center gap-2 rounded-md bg-[var(--foreground)] px-4 text-sm font-medium text-white"
              onClick={() => void loadTerms()}
            >
              <Search size={16} />
              검색
            </button>
          </div>

          <div className="border-t border-[var(--line)]">
            <table className="w-full table-fixed text-left text-sm">
              <thead className="bg-[#eef2f5] text-xs text-[var(--muted)]">
                <tr>
                  <th className="w-[34%] px-4 py-2">표준 용어</th>
                  <th className="w-[28%] px-2 py-2">DB</th>
                  <th className="w-[26%] px-2 py-2">API</th>
                  <th className="w-[12%] px-2 py-2">상태</th>
                </tr>
              </thead>
              <tbody>
                {terms.map((term) => (
                  <tr key={term.termId} className="cursor-pointer border-t border-[var(--line)] hover:bg-[#f8fafb]" onClick={() => void selectTerm(term.termId)}>
                    <td className="px-4 py-3 font-medium">{term.koreanName}</td>
                    <td className="truncate px-2 py-3 text-[var(--muted)]">{term.englishAbbreviation}</td>
                    <td className="truncate px-2 py-3 text-[var(--muted)]">{term.apiFieldName}</td>
                    <td className="px-2 py-3">
                      <StatusPill status={term.status} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>

        <section className="space-y-5">
          <div className="rounded-md border border-[var(--line)] bg-white">
            <div className="flex items-center justify-between border-b border-[var(--line)] px-4 py-3">
              <div className="flex items-center gap-2 font-semibold">
                <Database size={17} />
                용어 상세
              </div>
              {selectedTerm ? <StatusPill status={selectedTerm.status} /> : null}
            </div>
            {selectedTerm ? (
              <div className="grid gap-4 p-4 lg:grid-cols-2">
                <Info label="한글 용어명" value={selectedTerm.koreanName} />
                <Info label="영문 용어명" value={selectedTerm.englishName} />
                <Info label="영문 약어" value={selectedTerm.englishAbbreviation} />
                <Info label="도메인" value={selectedTerm.domainName} />
                <Info label="업무 정의" value={selectedTerm.businessDefinition} wide />
                <Info label="사용 맥락" value={selectedTerm.usageContext ?? "-"} wide />
                <Info label="물리 타입" value={selectedTerm.physicalType} />
                <Info label="자릿수 / 소수점" value={`${selectedTerm.digits} / ${selectedTerm.decimalPoint}`} />
              </div>
            ) : (
              <div className="p-8 text-sm text-[var(--muted)]">용어를 선택하거나 신규 용어를 등록하세요.</div>
            )}
          </div>

          <div className="grid gap-5 lg:grid-cols-2">
            <div className="rounded-md border border-[var(--line)] bg-white">
              <div className="flex items-center gap-2 border-b border-[var(--line)] px-4 py-3 font-semibold">
                <FilePenLine size={17} />
                산출물 표현
              </div>
              <div className="grid grid-cols-2 gap-3 p-4 text-sm">
                <Info label="DB 컬럼명" value={standardFields.db} />
                <Info label="API 필드명" value={standardFields.api} />
                <Info label="코드 변수명" value={standardFields.code} />
                <Info label="화면 표시명" value={standardFields.ui} />
              </div>
            </div>

            <div className="rounded-md border border-[var(--line)] bg-white">
              <div className="flex items-center gap-2 border-b border-[var(--line)] px-4 py-3 font-semibold">
                <Tags size={17} />
                별칭 / 금지어
              </div>
              <div className="divide-y divide-[var(--line)]">
                {(selectedTerm?.aliases ?? []).map((alias) => (
                  <div key={alias.aliasId} className="px-4 py-3 text-sm">
                    <div className="flex items-center justify-between gap-3">
                      <span className="font-medium">{alias.aliasName}</span>
                      <span className="rounded-sm bg-[#eef2f5] px-2 py-1 text-xs text-[var(--muted)]">{alias.aliasType}</span>
                    </div>
                    <p className="mt-1 text-[var(--muted)]">{alias.recommendationAction}</p>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <form className="rounded-md border border-[var(--line)] bg-white" onSubmit={(event) => void submitTerm(event)}>
            <div className="flex items-center justify-between border-b border-[var(--line)] px-4 py-3">
              <div className="flex items-center gap-2 font-semibold">
                <CheckCircle2 size={17} />
                {mode === "create" ? "용어 등록" : "용어 수정"}
              </div>
              <span className="text-sm text-[var(--muted)]">{message}</span>
            </div>
            <div className="grid gap-4 p-4 lg:grid-cols-2">
              <Field label="한글 용어명" value={form.koreanName} onChange={(value) => setForm({ ...form, koreanName: value })} />
              <Field label="영문 용어명" value={form.englishName} onChange={(value) => setForm({ ...form, englishName: value })} />
              <Field label="영문 약어" value={form.englishAbbreviation} onChange={(value) => setForm({ ...form, englishAbbreviation: value })} />
              <Field label="소유자" value={form.owner} onChange={(value) => setForm({ ...form, owner: value })} />
              <Field label="업무 정의" value={form.businessDefinition} onChange={(value) => setForm({ ...form, businessDefinition: value })} wide />
              <Field label="사용 맥락" value={form.usageContext ?? ""} onChange={(value) => setForm({ ...form, usageContext: value })} wide />
              <Field label="물리 타입" value={form.physicalType} onChange={(value) => setForm({ ...form, physicalType: value })} />
              <div className="grid grid-cols-2 gap-3">
                <Field label="자릿수" value={String(form.digits)} onChange={(value) => setForm({ ...form, digits: Number(value) })} />
                <Field label="소수점" value={String(form.decimalPoint)} onChange={(value) => setForm({ ...form, decimalPoint: Number(value) })} />
              </div>
            </div>
            <div className="flex justify-end border-t border-[var(--line)] px-4 py-3">
              <button type="submit" className="inline-flex h-10 items-center gap-2 rounded-md bg-[var(--accent)] px-4 text-sm font-medium text-white">
                <CheckCircle2 size={16} />
                {mode === "create" ? "등록" : "수정"}
              </button>
            </div>
          </form>

          <section className="rounded-md border border-[var(--line)] bg-white">
            <div className="flex items-center justify-between border-b border-[var(--line)] px-4 py-3">
              <div className="flex items-center gap-2 font-semibold">
                <ClipboardCheck size={17} />
                신규 용어 후보
              </div>
              <span className="text-sm text-[var(--muted)]">{candidateMessage}</span>
            </div>

            <div className="grid gap-5 p-4 xl:grid-cols-[1fr_340px]">
              <form className="grid gap-4 lg:grid-cols-2" onSubmit={(event) => void submitCandidate(event)}>
                <Field label="후보 한글명" value={candidateForm.koreanName} onChange={(value) => setCandidateForm({ ...candidateForm, koreanName: value })} />
                <Field label="후보 영문명" value={candidateForm.englishName} onChange={(value) => setCandidateForm({ ...candidateForm, englishName: value })} />
                <Field label="후보 약어" value={candidateForm.englishAbbreviation} onChange={(value) => setCandidateForm({ ...candidateForm, englishAbbreviation: value })} />
                <Field label="도메인" value={candidateForm.domainName} onChange={(value) => setCandidateForm({ ...candidateForm, domainName: value })} />
                <Field label="업무 정의" value={candidateForm.businessDefinition} onChange={(value) => setCandidateForm({ ...candidateForm, businessDefinition: value })} wide />
                <Field label="사용 맥락" value={candidateForm.usageContext ?? ""} onChange={(value) => setCandidateForm({ ...candidateForm, usageContext: value })} wide />
                <Field label="물리 타입" value={candidateForm.physicalType} onChange={(value) => setCandidateForm({ ...candidateForm, physicalType: value })} />
                <div className="grid grid-cols-2 gap-3">
                  <Field label="자릿수" value={String(candidateForm.digits)} onChange={(value) => setCandidateForm({ ...candidateForm, digits: Number(value) })} />
                  <Field label="소수점" value={String(candidateForm.decimalPoint)} onChange={(value) => setCandidateForm({ ...candidateForm, decimalPoint: Number(value) })} />
                </div>
                <div className="flex flex-wrap gap-2 lg:col-span-2">
                  <button type="submit" className="inline-flex h-10 items-center gap-2 rounded-md bg-[var(--foreground)] px-4 text-sm font-medium text-white">
                    <Plus size={16} />
                    후보 등록
                  </button>
                  <button type="button" className="inline-flex h-10 items-center gap-2 rounded-md border border-[var(--line)] px-4 text-sm font-medium disabled:opacity-50" onClick={() => void approveCandidate()} disabled={!selectedCandidate || selectedCandidate.status === CandidateStatus.Promoted}>
                    <CheckCircle2 size={16} />
                    승인
                  </button>
                  <button type="button" className="inline-flex h-10 items-center gap-2 rounded-md bg-[var(--accent)] px-4 text-sm font-medium text-white disabled:opacity-50" onClick={() => void promoteSelectedCandidate()} disabled={!selectedCandidate || selectedCandidate.status !== CandidateStatus.Approved}>
                    <Upload size={16} />
                    표준 승격
                  </button>
                </div>
              </form>

              <div className="space-y-3">
                <div className="rounded-md border border-[var(--line)]">
                  <div className="border-b border-[var(--line)] bg-[#eef2f5] px-3 py-2 text-sm font-semibold">후보 목록</div>
                  <div className="max-h-64 divide-y divide-[var(--line)] overflow-auto">
                    {candidates.map((candidate) => (
                      <button key={candidate.candidateId} type="button" className="block w-full px-3 py-3 text-left hover:bg-[#f8fafb]" onClick={() => void selectCandidate(candidate.candidateId)}>
                        <div className="flex items-center justify-between gap-3">
                          <span className="text-sm font-medium">{candidate.koreanName}</span>
                          <CandidatePill status={candidate.status} />
                        </div>
                        <div className="mt-1 truncate text-xs text-[var(--muted)]">{candidate.englishAbbreviation}</div>
                      </button>
                    ))}
                  </div>
                </div>

                <div className="rounded-md border border-[var(--line)] p-3">
                  <div className="text-sm font-semibold">유사 용어</div>
                  <div className="mt-2 space-y-2">
                    {(selectedCandidate?.similarTerms ?? []).map((item) => (
                      <div key={item.termId} className="rounded-md bg-[#fbfcfd] px-3 py-2 text-sm">
                        <div className="font-medium">{item.koreanName}</div>
                        <div className="mt-1 text-xs text-[var(--muted)]">{item.dbColumn} / {item.apiField}</div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          </section>
        </section>
      </div>
    </main>
  );
}

function StatusPill({ status }: { status: TermStatus }) {
  const tone =
    status === TermStatus.Approved ? "bg-[#e6f4f1] text-[var(--accent-strong)]" :
    status === TermStatus.Deprecated || status === TermStatus.Rejected ? "bg-[#fdecec] text-[var(--danger)]" :
    "bg-[#fff4e5] text-[var(--warning)]";
  return <span className={`inline-flex min-w-16 justify-center rounded-sm px-2 py-1 text-xs font-medium ${tone}`}>{status}</span>;
}

function CandidatePill({ status }: { status: CandidateStatus }) {
  const tone =
    status === CandidateStatus.Promoted || status === CandidateStatus.Approved ? "bg-[#e6f4f1] text-[var(--accent-strong)]" :
    status === CandidateStatus.Rejected ? "bg-[#fdecec] text-[var(--danger)]" :
    "bg-[#fff4e5] text-[var(--warning)]";
  return <span className={`inline-flex min-w-16 justify-center rounded-sm px-2 py-1 text-xs font-medium ${tone}`}>{status}</span>;
}

function Info({ label, value, wide = false }: { label: string; value: string; wide?: boolean }) {
  return (
    <div className={wide ? "lg:col-span-2" : ""}>
      <div className="text-xs font-medium text-[var(--muted)]">{label}</div>
      <div className="mt-1 rounded-md border border-[var(--line)] bg-[#fbfcfd] px-3 py-2 text-sm">{value}</div>
    </div>
  );
}

function Field({ label, value, onChange, wide = false }: { label: string; value: string; onChange: (value: string) => void; wide?: boolean }) {
  return (
    <label className={`block text-sm font-medium ${wide ? "lg:col-span-2" : ""}`}>
      {label}
      <input className="mt-1 h-10 w-full rounded-md border border-[var(--line)] px-3 outline-none focus:border-[var(--accent)]" value={value} onChange={(event) => onChange(event.target.value)} />
    </label>
  );
}
