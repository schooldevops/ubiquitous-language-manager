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

const sampleOrderNumberTerm: Term = {
  termId: "T-000004",
  termNumber: "TERM-000004",
  domainName: "주문",
  usageType: "표준항목",
  koreanName: "주문번호",
  englishName: "Order Number",
  englishAbbreviation: "ORD_NO",
  businessDefinition: "회사에서 주문을 업무적으로 식별하기 위해 사용하는 번호",
  usageContext: "주문 접수, 주문 조회, 주문 취소, 결제, 배송 등에서 주문 식별 기준으로 사용",
  physicalType: "VARCHAR",
  digits: 20,
  decimalPoint: 0,
  status: TermStatus.Approved,
  owner: "주문도메인 데이터스튜어드",
  version: "1.0",
  expressions: [
    { expressionId: 41, termId: "T-000004", expressionType: "Korean", expressionValue: "주문번호", language: "ko", style: "standard", isStandard: true },
    { expressionId: 42, termId: "T-000004", expressionType: "English", expressionValue: "Order Number", language: "en", style: "title", isStandard: true },
    { expressionId: 43, termId: "T-000004", expressionType: "DB_COLUMN", expressionValue: "ORD_NO", language: "en", style: "UPPER_SNAKE", isStandard: true },
    { expressionId: 44, termId: "T-000004", expressionType: "API_FIELD", expressionValue: "orderNumber", language: "en", style: "camelCase", isStandard: true },
    { expressionId: 45, termId: "T-000004", expressionType: "CODE_VARIABLE", expressionValue: "orderNumber", language: "en", style: "camelCase", isStandard: true },
    { expressionId: 46, termId: "T-000004", expressionType: "UI_LABEL", expressionValue: "주문번호", language: "ko", style: "label", isStandard: true },
    { expressionId: 47, termId: "T-000004", expressionType: "TEST_WORD", expressionValue: "주문번호", language: "ko", style: "gherkin", isStandard: true },
  ],
  aliases: [
    { aliasId: "A-000041", termId: "T-000004", aliasName: "주문ID", aliasType: "Synonym", recommendationAction: "주문번호로 변환 권장", reason: "업무 주문 식별 번호 의미로 사용되는 경우 표준 용어는 주문번호" },
    { aliasId: "A-000042", termId: "T-000004", aliasName: "orderId", aliasType: "Synonym", recommendationAction: "orderNumber로 변환 권장", reason: "API 표준 필드명은 orderNumber" },
    { aliasId: "A-000043", termId: "T-000004", aliasName: "ORD_ID", aliasType: "Forbidden", recommendationAction: "ORD_NO 사용", reason: "기술 ID와 업무 주문번호가 혼동될 수 있음" },
  ],
};

const sampleOrderDateTerm: Term = {
  termId: "T-000005",
  termNumber: "TERM-000005",
  domainName: "주문",
  usageType: "표준항목",
  koreanName: "주문일자",
  englishName: "Order Date",
  englishAbbreviation: "ORD_DT",
  businessDefinition: "주문이 업무적으로 발생한 날짜",
  usageContext: "주문 목록, 주문 상세, 매출 집계, 주문 이력 조회에서 날짜 기준으로 사용",
  physicalType: "DATE",
  digits: 8,
  decimalPoint: 0,
  status: TermStatus.Approved,
  owner: "주문도메인 데이터스튜어드",
  version: "1.0",
  expressions: [
    { expressionId: 51, termId: "T-000005", expressionType: "Korean", expressionValue: "주문일자", language: "ko", style: "standard", isStandard: true },
    { expressionId: 52, termId: "T-000005", expressionType: "English", expressionValue: "Order Date", language: "en", style: "title", isStandard: true },
    { expressionId: 53, termId: "T-000005", expressionType: "DB_COLUMN", expressionValue: "ORD_DT", language: "en", style: "UPPER_SNAKE", isStandard: true },
    { expressionId: 54, termId: "T-000005", expressionType: "API_FIELD", expressionValue: "orderDate", language: "en", style: "camelCase", isStandard: true },
    { expressionId: 55, termId: "T-000005", expressionType: "CODE_VARIABLE", expressionValue: "orderDate", language: "en", style: "camelCase", isStandard: true },
    { expressionId: 56, termId: "T-000005", expressionType: "UI_LABEL", expressionValue: "주문일자", language: "ko", style: "label", isStandard: true },
    { expressionId: 57, termId: "T-000005", expressionType: "TEST_WORD", expressionValue: "주문일자", language: "ko", style: "gherkin", isStandard: true },
  ],
  aliases: [
    { aliasId: "A-000051", termId: "T-000005", aliasName: "주문날짜", aliasType: "Synonym", recommendationAction: "주문일자로 변환 권장", reason: "날짜 단위 주문 발생 기준은 주문일자" },
    { aliasId: "A-000052", termId: "T-000005", aliasName: "orderDatetime", aliasType: "Synonym", recommendationAction: "시분초가 필요하면 주문일시 신규 검토", reason: "주문일자는 날짜만 표현함" },
  ],
};

const sampleTermMap: Record<string, Term> = {
  [sampleTerm.termId]: sampleTerm,
  [sampleOrderNumberTerm.termId]: sampleOrderNumberTerm,
  [sampleOrderDateTerm.termId]: sampleOrderDateTerm,
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
    termId: sampleOrderNumberTerm.termId,
    termNumber: sampleOrderNumberTerm.termNumber,
    domainName: sampleOrderNumberTerm.domainName,
    koreanName: sampleOrderNumberTerm.koreanName,
    englishName: sampleOrderNumberTerm.englishName,
    englishAbbreviation: sampleOrderNumberTerm.englishAbbreviation,
    apiFieldName: "orderNumber",
    status: sampleOrderNumberTerm.status,
    relatedSystems: ["ORDER"],
  },
  {
    termId: sampleOrderDateTerm.termId,
    termNumber: sampleOrderDateTerm.termNumber,
    domainName: sampleOrderDateTerm.domainName,
    koreanName: sampleOrderDateTerm.koreanName,
    englishName: sampleOrderDateTerm.englishName,
    englishAbbreviation: sampleOrderDateTerm.englishAbbreviation,
    apiFieldName: "orderDate",
    status: sampleOrderDateTerm.status,
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

const sampleOrderNumberImpact: ImpactAnalysisResponse = {
  termId: "T-000004",
  standardTerm: "주문번호",
  changeType: ImpactChangeType.ApiFieldRename,
  includeTwoHop: true,
  riskLevel: ImpactRiskLevel.High,
  riskScore: 88,
  impactedTargets: [
    { targetType: "SYSTEM", targetName: "ORDER", systemCode: "ORDER", location: "TERM_MASTER.T-000004", hop: 1, reason: "주문번호 용어를 사용하는 주문 기준 시스템" },
    { targetType: "DB_TABLE", targetName: "ORDER_HEADER", systemCode: "ORDER", location: "ORDER.ORDER_HEADER", hop: 1, reason: "주문번호가 주문 헤더 대표 식별자로 사용됨" },
    { targetType: "DB_COLUMN", targetName: "ORD_NO", systemCode: "ORDER", location: "ORDER.ORDER_HEADER.ORD_NO", hop: 1, reason: "ORD_NO 표현이 표준 용어 주문번호를 참조함" },
    { targetType: "API_FIELD", targetName: "orderNumber", systemCode: "ORDER", location: "GET /orders/{orderNumber}", hop: 1, reason: "orderNumber 경로 변수와 응답 필드가 주문번호를 참조함" },
    { targetType: "DTO", targetName: "OrderResponse.orderNumber", systemCode: "ORDER", location: "OrderResponse.orderNumber", hop: 1, reason: "DTO 필드가 주문번호 표준 표현을 사용함" },
    { targetType: "DOCUMENT", targetName: "주문 상세 조회 기획서", systemCode: "PLANNING", location: "docs/planning/order-detail.md", hop: 2, reason: "주문번호 변경 시 기획서 용어 매핑표와 조회 조건 확인 필요" },
    { targetType: "TEST_CASE", targetName: "주문번호로 주문 상세 조회", systemCode: "QA", location: "tests/order-detail-by-order-number.feature", hop: 2, reason: "Given/When/Then 테스트 용어와 오류 메시지 확인 필요" },
  ],
  recommendations: [
    { priority: 1, action: "주문 조회 API 경로 변수와 응답 필드 호환성을 검토한다", reason: "주문번호는 주문 조회 계약에서 직접 사용됨" },
    { priority: 2, action: "ORDER_HEADER.ORD_NO 참조 SQL과 DTO를 함께 점검한다", reason: "DB 컬럼, SQL Mapper, DTO 표현이 함께 변경되어야 함" },
    { priority: 3, action: "주문번호 별칭 orderId, 주문ID 사용 위치를 표준화한다", reason: "기술 ID와 업무 주문번호 혼동을 줄여야 함" },
  ],
};

const sampleOrderDateImpact: ImpactAnalysisResponse = {
  termId: "T-000005",
  standardTerm: "주문일자",
  changeType: ImpactChangeType.ApiFieldRename,
  includeTwoHop: true,
  riskLevel: ImpactRiskLevel.Medium,
  riskScore: 64,
  impactedTargets: [
    { targetType: "SYSTEM", targetName: "ORDER", systemCode: "ORDER", location: "TERM_MASTER.T-000005", hop: 1, reason: "주문일자 용어를 사용하는 주문 기준 시스템" },
    { targetType: "DB_COLUMN", targetName: "ORD_DT", systemCode: "ORDER", location: "ORDER.ORDER_HEADER.ORD_DT", hop: 1, reason: "ORD_DT 표현이 표준 용어 주문일자를 참조함" },
    { targetType: "API_FIELD", targetName: "orderDate", systemCode: "ORDER", location: "GET /orders response.orderDate", hop: 1, reason: "orderDate 응답 필드가 주문일자를 참조함" },
    { targetType: "DOCUMENT", targetName: "주문 목록 기획서", systemCode: "PLANNING", location: "docs/planning/order-list.md", hop: 2, reason: "주문 목록 표시 항목과 기간 검색 조건 확인 필요" },
  ],
  recommendations: [
    { priority: 1, action: "주문일자와 주문일시의 사용 기준을 명확히 한다", reason: "날짜와 시분초 정밀도 혼동 가능성이 있음" },
    { priority: 2, action: "기간 검색 API의 from/to 필드명을 검토한다", reason: "주문일자 변경은 검색 조건에도 영향이 있음" },
    { priority: 3, action: "집계/리포트 SQL의 날짜 기준을 확인한다", reason: "매출 집계 기준일로 사용될 수 있음" },
  ],
};

const sampleImpactMap: Record<string, ImpactAnalysisResponse> = {
  [sampleImpact.termId]: sampleImpact,
  [sampleOrderNumberImpact.termId]: sampleOrderNumberImpact,
  [sampleOrderDateImpact.termId]: sampleOrderDateImpact,
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
    if (response.data.items.length > 0) {
      return response.data;
    }
    return fallbackList(q, domainName, status);
  } catch {
    return fallbackList(q, domainName, status);
  }
}

export async function getTerm(termId: string): Promise<Term> {
  try {
    const response = await termApi.getTerm(termId);
    return response.data;
  } catch {
    return sampleTermMap[termId] ?? { ...sampleTerm, termId };
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
    return sampleTermMap[termId]?.expressions ?? sampleTerm.expressions;
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
    return sampleTermMap[termId]?.aliases ?? sampleTerm.aliases;
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
    const impact = sampleImpactMap[termId] ?? sampleImpact;
    return { ...impact, termId, changeType, includeTwoHop };
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
