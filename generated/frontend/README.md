## @aulms/api-client@0.1.0

This generator creates TypeScript/JavaScript client that utilizes [axios](https://github.com/axios/axios). The generated Node module can be used in the following environments:

Environment
* Node.js
* Webpack
* Browserify

Language level
* ES5 - you must have a Promises/A+ library installed
* ES6

Module system
* CommonJS
* ES6 module system

It can be used in both TypeScript and JavaScript. In TypeScript, the definition will be automatically resolved via `package.json`. ([Reference](https://www.typescriptlang.org/docs/handbook/declaration-files/consumption.html))

### Building

To build and compile the typescript sources to javascript use:
```
npm install
npm run build
```

### Publishing

First build the package then run `npm publish`

### Consuming

navigate to the folder of your consuming project and run one of the following commands.

_published:_

```
npm install @aulms/api-client@0.1.0 --save
```

_unPublished (not recommended):_

```
npm install PATH_TO_GENERATED_PACKAGE --save
```

### Documentation for API Endpoints

All URIs are relative to *http://localhost:8080*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AIApi* | [**developmentAssist**](docs/AIApi.md#developmentassist) | **POST** /ai/development-assist | AI 산출물 생성 지원
*AIApi* | [**recommendTermDraft**](docs/AIApi.md#recommendtermdraft) | **POST** /ai/term-recommendation | RAG/Graph/LLM 기반 용어 초안 추천
*AliasApi* | [**createTermAlias**](docs/AliasApi.md#createtermalias) | **POST** /terms/{termId}/aliases | 용어 별칭 등록
*AliasApi* | [**listTermAliases**](docs/AliasApi.md#listtermaliases) | **GET** /terms/{termId}/aliases | 용어 별칭 목록 조회
*CandidateApi* | [**createCandidate**](docs/CandidateApi.md#createcandidate) | **POST** /candidates | 신규 용어 후보 등록
*CandidateApi* | [**getCandidate**](docs/CandidateApi.md#getcandidate) | **GET** /candidates/{candidateId} | 신규 용어 후보 상세 조회
*CandidateApi* | [**listCandidates**](docs/CandidateApi.md#listcandidates) | **GET** /candidates | 신규 용어 후보 목록 조회
*CandidateApi* | [**promoteCandidate**](docs/CandidateApi.md#promotecandidate) | **POST** /candidates/{candidateId}/promote | 신규 용어 후보 표준 용어 승격
*CandidateApi* | [**reviewCandidate**](docs/CandidateApi.md#reviewcandidate) | **POST** /candidates/{candidateId}/review | 신규 용어 후보 검토
*ExpressionApi* | [**createTermExpression**](docs/ExpressionApi.md#createtermexpression) | **POST** /terms/{termId}/expressions | 용어 표현 매핑 등록
*ExpressionApi* | [**listTermExpressions**](docs/ExpressionApi.md#listtermexpressions) | **GET** /terms/{termId}/expressions | 용어 표현 매핑 목록 조회
*GovernanceApi* | [**approveTerm**](docs/GovernanceApi.md#approveterm) | **POST** /terms/{termId}/approve | 표준 용어 승인
*GovernanceApi* | [**deprecateTerm**](docs/GovernanceApi.md#deprecateterm) | **POST** /terms/{termId}/deprecate | 표준 용어 폐기
*GovernanceApi* | [**listTermHistory**](docs/GovernanceApi.md#listtermhistory) | **GET** /terms/{termId}/history | 용어 변경 이력 조회
*ImpactApi* | [**getTermImpact**](docs/ImpactApi.md#gettermimpact) | **GET** /impact/terms/{termId} | 용어 변경 영향도 분석
*PromptTemplateApi* | [**getPromptTemplate**](docs/PromptTemplateApi.md#getprompttemplate) | **GET** /prompt-templates/{templateId} | 프롬프트 템플릿 상세 조회
*PromptTemplateApi* | [**listPromptTemplateVersions**](docs/PromptTemplateApi.md#listprompttemplateversions) | **GET** /prompt-templates/{templateId}/versions | 프롬프트 템플릿 버전 목록 조회
*PromptTemplateApi* | [**listPromptTemplates**](docs/PromptTemplateApi.md#listprompttemplates) | **GET** /prompt-templates | 프롬프트 템플릿 목록 조회
*PromptTemplateApi* | [**previewPromptTemplate**](docs/PromptTemplateApi.md#previewprompttemplate) | **POST** /prompt-templates/{templateId}/preview | 프롬프트 템플릿 미리보기
*RelationshipApi* | [**getColumnSystems**](docs/RelationshipApi.md#getcolumnsystems) | **GET** /relationships/columns/{columnName}/systems | 컬럼 사용 시스템 조회
*RelationshipApi* | [**getDeprecatedUsages**](docs/RelationshipApi.md#getdeprecatedusages) | **GET** /relationships/deprecated | 폐기어와 금지어 사용 위치 조회
*RelationshipApi* | [**getDomainTerms**](docs/RelationshipApi.md#getdomainterms) | **GET** /relationships/domains/{domainName}/terms | 도메인별 표준 용어 관계 검색
*RelationshipApi* | [**getTermRelationships**](docs/RelationshipApi.md#gettermrelationships) | **GET** /relationships/terms/{termId} | 용어 관계 검색
*ReviewApi* | [**reviewCodeArtifact**](docs/ReviewApi.md#reviewcodeartifact) | **POST** /reviews/code | 코드 산출물 검증
*ReviewApi* | [**reviewDdlArtifact**](docs/ReviewApi.md#reviewddlartifact) | **POST** /reviews/ddl | DDL 산출물 검증
*ReviewApi* | [**reviewDocument**](docs/ReviewApi.md#reviewdocument) | **POST** /reviews/document | 기획서 용어 검토
*ReviewApi* | [**reviewOpenApiArtifact**](docs/ReviewApi.md#reviewopenapiartifact) | **POST** /reviews/openapi | OpenAPI 산출물 검증
*ReviewApi* | [**reviewPullRequestArtifacts**](docs/ReviewApi.md#reviewpullrequestartifacts) | **POST** /reviews/pr | PR 변경 파일 검증
*ReviewApi* | [**validateArtifact**](docs/ReviewApi.md#validateartifact) | **POST** /artifact-validations | 개발 산출물 표준 용어 검증
*SearchApi* | [**aliasSearch**](docs/SearchApi.md#aliassearch) | **GET** /search/alias | 유사어 검색
*SearchApi* | [**deprecatedSearch**](docs/SearchApi.md#deprecatedsearch) | **GET** /search/deprecated | 폐기어 검색
*SearchApi* | [**domainSearch**](docs/SearchApi.md#domainsearch) | **GET** /search/domain/{domainName} | 도메인별 검색
*SearchApi* | [**exactSearch**](docs/SearchApi.md#exactsearch) | **GET** /search/exact | 정확 검색
*SearchApi* | [**semanticSearch**](docs/SearchApi.md#semanticsearch) | **POST** /search/semantic | 의미 기반 검색
*TermApi* | [**createTerm**](docs/TermApi.md#createterm) | **POST** /terms | 표준 용어 등록
*TermApi* | [**getTerm**](docs/TermApi.md#getterm) | **GET** /terms/{termId} | 표준 용어 상세 조회
*TermApi* | [**listTerms**](docs/TermApi.md#listterms) | **GET** /terms | 표준 용어 목록 조회
*TermApi* | [**updateTerm**](docs/TermApi.md#updateterm) | **PUT** /terms/{termId} | 표준 용어 수정


### Documentation For Models

 - [AliasType](docs/AliasType.md)
 - [ArtifactSourceType](docs/ArtifactSourceType.md)
 - [ArtifactValidationRequest](docs/ArtifactValidationRequest.md)
 - [ArtifactValidationResult](docs/ArtifactValidationResult.md)
 - [ArtifactValidationSummary](docs/ArtifactValidationSummary.md)
 - [AssistCandidateTerm](docs/AssistCandidateTerm.md)
 - [AssistTargetArtifact](docs/AssistTargetArtifact.md)
 - [AssistTermMapping](docs/AssistTermMapping.md)
 - [CandidateHistory](docs/CandidateHistory.md)
 - [CandidatePromotionResult](docs/CandidatePromotionResult.md)
 - [CandidateStatus](docs/CandidateStatus.md)
 - [CandidateTerm](docs/CandidateTerm.md)
 - [ColumnSystemUsage](docs/ColumnSystemUsage.md)
 - [ColumnSystemUsageResponse](docs/ColumnSystemUsageResponse.md)
 - [DeprecatedSearchResponse](docs/DeprecatedSearchResponse.md)
 - [DeprecatedSearchResult](docs/DeprecatedSearchResult.md)
 - [DeprecatedUsage](docs/DeprecatedUsage.md)
 - [DeprecatedUsageResponse](docs/DeprecatedUsageResponse.md)
 - [DetectedTerm](docs/DetectedTerm.md)
 - [DevelopmentAssistRequest](docs/DevelopmentAssistRequest.md)
 - [DevelopmentAssistResponse](docs/DevelopmentAssistResponse.md)
 - [DocumentReviewOptions](docs/DocumentReviewOptions.md)
 - [DocumentReviewRequest](docs/DocumentReviewRequest.md)
 - [DocumentReviewResult](docs/DocumentReviewResult.md)
 - [ErrorResponse](docs/ErrorResponse.md)
 - [ExpressionType](docs/ExpressionType.md)
 - [ExtractedBusinessConcept](docs/ExtractedBusinessConcept.md)
 - [GeneratedArtifact](docs/GeneratedArtifact.md)
 - [GraphPathEdge](docs/GraphPathEdge.md)
 - [GraphPathNode](docs/GraphPathNode.md)
 - [GraphRecommendationContext](docs/GraphRecommendationContext.md)
 - [ImpactAnalysisResponse](docs/ImpactAnalysisResponse.md)
 - [ImpactChangeType](docs/ImpactChangeType.md)
 - [ImpactRecommendation](docs/ImpactRecommendation.md)
 - [ImpactRiskLevel](docs/ImpactRiskLevel.md)
 - [ImpactTarget](docs/ImpactTarget.md)
 - [MatchedExpression](docs/MatchedExpression.md)
 - [PageMetadata](docs/PageMetadata.md)
 - [PromptCandidateTerm](docs/PromptCandidateTerm.md)
 - [PromptTemplate](docs/PromptTemplate.md)
 - [PromptTemplateHistory](docs/PromptTemplateHistory.md)
 - [PromptTemplateListResponse](docs/PromptTemplateListResponse.md)
 - [PromptTemplatePreviewRequest](docs/PromptTemplatePreviewRequest.md)
 - [PromptTemplatePreviewResponse](docs/PromptTemplatePreviewResponse.md)
 - [PromptTemplateStatus](docs/PromptTemplateStatus.md)
 - [PromptTemplateSummary](docs/PromptTemplateSummary.md)
 - [PromptTemplateType](docs/PromptTemplateType.md)
 - [PromptTemplateVariable](docs/PromptTemplateVariable.md)
 - [PromptTemplateVersion](docs/PromptTemplateVersion.md)
 - [PromptTemplateVersionListResponse](docs/PromptTemplateVersionListResponse.md)
 - [PromptTermMapping](docs/PromptTermMapping.md)
 - [PullRequestArtifactFile](docs/PullRequestArtifactFile.md)
 - [PullRequestArtifactReviewRequest](docs/PullRequestArtifactReviewRequest.md)
 - [PullRequestArtifactReviewResult](docs/PullRequestArtifactReviewResult.md)
 - [Recommendation](docs/Recommendation.md)
 - [RecommendationEvidence](docs/RecommendationEvidence.md)
 - [RecommendedTermDraft](docs/RecommendedTermDraft.md)
 - [RelationshipPath](docs/RelationshipPath.md)
 - [RelationshipSearchResponse](docs/RelationshipSearchResponse.md)
 - [RelationshipSearchResult](docs/RelationshipSearchResult.md)
 - [RelationshipType](docs/RelationshipType.md)
 - [SearchResponse](docs/SearchResponse.md)
 - [SearchResult](docs/SearchResult.md)
 - [SemanticSearchRequest](docs/SemanticSearchRequest.md)
 - [SemanticSearchResponse](docs/SemanticSearchResponse.md)
 - [SemanticSearchResult](docs/SemanticSearchResult.md)
 - [SimilarTerm](docs/SimilarTerm.md)
 - [StandardMapping](docs/StandardMapping.md)
 - [StandardViolationWarning](docs/StandardViolationWarning.md)
 - [Term](docs/Term.md)
 - [TermAlias](docs/TermAlias.md)
 - [TermAliasCreateRequest](docs/TermAliasCreateRequest.md)
 - [TermApprovalRequest](docs/TermApprovalRequest.md)
 - [TermCandidate](docs/TermCandidate.md)
 - [TermCandidateCreateRequest](docs/TermCandidateCreateRequest.md)
 - [TermCandidateListResponse](docs/TermCandidateListResponse.md)
 - [TermCandidatePromoteRequest](docs/TermCandidatePromoteRequest.md)
 - [TermCandidateReviewRequest](docs/TermCandidateReviewRequest.md)
 - [TermCandidateSummary](docs/TermCandidateSummary.md)
 - [TermChangeHistory](docs/TermChangeHistory.md)
 - [TermChangeHistoryListResponse](docs/TermChangeHistoryListResponse.md)
 - [TermCreateRequest](docs/TermCreateRequest.md)
 - [TermDeprecationRequest](docs/TermDeprecationRequest.md)
 - [TermExpression](docs/TermExpression.md)
 - [TermExpressionCreateRequest](docs/TermExpressionCreateRequest.md)
 - [TermListResponse](docs/TermListResponse.md)
 - [TermRecommendationMode](docs/TermRecommendationMode.md)
 - [TermRecommendationRequest](docs/TermRecommendationRequest.md)
 - [TermRecommendationResponse](docs/TermRecommendationResponse.md)
 - [TermStatus](docs/TermStatus.md)
 - [TermSummary](docs/TermSummary.md)
 - [TermUpdateRequest](docs/TermUpdateRequest.md)
 - [UserRole](docs/UserRole.md)
 - [ValidationIssue](docs/ValidationIssue.md)
 - [ValidationSeverity](docs/ValidationSeverity.md)


<a id="documentation-for-authorization"></a>
## Documentation For Authorization


Authentication schemes defined for the API:
<a id="bearerAuth"></a>
### bearerAuth

- **Type**: Bearer authentication (JWT)

