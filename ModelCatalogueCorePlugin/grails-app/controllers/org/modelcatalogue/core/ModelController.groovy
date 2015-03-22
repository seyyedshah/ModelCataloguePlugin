package org.modelcatalogue.core

import groovy.xml.MarkupBuilder
import org.modelcatalogue.core.util.Lists

class ModelController extends AbstractCatalogueElementController<Model> {

    def modelService

    ModelController() {
        super(Model, false)
    }

    @Override
    def index(Integer max) {
        if (!params.boolean("toplevel")) {
            return super.index(max)
        }
        handleParams(max)

        respond Lists.wrap(params, "/${resourceName}/", modelService.getTopLevelModels(params))
    }

    def schema(){
       def model = queryForResource(params.id)
       modelService.printModels(model)
       respond model
    }

}
