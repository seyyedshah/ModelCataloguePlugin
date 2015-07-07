package org.modelcatalogue.core.grails

import org.modelcatalogue.core.repository.api.PartialResult
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions

class SimplePartialResult<T> implements PartialResult<T> {

    List<T> items
    Integer total
    SortAndPaginationOptions options

}
