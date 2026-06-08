# DashboardSummary

메인 대시보드 요약 데이터

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**totalTerms** | **number** | 전체 등록 용어 수 | [default to undefined]
**recentTerms** | [**Array&lt;RecentTermItem&gt;**](RecentTermItem.md) | 최근 등록 또는 수정된 용어 목록 (수정일 내림차순) | [default to undefined]
**domainStats** | [**Array&lt;DomainTermStat&gt;**](DomainTermStat.md) | 도메인별 용어 통계 | [default to undefined]

## Example

```typescript
import { DashboardSummary } from '@aulms/api-client';

const instance: DashboardSummary = {
    totalTerms,
    recentTerms,
    domainStats,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
