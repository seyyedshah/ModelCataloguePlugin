package org.modelcatalogue.core.grails

import grails.gorm.DetachedCriteria
import org.modelcatalogue.core.repository.api.PartialResult
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions

class GormPartialResult<T> implements PartialResult<T> {

    static <T> PartialResult<T> create(Class<T> type, SortAndPaginationOptions options){
        create(options, new DetachedCriteria<T>(type))
    }

    static <T> PartialResult<T> create(Class<T> type, SortAndPaginationOptions options, @DelegatesTo(DetachedCriteria) Closure buildClosure){
        create(options, new DetachedCriteria<T>(type).build(buildClosure))
    }

    static <T> PartialResult<T> create(SortAndPaginationOptions options, DetachedCriteria<T> criteria){
        new GormPartialResult<T>(criteria, options)
    }


    final DetachedCriteria<T> criteria
    final SortAndPaginationOptions options

    private Integer total = null
    private List<T> items = null

    protected GormPartialResult(DetachedCriteria<T> criteria, SortAndPaginationOptions options) {
        this.criteria = criteria
        this.options = options
    }

    @Override
    Integer getTotal() {
        if (total == null) {
            return total = criteria.count()
        }
        return total
    }

    static void setTotal(Long ignored) {
        throw new UnsupportedOperationException("Setting total is not supported")
    }

    static void setItems(List<T> ignored) {
        throw new UnsupportedOperationException("Setting items is not supported")
    }

    @Override
    List<T> getItems() {
        if (items == null)  {
            return (items = Collections.unmodifiableList(criteria.list(GormUtils.convertOptions(options))))
        }
        return items
    }



}
