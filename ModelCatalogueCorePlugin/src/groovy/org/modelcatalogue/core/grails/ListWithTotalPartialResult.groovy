package org.modelcatalogue.core.grails

import org.modelcatalogue.core.api.Persistable
import org.modelcatalogue.core.repository.api.PartialResult
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions
import org.modelcatalogue.core.util.ListWithTotal

class ListWithTotalPartialResult<I extends Persistable> implements PartialResult<I> {

    private final ListWithTotal<I> delegate

    final SortAndPaginationOptions options

    ListWithTotalPartialResult(ListWithTotal<I> delegate, SortAndPaginationOptions options) {
        this.delegate = delegate
        this.options = options
    }

    @Override
    List<I> getItems() {
        return delegate.items
    }

    @Override
    Integer getTotal() {
        return delegate.total
    }

    @Override
    SortAndPaginationOptions getOptions() {
        return options
    }
}
