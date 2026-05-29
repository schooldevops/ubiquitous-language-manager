# SemanticSearchResult

의미 기반 검색 결과

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**termId** | **string** | 표준 용어 ID | [default to undefined]
**standardTerm** | **string** | 한글 표준 용어명 | [default to undefined]
**englishName** | **string** | 영문 표준 용어명 | [default to undefined]
**dbColumn** | **string** | 표준 DB 컬럼명 | [default to undefined]
**apiField** | **string** | 표준 API 필드명 | [default to undefined]
**status** | [**TermStatus**](TermStatus.md) |  | [default to undefined]
**similarityScore** | **number** | 의미 유사도 점수 | [default to undefined]
**recommendationReason** | **string** | 후보 추천 사유 | [default to undefined]
**differenceDescription** | **string** | 유사 후보 간 차이 설명 | [default to undefined]
**guidance** | **string** | 상태별 사용 안내 | [optional] [default to undefined]
**validationIssues** | [**Array&lt;ValidationIssue&gt;**](ValidationIssue.md) | Rule Engine 후처리 결과 | [optional] [default to undefined]

## Example

```typescript
import { SemanticSearchResult } from '@aulms/api-client';

const instance: SemanticSearchResult = {
    termId,
    standardTerm,
    englishName,
    dbColumn,
    apiField,
    status,
    similarityScore,
    recommendationReason,
    differenceDescription,
    guidance,
    validationIssues,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
