# ImpactApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getTermImpact**](#gettermimpact) | **GET** /impact/terms/{termId} | 용어 변경 영향도 분석|

# **getTermImpact**
> ImpactAnalysisResponse getTermImpact()

Graphify 그래프를 이용해 특정 표준 용어 변경 시 영향받는 시스템, DB, API, DTO, 문서, 테스트를 조회한다.

### Example

```typescript
import {
    ImpactApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new ImpactApi(configuration);

let termId: string; //영향도 분석 기준 용어 ID (default to undefined)
let changeType: ImpactChangeType; //변경 유형 (optional) (default to undefined)
let includeTwoHop: boolean; //2-hop 관계까지 확장 조회할지 여부 (optional) (default to false)

const { status, data } = await apiInstance.getTermImpact(
    termId,
    changeType,
    includeTwoHop
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termId** | [**string**] | 영향도 분석 기준 용어 ID | defaults to undefined|
| **changeType** | **ImpactChangeType** | 변경 유형 | (optional) defaults to undefined|
| **includeTwoHop** | [**boolean**] | 2-hop 관계까지 확장 조회할지 여부 | (optional) defaults to false|


### Return type

**ImpactAnalysisResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 영향도 분석 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

