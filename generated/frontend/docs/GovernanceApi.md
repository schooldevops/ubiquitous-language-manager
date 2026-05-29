# GovernanceApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**approveTerm**](#approveterm) | **POST** /terms/{termId}/approve | 표준 용어 승인|
|[**deprecateTerm**](#deprecateterm) | **POST** /terms/{termId}/deprecate | 표준 용어 폐기|
|[**listTermHistory**](#listtermhistory) | **GET** /terms/{termId}/history | 용어 변경 이력 조회|

# **approveTerm**
> Term approveTerm(termApprovalRequest)


### Example

```typescript
import {
    GovernanceApi,
    Configuration,
    TermApprovalRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new GovernanceApi(configuration);

let termId: string; //용어 식별자 (default to undefined)
let termApprovalRequest: TermApprovalRequest; //

const { status, data } = await apiInstance.approveTerm(
    termId,
    termApprovalRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termApprovalRequest** | **TermApprovalRequest**|  | |
| **termId** | [**string**] | 용어 식별자 | defaults to undefined|


### Return type

**Term**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 표준 용어 승인 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |
|**409** | 중복 또는 상태 충돌 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deprecateTerm**
> Term deprecateTerm(termDeprecationRequest)


### Example

```typescript
import {
    GovernanceApi,
    Configuration,
    TermDeprecationRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new GovernanceApi(configuration);

let termId: string; //용어 식별자 (default to undefined)
let termDeprecationRequest: TermDeprecationRequest; //

const { status, data } = await apiInstance.deprecateTerm(
    termId,
    termDeprecationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termDeprecationRequest** | **TermDeprecationRequest**|  | |
| **termId** | [**string**] | 용어 식별자 | defaults to undefined|


### Return type

**Term**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 표준 용어 폐기 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |
|**409** | 중복 또는 상태 충돌 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listTermHistory**
> TermChangeHistoryListResponse listTermHistory()


### Example

```typescript
import {
    GovernanceApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new GovernanceApi(configuration);

let termId: string; //용어 식별자 (default to undefined)
let page: number; // (optional) (default to 0)
let size: number; // (optional) (default to 20)

const { status, data } = await apiInstance.listTermHistory(
    termId,
    page,
    size
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termId** | [**string**] | 용어 식별자 | defaults to undefined|
| **page** | [**number**] |  | (optional) defaults to 0|
| **size** | [**number**] |  | (optional) defaults to 20|


### Return type

**TermChangeHistoryListResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 용어 변경 이력 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

