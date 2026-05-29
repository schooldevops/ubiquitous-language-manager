# CandidateApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createCandidate**](#createcandidate) | **POST** /candidates | 신규 용어 후보 등록|
|[**getCandidate**](#getcandidate) | **GET** /candidates/{candidateId} | 신규 용어 후보 상세 조회|
|[**listCandidates**](#listcandidates) | **GET** /candidates | 신규 용어 후보 목록 조회|
|[**promoteCandidate**](#promotecandidate) | **POST** /candidates/{candidateId}/promote | 신규 용어 후보 표준 용어 승격|
|[**reviewCandidate**](#reviewcandidate) | **POST** /candidates/{candidateId}/review | 신규 용어 후보 검토|

# **createCandidate**
> TermCandidate createCandidate(termCandidateCreateRequest)


### Example

```typescript
import {
    CandidateApi,
    Configuration,
    TermCandidateCreateRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new CandidateApi(configuration);

let termCandidateCreateRequest: TermCandidateCreateRequest; //

const { status, data } = await apiInstance.createCandidate(
    termCandidateCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termCandidateCreateRequest** | **TermCandidateCreateRequest**|  | |


### Return type

**TermCandidate**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 후보 등록 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**409** | 중복 또는 상태 충돌 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getCandidate**
> TermCandidate getCandidate()


### Example

```typescript
import {
    CandidateApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new CandidateApi(configuration);

let candidateId: string; //후보 식별자 (default to undefined)

const { status, data } = await apiInstance.getCandidate(
    candidateId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **candidateId** | [**string**] | 후보 식별자 | defaults to undefined|


### Return type

**TermCandidate**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 후보 상세 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listCandidates**
> TermCandidateListResponse listCandidates()


### Example

```typescript
import {
    CandidateApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new CandidateApi(configuration);

let q: string; //후보 한글명, 영문명, 약어 검색어 (optional) (default to undefined)
let status: CandidateStatus; //후보 상태 필터 (optional) (default to undefined)
let domainName: string; //도메인명 필터 (optional) (default to undefined)
let page: number; // (optional) (default to 0)
let size: number; // (optional) (default to 20)

const { status, data } = await apiInstance.listCandidates(
    q,
    status,
    domainName,
    page,
    size
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **q** | [**string**] | 후보 한글명, 영문명, 약어 검색어 | (optional) defaults to undefined|
| **status** | **CandidateStatus** | 후보 상태 필터 | (optional) defaults to undefined|
| **domainName** | [**string**] | 도메인명 필터 | (optional) defaults to undefined|
| **page** | [**number**] |  | (optional) defaults to 0|
| **size** | [**number**] |  | (optional) defaults to 20|


### Return type

**TermCandidateListResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 후보 목록 조회 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **promoteCandidate**
> CandidatePromotionResult promoteCandidate(termCandidatePromoteRequest)


### Example

```typescript
import {
    CandidateApi,
    Configuration,
    TermCandidatePromoteRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new CandidateApi(configuration);

let candidateId: string; //후보 식별자 (default to undefined)
let termCandidatePromoteRequest: TermCandidatePromoteRequest; //

const { status, data } = await apiInstance.promoteCandidate(
    candidateId,
    termCandidatePromoteRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termCandidatePromoteRequest** | **TermCandidatePromoteRequest**|  | |
| **candidateId** | [**string**] | 후보 식별자 | defaults to undefined|


### Return type

**CandidatePromotionResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 후보 승격 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |
|**409** | 중복 또는 상태 충돌 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **reviewCandidate**
> TermCandidate reviewCandidate(termCandidateReviewRequest)


### Example

```typescript
import {
    CandidateApi,
    Configuration,
    TermCandidateReviewRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new CandidateApi(configuration);

let candidateId: string; //후보 식별자 (default to undefined)
let termCandidateReviewRequest: TermCandidateReviewRequest; //

const { status, data } = await apiInstance.reviewCandidate(
    candidateId,
    termCandidateReviewRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termCandidateReviewRequest** | **TermCandidateReviewRequest**|  | |
| **candidateId** | [**string**] | 후보 식별자 | defaults to undefined|


### Return type

**TermCandidate**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 후보 검토 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

