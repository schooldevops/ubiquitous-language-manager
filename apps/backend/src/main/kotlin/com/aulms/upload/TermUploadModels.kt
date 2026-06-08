package com.aulms.upload

import com.aulms.model.RelationshipType
import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
import com.aulms.model.TermStatus

/** 파싱된 한 줄. parseError != null 이면 파싱 실패 행. */
data class ParsedTermRow(
    val lineNo: Int,
    val rawJson: String,
    val term: ParsedTerm?,
    val parseError: String?,
)

data class ParsedTerm(
    val termId: String,
    val termNumber: String?,
    val domainName: String?,
    val usageType: String?,
    val koreanName: String?,
    val englishName: String?,
    val englishAbbreviation: String?,
    val businessDefinition: String?,
    val usageContext: String?,
    val physicalType: String?,
    val digits: Int?,
    val decimalPoint: Int?,
    val status: TermStatus?,
    val owner: String?,
    val version: String?,
    val expressions: List<TermExpression>,
    val aliases: List<TermAlias>,
    val relationships: List<ParsedRelationship>,
)

data class ParsedRelationship(
    val relationshipId: Long,
    val relationshipType: RelationshipType,
    val targetTermId: String,
    val description: String,
)
