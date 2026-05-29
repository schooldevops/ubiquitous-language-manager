# Term

표준 용어 상세 정보

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**termId** | **string** | 용어를 식별하는 내부 ID | [default to undefined]
**termNumber** | **string** | 업무적으로 노출 가능한 용어 번호 | [default to undefined]
**domainName** | **string** | 용어가 속한 업무 도메인명 | [default to undefined]
**usageType** | **string** | 용어 사용 구분 | [default to undefined]
**koreanName** | **string** | 한글 표준 용어명 | [default to undefined]
**englishName** | **string** | 영문 표준 용어명 | [default to undefined]
**englishAbbreviation** | **string** | 영문 약어 또는 DB 컬럼명 후보 | [default to undefined]
**businessDefinition** | **string** | 업무 정의 | [default to undefined]
**usageContext** | **string** | 사용 맥락 | [optional] [default to undefined]
**physicalType** | **string** | DB 물리 타입 | [default to undefined]
**digits** | **number** | 데이터 길이 | [default to undefined]
**decimalPoint** | **number** | 소수점 자리수 | [default to undefined]
**status** | [**TermStatus**](TermStatus.md) |  | [default to undefined]
**owner** | **string** | 용어 소유자 | [default to undefined]
**version** | **string** | 용어 버전 | [default to undefined]
**expressions** | [**Array&lt;TermExpression&gt;**](TermExpression.md) | 산출물별 표현 매핑 | [default to undefined]
**aliases** | [**Array&lt;TermAlias&gt;**](TermAlias.md) | 유사어, 금지어, 폐기어 목록 | [default to undefined]
**createdAt** | **string** | 생성 일시 | [optional] [default to undefined]
**updatedAt** | **string** | 수정 일시 | [optional] [default to undefined]

## Example

```typescript
import { Term } from '@aulms/api-client';

const instance: Term = {
    termId,
    termNumber,
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
    status,
    owner,
    version,
    expressions,
    aliases,
    createdAt,
    updatedAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
