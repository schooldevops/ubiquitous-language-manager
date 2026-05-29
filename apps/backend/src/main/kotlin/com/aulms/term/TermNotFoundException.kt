package com.aulms.term

class TermNotFoundException(termId: String) : RuntimeException("Term not found: $termId")

