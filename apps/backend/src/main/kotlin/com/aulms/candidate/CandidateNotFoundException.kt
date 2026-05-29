package com.aulms.candidate

class CandidateNotFoundException(candidateId: String) : RuntimeException("Candidate not found: $candidateId")
