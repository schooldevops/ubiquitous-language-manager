# SearchApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**aliasSearch**](#aliassearch) | **GET** /search/alias | 유사어 검색|
|[**deprecatedSearch**](#deprecatedsearch) | **GET** /search/deprecated | 폐기어 검색|
|[**domainSearch**](#domainsearch) | **GET** /search/domain/{domainName} | 도메인별 검색|
|[**exactSearch**](#exactsearch) | **GET** /search/exact | 정확 검색|
|[**semanticSearch**](#semanticsearch) | **POST** /search/semantic | 의미 기반 검색|

# **aliasSearch**
> SearchResponse aliasSearch()

유사어, 별칭, 문맥 확인 필요 표현을 표준 용어로 변환하기 위해 검색한다.

### Example

```typescript
import {
    SearchApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new SearchApi(configuration);

let q: string; //유사어 또는 비표준 표현 (default to undefined)

const { status, data } = await apiInstance.aliasSearch(
    q
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **q** | [**string**] | 유사어 또는 비표준 표현 | defaults to undefined|


### Return type

**SearchResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 유사어 검색 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deprecatedSearch**
> DeprecatedSearchResponse deprecatedSearch()

Deprecated 또는 사용 금지 표현을 검색하고 대체 표준 용어를 반환한다.

### Example

```typescript
import {
    SearchApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new SearchApi(configuration);

let q: string; //폐기어 또는 사용 금지 표현 (default to undefined)

const { status, data } = await apiInstance.deprecatedSearch(
    q
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **q** | [**string**] | 폐기어 또는 사용 금지 표현 | defaults to undefined|


### Return type

**DeprecatedSearchResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 폐기어 검색 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **domainSearch**
> SearchResponse domainSearch()

특정 업무 도메인에 속한 표준 용어를 조회한다.

### Example

```typescript
import {
    SearchApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new SearchApi(configuration);

let domainName: string; //업무 도메인명 (default to undefined)
let status: TermStatus; //용어 상태 필터 (optional) (default to undefined)
let page: number; //페이지 번호 (optional) (default to 0)
let size: number; //페이지 크기 (optional) (default to 20)

const { status, data } = await apiInstance.domainSearch(
    domainName,
    status,
    page,
    size
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **domainName** | [**string**] | 업무 도메인명 | defaults to undefined|
| **status** | **TermStatus** | 용어 상태 필터 | (optional) defaults to undefined|
| **page** | [**number**] | 페이지 번호 | (optional) defaults to 0|
| **size** | [**number**] | 페이지 크기 | (optional) defaults to 20|


### Return type

**SearchResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 도메인별 검색 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **exactSearch**
> SearchResponse exactSearch()

한글 표준명, 영문명, DB 컬럼명, API 필드명, 코드 변수명이 정확히 일치하는 표준 용어를 검색한다.

### Example

```typescript
import {
    SearchApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new SearchApi(configuration);

let q: string; //정확히 일치시킬 검색어 (default to undefined)
let status: TermStatus; //용어 상태 필터 (optional) (default to undefined)

const { status, data } = await apiInstance.exactSearch(
    q,
    status
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **q** | [**string**] | 정확히 일치시킬 검색어 | defaults to undefined|
| **status** | **TermStatus** | 용어 상태 필터 | (optional) defaults to undefined|


### Return type

**SearchResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 정확 검색 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **semanticSearch**
> SemanticSearchResponse semanticSearch(semanticSearchRequest)

자연어 질의문을 용어 문서 임베딩과 비교해 표준 용어 후보를 반환한다.

### Example

```typescript
import {
    SearchApi,
    Configuration,
    SemanticSearchRequest
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new SearchApi(configuration);

let semanticSearchRequest: SemanticSearchRequest; //

const { status, data } = await apiInstance.semanticSearch(
    semanticSearchRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **semanticSearchRequest** | **SemanticSearchRequest**|  | |


### Return type

**SemanticSearchResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 의미 기반 검색 성공 |  -  |
|**400** | 요청 형식 또는 validation 오류 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

