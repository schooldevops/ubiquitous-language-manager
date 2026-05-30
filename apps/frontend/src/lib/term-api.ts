"use client";

import axios from "axios";
import {
  AliasApi,
  CandidateApi,
  CandidateStatus,
  Configuration,
  ExpressionApi,
  ImpactApi,
  ImpactChangeType,
  ImpactRiskLevel,
  TermApi,
  TermStatus,
  type CandidatePromotionResult,
  type ImpactAnalysisResponse,
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
const impactApi = new ImpactApi(configuration, basePath, axiosInstance);

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

const sampleImpact: ImpactAnalysisResponse = {
  termId: "T-000001",
  standardTerm: "고객번호",
  changeType: ImpactChangeType.ApiFieldRename,
  includeTwoHop: true,
  riskLevel: ImpactRiskLevel.High,
  riskScore: 91,
  impactedTargets: [
    { targetType: "SYSTEM", targetName: "DICTIONARY", systemCode: "DICTIONARY", location: "TERM_MASTER.T-000001", hop: 1, reason: "고객번호 용어를 관리하는 기준 시스템" },
    { targetType: "DB_TABLE", targetName: "고객_MASTER", systemCode: "DICTIONARY", location: "DICTIONARY.고객_MASTER", hop: 1, reason: "고객번호 용어가 속한 고객 도메인 대표 테이블" },
    { targetType: "DB_COLUMN", targetName: "CUST_NO", systemCode: "DICTIONARY", location: "column:DICTIONARY.고객.CUST_NO", hop: 1, reason: "CUST_NO 표현이 표준 용어 고객번호를 참조함" },
    { targetType: "API_FIELD", targetName: "customerNumber", systemCode: "DICTIONARY", location: "apiField:DICTIONARY.고객.customerNumber", hop: 1, reason: "customerNumber 표현이 표준 용어 고객번호를 참조함" },
    { targetType: "DTO", targetName: "customerNumber", systemCode: "DICTIONARY", location: "dtoField:DICTIONARY.고객.customerNumber", hop: 1, reason: "customerNumber 표현이 표준 용어 고객번호를 참조함" },
    { targetType: "DOCUMENT", targetName: "고객번호 기획서 표현", systemCode: "PLANNING", location: "docs/planning/t-000001-requirements.md", hop: 2, reason: "고객번호 변경 시 기획서 용어 매핑표와 요구사항 문장 확인 필요" },
    { targetType: "TEST_CASE", targetName: "고객번호 필수 검증", systemCode: "QA", location: "tests/t-000001-required.feature", hop: 2, reason: "고객번호 변경 시 Given/When/Then 테스트 용어와 오류 메시지 확인 필요" },
  ],
  recommendations: [
    { priority: 1, action: "OpenAPI v2 필드와 하위 호환 기간을 정의한다", reason: "API 필드명 변경은 외부 계약과 프런트엔드 클라이언트에 직접 영향이 있음" },
    { priority: 2, action: "OpenAPI, DTO, 테스트, 기획서 용어를 함께 검토한다", reason: "유비쿼터스 랭기지는 산출물 간 표현 일관성이 핵심임" },
    { priority: 3, action: "변경 승인 후 RAG와 Graphify 인덱스를 재생성한다", reason: "2-hop 영향 대상까지 확장했으므로 관계 검색 결과도 최신화해야 함" },
  ],
};

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

export async function getTermImpact(termId: string, changeType: ImpactChangeType = ImpactChangeType.ApiFieldRename, includeTwoHop = true): Promise<ImpactAnalysisResponse> {
  try {
    const response = await impactApi.getTermImpact(termId, changeType, includeTwoHop);
    return response.data;
  } catch {
    return { ...sampleImpact, termId };
  }
}

export { CandidateStatus, ImpactChangeType, ImpactRiskLevel, TermStatus };
export type {
  CandidatePromotionResult,
  ImpactAnalysisResponse,
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
