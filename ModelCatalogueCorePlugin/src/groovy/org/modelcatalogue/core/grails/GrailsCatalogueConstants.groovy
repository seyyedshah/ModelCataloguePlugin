package org.modelcatalogue.core.grails

import org.modelcatalogue.core.repository.api.CatalogueConstants

enum GrailsCatalogueConstants implements CatalogueConstants {

    INSTANCE;

    @Override
    String getAttributeNameName() {
        'name'
    }

    @Override
    String getAttributeModelCatalogueIdName() {
        'modelCatalogueId'
    }

    @Override
    String getAttributeStatusName() {
        'status'
    }

    @Override
    String getAttributeRootIdName() {
        'latestVersionId'
    }

    @Override
    String getAttributeVersionName() {
        'versionNumber'
    }
}
