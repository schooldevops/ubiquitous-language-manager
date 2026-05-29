# AssistTermMapping

표준 용어 매핑 결과

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**concept** | **string** |  | [default to undefined]
**termId** | **string** |  | [default to undefined]
**standardTerm** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**dbColumn** | **string** |  | [default to undefined]
**apiField** | **string** |  | [default to undefined]
**codeVariable** | **string** |  | [default to undefined]
**status** | [**TermStatus**](TermStatus.md) |  | [default to undefined]
**mappingSource** | **string** | 매핑 출처 | [default to undefined]
**recommendationReason** | **string** |  | [default to undefined]

## Example

```typescript
import { AssistTermMapping } from '@aulms/api-client';

const instance: AssistTermMapping = {
    concept,
    termId,
    standardTerm,
    englishName,
    dbColumn,
    apiField,
    codeVariable,
    status,
    mappingSource,
    recommendationReason,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
