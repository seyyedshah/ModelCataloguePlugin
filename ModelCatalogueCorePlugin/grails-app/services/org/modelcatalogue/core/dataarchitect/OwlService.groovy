package org.modelcatalogue.core.dataarchitect


/**
 * Seyyed Shah
 */
import org.modelcatalogue.core.Classification
import org.modelcatalogue.core.EnumeratedType
import org.modelcatalogue.builder.api.CatalogueBuilder
import org.modelcatalogue.core.util.builder.DefaultCatalogueBuilder
import groovy.json.internal.LazyMap


import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.OWLOntologyManager
import org.semanticweb.owlapi.model.OWLOntology
import org.semanticweb.owlapi.model.OWLClass
import org.semanticweb.owlapi.model.OWLAnnotationProperty
import org.semanticweb.owlapi.model.OWLAnnotation
import org.semanticweb.owlapi.model.*;
import org.modelcatalogue.core.Model



class OwlService {

    def  classificationService, elementService

    OWLAnnotationProperty labels
    OWLAnnotationProperty comments
    static transactional = false

    protected importOwlOntology(FileInputStream is, String name, Classification classification) {
        //oen file with OWLAPI
        println( "Parsing Owl file for "+name)
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        OWLOntology owlOntology = m.loadOntologyFromOntologyDocument(is);
        //setup basic annotations
        OWLDataFactory df = owlOntology.getOWLOntologyManager().getOWLDataFactory();
        labels = df.getRDFSLabel();
        comments = df.getRDFSComment();


        generateCatalogueElements(owlOntology, classification)
    }



     protected void generateCatalogueElements(OWLOntology owlOntology, Classification clsf) {

        CatalogueBuilder builder = new DefaultCatalogueBuilder(classificationService, elementService)
        builder.build {
            classification(name: clsf.name) {
                for (OWLClass oc: owlOntology.getClassesInSignature()) {
                    createClass(builder, oc, owlOntology)
                }
            }
        }

    }



    protected createClass(CatalogueBuilder builder, OWLClass cls, OWLOntology owlOntology) {
        println("Outputting model: " + cls.getSignature())


        String comment = this.extractAnnotationString(cls, owlOntology, comments);
        String label = this.extractAnnotationString(cls, owlOntology, labels);
        if (label.length() == 0) label = cls.getIRI();


         builder.model(name: label, description: comment, id: cls.getIRI()) {

             //create subclass relationships
             for(OWLClassExpression subclass : cls.getSubClasses(owlOntology))
                 createClass(builder, subclass, owlOntology);

             //create attributes
             for(OWLObjectProperty oop: owlOntology.getObjectPropertiesInSignature()){
                 for (OWLClassExpression domain :oop.getDomains(owlOntology)){
                     if(domain.compareTo(cls)==0){

                         label = this.extractAnnotationString(oop, owlOntology, labels);
                         comment = this.extractAnnotationString(oop, owlOntology, comments);
                         if (label.length() == 0) label = oop.getIRI();

                         //to do, use range
                         println("Outputting Element: " + label )

                         dataElement(name: label, description: comment, id: oop.getIRI()) {

                             builder.valueDomain(name: "xs:string") {
                                 dataType(name: "xs:string")
                             }
                         }

                     }
                 }
             }


             //create relationships
         }


     }

    private static String extractAnnotationString(OWLEntity oe, OWLOntology owlOntology, OWLAnnotationProperty prop){
        String str = "";

        for (OWLAnnotation an :oe.getAnnotations(owlOntology, prop))
            str = an.getValue() ;

        return str.replaceAll (/"/, '');
    }


    static createValueDomain(CatalogueBuilder builder, LazyMap att, OWLOntology owlFile) {

        if (!(att.type instanceof String) && att.type?.$ref && owlFile.allDataTypes.get(att?.type?.$ref)) {
            // Find highest supertype
            def currType = owlFile.allDataTypes.get(att.type?.$ref)
            while (currType.ownedElements?.findAll({ oe -> oe._type.equals("UMLGeneralization") }) != null) {
                currType = owlFile.allDataTypes.get(currType.ownedElements.findAll({ oe -> oe._type.equals("UMLGeneralization") }).get(0).target?.$ref)
            }

            return builder.valueDomain(name: currType.name.toString()) {
                dataType(name: currType.name.toString())
            }

        }

        if (!(att.type instanceof String) && att.type?.$ref && owlFile.allEnumerations.get(att.type?.$ref)) {
            def enumeration = owlFile.allEnumerations.get(att.type?.$ref)
            def enumMap = [:]
            enumeration.literals.each { ev ->
                enumMap.put(ev.name, ev.documentation)
            }

            return builder.valueDomain(name: enumeration.name) {
                dataType(name: enumeration.name, enumerations: enumMap)
            }

        } else if (att.type instanceof String) {
            if (att.type == "") att.type = "xs:string"

            return builder.valueDomain(name: att.type) {
                dataType(name: att.type)
            }
        }
    }



     protected createClasses(CatalogueBuilder builder, LazyMap cls, OWLOntology owlFile, ArrayList<Object> carried_forward_atts = new ArrayList<Object>(), ArrayList<Object> carried_forward_comps = new ArrayList<Object>()) {

        println("Outputting model: " + cls.name)

        def cfa = getAttributes(cls, carried_forward_atts)
        def cfc = getComponents(cls, carried_forward_comps, owlFile)
        def subtypes = getSubTypes(cls, owlFile)
        if (!cls.isAbstract) {
            builder.model(name: cls.name.replaceAll("_", " "), description: cls.documentation) {
                // first output the attributes for this class
                cfa.each { att ->
                    def multiplicity = getMultiplicity(att)

                    dataElement(name: att.name.replaceAll("_", " "), description: att.documentation) {

                        if(att.tags?.value) ext("cosd id", att.tags?.value[0])
                        if(multiplicity.size()>0){
                            relationship {
                                if (multiplicity["minRepeat"]) ext("Min Occurs", multiplicity["minRepeat"])
                                if (multiplicity["maxRepeat"]) ext("Max Occurs", multiplicity["maxRepeat"])
                            }
                        }

                        createValueDomain(builder, att, owlFile)

                    }


                }
                println("No. of components: " + cfc.size())
                cfc.each { component ->
                    createClasses(builder, component, owlFile)
                }
            }

        } else {
            println("Abstract class: " + cls.name);
        }

        subtypes.each { subtype ->
            createClasses(builder, subtype.value, owlFile, cfa, cfc)
        }

    }




    protected static getAttributes(cls, carried_forward_atts){
        def cfa = new ArrayList<Object>()
        cfa.addAll(carried_forward_atts)
        if(cls.attributes)cfa.addAll(cls.attributes)
        return cfa
    }

    protected static getComponents(cls, carried_forward_comps, owlFile){
        def cfc = new ArrayList<Object>()
        cfc.addAll(carried_forward_comps)
        def components = owlFile.allClasses.findAll{
            id, component -> component.ownedElements.findAll{
                ct ->
                    ct._type.equals("UMLAssociation") && ct.end2.aggregation.equals("composite") && ct.end2.reference?.$ref?.equals(cls._id)
            }.size() > 0

        }

        if(components) components.each { id, component -> cfc.add(component) }

        return cfc

    }

    protected static getMultiplicity(att){

        def multiplicity = [:]

        switch (att.multiplicity){
            case "1":
                multiplicity["minRepeat"] = "1";
                multiplicity["maxRepeat"] = "1";
                break;
            case "0..1":
                multiplicity["minRepeat"] = "0";
                multiplicity["maxRepeat"] = "1";
                break;
            case "0..*":
                multiplicity["minRepeat"] = "0";
                multiplicity["maxRepeat"] = "unbounded";
                break;
            case "1..*":
                multiplicity["minRepeat"] = "1";
                multiplicity["maxRepeat"] = "unbounded";
                break;

            default:
                println("unknown multiplicity: " + att.multiplicity);

        }

        return multiplicity

    }



    protected static getSubTypes(cls, owlFile){
        return owlFile.allClasses.findAll{
            id, subtype -> subtype.ownedElements.findAll{
                oe ->
                    oe._type?.equals("UMLGeneralization") && oe?.target?.$ref?.equals(cls._id)
            }.size() > 0
        }
    }


    static String quote(String s) {
        if (s == null) return null
        String ret = s
        EnumeratedType.QUOTED_CHARS.each { original, replacement ->
            ret = ret.replace(original, replacement)
        }
        ret
    }


}