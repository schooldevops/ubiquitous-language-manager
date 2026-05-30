# ReviewApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**reviewCodeArtifact**](#reviewcodeartifact) | **POST** /reviews/code | 코드 산출물 검증|
|[**reviewDdlArtifact**](#reviewddlartifact) | **POST** /reviews/ddl | DDL 산출물 검증|
|[**reviewDocument**](#reviewdocument) | **POST** /reviews/document | 기획서 용어 검토|
|[**reviewOpenApiArtifact**](#reviewopenapiartifact) | **POST** /reviews/openapi | OpenAPI 산출물 검증|
|[**reviewPullRequestArtifacts**](#reviewpullrequestartifacts) | **POST** /reviews/pr | PR 변경 파일 검증|
|[**validateArtifact**](#validateartifact) | **POST** /artifact-validations | 개발 산출물 표준 용어 검증|

# **reviewCodeArtifact**
> ArtifactValidationResult reviewCodeArtifact(artifactValidationRequest)

Kotlin, Java, TypeScript 코드의 DTO, Entity, Request/Response 필드명을 데이터 사전 CODE_VARIABLE 표현 기준으로 검증한다.

### Example

```typescript
import {
    ReviewApi,
    Configuration,
    ArtifactValidationRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ReviewApi(configuration);

let artifactValidationRequest: ArtifactValidationRequest; //

const { status, data } = await apiInstance.reviewCodeArtifact(
    artifactValidationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **artifactValidationRequest** | **ArtifactValidationRequest**|  | |


### Return type

**ArtifactValidationResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 코드 산출물 검증 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **reviewDdlArtifact**
> ArtifactValidationResult reviewDdlArtifact(artifactValidationRequest)

DB DDL 컬럼명과 물리 스펙을 데이터 사전 DB_COLUMN 표현 기준으로 검증한다.

### Example

```typescript
import {
    ReviewApi,
    Configuration,
    ArtifactValidationRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ReviewApi(configuration);

let artifactValidationRequest: ArtifactValidationRequest; //

const { status, data } = await apiInstance.reviewDdlArtifact(
    artifactValidationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **artifactValidationRequest** | **ArtifactValidationRequest**|  | |


### Return type

**ArtifactValidationResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | DDL 산출물 검증 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **reviewDocument**
> DocumentReviewResult reviewDocument(documentReviewRequest)

기획서 본문에서 비표준 용어를 탐지하고 표준 용어, 산출물 표현, 신규 용어 후보를 반환한다.

### Example

```typescript
import {
    ReviewApi,
    Configuration,
    DocumentReviewRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ReviewApi(configuration);

let documentReviewRequest: DocumentReviewRequest; //

const { status, data } = await apiInstance.reviewDocument(
    documentReviewRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **documentReviewRequest** | **DocumentReviewRequest**|  | |


### Return type

**DocumentReviewResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 기획서 용어 검토 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **reviewOpenApiArtifact**
> ArtifactValidationResult reviewOpenApiArtifact(artifactValidationRequest)

OpenAPI YAML의 API 필드명을 데이터 사전 API_FIELD 표현 기준으로 검증한다.

### Example

```typescript
import {
    ReviewApi,
    Configuration,
    ArtifactValidationRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ReviewApi(configuration);

let artifactValidationRequest: ArtifactValidationRequest; //

const { status, data } = await apiInstance.reviewOpenApiArtifact(
    artifactValidationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **artifactValidationRequest** | **ArtifactValidationRequest**|  | |


### Return type

**ArtifactValidationResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OpenAPI 산출물 검증 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **reviewPullRequestArtifacts**
> PullRequestArtifactReviewResult reviewPullRequestArtifacts(pullRequestArtifactReviewRequest)

PR 또는 CI에서 전달한 여러 변경 파일을 표준 용어 기준으로 검증하고 파일별 결과와 전체 집계를 반환한다.

### Example

```typescript
import {
    ReviewApi,
    Configuration,
    PullRequestArtifactReviewRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ReviewApi(configuration);

let pullRequestArtifactReviewRequest: PullRequestArtifactReviewRequest; //

const { status, data } = await apiInstance.reviewPullRequestArtifacts(
    pullRequestArtifactReviewRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **pullRequestArtifactReviewRequest** | **PullRequestArtifactReviewRequest**|  | |


### Return type

**PullRequestArtifactReviewResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | PR 변경 파일 검증 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **validateArtifact**
> ArtifactValidationResult validateArtifact(artifactValidationRequest)

OpenAPI YAML과 DB DDL에서 필드명, 컬럼명, 물리 스펙을 추출해 데이터 사전 기준으로 검증한다.

### Example

```typescript
import {
    ReviewApi,
    Configuration,
    ArtifactValidationRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ReviewApi(configuration);

let artifactValidationRequest: ArtifactValidationRequest; //

const { status, data } = await apiInstance.validateArtifact(
    artifactValidationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **artifactValidationRequest** | **ArtifactValidationRequest**|  | |


### Return type

**ArtifactValidationResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 개발 산출물 검증 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

