package org.modelcatalogue.core.grails

import org.modelcatalogue.core.api.Identifier
import org.modelcatalogue.core.api.Persistable
import org.modelcatalogue.core.repository.api.PartialResult
import org.modelcatalogue.core.repository.api.Repository
import org.modelcatalogue.core.repository.api.SortAndPaginationOptions

class AbstractGrailsRepository<P extends Persistable, T extends P> implements Repository<P> {

    protected final Class<P> domain;

    AbstractGrailsRepository(Class<P> domain) {
        this.domain = domain
    }

    P findByName(String name, SortAndPaginationOptions options) {
        domain.findByName(name, GormUtils.convertOptions(options))
    }

    @Override
    PartialResult<P> list(SortAndPaginationOptions options) {
        GormPartialResult.create(domain, options)
    }

    @Override
    int count() {
        GormPartialResult.create(domain, SortAndPaginationOptions.noOptions()).total
    }

    @Override
    P get(Identifier id) {
        domain.get(id.toExternalForm())
    }

    @Override
    Identifier createId(Serializable externalForm) {
        return LongIdentifier.create(externalForm)
    }

    @Override
    boolean save(P item) {
        if (domain.isAssignableFrom(item.class)) {
            T type = (T) item;
            return type.save()
        }
        return false
    }

    @Override
    boolean delete(P item) {
        if (domain.isAssignableFrom(item.class)) {
            T type = (T) item;
            try {
                type.delete()
                return true
            } catch(ignored) {
                return false
            }
        }
        return false
    }

    @Override
    boolean isValid(P item) {
        if (domain.isAssignableFrom(item.class)) {
            T type = (T) item;
            return type.validate(deepValidate: false)
        }
        return false
    }

    @Override
    Object getErrors(P item) {
        if (domain.isAssignableFrom(item.class)) {
            T type = (T) item;
            type.validate(deepValidate: false)
            return type.errors
        }
        return null
    }

    // CLOCK: 2:15 | start: 20:30
}
