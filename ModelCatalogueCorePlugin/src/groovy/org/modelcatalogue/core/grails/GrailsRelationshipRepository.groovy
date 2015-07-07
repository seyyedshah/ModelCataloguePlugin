package org.modelcatalogue.core.grails

import org.modelcatalogue.core.Classification
import org.modelcatalogue.core.RelationshipDefinitionBuilder
import org.modelcatalogue.core.RelationshipService
import org.modelcatalogue.core.api.CatalogueElement
import org.modelcatalogue.core.api.Relationship
import org.modelcatalogue.core.api.RelationshipDirection
import org.modelcatalogue.core.api.RelationshipType
import org.modelcatalogue.core.repository.api.RelationshipDefinition
import org.modelcatalogue.core.repository.api.RelationshipRepository
import org.modelcatalogue.core.repository.api.RelationshipsPartialResult
import org.modelcatalogue.core.repository.api.RelationshipsPartialResultDecorator
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions

class GrailsRelationshipRepository implements RelationshipRepository {

    private static org.modelcatalogue.core.util.RelationshipDirection getLegacyDirection(RelationshipDirection direction) {
        switch (direction) {
            case RelationshipDirection.OUTGOING: return org.modelcatalogue.core.util.RelationshipDirection.OUTGOING
            case RelationshipDirection.INCOMING: return org.modelcatalogue.core.util.RelationshipDirection.INCOMING
            case RelationshipDirection.COMBINED: return org.modelcatalogue.core.util.RelationshipDirection.BOTH
            default: throw new IllegalArgumentException("Unknown direction: $direction")
        }
    }


    private static org.modelcatalogue.core.RelationshipDefinition getLegacyDefinition(RelationshipDefinition relationshipDefinition) {
        RelationshipDefinitionBuilder builder = org.modelcatalogue.core.RelationshipDefinition.create(
                relationshipDefinition.source as org.modelcatalogue.core.CatalogueElement,
                relationshipDefinition.destination as org.modelcatalogue.core.CatalogueElement,
                relationshipDefinition.relationshipType as org.modelcatalogue.core.RelationshipType
        )

        builder.withArchived(relationshipDefinition.archived)
        builder.withClassification(relationshipDefinition.namespace as Classification)
        builder.withMetadata(relationshipDefinition.metadata)


        if (relationshipDefinition.extra.incomingIndex)         builder.withIncomingIndex(relationshipDefinition.extra.incomingIndex as Long)
        if (relationshipDefinition.extra.combinedIndex)         builder.withCombinedIndex(relationshipDefinition.extra.combinedIndex as Long)
        if (relationshipDefinition.extra.outogingIndex)         builder.withOutgoingIndex(relationshipDefinition.extra.outogingIndex as Long)
        if (relationshipDefinition.extra.ignoreRules)           builder.withIgnoreRules(true)
        if (relationshipDefinition.extra.resetIndicies)         builder.withResetIndices(true)
        if (relationshipDefinition.extra.skipUniqueChecking)    builder.withSkipUniqueChecking(true)

        builder.definition
    }


    private final RelationshipService relationshipService

    GrailsRelationshipRepository(RelationshipService relationshipService) {
        this.relationshipService = relationshipService
    }

    @Override
    RelationshipsPartialResult getRelationships(CatalogueElement catalogueElement, RelationshipDirection relationshipDirection, RelationshipType relationshipType, SortAndPaginationOptions sortAndPaginationOptions) {
        RelationshipsPartialResultDecorator.create(
                catalogueElement,
                relationshipDirection,
                relationshipType,
                new ListWithTotalPartialResult<org.modelcatalogue.core.Relationship>(relationshipService.getRelationships(
                        GormUtils.convertOptions(sortAndPaginationOptions),
                        getLegacyDirection(relationshipDirection),
                        catalogueElement as org.modelcatalogue.core.CatalogueElement,
                        relationshipType as org.modelcatalogue.core.RelationshipType
                ) ,sortAndPaginationOptions)
        )


    }

    @Override
    int countRelationships(CatalogueElement element, RelationshipDirection direction, RelationshipType type) {
        return getRelationships(element, direction, type, SortAndPaginationOptions.noOptions()).total
    }

    @Override
    Relationship link(RelationshipDefinition definition) {
        return relationshipService.link(getLegacyDefinition(definition))
    }

    @Override
    Relationship unlink(RelationshipDefinition definition) {
        return relationshipService.unlink(
                definition.source as org.modelcatalogue.core.CatalogueElement,
                definition.destination as org.modelcatalogue.core.CatalogueElement,
                definition.relationshipType as org.modelcatalogue.core.RelationshipType,
                definition.namespace as Classification,
                definition.extra.ignoreRules ? true : false
        )
    }

    @Override
    Relationship moveAfter(CatalogueElement catalogueElement, RelationshipDirection relationshipDirection, Relationship relationship, Relationship other) {
        return relationshipService.moveAfter(getLegacyDirection(relationshipDirection), catalogueElement as org.modelcatalogue.core.CatalogueElement, relationship as org.modelcatalogue.core.Relationship, other as org.modelcatalogue.core.Relationship)
    }

    @Override
    Relationship findExistingRelationship(RelationshipDefinition relationshipDefinition) {
        return relationshipService.findExistingRelationship(getLegacyDefinition(relationshipDefinition))
    }


}
