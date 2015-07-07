package org.modelcatalogue.core.grails

import org.modelcatalogue.core.RelationshipType
import org.modelcatalogue.core.repository.api.RelationshipTypeRepository
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions


class GrailsRelationshipTypeRepository
        extends AbstractGrailsRepository<org.modelcatalogue.core.api.RelationshipType, RelationshipType>
        implements RelationshipTypeRepository {

    GrailsRelationshipTypeRepository() {
        super(RelationshipType)
    }

    @Override
    org.modelcatalogue.core.api.RelationshipType findByName(String name, SortAndPaginationOptions options) {
        return super.findByName(name, options)
    }
}
