package com.aulms.candidate

import com.aulms.model.CandidateStatus
import com.aulms.model.SimilarTerm
import com.aulms.model.TermCandidate
import com.aulms.model.TermCandidateListResponse

/**
 * 신규 용어 후보 저장소. 영속화 모드별 구현:
 *  - [InMemoryCandidateRepository] : 기본(memory) 프로파일
 *  - JpaCandidateRepository        : postgres 프로파일
 */
interface CandidateRepository {
    fun create(command: CandidateCommand, similarTerms: List<SimilarTerm>): TermCandidate
    fun list(q: String?, status: CandidateStatus?, domainName: String?, page: Int, size: Int): TermCandidateListResponse
    fun get(candidateId: String): TermCandidate
    fun updateStatus(candidateId: String, status: CandidateStatus, actor: String, reason: String, reviewedBy: String? = null, promotedTermId: String? = null): TermCandidate
}

data class CandidateCommand(
    val koreanName: String,
    val englishName: String,
    val englishAbbreviation: String,
    val domainName: String,
    val businessDefinition: String,
    val usageContext: String?,
    val physicalType: String,
    val digits: Int,
    val decimalPoint: Int,
    val requestedBy: String,
)
