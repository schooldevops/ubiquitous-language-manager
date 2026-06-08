# 개발 Rule

## 개발 기술 스택

### Backend 기술 스택 

- kotlin
- spring boot
- open api
- querydsl sql
- postgresql

### Frontend 기술 스택

- react
- nextjs
- tailwindcss 4
- base ui

### 개발시 주요 사안

- 항상 open api spec 을 생성해야한다. 
- open api generator을 통해서 Backend server stub을 spring boot, kotlin 언어용으로 생성해서 이를 구현하도록 해야한다.
- open api generator을 통해서 Frontend client api 를 axios, typescript 언어용으로 생성해서 이를 구현하도록 해야한다.
- 우리가 유비쿼터스 언어를 관리하는 시스템이기 때문에 우리의 개발 역시 유비쿼터스 언어로 작성된 데이터 딕셔너리를 작성하고 나서, 이를 바탕으로 개발을 해야한다. 
- 항상 [caveman스킬](./caveman.md) 의 내용대로 수행해줘
 
### 진행 규칙 

- 매 단계마다 나에게 변화된 내용을 확인시켜주고, 승인이 나면 다음 단계로 넘어가도록 해줘.

## 모델선정

- 계획에서는 Opus로 개발은 Sonnet 으로 모델을 변경하여 개발을 해야해