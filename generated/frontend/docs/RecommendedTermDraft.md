# RecommendedTermDraft

추천으로 채워줄 용어 초안

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**domainName** | **string** |  | [default to undefined]
**usageType** | **string** |  | [default to undefined]
**englishName** | **string** |  | [default to undefined]
**englishAbbreviation** | **string** |  | [default to undefined]
**businessDefinition** | **string** |  | [default to undefined]
**usageContext** | **string** |  | [default to undefined]
**physicalType** | **string** |  | [default to undefined]
**digits** | **number** |  | [default to undefined]
**decimalPoint** | **number** |  | [default to undefined]
**owner** | **string** |  | [default to undefined]
**expressions** | [**Array&lt;RecommendedExpression&gt;**](RecommendedExpression.md) | 산출물별 표현 추천 (DB 컬럼, API 필드, 코드 변수, UI 라벨 등) | [default to undefined]
**aliases** | [**Array&lt;RecommendedAlias&gt;**](RecommendedAlias.md) | 별칭/유사어/금지어 추천 목록 | [default to undefined]

## Example

```typescript
import { RecommendedTermDraft } from '@aulms/api-client';

const instance: RecommendedTermDraft = {
    domainName,
    usageType,
    englishName,
    englishAbbreviation,
    businessDefinition,
    usageContext,
    physicalType,
    digits,
    decimalPoint,
    owner,
    expressions,
    aliases,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
