package com.aulms.search

import com.aulms.api.SearchApi
import com.aulms.model.SemanticSearchRequest
import com.aulms.model.TermStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchApiController(
    private val service: SearchService,
    private val semanticSearchService: SemanticSearchService,
) : SearchApi {
    override fun exactSearch(q: String, status: TermStatus?) =
        ResponseEntity.ok(service.exactSearch(q, status))

    override fun aliasSearch(q: String) =
        ResponseEntity.ok(service.aliasSearch(q))

    override fun domainSearch(domainName: String, status: TermStatus?, page: Int, size: Int) =
        ResponseEntity.ok(service.domainSearch(domainName, status, page, size))

    override fun deprecatedSearch(q: String) =
        ResponseEntity.ok(service.deprecatedSearch(q))

    override fun semanticSearch(semanticSearchRequest: SemanticSearchRequest) =
        ResponseEntity.ok(semanticSearchService.semanticSearch(semanticSearchRequest))
}
