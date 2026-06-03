package com.aulms.ai

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
)
