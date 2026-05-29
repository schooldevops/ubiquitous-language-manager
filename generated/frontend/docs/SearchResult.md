# SearchResult

표준 용어 검색 결과

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**termId** | **string** | 표준 용어 식별자 | [default to undefined]
**standardTerm** | **string** | 한글 표준 용어명 | [default to undefined]
**englishName** | **string** | 영문 표준 용어명 | [default to undefined]
**dbColumn** | **string** | 표준 DB 컬럼명 | [default to undefined]
**apiField** | **string** | 표준 API 필드명 | [default to undefined]
**codeVariable** | **string** | 표준 코드 변수명 | [optional] [default to undefined]
**status** | [**TermStatus**](TermStatus.md) |  | [default to undefined]
**domainName** | **string** | 업무 도메인명 | [optional] [default to undefined]
**businessDefinition** | **string** | 업무 정의 | [optional] [default to undefined]
**usageContext** | **string** | 사용 맥락 | [optional] [default to undefined]
**score** | **number** | 검색 적합도 점수 | [optional] [default to undefined]
**matchedExpressions** | [**Array&lt;MatchedExpression&gt;**](MatchedExpression.md) | 입력 검색어와 매칭된 표현 | [default to undefined]
**recommendations** | [**Array&lt;Recommendation&gt;**](Recommendation.md) | 표준 표현 사용 권고 | [default to undefined]

## Example

```typescript
import { SearchResult } from '@aulms/api-client';

const instance: SearchResult = {
    termId,
    standardTerm,
    englishName,
    dbColumn,
    apiField,
    codeVariable,
    status,
    domainName,
    businessDefinition,
    usageContext,
    score,
    matchedExpressions,
    recommendations,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
