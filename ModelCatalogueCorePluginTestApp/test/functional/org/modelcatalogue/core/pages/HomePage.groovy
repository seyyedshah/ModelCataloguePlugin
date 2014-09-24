package org.modelcatalogue.core.pages

import geb.Page

class HomePage extends Page {

    static url = "#/catalogue/model/all"

    static at = {
        url == "#/catalogue/model/all" &&
		title == "Models"
    }
}
