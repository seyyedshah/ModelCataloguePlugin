package org.modelcatalogue.core.grails

import grails.util.Holders
import org.modelcatalogue.core.api.Catalogue
import org.modelcatalogue.core.api.CatalogueFactory

class GrailsCatalogueFactory extends CatalogueFactory {

    @Override protected Catalogue createCatalogue() {
        return Holders.applicationContext.getBean(Catalogue)
    }
}
