# AliasApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createTermAlias**](#createtermalias) | **POST** /terms/{termId}/aliases | 용어 별칭 등록|
|[**listTermAliases**](#listtermaliases) | **GET** /terms/{termId}/aliases | 용어 별칭 목록 조회|

# **createTermAlias**
> TermAlias createTermAlias(termAliasCreateRequest)


### Example

```typescript
import {
    AliasApi,
    Configuration,
    TermAliasCreateRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new AliasApi(configuration);

let termId: string; //용어 식별자 (default to undefined)
let termAliasCreateRequest: TermAliasCreateRequest; //

const { status, data } = await apiInstance.createTermAlias(
    termId,
    termAliasCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termAliasCreateRequest** | **TermAliasCreateRequest**|  | |
| **termId** | [**string**] | 용어 식별자 | defaults to undefined|


### Return type

**TermAlias**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 별칭 등록 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |
|**409** | 중복 또는 상태 충돌 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listTermAliases**
> Array<TermAlias> listTermAliases()


### Example

```typescript
import {
    AliasApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new AliasApi(configuration);

let termId: string; //용어 식별자 (default to undefined)

const { status, data } = await apiInstance.listTermAliases(
    termId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termId** | [**string**] | 용어 식별자 | defaults to undefined|


### Return type

**Array<TermAlias>**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 별칭 목록 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

