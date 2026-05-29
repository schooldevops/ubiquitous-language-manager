"use client";

import axios from "axios";
import {
  AliasApi,
  CandidateApi,
  CandidateStatus,
  Configuration,
  ExpressionApi,
  TermApi,
  TermStatus,
  type CandidatePromotionResult,
  type Term,
  type TermAlias,
  type TermAliasCreateRequest,
  type TermCandidate,
  type TermCandidateCreateRequest,
  type TermCandidateListResponse,
  type TermCandidatePromoteRequest,
  type TermCandidateReviewRequest,
  type TermCandidateSummary,
  type TermCreateRequest,
  type TermExpression,
  type TermExpressionCreateRequest,
  type TermListResponse,
  type TermSummary,
  type TermUpdateRequest,
} from "@aulms/api-client";

const basePath = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

const configuration = new Configuration({
  basePath,
  accessToken: process.env.NEXT_PUBLIC_API_TOKEN ?? "local-dev-token",
});

const axiosInstance = axios.create({
  baseURL: basePath,
  timeout: 3000,
});

const termApi = new TermApi(configuration, basePath, axiosInstance);
const expressionApi = new ExpressionApi(configuration, basePath, axiosInstance);
const aliasApi = new AliasApi(configuration, basePath, axiosInstance);
const candidateApi = new CandidateApi(configuration, basePath, axiosInstance);

const sampleTerm: Term = {
  termId: "T-000001",
  termNumber: "TERM-000001",
  domainName: "고객",
  usageType: "표준항목",
  koreanName: "고객번호",
  englishName: "Customer Number",
  englishAbbreviation: "CUST_NO",
  businessDefinition: "회사에서 고객을 업무적으로 식별하기 위해 사용하는 번호",
  usageContext: "주문, 계약, 청구, 상담 등에서 고객 식별 기준으로 사용",
  physicalType: "VARCHAR",
  digits: 20,
  decimalPoint: 0,
  status: TermStatus.Approved,
  owner: "고객도메인 데이터스튜어드",
  version: "1.0",
  expressions: [
    { expressionId: 1, termId: "T-000001", expressionType: "Korean", expressionValue: "고객번호", language: "ko", style: "standard", isStandard: true },
    { expressionId: 2, termId: "T-000001", expressionType: "English", expressionValue: "Customer Number", language: "en", style: "title", isStandard: true },
    { expressionId: 3, termId: "T-000001", expressionType: "DB_COLUMN", expressionValue: "CUST_NO", language: "en", style: "UPPER_SNAKE", isStandard: true },
    { expressionId: 4, termId: "T-000001", expressionType: "API_FIELD", expressionValue: "customerNumber", language: "en", style: "camelCase", isStandard: true },
    { expressionId: 5, termId: "T-000001", expressionType: "CODE_VARIABLE", expressionValue: "customerNumber", language: "en", style: "camelCase", isStandard: true },
    { expressionId: 6, termId: "T-000001", expressionType: "UI_LABEL", expressionValue: "고객번호", language: "ko", style: "label", isStandard: true },
    { expressionId: 7, termId: "T-000001", expressionType: "TEST_WORD", expressionValue: "고객번호", language: "ko", style: "gherkin", isStandard: true },
  ],
  aliases: [
    { aliasId: "A-000001", termId: "T-000001", aliasName: "고객ID", aliasType: "Synonym", recommendationAction: "고객번호로 변환 권장", reason: "업무 고객 식별 번호 의미로 사용되는 경우 표준 용어는 고객번호" },
    { aliasId: "A-000004", termId: "T-000001", aliasName: "customerId", aliasType: "Synonym", recommendationAction: "customerNumber로 변환 권장", reason: "API 표준 필드명은 customerNumber" },
    { aliasId: "A-000006", termId: "T-000001", aliasName: "CUST_ID", aliasType: "Forbidden", recommendationAction: "CUST_NO 사용", reason: "기술 ID와 업무 고객번호가 혼동될 수 있음" },
  ],
};

const sampleTerms: TermSummary[] = [
  {
    termId: sampleTerm.termId,
    termNumber: sampleTerm.termNumber,
    domainName: sampleTerm.domainName,
    koreanName: sampleTerm.koreanName,
    englishName: sampleTerm.englishName,
    englishAbbreviation: sampleTerm.englishAbbreviation,
    apiFieldName: "customerNumber",
    status: sampleTerm.status,
    relatedSystems: ["CRM", "ORDER"],
  },
  {
    termId: "T-000004",
    termNumber: "TERM-000004",
    domainName: "주문",
    koreanName: "주문번호",
    englishName: "Order Number",
    englishAbbreviation: "ORD_NO",
    apiFieldName: "orderNumber",
    status: TermStatus.Approved,
    relatedSystems: ["ORDER"],
  },
  {
    termId: "T-000005",
    termNumber: "TERM-000005",
    domainName: "주문",
    koreanName: "주문일자",
    englishName: "Order Date",
    englishAbbreviation: "ORD_DT",
    apiFieldName: "orderDate",
    status: TermStatus.Approved,
    relatedSystems: ["ORDER"],
  },
];

const sampleCandidate: TermCandidate = {
  candidateId: "CAND-000001",
  koreanName: "고객선호배송시간대",
  englishName: "Customer Preferred Delivery Time Slot",
  englishAbbreviation: "CUST_PREF_DLV_TM_SLOT",
  domainName: "고객",
  businessDefinition: "고객이 선호하는 배송 시간대",
  usageContext: "배송 옵션 추천과 배송 요청 화면에서 사용",
  physicalType: "VARCHAR",
  digits: 20,
  decimalPoint: 0,
  status: CandidateStatus.Draft,
  requestedBy: "planner01",
  similarTerms: [
    {
      termId: "T-000001",
      koreanName: "고객번호",
      englishName: "Customer Number",
      dbColumn: "CUST_NO",
      apiField: "customerNumber",
      reason: "같은 고객 도메인의 기존 표준 용어",
    },
  ],
  histories: [
    {
      historyId: "CAND-HIST-000001",
      candidateId: "CAND-000001",
      status: CandidateStatus.Draft,
      reason: "신규 후보 등록",
      actor: "planner01",
      createdAt: new Date().toISOString(),
    },
  ],
};

function fallbackCandidateList(q?: string, status?: CandidateStatus, domainName?: string): TermCandidateListResponse {
  const normalized = q?.trim().toLowerCase();
  const matches =
    (!normalized || sampleCandidate.koreanName.toLowerCase().includes(normalized) || sampleCandidate.englishName.toLowerCase().includes(normalized) || sampleCandidate.englishAbbreviation.toLowerCase().includes(normalized)) &&
    (!status || sampleCandidate.status === status) &&
    (!domainName || sampleCandidate.domainName === domainName);
  const items: TermCandidateSummary[] = matches ? [toCandidateSummary(sampleCandidate)] : [];
  return {
    items,
    page: {
      page: 0,
      size: 20,
      totalElements: items.length,
      totalPages: items.length > 0 ? 1 : 0,
    },
  };
}

function toCandidateSummary(candidate: TermCandidate): TermCandidateSummary {
  return {
    candidateId: candidate.candidateId,
    koreanName: candidate.koreanName,
    englishName: candidate.englishName,
    englishAbbreviation: candidate.englishAbbreviation,
    domainName: candidate.domainName,
    status: candidate.status,
    requestedBy: candidate.requestedBy,
    promotedTermId: candidate.promotedTermId,
  };
}

function fallbackList(q?: string, domainName?: string, status?: TermStatus): TermListResponse {
  const normalized = q?.trim().toLowerCase();
  const items = sampleTerms.filter((term) => {
    const matchesQuery =
      !normalized ||
      term.koreanName.toLowerCase().includes(normalized) ||
      term.englishName.toLowerCase().includes(normalized) ||
      term.englishAbbreviation.toLowerCase() === normalized ||
      term.apiFieldName?.toLowerCase() === normalized ||
      (normalized === "고객id" && term.termId === "T-000001") ||
      (normalized === "customerid" && term.termId === "T-000001");
    const matchesDomain = !domainName || term.domainName === domainName;
    const matchesStatus = !status || term.status === status;
    return matchesQuery && matchesDomain && matchesStatus;
  });

  return {
    items,
    page: {
      page: 0,
      size: 20,
      totalElements: items.length,
      totalPages: items.length > 0 ? 1 : 0,
    },
  };
}

export async function listTerms(q?: string, domainName?: string, status?: TermStatus): Promise<TermListResponse> {
  try {
    const response = await termApi.listTerms(q || undefined, domainName || undefined, status || undefined, 0, 20);
    return response.data;
  } catch {
    return fallbackList(q, domainName, status);
  }
}

export async function getTerm(termId: string): Promise<Term> {
  try {
    const response = await termApi.getTerm(termId);
    return response.data;
  } catch {
    return termId === sampleTerm.termId ? sampleTerm : { ...sampleTerm, termId };
  }
}

export async function createTerm(request: TermCreateRequest): Promise<Term> {
  const response = await termApi.createTerm(request);
  return response.data;
}

export async function updateTerm(termId: string, request: TermUpdateRequest): Promise<Term> {
  const response = await termApi.updateTerm(termId, request);
  return response.data;
}

export async function listExpressions(termId: string): Promise<TermExpression[]> {
  try {
    const response = await expressionApi.listTermExpressions(termId);
    return response.data;
  } catch {
    return sampleTerm.expressions;
  }
}

export async function createExpression(termId: string, request: TermExpressionCreateRequest): Promise<TermExpression> {
  const response = await expressionApi.createTermExpression(termId, request);
  return response.data;
}

export async function listAliases(termId: string): Promise<TermAlias[]> {
  try {
    const response = await aliasApi.listTermAliases(termId);
    return response.data;
  } catch {
    return sampleTerm.aliases;
  }
}

export async function createAlias(termId: string, request: TermAliasCreateRequest): Promise<TermAlias> {
  const response = await aliasApi.createTermAlias(termId, request);
  return response.data;
}

export async function listCandidates(q?: string, status?: CandidateStatus, domainName?: string): Promise<TermCandidateListResponse> {
  try {
    const response = await candidateApi.listCandidates(q || undefined, status || undefined, domainName || undefined, 0, 20);
    return response.data;
  } catch {
    return fallbackCandidateList(q, status, domainName);
  }
}

export async function getCandidate(candidateId: string): Promise<TermCandidate> {
  try {
    const response = await candidateApi.getCandidate(candidateId);
    return response.data;
  } catch {
    return { ...sampleCandidate, candidateId };
  }
}

export async function createCandidate(request: TermCandidateCreateRequest): Promise<TermCandidate> {
  try {
    const response = await candidateApi.createCandidate(request);
    return response.data;
  } catch {
    return { ...sampleCandidate, ...request, candidateId: "CAND-LOCAL" };
  }
}

export async function reviewCandidate(candidateId: string, request: TermCandidateReviewRequest): Promise<TermCandidate> {
  const response = await candidateApi.reviewCandidate(candidateId, request);
  return response.data;
}

export async function promoteCandidate(candidateId: string, request: TermCandidatePromoteRequest): Promise<CandidatePromotionResult> {
  const response = await candidateApi.promoteCandidate(candidateId, request);
  return response.data;
}

export { CandidateStatus, TermStatus };
export type {
  CandidatePromotionResult,
  Term,
  TermAlias,
  TermCandidate,
  TermCandidateCreateRequest,
  TermCandidateListResponse,
  TermCandidatePromoteRequest,
  TermCandidateReviewRequest,
  TermCandidateSummary,
  TermCreateRequest,
  TermExpression,
  TermSummary,
  TermUpdateRequest,
};
