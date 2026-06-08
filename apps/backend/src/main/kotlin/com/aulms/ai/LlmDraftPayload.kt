package com.aulms.ai

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LlmDraftPayload(
    val domainName: String,
    val usageType: String,
    val englishName: String,
    val englishAbbreviation: String,
    val businessDefinition: String,
    val usageContext: String,
    val physicalType: String,
    val digits: Int,
    val decimalPoint: Int,
    val owner: String,
    val expressions: List<LlmExpressionPayload>? = null,
    val aliases: List<LlmAliasPayload>? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class LlmExpressionPayload(
    val expressionType: String? = null,
    val expressionValue: String? = null,
    val isStandard: Boolean? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class LlmAliasPayload(
    val aliasName: String? = null,
    val aliasType: String? = null,
    val recommendationAction: String? = null,
    val reason: String? = null,
)
