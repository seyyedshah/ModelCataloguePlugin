package org.modelcatalogue.core.grails

import grails.gorm.DetachedCriteria
import org.modelcatalogue.core.ClassificationService
import org.modelcatalogue.core.api.CatalogueElement
import org.modelcatalogue.core.api.ElementStatus
import org.modelcatalogue.core.api.Identifier
import org.modelcatalogue.core.repository.api.CatalogueElementRepository
import org.modelcatalogue.core.repository.api.CatalogueElementsPartialResult
import org.modelcatalogue.core.repository.api.CatalogueElementsPartialResultDecorator
import org.modelcatalogue.core.repository.api.NamespaceFilter
import org.modelcatalogue.core.repository.api.PartialResult
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions

class GrailsCatalogueElementRepository extends AbstractGrailsRepository<CatalogueElement, org.modelcatalogue.core.CatalogueElement> implements CatalogueElementRepository {

    final GrailsElementType elementType
    final NamespaceFilter filter

    GrailsCatalogueElementRepository(GrailsElementType elementType) {
        super(elementType ? elementType.implementation : org.modelcatalogue.core.CatalogueElement)
        this.elementType = elementType
        this.filter = NamespaceFilter.create()
    }

    private GrailsCatalogueElementRepository(GrailsElementType elementType, NamespaceFilter filter) {
        super(elementType ? elementType.implementation : org.modelcatalogue.core.CatalogueElement)
        this.elementType = elementType
        this.filter = filter
    }


    @Override
    CatalogueElement findByModelCatalogueId(String modelCatalogueId, SortAndPaginationOptions options) {
        DetachedCriteria<CatalogueElement> criteria = prepareCriteria().build {
            eq 'modelCatalogueId', modelCatalogueId
        }
        criteria.get(GormUtils.convertOptions(options))
    }


    CatalogueElement findByName(String name, SortAndPaginationOptions options) {
        DetachedCriteria<CatalogueElement> criteria = prepareCriteria().build {
            eq 'name', name
        }
        criteria.get(GormUtils.convertOptions(options))
    }

    private DetachedCriteria<CatalogueElement> prepareCriteria() {
        if (filtered) {
            return ClassificationService.classified(new DetachedCriteria<CatalogueElement>(domain), GormUtils.convertFilter(filter))
        }
        return new DetachedCriteria<CatalogueElement>(domain)
    }

    @Override
    CatalogueElement findByExtension(String key, String value, SortAndPaginationOptions options) {
        DetachedCriteria<CatalogueElement> criteria = prepareCriteria().build {
            extensions {
                eq 'name', key
                eq 'extensionValue', value
            }
        }
        criteria.get(GormUtils.convertOptions(options))
    }

    @Override
    CatalogueElementsPartialResult findAllByName(String name, SortAndPaginationOptions options) {
        DetachedCriteria<CatalogueElement> criteria = prepareCriteria().build {
            eq 'name', name
        }
        CatalogueElementsPartialResultDecorator.create(elementType, GormPartialResult.create(options, criteria))
    }

    @Override
    CatalogueElementsPartialResult findAllByStatus(ElementStatus status, SortAndPaginationOptions options) {
        DetachedCriteria<CatalogueElement> criteria = prepareCriteria().build {
            eq 'status', status
        }
        CatalogueElementsPartialResultDecorator.create(elementType, GormPartialResult.create(options, criteria))
    }

    @Override
    CatalogueElementsPartialResult findAllByStatusAndRootId(ElementStatus status, Identifier rootId, SortAndPaginationOptions options) {
        DetachedCriteria<CatalogueElement> criteria = prepareCriteria().build {
            eq 'status', status
            or {
                eq 'latestVersionId', rootId.toExternalForm()
                eq 'id', rootId.toExternalForm()
            }
        }
        CatalogueElementsPartialResultDecorator.create(elementType, GormPartialResult.create(options, criteria))
    }

    @Override
    CatalogueElementsPartialResult findAllByRootId(Identifier rootId, SortAndPaginationOptions options) {
        DetachedCriteria<CatalogueElement> criteria = prepareCriteria().build {
            or {
                eq 'latestVersionId', rootId.toExternalForm()
                eq 'id', rootId.toExternalForm()
            }
        }
        CatalogueElementsPartialResultDecorator.create(elementType, GormPartialResult.create(options, criteria))
    }


    @Override
    boolean isFiltered() {
        filter.excludes || filter.includes || filter.unclassifiedOnly
    }

    @Override
    CatalogueElementRepository filter(NamespaceFilter filter) {
        new GrailsCatalogueElementRepository(elementType, filter)
    }

    @Override
    PartialResult<CatalogueElement> list(SortAndPaginationOptions options) {
        CatalogueElementsPartialResultDecorator.create(elementType, GormPartialResult.create(options, prepareCriteria()))
    }

    @Override
    int count() {
        CatalogueElementsPartialResultDecorator.create(elementType, GormPartialResult.create(SortAndPaginationOptions.noOptions(), prepareCriteria())).total
    }



}
