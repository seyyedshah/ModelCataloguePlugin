package org.modelcatalogue.core.grails

import org.modelcatalogue.core.RelationshipService
import org.modelcatalogue.core.api.Catalogue
import org.modelcatalogue.core.repository.api.CatalogueConstants
import org.modelcatalogue.core.repository.api.ElementTypeRepository
import org.modelcatalogue.core.repository.api.RelationshipRepository
import org.modelcatalogue.core.repository.api.RelationshipTypeRepository

class GrailsCatalogue implements Catalogue {

    private final RelationshipService relationshipService

    GrailsCatalogue(RelationshipService relationshipService) {
        this.relationshipService = relationshipService
    }

    @Override
    ElementTypeRepository getElementTypeRepository() {
        new GrailsElementTypeRepository()
    }

    @Override
    RelationshipTypeRepository getRelationshipTypeRepository() {
        new GrailsRelationshipTypeRepository()
    }

    @Override
    RelationshipRepository getRelationshipRepository() {
        new GrailsRelationshipRepository(relationshipService)
    }

    @Override
    CatalogueConstants getCatalogueConstants() {
        GrailsCatalogueConstants.INSTANCE
    }
}
