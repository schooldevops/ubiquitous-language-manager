# TermCandidate

신규 용어 후보 상세 정보

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**candidateId** | **string** | 후보 식별자 | [default to undefined]
**koreanName** | **string** | 후보 한글 용어명 | [default to undefined]
**englishName** | **string** | 후보 영문 용어명 | [default to undefined]
**englishAbbreviation** | **string** | 후보 영문 약어 | [default to undefined]
**domainName** | **string** | 업무 도메인명 | [default to undefined]
**businessDefinition** | **string** | 업무 정의 | [default to undefined]
**usageContext** | **string** | 사용 맥락 | [optional] [default to undefined]
**physicalType** | **string** | DB 물리 타입 | [default to undefined]
**digits** | **number** | 자릿수 | [default to undefined]
**decimalPoint** | **number** | 소수점 | [default to undefined]
**status** | [**CandidateStatus**](CandidateStatus.md) |  | [default to undefined]
**requestedBy** | **string** | 요청자 | [default to undefined]
**reviewedBy** | **string** | 검토자 | [optional] [default to undefined]
**promotedTermId** | **string** | 승격된 표준 용어 ID | [optional] [default to undefined]
**similarTerms** | [**Array&lt;SimilarTerm&gt;**](SimilarTerm.md) | 등록 시 검색된 기존 유사 용어 | [default to undefined]
**histories** | [**Array&lt;CandidateHistory&gt;**](CandidateHistory.md) | 후보 상태 변경 이력 | [default to undefined]
**createdAt** | **string** | 생성 일시 | [optional] [default to undefined]
**updatedAt** | **string** | 수정 일시 | [optional] [default to undefined]

## Example

```typescript
import { TermCandidate } from '@aulms/api-client';

const instance: TermCandidate = {
    candidateId,
    koreanName,
    englishName,
    englishAbbreviation,
    domainName,
    businessDefinition,
    usageContext,
    physicalType,
    digits,
    decimalPoint,
    status,
    requestedBy,
    reviewedBy,
    promotedTermId,
    similarTerms,
    histories,
    createdAt,
    updatedAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
