"use client";

import { FormEvent, useEffect, useMemo, useState } from "react";
import { AlertTriangle, CheckCircle2, ClipboardCheck, Database, FilePenLine, GitBranch, History, Plus, Search, Tags, Upload, WandSparkles } from "lucide-react";
import {
  CandidateStatus,
  ImpactChangeType,
  TermRecommendationMode,
  TermStatus,
  createCandidate,
  createTerm,
  extractApiError,
  getCandidate,
  getTerm,
  getTermImpact,
  listCandidates,
  listTermHistory,
  listTerms,
  promoteCandidate,
  recommendTermDraft,
  reviewCandidate,
  updateTerm,
  type ImpactAnalysisResponse,
  type Term,
  type TermCandidate,
  type TermCandidateCreateRequest,
  type TermCandidateSummary,
  type TermChangeHistory,
  type TermCreateRequest,
  type TermSummary,
  type TermUpdateRequest,
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

type ScreenTab = "dictionary" | "term-create" | "candidate";

type ErrorModalState = {
  title: string;
  message: string;
  detail?: string;
};

type DuplicateModalState = {
  title: string;
  message: string;
  items: TermSummary[];
  confirmLabel: string;
  onConfirm: () => Promise<void> | void;
};

export default function Home() {
  const [activeTab, setActiveTab] = useState<ScreenTab>("dictionary");
  const [query, setQuery] = useState("고객ID");
  const [domainName, setDomainName] = useState("");
  const [status, setStatus] = useState<TermStatus | "">("");
  const [terms, setTerms] = useState<TermSummary[]>([]);
  const [selectedTerm, setSelectedTerm] = useState<Term | null>(null);
  const [termHistory, setTermHistory] = useState<TermChangeHistory[]>([]);
  const [createForm, setCreateForm] = useState<TermCreateRequest>(initialForm);
  const [editForm, setEditForm] = useState<TermCreateRequest>(initialForm);
  const [changeReason, setChangeReason] = useState("");
  const [registerMessage, setRegisterMessage] = useState("신규 표준 용어를 등록합니다.");
  const [editMessage, setEditMessage] = useState("선택한 표준 용어를 수정합니다.");
  const [candidateForm, setCandidateForm] = useState<TermCandidateCreateRequest>(initialCandidateForm);
  const [candidates, setCandidates] = useState<TermCandidateSummary[]>([]);
  const [selectedCandidate, setSelectedCandidate] = useState<TermCandidate | null>(null);
  const [impact, setImpact] = useState<ImpactAnalysisResponse | null>(null);
  const [impactChangeType, setImpactChangeType] = useState<ImpactChangeType>(ImpactChangeType.ApiFieldRename);
  const [candidateMessage, setCandidateMessage] = useState("미등록 표현을 후보로 등록하고 검토 후 표준 용어로 승격합니다.");
  const [errorModal, setErrorModal] = useState<ErrorModalState | null>(null);
  const [duplicateModal, setDuplicateModal] = useState<DuplicateModalState | null>(null);

  useEffect(() => {
    void loadTerms();
    void loadCandidates();
  }, []);

  async function loadTerms(nextQuery = query) {
    const response = await listTerms(nextQuery, domainName, status || undefined);
    setTerms(response.items);
    if (response.items[0]) {
      await selectTerm(response.items[0].termId);
    } else {
      setSelectedTerm(null);
      setImpact(null);
      setTermHistory([]);
    }
  }

  async function selectTerm(termId: string) {
    const [term, historyResponse] = await Promise.all([getTerm(termId), listTermHistory(termId)]);
    setSelectedTerm(term);
    setTermHistory(historyResponse.items);
    await loadImpact(termId, impactChangeType);
    setChangeReason("");
    setEditForm({
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

  async function loadImpact(termId = selectedTerm?.termId, changeType = impactChangeType) {
    if (!termId) return;
    const response = await getTermImpact(termId, changeType, true);
    setImpact(response);
  }

  async function loadCandidates() {
    const response = await listCandidates();
    setCandidates(response.items);
    if (response.items[0]) {
      await selectCandidate(response.items[0].candidateId);
    } else {
      setSelectedCandidate(null);
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

  async function findDuplicateTerms(input: { koreanName: string; englishName?: string; englishAbbreviation: string }) {
    const queries = Array.from(new Set([input.koreanName, input.englishName, input.englishAbbreviation].filter(Boolean))) as string[];
    const responses = await Promise.all(queries.map((item) => listTerms(item, undefined, undefined)));
    const duplicateMap = new Map<string, TermSummary>();
    for (const response of responses) {
      for (const term of response.items) {
        if (
          term.koreanName === input.koreanName ||
          term.englishName === input.englishName ||
          term.englishAbbreviation === input.englishAbbreviation
        ) {
          duplicateMap.set(term.termId, term);
        }
      }
    }
    return Array.from(duplicateMap.values());
  }

  function openApiErrorModal(title: string, error: unknown) {
    const apiError = extractApiError(error);
    setErrorModal({
      title,
      message: apiError.message,
      detail: [apiError.code, apiError.status ? `HTTP ${apiError.status}` : undefined, apiError.detail, apiError.traceId ? `traceId: ${apiError.traceId}` : undefined]
        .filter(Boolean)
        .join("\n"),
    });
  }

  async function proceedCreateTerm() {
    try {
      const created = await createTerm(createForm);
      setRegisterMessage(`${created.koreanName} 등록 완료`);
      setCreateForm(initialForm);
      setActiveTab("dictionary");
      setQuery(created.koreanName);
      await loadTerms(created.koreanName);
    } catch (error) {
      openApiErrorModal("용어 등록 실패", error);
    }
  }

  async function recommendCreateForm() {
    if (!createForm.koreanName.trim()) {
      setErrorModal({
        title: "추천 실패",
        message: "한글 용어명을 먼저 입력해야 합니다.",
      });
      return;
    }
    try {
      const response = await recommendTermDraft({
        koreanName: createForm.koreanName,
        mode: TermRecommendationMode.TermCreate,
        currentDomainName: createForm.domainName,
        currentBusinessDefinition: createForm.businessDefinition || undefined,
        currentUsageContext: createForm.usageContext || undefined,
      });
      setCreateForm((current) => ({
        ...current,
        koreanName: current.koreanName,
        ...response.recommendation,
      }));
      setRegisterMessage(`${createForm.koreanName} 추천값을 채웠습니다.`);
    } catch (error) {
      openApiErrorModal("용어 추천 실패", error);
    }
  }

  async function recommendCandidateForm() {
    if (!candidateForm.koreanName.trim()) {
      setErrorModal({
        title: "추천 실패",
        message: "후보 한글명을 먼저 입력해야 합니다.",
      });
      return;
    }
    try {
      const response = await recommendTermDraft({
        koreanName: candidateForm.koreanName,
        mode: TermRecommendationMode.CandidateCreate,
        currentDomainName: candidateForm.domainName,
        currentBusinessDefinition: candidateForm.businessDefinition || undefined,
        currentUsageContext: candidateForm.usageContext || undefined,
      });
      setCandidateForm((current) => ({
        ...current,
        koreanName: current.koreanName,
        englishName: response.recommendation.englishName,
        englishAbbreviation: response.recommendation.englishAbbreviation,
        domainName: response.recommendation.domainName,
        businessDefinition: response.recommendation.businessDefinition,
        usageContext: response.recommendation.usageContext,
        physicalType: response.recommendation.physicalType,
        digits: response.recommendation.digits,
        decimalPoint: response.recommendation.decimalPoint,
      }));
      setCandidateMessage(`${candidateForm.koreanName} 추천값을 채웠습니다.`);
    } catch (error) {
      openApiErrorModal("후보 추천 실패", error);
    }
  }

  async function submitCreateTerm(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const duplicates = await findDuplicateTerms({
      koreanName: createForm.koreanName,
      englishName: createForm.englishName,
      englishAbbreviation: createForm.englishAbbreviation,
    });
    if (duplicates.length > 0) {
      setDuplicateModal({
        title: "기존 용어 존재",
        message: "같거나 유사한 표준 용어가 이미 있습니다. 그대로 등록할지 확인하세요.",
        items: duplicates,
        confirmLabel: "그대로 등록",
        onConfirm: () => proceedCreateTerm(),
      });
      return;
    }
    await proceedCreateTerm();
  }

  async function submitEditTerm(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedTerm) return;
    if (!changeReason.trim()) {
      setErrorModal({
        title: "용어 수정 실패",
        message: "변경 사유를 입력해야 합니다.",
        detail: "백엔드 수정 API는 changeReason 필드를 필수로 요구합니다.",
      });
      return;
    }
    try {
      const request: TermUpdateRequest = {
        ...editForm,
        version: incrementVersion(selectedTerm.version),
        changeReason: changeReason.trim(),
      };
      const updated = await updateTerm(selectedTerm.termId, request);
      setSelectedTerm(updated);
      setTerms((current) =>
        current.map((term) =>
          term.termId === updated.termId
            ? {
                ...term,
                domainName: updated.domainName,
                koreanName: updated.koreanName,
                englishName: updated.englishName,
                englishAbbreviation: updated.englishAbbreviation,
                apiFieldName: updated.expressions?.find((item) => item.expressionType === "API_FIELD")?.expressionValue ?? term.apiFieldName,
                status: updated.status,
              }
            : term,
        ),
      );
      const historyResponse = await listTermHistory(updated.termId);
      setTermHistory(historyResponse.items);
      setChangeReason("");
      setEditMessage(`${updated.koreanName} 수정 완료`);
    } catch (error) {
      openApiErrorModal("용어 수정 실패", error);
    }
  }

  async function proceedCreateCandidate() {
    try {
      const candidate = await createCandidate(candidateForm);
      setSelectedCandidate(candidate);
      setCandidateMessage(`${candidate.koreanName} 후보 등록 완료`);
      await loadCandidates();
    } catch (error) {
      openApiErrorModal("신규 용어 후보 등록 실패", error);
    }
  }

  async function submitCandidate(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const duplicates = await findDuplicateTerms({
      koreanName: candidateForm.koreanName,
      englishName: candidateForm.englishName,
      englishAbbreviation: candidateForm.englishAbbreviation,
    });
    if (duplicates.length > 0) {
      setDuplicateModal({
        title: "기존 용어 존재",
        message: "동일하거나 유사한 표준 용어가 있습니다. 후보 등록을 계속할지 확인하세요.",
        items: duplicates,
        confirmLabel: "후보 계속 등록",
        onConfirm: () => proceedCreateCandidate(),
      });
      return;
    }
    await proceedCreateCandidate();
  }

  async function approveCandidate() {
    if (!selectedCandidate) return;
    try {
      const candidate = await reviewCandidate(selectedCandidate.candidateId, {
        reviewer: "data.steward",
        decision: "Approve",
        reason: "중복 없음, 약어와 타입 적합",
      });
      setSelectedCandidate(candidate);
      setCandidateMessage(`${candidate.koreanName} 후보 승인 완료`);
      await loadCandidates();
    } catch (error) {
      openApiErrorModal("후보 승인 실패", error);
    }
  }

  async function promoteSelectedCandidate() {
    if (!selectedCandidate) return;
    try {
      const result = await promoteCandidate(selectedCandidate.candidateId, {
        approver: "data.steward",
        owner: `${selectedCandidate.domainName}도메인 데이터스튜어드`,
        reason: "표준 용어로 승인 및 개발 반영 가능",
      });
      setSelectedCandidate(result.candidate);
      setCandidateMessage(`${result.term.koreanName} 표준 용어 승격 완료`);
      await loadCandidates();
      await loadTerms("");
      setActiveTab("dictionary");
    } catch (error) {
      openApiErrorModal("표준 승격 실패", error);
    }
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
        <div className="mx-auto max-w-7xl px-6 py-4">
          <div className="flex flex-wrap items-center justify-between gap-4">
            <div>
              <h1 className="text-xl font-semibold">AULMS 용어 관리</h1>
              <p className="mt-1 text-sm text-[var(--muted)]">표준 용어 조회, 등록, 후보 관리 화면을 탭으로 분리했습니다.</p>
            </div>
            <div className="inline-flex rounded-md border border-[var(--line)] bg-[#fbfcfd] p-1">
              <TabButton active={activeTab === "dictionary"} onClick={() => setActiveTab("dictionary")} label="용어 조회" />
              <TabButton active={activeTab === "term-create"} onClick={() => setActiveTab("term-create")} label="용어 등록" />
              <TabButton active={activeTab === "candidate"} onClick={() => setActiveTab("candidate")} label="신규 용어 후보" />
            </div>
          </div>
        </div>
      </header>

      {activeTab === "dictionary" ? (
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
              <button type="button" className="inline-flex h-10 w-full items-center justify-center gap-2 rounded-md bg-[var(--foreground)] px-4 text-sm font-medium text-white" onClick={() => void loadTerms()}>
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
                <div className="p-8 text-sm text-[var(--muted)]">용어를 선택하세요.</div>
              )}
            </div>

            <div className="rounded-md border border-[var(--line)] bg-white">
              <div className="flex flex-wrap items-center justify-between gap-3 border-b border-[var(--line)] px-4 py-3">
                <div className="flex items-center gap-2 font-semibold">
                  <GitBranch size={17} />
                  영향도 분석
                </div>
                <div className="flex items-center gap-2">
                  <select
                    className="h-9 rounded-md border border-[var(--line)] px-3 text-sm"
                    value={impactChangeType}
                    onChange={(event) => {
                      const nextType = event.target.value as ImpactChangeType;
                      setImpactChangeType(nextType);
                      void loadImpact(selectedTerm?.termId, nextType);
                    }}
                  >
                    {Object.values(ImpactChangeType).map((item) => (
                      <option key={item} value={item}>
                        {item}
                      </option>
                    ))}
                  </select>
                  <button type="button" className="inline-flex h-9 items-center gap-2 rounded-md bg-[var(--foreground)] px-3 text-sm font-medium text-white disabled:opacity-50" onClick={() => void loadImpact()} disabled={!selectedTerm}>
                    <Search size={15} />
                    분석
                  </button>
                </div>
              </div>
              {impact ? (
                <div className="grid gap-4 p-4 xl:grid-cols-[220px_1fr]">
                  <div className="rounded-md border border-[var(--line)] bg-[#fbfcfd] p-4">
                    <div className="text-xs font-medium text-[var(--muted)]">위험도</div>
                    <div className={`mt-2 inline-flex items-center gap-2 rounded-sm px-2 py-1 text-sm font-semibold ${impact.riskLevel === "HIGH" ? "bg-[#fdecec] text-[var(--danger)]" : impact.riskLevel === "MEDIUM" ? "bg-[#fff4e5] text-[var(--warning)]" : "bg-[#e6f4f1] text-[var(--accent-strong)]"}`}>
                      <AlertTriangle size={15} />
                      {impact.riskLevel} {impact.riskScore}
                    </div>
                    <div className="mt-4 text-xs font-medium text-[var(--muted)]">대상</div>
                    <div className="mt-1 text-2xl font-semibold">{impact.impactedTargets.length}</div>
                    <div className="mt-4 text-xs text-[var(--muted)]">2-hop 포함: {impact.includeTwoHop ? "Y" : "N"}</div>
                  </div>
                  <div className="space-y-4">
                    <div className="overflow-hidden rounded-md border border-[var(--line)]">
                      <table className="w-full table-fixed text-left text-sm">
                        <thead className="bg-[#eef2f5] text-xs text-[var(--muted)]">
                          <tr>
                            <th className="w-[14%] px-3 py-2">유형</th>
                            <th className="w-[22%] px-3 py-2">대상</th>
                            <th className="w-[14%] px-3 py-2">시스템</th>
                            <th className="w-[12%] px-3 py-2">Hop</th>
                            <th className="px-3 py-2">사유</th>
                          </tr>
                        </thead>
                        <tbody>
                          {impact.impactedTargets.map((target) => (
                            <tr key={`${target.targetType}-${target.targetName}-${target.location}`} className="border-t border-[var(--line)]">
                              <td className="px-3 py-2 font-medium">{target.targetType}</td>
                              <td className="truncate px-3 py-2">{target.targetName}</td>
                              <td className="truncate px-3 py-2 text-[var(--muted)]">{target.systemCode}</td>
                              <td className="px-3 py-2 text-[var(--muted)]">{target.hop}</td>
                              <td className="px-3 py-2 text-[var(--muted)]">{target.reason}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                    <div className="grid gap-3 lg:grid-cols-3">
                      {impact.recommendations.map((item) => (
                        <div key={item.priority} className="rounded-md border border-[var(--line)] bg-[#fbfcfd] p-3 text-sm">
                          <div className="font-semibold">P{item.priority}. {item.action}</div>
                          <p className="mt-2 text-[var(--muted)]">{item.reason}</p>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
              ) : (
                <div className="p-8 text-sm text-[var(--muted)]">용어를 선택하면 변경 영향 대상을 조회합니다.</div>
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

            <form className="rounded-md border border-[var(--line)] bg-white" onSubmit={(event) => void submitEditTerm(event)}>
              <div className="flex items-center justify-between border-b border-[var(--line)] px-4 py-3">
                <div className="flex items-center gap-2 font-semibold">
                  <CheckCircle2 size={17} />
                  용어 수정
                </div>
                <span className="text-sm text-[var(--muted)]">{editMessage}</span>
              </div>
              <div className="grid gap-4 p-4 lg:grid-cols-2">
                <Field label="한글 용어명" value={editForm.koreanName} onChange={(value) => setEditForm({ ...editForm, koreanName: value })} />
                <Field label="영문 용어명" value={editForm.englishName} onChange={(value) => setEditForm({ ...editForm, englishName: value })} />
                <Field label="영문 약어" value={editForm.englishAbbreviation} onChange={(value) => setEditForm({ ...editForm, englishAbbreviation: value })} />
                <Field label="소유자" value={editForm.owner} onChange={(value) => setEditForm({ ...editForm, owner: value })} />
                <Field label="업무 정의" value={editForm.businessDefinition} onChange={(value) => setEditForm({ ...editForm, businessDefinition: value })} wide />
                <Field label="사용 맥락" value={editForm.usageContext ?? ""} onChange={(value) => setEditForm({ ...editForm, usageContext: value })} wide />
                <Field label="물리 타입" value={editForm.physicalType} onChange={(value) => setEditForm({ ...editForm, physicalType: value })} />
                <div className="grid grid-cols-2 gap-3">
                  <Field label="자릿수" value={String(editForm.digits)} onChange={(value) => setEditForm({ ...editForm, digits: Number(value) })} />
                  <Field label="소수점" value={String(editForm.decimalPoint)} onChange={(value) => setEditForm({ ...editForm, decimalPoint: Number(value) })} />
                </div>
                <Info label="다음 버전" value={selectedTerm ? incrementVersion(selectedTerm.version) : "-"} />
                <Field label="변경 사유" value={changeReason} onChange={setChangeReason} placeholder="예: 업무 정의 보완" />
              </div>
              <div className="flex justify-end border-t border-[var(--line)] px-4 py-3">
                <button type="submit" className="inline-flex h-10 items-center gap-2 rounded-md bg-[var(--accent)] px-4 text-sm font-medium text-white disabled:opacity-50" disabled={!selectedTerm}>
                  <CheckCircle2 size={16} />
                  수정
                </button>
              </div>
            </form>

            <section className="rounded-md border border-[var(--line)] bg-white">
              <div className="flex items-center gap-2 border-b border-[var(--line)] px-4 py-3 font-semibold">
                <History size={17} />
                용어 변경 히스토리
              </div>
              <div className="overflow-hidden">
                <table className="w-full table-fixed text-left text-sm">
                  <thead className="bg-[#eef2f5] text-xs text-[var(--muted)]">
                    <tr>
                      <th className="w-[14%] px-4 py-2">유형</th>
                      <th className="w-[12%] px-3 py-2">이전 상태</th>
                      <th className="w-[12%] px-3 py-2">새 상태</th>
                      <th className="w-[20%] px-3 py-2">사유</th>
                      <th className="w-[16%] px-3 py-2">승인자</th>
                      <th className="px-3 py-2">일시</th>
                    </tr>
                  </thead>
                  <tbody>
                    {termHistory.length > 0 ? (
                      termHistory.map((item) => (
                        <tr key={item.changeHistoryId} className="border-t border-[var(--line)]">
                          <td className="px-4 py-3 font-medium">{item.changeType}</td>
                          <td className="px-3 py-3 text-[var(--muted)]">{item.previousStatus ?? "-"}</td>
                          <td className="px-3 py-3 text-[var(--muted)]">{item.newStatus ?? "-"}</td>
                          <td className="px-3 py-3">{item.reason}</td>
                          <td className="px-3 py-3 text-[var(--muted)]">{item.approvedBy ?? item.requestedBy ?? "-"}</td>
                          <td className="px-3 py-3 text-[var(--muted)]">{formatDateTime(item.createdAt)}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan={6} className="px-4 py-8 text-center text-sm text-[var(--muted)]">
                          변경 히스토리가 없습니다.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </section>
          </section>
        </div>
      ) : null}

      {activeTab === "term-create" ? (
        <div className="mx-auto max-w-5xl px-6 py-5">
          <form className="rounded-md border border-[var(--line)] bg-white" onSubmit={(event) => void submitCreateTerm(event)}>
            <div className="flex items-center justify-between border-b border-[var(--line)] px-4 py-3">
              <div className="flex items-center gap-2 font-semibold">
                <Plus size={17} />
                용어 등록
              </div>
              <span className="text-sm text-[var(--muted)]">{registerMessage}</span>
            </div>
            <div className="grid gap-4 p-4 lg:grid-cols-2">
              <label className="block text-sm font-medium">
                한글 용어명
                <div className="mt-1 flex gap-2">
                  <input
                    className="h-10 w-full rounded-md border border-[var(--line)] px-3 outline-none focus:border-[var(--accent)]"
                    value={createForm.koreanName}
                    onChange={(event) => setCreateForm({ ...createForm, koreanName: event.target.value })}
                  />
                  <button type="button" className="inline-flex h-10 shrink-0 items-center gap-2 rounded-md border border-[var(--line)] px-3 text-sm font-medium" onClick={recommendCreateForm}>
                    <WandSparkles size={15} />
                    추천
                  </button>
                </div>
              </label>
              <Field label="영문 용어명" value={createForm.englishName} onChange={(value) => setCreateForm({ ...createForm, englishName: value })} />
              <Field label="영문 약어" value={createForm.englishAbbreviation} onChange={(value) => setCreateForm({ ...createForm, englishAbbreviation: value })} />
              <Field label="도메인" value={createForm.domainName} onChange={(value) => setCreateForm({ ...createForm, domainName: value })} />
              <Field label="사용 구분" value={createForm.usageType} onChange={(value) => setCreateForm({ ...createForm, usageType: value })} />
              <Field label="소유자" value={createForm.owner} onChange={(value) => setCreateForm({ ...createForm, owner: value })} />
              <Field label="업무 정의" value={createForm.businessDefinition} onChange={(value) => setCreateForm({ ...createForm, businessDefinition: value })} wide />
              <Field label="사용 맥락" value={createForm.usageContext ?? ""} onChange={(value) => setCreateForm({ ...createForm, usageContext: value })} wide />
              <Field label="물리 타입" value={createForm.physicalType} onChange={(value) => setCreateForm({ ...createForm, physicalType: value })} />
              <div className="grid grid-cols-2 gap-3">
                <Field label="자릿수" value={String(createForm.digits)} onChange={(value) => setCreateForm({ ...createForm, digits: Number(value) })} />
                <Field label="소수점" value={String(createForm.decimalPoint)} onChange={(value) => setCreateForm({ ...createForm, decimalPoint: Number(value) })} />
              </div>
            </div>
            <div className="flex justify-end border-t border-[var(--line)] px-4 py-3">
              <button type="submit" className="inline-flex h-10 items-center gap-2 rounded-md bg-[var(--accent)] px-4 text-sm font-medium text-white">
                <Plus size={16} />
                등록
              </button>
            </div>
          </form>
        </div>
      ) : null}

      {activeTab === "candidate" ? (
        <div className="mx-auto max-w-7xl px-6 py-5">
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
                <label className="block text-sm font-medium">
                  후보 한글명
                  <div className="mt-1 flex gap-2">
                    <input
                      className="h-10 w-full rounded-md border border-[var(--line)] px-3 outline-none focus:border-[var(--accent)]"
                      value={candidateForm.koreanName}
                      onChange={(event) => setCandidateForm({ ...candidateForm, koreanName: event.target.value })}
                    />
                    <button type="button" className="inline-flex h-10 shrink-0 items-center gap-2 rounded-md border border-[var(--line)] px-3 text-sm font-medium" onClick={recommendCandidateForm}>
                      <WandSparkles size={15} />
                      추천
                    </button>
                  </div>
                </label>
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
        </div>
      ) : null}

      {duplicateModal ? (
        <ConfirmModal
          title={duplicateModal.title}
          message={duplicateModal.message}
          items={duplicateModal.items}
          confirmLabel={duplicateModal.confirmLabel}
          onClose={() => setDuplicateModal(null)}
          onConfirm={async () => {
            const action = duplicateModal.onConfirm;
            setDuplicateModal(null);
            await action();
          }}
        />
      ) : null}

      {errorModal ? (
        <ErrorModal title={errorModal.title} message={errorModal.message} detail={errorModal.detail} onClose={() => setErrorModal(null)} />
      ) : null}
    </main>
  );
}

function incrementVersion(version: string): string {
  const parts = version.split(".");
  const lastIndex = parts.length - 1;
  const last = Number(parts[lastIndex]);
  if (Number.isNaN(last)) {
    return version;
  }
  const next = [...parts];
  next[lastIndex] = String(last + 1);
  return next.join(".");
}


function formatDateTime(value: string) {
  return value.replace("T", " ").replace(/\+\d{2}:\d{2}$/, "");
}

function TabButton({ active, label, onClick }: { active: boolean; label: string; onClick: () => void }) {
  return (
    <button type="button" className={`inline-flex h-10 items-center rounded-sm px-4 text-sm font-medium ${active ? "bg-[var(--foreground)] text-white" : "text-[var(--foreground)]"}`} onClick={onClick}>
      {label}
    </button>
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

function Field({ label, value, onChange, wide = false, placeholder }: { label: string; value: string; onChange: (value: string) => void; wide?: boolean; placeholder?: string }) {
  return (
    <label className={`block text-sm font-medium ${wide ? "lg:col-span-2" : ""}`}>
      {label}
      <input className="mt-1 h-10 w-full rounded-md border border-[var(--line)] px-3 outline-none focus:border-[var(--accent)]" value={value} onChange={(event) => onChange(event.target.value)} placeholder={placeholder} />
    </label>
  );
}

function ErrorModal({ title, message, detail, onClose }: { title: string; message: string; detail?: string; onClose: () => void }) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-[rgba(23,32,38,0.45)] px-4">
      <div className="w-full max-w-md rounded-md bg-white shadow-xl">
        <div className="border-b border-[var(--line)] px-4 py-3">
          <div className="text-base font-semibold">{title}</div>
        </div>
        <div className="space-y-3 px-4 py-4">
          <p className="text-sm">{message}</p>
          {detail ? <pre className="overflow-auto whitespace-pre-wrap rounded-md bg-[#fbfcfd] px-3 py-3 text-xs text-[var(--muted)]">{detail}</pre> : null}
        </div>
        <div className="flex justify-end border-t border-[var(--line)] px-4 py-3">
          <button type="button" className="inline-flex h-10 items-center rounded-md bg-[var(--foreground)] px-4 text-sm font-medium text-white" onClick={onClose}>
            확인
          </button>
        </div>
      </div>
    </div>
  );
}

function ConfirmModal({
  title,
  message,
  items,
  confirmLabel,
  onConfirm,
  onClose,
}: {
  title: string;
  message: string;
  items: TermSummary[];
  confirmLabel: string;
  onConfirm: () => Promise<void> | void;
  onClose: () => void;
}) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-[rgba(23,32,38,0.45)] px-4">
      <div className="w-full max-w-2xl rounded-md bg-white shadow-xl">
        <div className="border-b border-[var(--line)] px-4 py-3">
          <div className="text-base font-semibold">{title}</div>
        </div>
        <div className="space-y-4 px-4 py-4">
          <p className="text-sm">{message}</p>
          <div className="overflow-hidden rounded-md border border-[var(--line)]">
            <table className="w-full table-fixed text-left text-sm">
              <thead className="bg-[#eef2f5] text-xs text-[var(--muted)]">
                <tr>
                  <th className="w-[28%] px-4 py-2">표준 용어</th>
                  <th className="w-[22%] px-3 py-2">영문명</th>
                  <th className="w-[18%] px-3 py-2">약어</th>
                  <th className="w-[18%] px-3 py-2">API</th>
                  <th className="px-3 py-2">상태</th>
                </tr>
              </thead>
              <tbody>
                {items.map((item) => (
                  <tr key={item.termId} className="border-t border-[var(--line)]">
                    <td className="px-4 py-3 font-medium">{item.koreanName}</td>
                    <td className="truncate px-3 py-3">{item.englishName}</td>
                    <td className="px-3 py-3 text-[var(--muted)]">{item.englishAbbreviation}</td>
                    <td className="px-3 py-3 text-[var(--muted)]">{item.apiFieldName}</td>
                    <td className="px-3 py-3"><StatusPill status={item.status} /></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        <div className="flex justify-end gap-2 border-t border-[var(--line)] px-4 py-3">
          <button type="button" className="inline-flex h-10 items-center rounded-md border border-[var(--line)] px-4 text-sm font-medium" onClick={onClose}>
            취소
          </button>
          <button type="button" className="inline-flex h-10 items-center rounded-md bg-[var(--accent)] px-4 text-sm font-medium text-white" onClick={() => void onConfirm()}>
            {confirmLabel}
          </button>
        </div>
      </div>
    </div>
  );
}
