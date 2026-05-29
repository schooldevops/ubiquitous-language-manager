# TermCandidateSummary

신규 용어 후보 목록 요약

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**candidateId** | **string** |  | [default to undefined]
**koreanName** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**englishAbbreviation** | **string** |  | [default to undefined]
**domainName** | **string** |  | [default to undefined]
**status** | [**CandidateStatus**](CandidateStatus.md) |  | [default to undefined]
**requestedBy** | **string** |  | [default to undefined]
**promotedTermId** | **string** |  | [optional] [default to undefined]

## Example

```typescript
import { TermCandidateSummary } from '@aulms/api-client';

const instance: TermCandidateSummary = {
    candidateId,
    koreanName,
    englishName,
    englishAbbreviation,
    domainName,
    status,
    requestedBy,
    promotedTermId,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
