# RecommendationEvidence

RAG 검색으로 수집한 근거 용어

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**termId** | **string** |  | [default to undefined]
**standardTerm** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**dbColumn** | **string** |  | [default to undefined]
**apiField** | **string** |  | [default to undefined]
**domainName** | **string** |  | [default to undefined]
**source** | **string** |  | [default to undefined]
**score** | **number** |  | [default to undefined]
**reason** | **string** |  | [default to undefined]

## Example

```typescript
import { RecommendationEvidence } from '@aulms/api-client';

const instance: RecommendationEvidence = {
    termId,
    standardTerm,
    englishName,
    dbColumn,
    apiField,
    domainName,
    source,
    score,
    reason,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
