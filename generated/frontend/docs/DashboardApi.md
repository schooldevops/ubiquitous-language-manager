# DashboardApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getDashboard**](#getdashboard) | **GET** /dashboard | 메인 대시보드 요약 조회|

# **getDashboard**
> DashboardSummary getDashboard()

최근 등록/수정된 용어 목록과 도메인별 용어 통계를 반환한다.

### Example

```typescript
import {
    DashboardApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new DashboardApi(configuration);

let recentLimit: number; //최근 용어 목록 최대 개수 (optional) (default to 10)

const { status, data } = await apiInstance.getDashboard(
    recentLimit
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **recentLimit** | [**number**] | 최근 용어 목록 최대 개수 | (optional) defaults to 10|


### Return type

**DashboardSummary**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 대시보드 요약 |  -  |
|**401** | 인증 필요 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

