# PromptTemplateApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getPromptTemplate**](#getprompttemplate) | **GET** /prompt-templates/{templateId} | 프롬프트 템플릿 상세 조회|
|[**listPromptTemplateVersions**](#listprompttemplateversions) | **GET** /prompt-templates/{templateId}/versions | 프롬프트 템플릿 버전 목록 조회|
|[**listPromptTemplates**](#listprompttemplates) | **GET** /prompt-templates | 프롬프트 템플릿 목록 조회|
|[**previewPromptTemplate**](#previewprompttemplate) | **POST** /prompt-templates/{templateId}/preview | 프롬프트 템플릿 미리보기|

# **getPromptTemplate**
> PromptTemplate getPromptTemplate()

템플릿 본문, 변수, 버전 정책, 변경 이력을 조회한다.

### Example

```typescript
import {
    PromptTemplateApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new PromptTemplateApi(configuration);

let templateId: string; //프롬프트 템플릿 식별자 (default to undefined)

const { status, data } = await apiInstance.getPromptTemplate(
    templateId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **templateId** | [**string**] | 프롬프트 템플릿 식별자 | defaults to undefined|


### Return type

**PromptTemplate**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 프롬프트 템플릿 상세 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listPromptTemplateVersions**
> PromptTemplateVersionListResponse listPromptTemplateVersions()

템플릿의 버전별 변경 사유와 활성 여부를 조회한다.

### Example

```typescript
import {
    PromptTemplateApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new PromptTemplateApi(configuration);

let templateId: string; //프롬프트 템플릿 식별자 (default to undefined)

const { status, data } = await apiInstance.listPromptTemplateVersions(
    templateId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **templateId** | [**string**] | 프롬프트 템플릿 식별자 | defaults to undefined|


### Return type

**PromptTemplateVersionListResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 프롬프트 템플릿 버전 목록 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listPromptTemplates**
> PromptTemplateListResponse listPromptTemplates()

바이브코딩과 기획서 작성에 사용할 데이터 사전 기반 AI 프롬프트 템플릿 목록을 조회한다.

### Example

```typescript
import {
    PromptTemplateApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new PromptTemplateApi(configuration);

let type: PromptTemplateType; //템플릿 유형 필터 (optional) (default to undefined)
let status: PromptTemplateStatus; //템플릿 상태 필터 (optional) (default to undefined)

const { status, data } = await apiInstance.listPromptTemplates(
    type,
    status
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **type** | **PromptTemplateType** | 템플릿 유형 필터 | (optional) defaults to undefined|
| **status** | **PromptTemplateStatus** | 템플릿 상태 필터 | (optional) defaults to undefined|


### Return type

**PromptTemplateListResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 프롬프트 템플릿 목록 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **previewPromptTemplate**
> PromptTemplatePreviewResponse previewPromptTemplate(promptTemplatePreviewRequest)

요구사항, 데이터 사전 검색 결과, 신규 후보를 템플릿 변수로 주입해 AI 도구에 전달할 최종 프롬프트를 생성한다.

### Example

```typescript
import {
    PromptTemplateApi,
    Configuration,
    PromptTemplatePreviewRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new PromptTemplateApi(configuration);

let templateId: string; //프롬프트 템플릿 식별자 (default to undefined)
let promptTemplatePreviewRequest: PromptTemplatePreviewRequest; //

const { status, data } = await apiInstance.previewPromptTemplate(
    templateId,
    promptTemplatePreviewRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promptTemplatePreviewRequest** | **PromptTemplatePreviewRequest**|  | |
| **templateId** | [**string**] | 프롬프트 템플릿 식별자 | defaults to undefined|


### Return type

**PromptTemplatePreviewResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 프롬프트 템플릿 미리보기 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

