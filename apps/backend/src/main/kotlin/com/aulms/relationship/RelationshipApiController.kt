package com.aulms.relationship

import com.aulms.api.RelationshipApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class RelationshipApiController(private val service: RelationshipSearchService) : RelationshipApi {
    override fun getTermRelationships(termId: String, relationshipType: String?) =
        ResponseEntity.ok(service.getTermRelationships(termId, relationshipType))

    override fun getDomainTerms(domainName: String) =
        ResponseEntity.ok(service.getDomainTerms(domainName))

    override fun getColumnSystems(columnName: String) =
        ResponseEntity.ok(service.getColumnSystems(columnName))

    override fun getDeprecatedUsages() =
        ResponseEntity.ok(service.getDeprecatedUsages())
}
