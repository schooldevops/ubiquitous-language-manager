# ExpressionApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createTermExpression**](#createtermexpression) | **POST** /terms/{termId}/expressions | 용어 표현 매핑 등록|
|[**listTermExpressions**](#listtermexpressions) | **GET** /terms/{termId}/expressions | 용어 표현 매핑 목록 조회|

# **createTermExpression**
> TermExpression createTermExpression(termExpressionCreateRequest)


### Example

```typescript
import {
    ExpressionApi,
    Configuration,
    TermExpressionCreateRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ExpressionApi(configuration);

let termId: string; //용어 식별자 (default to undefined)
let termExpressionCreateRequest: TermExpressionCreateRequest; //

const { status, data } = await apiInstance.createTermExpression(
    termId,
    termExpressionCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termExpressionCreateRequest** | **TermExpressionCreateRequest**|  | |
| **termId** | [**string**] | 용어 식별자 | defaults to undefined|


### Return type

**TermExpression**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 표현 매핑 등록 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |
|**409** | 중복 또는 상태 충돌 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listTermExpressions**
> Array<TermExpression> listTermExpressions()


### Example

```typescript
import {
    ExpressionApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ExpressionApi(configuration);

let termId: string; //용어 식별자 (default to undefined)

const { status, data } = await apiInstance.listTermExpressions(
    termId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termId** | [**string**] | 용어 식별자 | defaults to undefined|


### Return type

**Array<TermExpression>**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 표현 매핑 목록 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

