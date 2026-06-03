"use client";

import axios from "axios";
import {
  AIApi,
  AliasApi,
  CandidateApi,
  CandidateStatus,
  Configuration,
  type ErrorResponse,
  ExpressionApi,
  GovernanceApi,
  ImpactApi,
  ImpactChangeType,
  ImpactRiskLevel,
  TermRecommendationMode,
  TermApi,
  TermStatus,
  type GraphRecommendationContext,
  type RecommendationEvidence,
  type RecommendedTermDraft,
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
  type TermChangeHistory,
  type TermChangeHistoryListResponse,
  type TermExpression,
  type TermExpressionCreateRequest,
  type TermListResponse,
  type TermRecommendationRequest,
  type TermRecommendationResponse,
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
const aiApi = new AIApi(configuration, basePath, axiosInstance);
const expressionApi = new ExpressionApi(configuration, basePath, axiosInstance);
const aliasApi = new AliasApi(configuration, basePath, axiosInstance);
const candidateApi = new CandidateApi(configuration, basePath, axiosInstance);
const impactApi = new ImpactApi(configuration, basePath, axiosInstance);
const governanceApi = new GovernanceApi(configuration, basePath, axiosInstance);

export type ApiErrorInfo = {
  status?: number;
  code?: string;
  message: string;
  detail?: string;
  traceId?: string;
};

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

const sampleTermHistories: Record<string, TermChangeHistory[]> = {
  "T-000001": [
    { changeHistoryId: 1, termId: "T-000001", changeType: "CREATE", reason: "표준 용어 등록", createdAt: "2026-05-30T09:00:00+09:00", newStatus: TermStatus.Approved },
    { changeHistoryId: 2, termId: "T-000001", changeType: "UPDATE", reason: "업무 정의와 사용 맥락 보완", createdAt: "2026-05-30T10:00:00+09:00", previousStatus: TermStatus.Approved, newStatus: TermStatus.Approved, approvedBy: "data.steward" },
  ],
  "T-000004": [
    { changeHistoryId: 3, termId: "T-000004", changeType: "CREATE", reason: "표준 용어 등록", createdAt: "2026-05-30T09:10:00+09:00", newStatus: TermStatus.Approved },
    { changeHistoryId: 4, termId: "T-000004", changeType: "UPDATE", reason: "주문 조회 API 설명 보완", createdAt: "2026-05-30T10:30:00+09:00", previousStatus: TermStatus.Approved, newStatus: TermStatus.Approved, approvedBy: "order.steward" },
  ],
  "T-000005": [
    { changeHistoryId: 5, termId: "T-000005", changeType: "CREATE", reason: "표준 용어 등록", createdAt: "2026-05-30T09:20:00+09:00", newStatus: TermStatus.Approved },
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

const localCandidates: Record<string, TermCandidate> = {
  [sampleCandidate.candidateId]: sampleCandidate,
};

function fallbackCandidateList(q?: string, status?: CandidateStatus, domainName?: string): TermCandidateListResponse {
  const normalized = q?.trim().toLowerCase();
  const items = Object.values(localCandidates)
    .filter((candidate) => {
      const matchesQuery =
        !normalized ||
        candidate.koreanName.toLowerCase().includes(normalized) ||
        candidate.englishName.toLowerCase().includes(normalized) ||
        candidate.englishAbbreviation.toLowerCase().includes(normalized);
      return matchesQuery && (!status || candidate.status === status) && (!domainName || candidate.domainName === domainName);
    })
    .map(toCandidateSummary);
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

function fallbackReviewCandidate(candidateId: string, request: TermCandidateReviewRequest): TermCandidate {
  const candidate = localCandidates[candidateId] ?? { ...sampleCandidate, candidateId };
  const nextStatus =
    request.decision === "Approve" ? CandidateStatus.Approved :
    request.decision === "Reject" ? CandidateStatus.Rejected :
    CandidateStatus.Reviewing;
  const reviewed: TermCandidate = {
    ...candidate,
    status: nextStatus,
    reviewedBy: request.reviewer,
    updatedAt: new Date().toISOString(),
    histories: [
      ...candidate.histories,
      {
        historyId: `CAND-HIST-LOCAL-${candidate.histories.length + 1}`,
        candidateId,
        status: nextStatus,
        reason: request.reason,
        actor: request.reviewer,
        createdAt: new Date().toISOString(),
      },
    ],
  };
  localCandidates[candidateId] = reviewed;
  return reviewed;
}

function fallbackPromoteCandidate(candidateId: string, request: TermCandidatePromoteRequest): CandidatePromotionResult {
  const candidate = localCandidates[candidateId] ?? { ...sampleCandidate, candidateId };
  const termId = `T-${candidateId.replace(/[^A-Z0-9]/gi, "").toUpperCase()}`;
  const apiFieldName = toCamelCase(candidate.englishName);
  const term: Term = {
    termId,
    termNumber: `TERM-${termId.replace(/^T-/, "")}`,
    domainName: candidate.domainName,
    usageType: "표준항목",
    koreanName: candidate.koreanName,
    englishName: candidate.englishName,
    englishAbbreviation: candidate.englishAbbreviation,
    businessDefinition: candidate.businessDefinition,
    usageContext: candidate.usageContext,
    physicalType: candidate.physicalType,
    digits: candidate.digits,
    decimalPoint: candidate.decimalPoint,
    status: TermStatus.Approved,
    owner: request.owner,
    version: "1.0",
    expressions: [
      { expressionId: Date.now(), termId, expressionType: "Korean", expressionValue: candidate.koreanName, language: "ko", style: "standard", isStandard: true },
      { expressionId: Date.now() + 1, termId, expressionType: "English", expressionValue: candidate.englishName, language: "en", style: "title", isStandard: true },
      { expressionId: Date.now() + 2, termId, expressionType: "DB_COLUMN", expressionValue: candidate.englishAbbreviation, language: "en", style: "UPPER_SNAKE", isStandard: true },
      { expressionId: Date.now() + 3, termId, expressionType: "API_FIELD", expressionValue: apiFieldName, language: "en", style: "camelCase", isStandard: true },
      { expressionId: Date.now() + 4, termId, expressionType: "CODE_VARIABLE", expressionValue: apiFieldName, language: "en", style: "camelCase", isStandard: true },
      { expressionId: Date.now() + 5, termId, expressionType: "UI_LABEL", expressionValue: candidate.koreanName, language: "ko", style: "label", isStandard: true },
    ],
    aliases: [],
  };
  const promoted: TermCandidate = {
    ...candidate,
    status: CandidateStatus.Promoted,
    reviewedBy: request.approver,
    promotedTermId: termId,
    updatedAt: new Date().toISOString(),
    histories: [
      ...candidate.histories,
      {
        historyId: `CAND-HIST-LOCAL-${candidate.histories.length + 1}`,
        candidateId,
        status: CandidateStatus.Promoted,
        reason: request.reason,
        actor: request.approver,
        createdAt: new Date().toISOString(),
      },
    ],
  };
  localCandidates[candidateId] = promoted;
  sampleTermMap[termId] = term;
  sampleTerms.push({
    termId: term.termId,
    termNumber: term.termNumber,
    domainName: term.domainName,
    koreanName: term.koreanName,
    englishName: term.englishName,
    englishAbbreviation: term.englishAbbreviation,
    apiFieldName,
    status: term.status,
    relatedSystems: [term.domainName],
  });
  return { candidate: promoted, term };
}

function toCamelCase(value: string): string {
  const words = value.trim().split(/\s+/);
  return words
    .map((word, index) => {
      const normalized = word.charAt(0).toUpperCase() + word.slice(1);
      return index === 0 ? normalized.charAt(0).toLowerCase() + normalized.slice(1) : normalized;
    })
    .join("");
}

function fallbackRecommendedDraft(koreanName: string, currentDomainName?: string): RecommendedTermDraft {
  const normalized = koreanName.trim();
  const domainName = inferDomainName(normalized, currentDomainName);
  const baseName = normalized.startsWith(domainName) ? normalized.slice(domainName.length) : normalized;
  const pattern = inferSuffixPattern(normalized);
  return {
    domainName,
    usageType: "표준항목",
    englishName: [domainEnglishMap[domainName] ?? "Common", toEnglishPhrase(baseName)].filter(Boolean).join(" ").replace(/\s+/g, " ").trim(),
    englishAbbreviation: [domainAbbreviationMap[domainName] ?? "CMN", ...toAbbreviationTokens(baseName), pattern.suffixToken].filter(Boolean).join("_").replace(/__+/g, "_"),
    businessDefinition: `${domainName} 도메인에서 ${normalized}를 업무적으로 관리하기 위해 사용하는 표준 항목`,
    usageContext: `${domainName} 관련 화면, API, DB, 테스트 시나리오에서 ${normalized} 표현으로 사용`,
    physicalType: pattern.physicalType,
    digits: pattern.digits,
    decimalPoint: pattern.decimalPoint,
    owner: `${domainName}도메인 데이터스튜어드`,
  };
}

function fallbackRecommendTermDraft(request: TermRecommendationRequest): TermRecommendationResponse {
  const recommendation = fallbackRecommendedDraft(request.koreanName, request.currentDomainName);
  const ragMatches: RecommendationEvidence[] = sampleTerms
    .filter((term) => term.domainName === recommendation.domainName)
    .slice(0, 3)
    .map((term, index) => ({
      termId: term.termId,
      standardTerm: term.koreanName,
      englishName: term.englishName,
      dbColumn: term.englishAbbreviation,
      apiField: term.apiFieldName ?? toCamelCase(term.englishName),
      domainName: term.domainName,
      source: index === 0 ? "Semantic" as RecommendationEvidence["source"] : "Alias" as RecommendationEvidence["source"],
      score: index === 0 ? 0.82 : 0.54,
      reason: `${term.domainName} 도메인의 기존 표준 용어 패턴을 참고한 fallback 추천`,
    }));
  const graphContext: GraphRecommendationContext = {
    inferredDomainName: recommendation.domainName,
    relatedTerms: ragMatches.map((item) => item.standardTerm),
    relationshipHints: [
      `${recommendation.domainName} 도메인 표준 용어와 함께 검토해야 함`,
      `대표 식별 용어를 기준으로 신규 속성이 연결되는 패턴을 참고함`,
    ],
  };
  return {
    inputKoreanName: request.koreanName,
    normalizedKoreanName: request.koreanName.replace(/\s+/g, ""),
    recommendation,
    ragMatches,
    graphContext,
    llmReasoning: `fallback 추천. ${recommendation.domainName} 도메인 패턴과 기존 표준 용어를 조합함`,
    warnings: ["백엔드 추천 API 연결 실패로 로컬 fallback 추천을 사용함"],
  };
}

const domainEnglishMap: Record<string, string> = {
  고객: "Customer",
  주문: "Order",
  결제: "Payment",
  상품: "Product",
  계약: "Contract",
  청구: "Billing",
  상담: "Consultation",
  공통: "Common",
};

const domainAbbreviationMap: Record<string, string> = {
  고객: "CUST",
  주문: "ORD",
  결제: "PAY",
  상품: "PRD",
  계약: "CTRT",
  청구: "BILL",
  상담: "CNSL",
  공통: "CMN",
};

const koreanEnglishWordMap: Array<[string, string]> = [
  ["상태코드", "Status Code"],
  ["시간대", "Time Slot"],
  ["선호", "Preferred"],
  ["배송", "Delivery"],
  ["고객", "Customer"],
  ["주문", "Order"],
  ["결제", "Payment"],
  ["상품", "Product"],
  ["계약", "Contract"],
  ["청구", "Billing"],
  ["상담", "Consultation"],
  ["번호", "Number"],
  ["일자", "Date"],
  ["일시", "Date Time"],
  ["금액", "Amount"],
  ["상태", "Status"],
  ["코드", "Code"],
  ["명", "Name"],
  ["여부", "Yn"],
  ["목록", "List"],
  ["이미지", "Image"],
  ["시간", "Time"],
];

const abbreviationWordMap: Array<[string, string]> = [
  ["상태코드", "STS_CD"],
  ["시간대", "TM_SLOT"],
  ["선호", "PREF"],
  ["배송", "DLV"],
  ["고객", "CUST"],
  ["주문", "ORD"],
  ["결제", "PAY"],
  ["상품", "PRD"],
  ["계약", "CTRT"],
  ["청구", "BILL"],
  ["상담", "CNSL"],
  ["번호", "NO"],
  ["일자", "DT"],
  ["일시", "DTTM"],
  ["금액", "AMT"],
  ["상태", "STS"],
  ["코드", "CD"],
  ["명", "NM"],
  ["여부", "YN"],
  ["목록", "LIST"],
  ["이미지", "IMG"],
  ["시간", "TM"],
];

function inferDomainName(koreanName: string, currentDomainName?: string): string {
  if (currentDomainName?.trim()) {
    return currentDomainName;
  }
  return Object.keys(domainEnglishMap).find((domain) => domain !== "공통" && koreanName.startsWith(domain)) ?? "공통";
}

function toEnglishPhrase(koreanText: string): string {
  let remaining = koreanText;
  const words: string[] = [];
  for (const [source, target] of [...koreanEnglishWordMap].sort((left, right) => right[0].length - left[0].length)) {
    while (remaining.includes(source)) {
      remaining = remaining.replace(source, " ");
      words.push(target);
    }
  }
  return words.join(" ").replace(/\s+/g, " ").trim() || "Term";
}

function toAbbreviationTokens(koreanText: string): string[] {
  let remaining = koreanText;
  const words: string[] = [];
  for (const [source, target] of [...abbreviationWordMap].sort((left, right) => right[0].length - left[0].length)) {
    while (remaining.includes(source)) {
      remaining = remaining.replace(source, " ");
      words.push(target);
    }
  }
  return Array.from(new Set(words));
}

function inferSuffixPattern(koreanName: string) {
  if (koreanName.endsWith("상태코드")) return { suffixToken: "STS_CD", physicalType: "VARCHAR", digits: 10, decimalPoint: 0 };
  if (koreanName.endsWith("코드")) return { suffixToken: "CD", physicalType: "VARCHAR", digits: 10, decimalPoint: 0 };
  if (koreanName.endsWith("번호")) return { suffixToken: "NO", physicalType: "VARCHAR", digits: 20, decimalPoint: 0 };
  if (koreanName.endsWith("일시")) return { suffixToken: "DTTM", physicalType: "TIMESTAMP", digits: 14, decimalPoint: 0 };
  if (koreanName.endsWith("일자")) return { suffixToken: "DT", physicalType: "DATE", digits: 8, decimalPoint: 0 };
  if (koreanName.endsWith("금액")) return { suffixToken: "AMT", physicalType: "NUMERIC", digits: 15, decimalPoint: 2 };
  if (koreanName.endsWith("명")) return { suffixToken: "NM", physicalType: "VARCHAR", digits: 100, decimalPoint: 0 };
  if (koreanName.endsWith("여부")) return { suffixToken: "YN", physicalType: "CHAR", digits: 1, decimalPoint: 0 };
  if (koreanName.endsWith("시간대")) return { suffixToken: "TM_SLOT", physicalType: "VARCHAR", digits: 40, decimalPoint: 0 };
  return { suffixToken: "VAL", physicalType: "VARCHAR", digits: 100, decimalPoint: 0 };
}

function isNetworkFallbackCandidate(error: unknown): boolean {
  return axios.isAxiosError(error) && !error.response;
}

export function extractApiError(error: unknown): ApiErrorInfo {
  if (axios.isAxiosError(error)) {
    const response = error.response?.data as ErrorResponse | undefined;
    return {
      status: error.response?.status,
      code: response?.code,
      message: response?.message ?? error.message ?? "요청 처리 중 오류가 발생했습니다.",
      detail: response?.detail,
      traceId: response?.traceId,
    };
  }
  if (error instanceof Error) {
    return { message: error.message };
  }
  return { message: "알 수 없는 오류가 발생했습니다." };
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

function toTermSummary(term: Term): TermSummary {
  const apiFieldName = term.expressions?.find((item) => item.expressionType === "API_FIELD")?.expressionValue;
  return {
    termId: term.termId,
    termNumber: term.termNumber,
    domainName: term.domainName,
    koreanName: term.koreanName,
    englishName: term.englishName,
    englishAbbreviation: term.englishAbbreviation,
    apiFieldName,
    status: term.status,
    relatedSystems: [term.domainName],
  };
}

function fallbackUpdateTerm(termId: string, request: TermUpdateRequest): Term {
  const current = sampleTermMap[termId] ?? { ...sampleTerm, termId };
  const apiFieldName = current.expressions?.find((item) => item.expressionType === "API_FIELD")?.expressionValue ?? toCamelCase(request.englishName);
  const updated: Term = {
    ...current,
    domainName: request.domainName,
    usageType: request.usageType,
    koreanName: request.koreanName,
    englishName: request.englishName,
    englishAbbreviation: request.englishAbbreviation,
    businessDefinition: request.businessDefinition,
    usageContext: request.usageContext,
    physicalType: request.physicalType,
    digits: request.digits,
    decimalPoint: request.decimalPoint,
    owner: request.owner,
    status: request.status ?? current.status,
    updatedAt: new Date().toISOString(),
    expressions: [
      { expressionId: Date.now(), termId, expressionType: "Korean", expressionValue: request.koreanName, language: "ko", style: "standard", isStandard: true },
      { expressionId: Date.now() + 1, termId, expressionType: "English", expressionValue: request.englishName, language: "en", style: "title", isStandard: true },
      { expressionId: Date.now() + 2, termId, expressionType: "DB_COLUMN", expressionValue: request.englishAbbreviation, language: "en", style: "UPPER_SNAKE", isStandard: true },
      { expressionId: Date.now() + 3, termId, expressionType: "API_FIELD", expressionValue: apiFieldName, language: "en", style: "camelCase", isStandard: true },
      { expressionId: Date.now() + 4, termId, expressionType: "CODE_VARIABLE", expressionValue: apiFieldName, language: "en", style: "camelCase", isStandard: true },
      { expressionId: Date.now() + 5, termId, expressionType: "UI_LABEL", expressionValue: request.koreanName, language: "ko", style: "label", isStandard: true },
      { expressionId: Date.now() + 6, termId, expressionType: "TEST_WORD", expressionValue: request.koreanName, language: "ko", style: "gherkin", isStandard: true },
    ],
  };
  sampleTermMap[termId] = updated;
  const index = sampleTerms.findIndex((term) => term.termId === termId);
  const summary = toTermSummary(updated);
  if (index >= 0) {
    sampleTerms[index] = summary;
  } else {
    sampleTerms.push(summary);
  }
  const currentHistories = sampleTermHistories[termId] ?? [];
  sampleTermHistories[termId] = [
    ...currentHistories,
    {
      changeHistoryId: currentHistories.length > 0 ? Math.max(...currentHistories.map((item) => item.changeHistoryId)) + 1 : 1,
      termId,
      changeType: "UPDATE",
      previousStatus: current.status,
      newStatus: updated.status,
      reason: request.changeReason,
      approvedBy: request.owner,
      createdAt: updated.updatedAt ?? new Date().toISOString(),
    },
  ];
  return updated;
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
  try {
    const response = await termApi.updateTerm(termId, request);
    return response.data;
  } catch (error) {
    if (!isNetworkFallbackCandidate(error)) {
      throw error;
    }
    return fallbackUpdateTerm(termId, request);
  }
}

export async function listExpressions(termId: string): Promise<TermExpression[]> {
  try {
    const response = await expressionApi.listTermExpressions(termId);
    return response.data;
  } catch {
    return sampleTermMap[termId]?.expressions ?? sampleTerm.expressions;
  }
}

export async function listTermHistory(termId: string): Promise<TermChangeHistoryListResponse> {
  try {
    const response = await governanceApi.listTermHistory(termId, 0, 20);
    return response.data;
  } catch {
    const items = sampleTermHistories[termId] ?? [];
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
    return localCandidates[candidateId] ?? { ...sampleCandidate, candidateId };
  }
}

export async function createCandidate(request: TermCandidateCreateRequest): Promise<TermCandidate> {
  try {
    const response = await candidateApi.createCandidate(request);
    return response.data;
  } catch {
    const candidate = { ...sampleCandidate, ...request, candidateId: "CAND-LOCAL", status: CandidateStatus.Draft };
    localCandidates[candidate.candidateId] = candidate;
    return candidate;
  }
}

export async function reviewCandidate(candidateId: string, request: TermCandidateReviewRequest): Promise<TermCandidate> {
  try {
    const response = await candidateApi.reviewCandidate(candidateId, request);
    return response.data;
  } catch (error) {
    if (!isNetworkFallbackCandidate(error)) {
      throw error;
    }
    return fallbackReviewCandidate(candidateId, request);
  }
}

export async function promoteCandidate(candidateId: string, request: TermCandidatePromoteRequest): Promise<CandidatePromotionResult> {
  try {
    const response = await candidateApi.promoteCandidate(candidateId, request);
    return response.data;
  } catch (error) {
    if (!isNetworkFallbackCandidate(error)) {
      throw error;
    }
    return fallbackPromoteCandidate(candidateId, request);
  }
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

export async function recommendTermDraft(request: TermRecommendationRequest): Promise<TermRecommendationResponse> {
  try {
    // LLM(특히 로컬 Ollama) 추론은 수십 초~수 분 걸릴 수 있어 기본 3s timeout 을 넘긴다. 이 호출만 길게.
    const response = await aiApi.recommendTermDraft(request, { timeout: 300000 });
    return response.data;
  } catch (error) {
    if (!isNetworkFallbackCandidate(error)) {
      throw error;
    }
    return fallbackRecommendTermDraft(request);
  }
}

export { CandidateStatus, ImpactChangeType, ImpactRiskLevel, TermRecommendationMode, TermStatus };
export type {
  CandidatePromotionResult,
  GraphRecommendationContext,
  ImpactAnalysisResponse,
  RecommendationEvidence,
  RecommendedTermDraft,
  Term,
  TermAlias,
  TermCandidate,
  TermChangeHistory,
  TermCandidateCreateRequest,
  TermChangeHistoryListResponse,
  TermCandidateListResponse,
  TermCandidatePromoteRequest,
  TermCandidateReviewRequest,
  TermCandidateSummary,
  TermCreateRequest,
  TermExpression,
  TermRecommendationRequest,
  TermRecommendationResponse,
  TermSummary,
  TermUpdateRequest,
};
