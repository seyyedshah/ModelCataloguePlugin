package org.modelcatalogue.core.grails

import org.modelcatalogue.core.api.ElementType
import org.modelcatalogue.core.api.Identifier
import org.modelcatalogue.core.repository.api.ElementTypeRepository
import org.modelcatalogue.core.repository.api.PartialResult
import org.modelcatalogue.core.repository.api.CatalogueElementRepository
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions
import org.modelcatalogue.core.repository.api.StringIdentifier

class GrailsElementTypeRepository implements ElementTypeRepository {

    private static final Set<ElementType> TYPES_WITH_UNIQUE_NAMES = Collections.unmodifiableSet(new HashSet([GrailsElementType.CLASSIFICATION, GrailsElementType.MEASUREMENT_UNIT]))

    @Override
    ElementType getDefaultElementType() {
        return GrailsElementType.CATALOGUE_ELEMENT
    }

    @Override
    Set<ElementType> getTypesWithUniqueNames() {
        return TYPES_WITH_UNIQUE_NAMES
    }

    @Override
    CatalogueElementRepository getCatalogueElementRepository(ElementType type) {
        if (type instanceof GrailsElementType) {
            return new GrailsCatalogueElementRepository(type)

        }
        return new GrailsCatalogueElementRepository(GrailsElementType.byName(type.name))
    }

    @Override
    ElementType findByName(String name, SortAndPaginationOptions options) {
        GrailsElementType.byName(name)
    }

    @Override
    PartialResult<ElementType> list(SortAndPaginationOptions options) {
        List<ElementType> items = GrailsElementType.values().toList()
        if (options.hasOffset()) {
            items = items.drop(options.offset)
        }
        if (options.hasMax()) {
            items = items.take(options.max)
        }

        new SimplePartialResult<ElementType>(
            items: items,
            total: GrailsElementType.values().size(),
            options: options
        )
    }

    @Override
    int count() {
        GrailsElementType.values().size()
    }

    @Override
    ElementType get(Identifier id) {
        GrailsElementType.byName(id.toExternalForm().toString())
    }

    @Override
    Identifier createId(Serializable externalForm) {
        StringIdentifier.create(externalForm)
    }

    @Override
    boolean save(ElementType item) {
        throw new UnsupportedOperationException("Not yet implemented")
    }

    @Override
    boolean delete(ElementType item) {
        throw new UnsupportedOperationException("Not yet implemented")
    }

    @Override
    boolean isValid(ElementType type) {
        return true
    }

    @Override
    Object getErrors(ElementType item) {
        return null
    }
}
