package org.modelcatalogue.core

import grails.util.GrailsNameUtils
import org.modelcatalogue.core.api.ElementType
import org.modelcatalogue.core.api.RelationshipDirection
import org.modelcatalogue.core.security.User

enum ElementTypes implements ElementType {

    ASSET(Asset),
    USER(User),

    CLASSIFICATION(Classification) {

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getPublishDependencies() {
            [(RelationshipDirection.OUTGOING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([RelationshipType.classificationType])]
        }
    },

    MODEL(Model) {

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getDraftDependencies() {
            [(RelationshipDirection.INCOMING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([
                    RelationshipType.classificationType,
                    RelationshipType.hierarchyType
            ])]
        }

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getPublishDependencies() {
            [(RelationshipDirection.OUTGOING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([
                    RelationshipType.containmentType,
                    RelationshipType.hierarchyType
            ])]
        }
    },

    DATA_ELEMENT(DataElement){

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getDraftDependencies() {
            [(RelationshipDirection.INCOMING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([
                    RelationshipType.classificationType
            ])]
        }

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getPublishDependencies() {
            [(RelationshipDirection.INCOMING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([
                    RelationshipType.valueDomainType
            ])]
        }
    },

    VALUE_DOMAIN(ValueDomain){

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getDraftDependencies() {
            [(RelationshipDirection.INCOMING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([
                    RelationshipType.valueDomainType
            ])]
        }

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getPublishDependencies() {
            [(RelationshipDirection.INCOMING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([
                    RelationshipType.unitOfMeasureType,
                    RelationshipType.dataTypeType
            ])]
        }
    },

    DATA_TYPE(DataType){

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getDraftDependencies() {
            [(RelationshipDirection.INCOMING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([
                    RelationshipType.dataTypeType
            ])]
        }
    },

    MEASUREMENT_UNIT(MeasurementUnit) {

        @Override
        Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getDraftDependencies() {
            [(RelationshipDirection.INCOMING): new HashSet<org.modelcatalogue.core.api.RelationshipType>([
                    RelationshipType.unitOfMeasureType
            ])]
        }
    }

    private static Map<Class, ElementType> typesByClass
    private static Map<String, ElementType> typesByName

    static ElementType getByName(String name) {
        typesByName[name]
    }

    static ElementType getByClass(Class clazz) {
        typesByClass[clazz]
    }

    private final Class implementation

    ElementTypes(Class implementation) {
        if (!typesByClass) {
            typesByClass = new HashMap<Class, ElementType>()
        }

        if(!typesByName) {
            typesByName = new HashMap<String, ElementType>()
        }

        this.implementation = implementation == EnumeratedType ? DataType : implementation
        typesByClass[this.implementation] = this
        typesByName[getName()] = this
        typesByName[this.implementation.name] = this

    }



    @Override final boolean isInstanceOf(org.modelcatalogue.core.api.CatalogueElement element) {
        if (element instanceof CatalogueElement) {
            return element.instanceOf(implementation)
        }
        return false
    }

    @Override final String getName() {
        return GrailsNameUtils.getPropertyName(implementation)
    }
    @Override Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getDraftDependencies() {
        return [:]
    }

    @Override Map<RelationshipDirection, Set<org.modelcatalogue.core.api.RelationshipType>> getPublishDependencies() {
        return [:]
    }



}
