package org.modelcatalogue.core.testapp

class Role {

    String authority

    static mapping = {
    }

    static constraints = {
        authority blank: false, unique: true
    }
}
