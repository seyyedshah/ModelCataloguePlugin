package org.modelcatalogue.core.grails

import org.modelcatalogue.core.Classification
import org.modelcatalogue.core.repository.api.NamespaceFilter
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions
import org.modelcatalogue.core.util.ClassificationFilter;

class GormUtils {

    static Map<String, Object> convertOptions(SortAndPaginationOptions options) {
        Map<String, Object> params = [:]

        if (options.hasOffset()) {
            params.offset = options.offset
        }

        if (options.hasMax()) {
            params.max = options.max
        }

        if (options.hasOrder()) {
            params.sort = options.sort
        }

        if (options.hasOrder()) {
            params.order = options.order
        }

        params
    }

    static ClassificationFilter convertFilter(NamespaceFilter filter) {
        if (filter.unclassifiedOnly) {
            return ClassificationFilter.create(true)
        }

        return ClassificationFilter.create(filter.includes.collect { it as Classification }, filter.excludes.collect { it as Classification })
    }
}
