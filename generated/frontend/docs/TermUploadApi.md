# TermUploadApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getTermUploadBatch**](#gettermuploadbatch) | **GET** /terms/uploads/{uploadBatchId} | 업로드 배치 결과 단건 조회|
|[**listTermUploadBatches**](#listtermuploadbatches) | **GET** /terms/uploads | 업로드 배치 목록(최근순)|
|[**uploadTerms**](#uploadterms) | **POST** /terms/uploads | 용어 JSONL 파일 업로드(upsert)|

# **getTermUploadBatch**
> TermUploadResult getTermUploadBatch()


### Example

```typescript
import {
    TermUploadApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new TermUploadApi(configuration);

let uploadBatchId: string; // (default to undefined)

const { status, data } = await apiInstance.getTermUploadBatch(
    uploadBatchId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uploadBatchId** | [**string**] |  | defaults to undefined|


### Return type

**TermUploadResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 배치 결과 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listTermUploadBatches**
> TermUploadBatchListResponse listTermUploadBatches()


### Example

```typescript
import {
    TermUploadApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new TermUploadApi(configuration);

const { status, data } = await apiInstance.listTermUploadBatches();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**TermUploadBatchListResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 배치 목록 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **uploadTerms**
> TermUploadResult uploadTerms()


### Example

```typescript
import {
    TermUploadApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new TermUploadApi(configuration);

let file: File; //JSONL 파일(.jsonl) (default to undefined)

const { status, data } = await apiInstance.uploadTerms(
    file
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **file** | [**File**] | JSONL 파일(.jsonl) | defaults to undefined|


### Return type

**TermUploadResult**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 업로드 처리 결과 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

