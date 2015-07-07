package org.modelcatalogue.core.grails

import org.modelcatalogue.core.Asset
import org.modelcatalogue.core.Classification
import org.modelcatalogue.core.DataElement
import org.modelcatalogue.core.DataType
import org.modelcatalogue.core.EnumeratedType
import org.modelcatalogue.core.MeasurementUnit
import org.modelcatalogue.core.Model
import org.modelcatalogue.core.ValueDomain
import org.modelcatalogue.core.api.CatalogueElement
import org.modelcatalogue.core.api.ElementType
import org.modelcatalogue.core.api.Identifier
import org.modelcatalogue.core.api.RelationshipDirection
import org.modelcatalogue.core.api.RelationshipType
import org.modelcatalogue.core.repository.api.StringIdentifier
import org.modelcatalogue.core.security.User

enum GrailsElementType implements ElementType {

    ASSET("asset", Asset),
    USER("user", User),
    CLASSIFICATION("classification", Classification),
    MODEL("model", Model),
    DATA_ELEMENT("dataElement", DataElement),
    VALUE_DOMAIN("valueDomain", ValueDomain),
    MEASUREMENT_UNIT("measurementUnit", MeasurementUnit),
    DATA_TYPE("dataType", DataType),
    ENUMERATED_TYPE("enumeratedType", EnumeratedType);

    private static Map<Class, GrailsElementType> CLASSES_TO_TYPES = new HashMap<Class, ElementType>();
    private static Map<String, GrailsElementType> NAMES_TO_TYPES = new TreeMap<String, ElementType>();


    public static ElementType getType(Class clazz) {
        initialize()
        return CLASSES_TO_TYPES.get(clazz);
    }

    private final String label;
    private final Class implementation;

    GrailsElementType(String label, Class implementation) {
        this.label = label;
        this.implementation = implementation
    }

    Class getImplementation() {
        return implementation;
    }

    static GrailsElementType byName(String name) {
        initialize()
        NAMES_TO_TYPES[name]
    }

    static initialize() {
        if (CLASSES_TO_TYPES && NAMES_TO_TYPES) {
            return
        }
        for (GrailsElementType type in values()) {
            CLASSES_TO_TYPES.put(type.implementation, type);
            NAMES_TO_TYPES.put(type.label, type);
        }

    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    String getName() {
        return label;
    }

    @Override
    boolean isNamespace() {
        CLASSIFICATION == this
    }

    @Override
    boolean isInstanceOf(CatalogueElement element) {
        implementation.isAssignableFrom(NAMES_TO_TYPES[element.elementType.name]?.implementation)
    }

    @Override
    Map<RelationshipDirection, Set<RelationshipType>> getDraftDependencies() {
        throw new UnsupportedOperationException("Not yet implemented")
    }

    @Override
    Map<RelationshipDirection, Set<RelationshipType>> getPublishDependencies() {
        throw new UnsupportedOperationException("Not yet implemented")
    }

    Identifier getIdentifier() {
        return StringIdentifier.create(label)
    }

}