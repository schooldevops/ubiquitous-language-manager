# SemanticSearchRequest

자연어 의미 기반 용어 검색 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**query** | **string** | 자연어 질의문 | [default to undefined]
**domainNames** | **Array&lt;string&gt;** | 검색 대상 도메인 필터 | [optional] [default to undefined]
**statuses** | [**Array&lt;TermStatus&gt;**](TermStatus.md) | 검색 대상 용어 상태 필터 | [optional] [default to undefined]
**limit** | **number** | 반환할 후보 개수 | [optional] [default to 5]

## Example

```typescript
import { SemanticSearchRequest } from '@aulms/api-client';

const instance: SemanticSearchRequest = {
    query,
    domainNames,
    statuses,
    limit,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
