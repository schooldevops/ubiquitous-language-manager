# ReviewApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**reviewDocument**](#reviewdocument) | **POST** /reviews/document | 기획서 용어 검토|

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

