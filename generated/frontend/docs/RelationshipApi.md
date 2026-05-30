# RelationshipApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getColumnSystems**](#getcolumnsystems) | **GET** /relationships/columns/{columnName}/systems | 컬럼 사용 시스템 조회|
|[**getDeprecatedUsages**](#getdeprecatedusages) | **GET** /relationships/deprecated | 폐기어와 금지어 사용 위치 조회|
|[**getDomainTerms**](#getdomainterms) | **GET** /relationships/domains/{domainName}/terms | 도메인별 표준 용어 관계 검색|
|[**getTermRelationships**](#gettermrelationships) | **GET** /relationships/terms/{termId} | 용어 관계 검색|

# **getColumnSystems**
> ColumnSystemUsageResponse getColumnSystems()

Graphify 그래프에서 특정 DB 컬럼을 사용하는 시스템, 테이블, API 필드를 조회한다.

### Example

```typescript
import {
    RelationshipApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new RelationshipApi(configuration);

let columnName: string; //DB 컬럼명 (default to undefined)

const { status, data } = await apiInstance.getColumnSystems(
    columnName
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **columnName** | [**string**] | DB 컬럼명 | defaults to undefined|


### Return type

**ColumnSystemUsageResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 컬럼 사용 시스템 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getDeprecatedUsages**
> DeprecatedUsageResponse getDeprecatedUsages()

Graphify 그래프에서 Deprecated 또는 Forbidden alias 사용 위치와 대체 표준 용어를 조회한다.

### Example

```typescript
import {
    RelationshipApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new RelationshipApi(configuration);

const { status, data } = await apiInstance.getDeprecatedUsages();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**DeprecatedUsageResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 폐기어와 금지어 사용 위치 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getDomainTerms**
> RelationshipSearchResponse getDomainTerms()

Graphify 그래프에서 특정 도메인에 속한 표준 용어를 조회한다.

### Example

```typescript
import {
    RelationshipApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new RelationshipApi(configuration);

let domainName: string; //도메인명 (default to undefined)

const { status, data } = await apiInstance.getDomainTerms(
    domainName
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **domainName** | [**string**] | 도메인명 | defaults to undefined|


### Return type

**RelationshipSearchResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 도메인별 용어 조회 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getTermRelationships**
> RelationshipSearchResponse getTermRelationships()

Graphify 그래프에서 특정 표준 용어와 연결된 관련 용어와 경로를 조회한다.

### Example

```typescript
import {
    RelationshipApi,
    Configuration
} from '@aulms/api-client';

const configuration = new Configuration();
const apiInstance = new RelationshipApi(configuration);

let termId: string; //기준 용어 ID (default to undefined)
let relationshipType: string; //관계 유형 필터 (optional) (default to undefined)

const { status, data } = await apiInstance.getTermRelationships(
    termId,
    relationshipType
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **termId** | [**string**] | 기준 용어 ID | defaults to undefined|
| **relationshipType** | [**string**] | 관계 유형 필터 | (optional) defaults to undefined|


### Return type

**RelationshipSearchResponse**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 용어 관계 검색 성공 |  -  |
|**401** | 인증 필요 |  -  |
|**403** | 권한 없음 |  -  |
|**404** | 리소스 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

