# ImpactAnalysisResponse

용어 변경 영향도 분석 응답

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**termId** | **string** |  | [default to undefined]
**standardTerm** | **string** |  | [default to undefined]
**changeType** | [**ImpactChangeType**](ImpactChangeType.md) |  | [default to undefined]
**includeTwoHop** | **boolean** |  | [default to undefined]
**riskLevel** | [**ImpactRiskLevel**](ImpactRiskLevel.md) |  | [default to undefined]
**riskScore** | **number** |  | [default to undefined]
**impactedTargets** | [**Array&lt;ImpactTarget&gt;**](ImpactTarget.md) | 영향받는 시스템, DB, API, DTO, 문서, 테스트 | [default to undefined]
**recommendations** | [**Array&lt;ImpactRecommendation&gt;**](ImpactRecommendation.md) | 권장 조치 목록 | [default to undefined]

## Example

```typescript
import { ImpactAnalysisResponse } from '@aulms/api-client';

const instance: ImpactAnalysisResponse = {
    termId,
    standardTerm,
    changeType,
    includeTwoHop,
    riskLevel,
    riskScore,
    impactedTargets,
    recommendations,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
