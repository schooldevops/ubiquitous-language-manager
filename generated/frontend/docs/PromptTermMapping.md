# PromptTermMapping

프롬프트에 주입할 표준 용어 매핑

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**concept** | **string** |  | [default to undefined]
**standardTerm** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**dbColumn** | **string** |  | [default to undefined]
**apiField** | **string** |  | [default to undefined]
**codeVariable** | **string** |  | [default to undefined]
**status** | [**TermStatus**](TermStatus.md) |  | [default to undefined]

## Example

```typescript
import { PromptTermMapping } from '@aulms/api-client';

const instance: PromptTermMapping = {
    concept,
    standardTerm,
    englishName,
    dbColumn,
    apiField,
    codeVariable,
    status,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
