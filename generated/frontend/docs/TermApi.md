# TermApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createTerm**](#createterm) | **POST** /terms | 표준 용어 등록|
|[**getTerm**](#getterm) | **GET** /terms/{termId} | 표준 용어 상세 조회|
|[**listTerms**](#listterms) | **GET** /terms | 표준 용어 목록 조회|
|[**updateTerm**](#updateterm) | **PUT** /terms/{termId} | 표준 용어 수정|

# **createTerm**
> Term createTerm(termCreateRequest)

신규 표준 용어를 Draft 또는 지정 상태로 등록한다.

### Example

```typescript
import {
    TermApi,
    Configuration,
    TermCreateRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new TermApi(configuration);

let termCreateRequest: TermCreateRequest; //

const { status, data } = await apiInstance.createTerm(
    termCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termCreateRequest** | **TermCreateRequest**|  | |


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
|**201** | 표준 용어 등록 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**409** | 중복 또는 상태 충돌 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getTerm**
> Term getTerm()


### Example

```typescript
import {
    TermApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new TermApi(configuration);

let termId: string; //용어 식별자 (default to undefined)

const { status, data } = await apiInstance.getTerm(
    termId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termId** | [**string**] | 용어 식별자 | defaults to undefined|


### Return type

**Term**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 표준 용어 상세 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listTerms**
> TermListResponse listTerms()

한글명, 영문명, 약어, 도메인, 상태 조건으로 표준 용어 목록을 조회한다.

### Example

```typescript
import {
    TermApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new TermApi(configuration);

let q: string; //한글명, 영문명, 약어, API 필드명 검색어 (optional) (default to undefined)
let domainName: string; //도메인명 필터 (optional) (default to undefined)
let status: TermStatus; //용어 상태 필터 (optional) (default to undefined)
let page: number; //페이지 번호 (optional) (default to 0)
let size: number; //페이지 크기 (optional) (default to 20)

const { status, data } = await apiInstance.listTerms(
    q,
    domainName,
    status,
    page,
    size
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **q** | [**string**] | 한글명, 영문명, 약어, API 필드명 검색어 | (optional) defaults to undefined|
| **domainName** | [**string**] | 도메인명 필터 | (optional) defaults to undefined|
| **status** | **TermStatus** | 용어 상태 필터 | (optional) defaults to undefined|
| **page** | [**number**] | 페이지 번호 | (optional) defaults to 0|
| **size** | [**number**] | 페이지 크기 | (optional) defaults to 20|


### Return type

**TermListResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 표준 용어 목록 조회 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateTerm**
> Term updateTerm(termUpdateRequest)


### Example

```typescript
import {
    TermApi,
    Configuration,
    TermUpdateRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new TermApi(configuration);

let termId: string; //용어 식별자 (default to undefined)
let termUpdateRequest: TermUpdateRequest; //

const { status, data } = await apiInstance.updateTerm(
    termId,
    termUpdateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termUpdateRequest** | **TermUpdateRequest**|  | |
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
|**200** | 표준 용어 수정 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |
|**409** | 중복 또는 상태 충돌 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

