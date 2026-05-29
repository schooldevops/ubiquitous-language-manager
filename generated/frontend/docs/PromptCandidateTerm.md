# PromptCandidateTerm

프롬프트에 주입할 신규 용어 후보

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**candidateTerm** | **string** |  | [default to undefined]
**recommendedEnglishName** | **string** |  | [optional] [default to undefined]
**recommendedAbbreviation** | **string** |  | [optional] [default to undefined]
**reason** | **string** |  | [default to undefined]
**approvalRequired** | **boolean** |  | [default to undefined]

## Example

```typescript
import { PromptCandidateTerm } from '@aulms/api-client';

const instance: PromptCandidateTerm = {
    candidateTerm,
    recommendedEnglishName,
    recommendedAbbreviation,
    reason,
    approvalRequired,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
