# DomainTermStat

도메인별 용어 수와 상태별 분포

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**domainName** | **string** |  | [default to undefined]
**totalCount** | **number** | 도메인 전체 용어 수 | [default to undefined]
**approvedCount** | **number** | 확정(Approved) 용어 수 | [default to undefined]
**draftCount** | **number** |  | [default to undefined]
**reviewingCount** | **number** |  | [default to undefined]
**deprecatedCount** | **number** |  | [default to undefined]
**rejectedCount** | **number** |  | [default to undefined]

## Example

```typescript
import { DomainTermStat } from '@aulms/api-client';

const instance: DomainTermStat = {
    domainName,
    totalCount,
    approvedCount,
    draftCount,
    reviewingCount,
    deprecatedCount,
    rejectedCount,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
