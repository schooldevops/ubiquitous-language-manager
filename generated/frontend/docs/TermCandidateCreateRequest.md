# TermCandidateCreateRequest

신규 용어 후보 등록 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**koreanName** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**englishAbbreviation** | **string** |  | [default to undefined]
**domainName** | **string** |  | [default to undefined]
**businessDefinition** | **string** |  | [default to undefined]
**usageContext** | **string** |  | [optional] [default to undefined]
**physicalType** | **string** |  | [default to undefined]
**digits** | **number** |  | [default to undefined]
**decimalPoint** | **number** |  | [default to undefined]
**requestedBy** | **string** |  | [default to undefined]

## Example

```typescript
import { TermCandidateCreateRequest } from '@aulms/api-client';

const instance: TermCandidateCreateRequest = {
    koreanName,
    englishName,
    englishAbbreviation,
    domainName,
    businessDefinition,
    usageContext,
    physicalType,
    digits,
    decimalPoint,
    requestedBy,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
