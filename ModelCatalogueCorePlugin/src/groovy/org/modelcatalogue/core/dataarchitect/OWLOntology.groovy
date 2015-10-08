package org.modelcatalogue.core.dataarchitect

import org.modelcatalogue.core.DataElement
import org.modelcatalogue.core.DataType
import org.modelcatalogue.core.EnumeratedType
import org.modelcatalogue.core.Model
import org.modelcatalogue.core.ValueDomain

/**
 * Seyyed Shah
 */
import java.lang.String.CaseInsensitiveComparator;
import java.lang.invoke.SwitchPoint;

import org.semanticweb.owlapi.model.*


class OWLOntology {

//OWLOntology object
    Object owl;

    HashMap<String, Object> allClasses, topLevelClasses, allDataTypes, allEnumerations;

    OWLOntology(owl) {

        // We'll also add each class to a list of classes
        this.owl = owl
        allClasses = new HashMap<String, Object>()
        allDataTypes = new HashMap<String, Object>()
        allEnumerations = new HashMap<String, Object>()
        getAllClasses(owl)
    }

    void getAllClasses(element) {

    }





}