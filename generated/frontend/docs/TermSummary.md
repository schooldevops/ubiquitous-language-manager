# TermSummary

표준 용어 목록 조회용 요약 정보

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**termId** | **string** |  | [default to undefined]
**termNumber** | **string** |  | [default to undefined]
**domainName** | **string** |  | [default to undefined]
**koreanName** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**englishAbbreviation** | **string** |  | [default to undefined]
**apiFieldName** | **string** | 표준 API 필드명 | [optional] [default to undefined]
**status** | [**TermStatus**](TermStatus.md) |  | [default to undefined]
**relatedSystems** | **Array&lt;string&gt;** | 관련 시스템 목록 | [optional] [default to undefined]

## Example

```typescript
import { TermSummary } from '@aulms/api-client';

const instance: TermSummary = {
    termId,
    termNumber,
    domainName,
    koreanName,
    englishName,
    englishAbbreviation,
    apiFieldName,
    status,
    relatedSystems,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
