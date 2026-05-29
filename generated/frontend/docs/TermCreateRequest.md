# TermCreateRequest

표준 용어 등록 요청

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**domainName** | **string** |  | [default to undefined]
**usageType** | **string** |  | [default to undefined]
**koreanName** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**englishAbbreviation** | **string** |  | [default to undefined]
**businessDefinition** | **string** |  | [default to undefined]
**usageContext** | **string** |  | [optional] [default to undefined]
**physicalType** | **string** |  | [default to undefined]
**digits** | **number** |  | [default to undefined]
**decimalPoint** | **number** |  | [default to undefined]
**owner** | **string** |  | [default to undefined]
**status** | [**TermStatus**](TermStatus.md) |  | [optional] [default to undefined]

## Example

```typescript
import { TermCreateRequest } from '@aulms/api-client';

const instance: TermCreateRequest = {
    domainName,
    usageType,
    koreanName,
    englishName,
    englishAbbreviation,
    businessDefinition,
    usageContext,
    physicalType,
    digits,
    decimalPoint,
    owner,
    status,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
