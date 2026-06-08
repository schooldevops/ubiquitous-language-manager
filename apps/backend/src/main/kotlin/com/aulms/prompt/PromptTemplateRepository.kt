package com.aulms.prompt

import com.aulms.model.PromptTemplate
import com.aulms.model.PromptTemplateListResponse
import com.aulms.model.PromptTemplateStatus
import com.aulms.model.PromptTemplateType
import com.aulms.model.PromptTemplateVersionListResponse

/**
 * 프롬프트 템플릿 저장소(읽기 전용 seed). 영속화 모드별 구현:
 *  - [InMemoryPromptTemplateRepository] : 기본(memory) 프로파일
 *  - JpaPromptTemplateRepository        : postgres 프로파일
 */
interface PromptTemplateRepository {
    fun list(type: PromptTemplateType?, status: PromptTemplateStatus?): PromptTemplateListResponse
    fun get(templateId: String): PromptTemplate
    fun listVersions(templateId: String): PromptTemplateVersionListResponse
}
