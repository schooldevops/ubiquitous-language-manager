package com.aulms.prompt

class PromptTemplateNotFoundException(templateId: String) : RuntimeException("Prompt template not found: $templateId")
