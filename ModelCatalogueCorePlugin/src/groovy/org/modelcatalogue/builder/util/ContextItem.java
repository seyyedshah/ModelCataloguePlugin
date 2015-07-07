package org.modelcatalogue.builder.util;

import groovy.lang.Closure;
import org.modelcatalogue.core.api.CatalogueElement;

public class ContextItem {

    private CatalogueElement element;
    private Closure relationshipConfiguration;

    public ContextItem(CatalogueElement element, Closure relationshipConfiguration) {
        this.element = element;
        this.relationshipConfiguration = relationshipConfiguration;
    }

    public CatalogueElement getElement() {
        return element;
    }

    public void setElement(CatalogueElement element) {
        this.element = element;
    }

    public Closure getRelationshipConfiguration() {
        return relationshipConfiguration;
    }

    public void setRelationshipConfiguration(Closure relationshipConfiguration) {
        this.relationshipConfiguration = relationshipConfiguration;
    }
}
