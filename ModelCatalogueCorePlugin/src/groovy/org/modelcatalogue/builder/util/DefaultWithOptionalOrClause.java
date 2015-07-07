package org.modelcatalogue.builder.util;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.modelcatalogue.builder.api.CatalogueBuilder;

public class DefaultWithOptionalOrClause implements WithOptionalOrClause {

    private final CatalogueBuilder builder;

    public DefaultWithOptionalOrClause(CatalogueBuilder builder) {
        this.builder = builder;
    }

    public void or(Closure c) {
        DefaultGroovyMethods.with(builder, c);
    }

    public final CatalogueBuilder getBuilder() {
        return builder;
    }
}
