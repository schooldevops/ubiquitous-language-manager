# AIApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**developmentAssist**](#developmentassist) | **POST** /ai/development-assist | AI 산출물 생성 지원|
|[**recommendTermDraft**](#recommendtermdraft) | **POST** /ai/term-recommendation | RAG/Graph/LLM 기반 용어 초안 추천|

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

# **recommendTermDraft**
> TermRecommendationResponse recommendTermDraft(termRecommendationRequest)

한글 용어명을 입력받아 먼저 정확 검색, 유사어 검색, 의미 기반 검색으로 관련 용어를 수집하고, Graph 관계를 확장한 뒤, 최종 추론 단계에서 영문명, 약어, 타입, 자릿수, 소유자 등을 추천한다.

### Example

```typescript
import {
    AIApi,
    Configuration,
    TermRecommendationRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new AIApi(configuration);

let termRecommendationRequest: TermRecommendationRequest; //

const { status, data } = await apiInstance.recommendTermDraft(
    termRecommendationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termRecommendationRequest** | **TermRecommendationRequest**|  | |


### Return type

**TermRecommendationResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 용어 초안 추천 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

