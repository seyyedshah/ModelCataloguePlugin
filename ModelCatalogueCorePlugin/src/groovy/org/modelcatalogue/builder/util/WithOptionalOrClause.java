package org.modelcatalogue.builder.util;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.modelcatalogue.builder.api.CatalogueBuilder;

public interface WithOptionalOrClause {
    WithOptionalOrClause NOOP = new WithOptionalOrClause() {
        @Override public void or(@DelegatesTo(CatalogueBuilder.class) Closure orClosure) {}
    };

    void or(@DelegatesTo(CatalogueBuilder.class) Closure orClosure);
}
