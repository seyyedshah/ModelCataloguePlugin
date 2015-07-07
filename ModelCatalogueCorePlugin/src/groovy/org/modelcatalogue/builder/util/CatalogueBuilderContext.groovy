package org.modelcatalogue.builder.util

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import org.modelcatalogue.builder.api.CatalogueBuilder
import org.modelcatalogue.builder.api.RelationshipConfiguration
import org.modelcatalogue.core.api.Catalogue
import org.modelcatalogue.core.api.CatalogueElement
import org.modelcatalogue.core.api.ElementType
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions

@CompileStatic
class CatalogueBuilderContext {

    private List<Map<ElementType, ContextItem>> contexts = []

    private final CatalogueBuilder builder
    private final Catalogue catalogue
    private final ElementType defaultElementType

    CatalogueBuilderContext(Catalogue catalogue, CatalogueBuilder builder, ElementType defaultElementType) {
        this.catalogue = catalogue
        this.builder = builder
        this.defaultElementType = defaultElementType
    }

    void clear() {
        contexts.clear()
    }

    void withNewContext(CatalogueElement contextElement, @DelegatesTo(CatalogueBuilder) Closure c) {
        pushContext()
        setContextElement(contextElement)
        builder.with c
        popContext()
    }

    /**
     * Executes closure with context element of given type if present.
     * @param contextElementType
     * @param closure
     */
    WithOptionalOrClause withContextElement(ElementType contextElementType, @DelegatesTo(CatalogueBuilder) @ClosureParams(value=FromString, options = ['org.modelcatalogue.core.util.builder.CatalogueElementProxy<T>', 'org.modelcatalogue.core.util.builder.CatalogueElementProxy<T>,Closure']) Closure closure) {
        ContextItem contextElement = getContextElement(contextElementType)
        if (contextElement) {
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure.delegate = builder
            if (closure.maximumNumberOfParameters == 2) {
                closure(contextElement.element, contextElement.relationshipConfiguration)
                // relationship configuration can only be used one
                contextElement.relationshipConfiguration = null
            } else {
                closure(contextElement.element)
            }

            return WithOptionalOrClause.NOOP
        }
        new DefaultWithOptionalOrClause(builder)
    }

    void configureCurrentRelationship(@DelegatesTo(RelationshipConfiguration) Closure relationshipExtensionsConfiguration) {
        ContextItem item = getContextElement(defaultElementType, 1)
        if (item) {
            if (item.relationshipConfiguration) {
                item.relationshipConfiguration = item.relationshipConfiguration << relationshipExtensionsConfiguration
            } else {
                item.relationshipConfiguration = relationshipExtensionsConfiguration
            }
        }
    }

    private void pushContext() {
        contexts.push([:])
    }

    private void popContext() {
        contexts.pop()
    }

    private CatalogueElement setContextElement(CatalogueElement contextElement) {
        if (!contextElement) {
            return contextElement
        }
        ContextItem item = new ContextItem(contextElement, null)
        for (ElementType type in catalogue.elementTypeRepository.list(SortAndPaginationOptions.noOptions()).items) {
            if (type.isInstanceOf(contextElement)) {
                contexts.last()[type] = item
            }
        }
        contextElement
    }

    private <T extends CatalogueElement> ContextItem getContextElement(ElementType contextElementType = defaultElementType, int skip = 0) {
        int skipped = 0
        for (Map<ElementType, ContextItem> context in contexts.reverse()) {
            if (skipped++ < skip) {
                continue
            }
            ContextItem result = context[contextElementType]
            if (result) {
                return result
            }
        }
        return null
    }

}