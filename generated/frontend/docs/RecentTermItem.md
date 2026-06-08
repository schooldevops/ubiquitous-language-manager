# RecentTermItem

최근 등록/수정 용어 항목

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**termId** | **string** |  | [default to undefined]
**koreanName** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**domainName** | **string** |  | [default to undefined]
**status** | [**TermStatus**](TermStatus.md) |  | [default to undefined]
**createdAt** | **string** | 등록 일시 | [default to undefined]
**updatedAt** | **string** | 최종 수정 일시 | [default to undefined]

## Example

```typescript
import { RecentTermItem } from '@aulms/api-client';

const instance: RecentTermItem = {
    termId,
    koreanName,
    englishName,
    domainName,
    status,
    createdAt,
    updatedAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
