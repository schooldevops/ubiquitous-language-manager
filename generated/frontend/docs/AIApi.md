# AIApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**developmentAssist**](#developmentassist) | **POST** /ai/development-assist | AI 산출물 생성 지원|

# **developmentAssist**
> DevelopmentAssistResponse developmentAssist(developmentAssistRequest)

요구사항 텍스트에서 업무 개념을 추출하고 데이터 사전 기반 표준 용어 매핑, 신규 후보, 표준 위반 경고, 산출물 예시를 생성한다.

### Example

```typescript
import {
    AIApi,
    Configuration,
    DevelopmentAssistRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new AIApi(configuration);

let developmentAssistRequest: DevelopmentAssistRequest; //

const { status, data } = await apiInstance.developmentAssist(
    developmentAssistRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **developmentAssistRequest** | **DevelopmentAssistRequest**|  | |


### Return type

**DevelopmentAssistResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | AI 산출물 생성 지원 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

