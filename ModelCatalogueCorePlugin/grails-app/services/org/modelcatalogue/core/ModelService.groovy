package org.modelcatalogue.core

import grails.transaction.Transactional
import groovy.xml.MarkupBuilder
import org.modelcatalogue.core.util.ListCountAndType
import org.modelcatalogue.core.util.ListWithTotalAndType
import org.modelcatalogue.core.util.Lists

@Transactional
class ModelService {

    SecurityService modelCatalogueSecurityService

    Classification xmlSchema
    Set<ValueDomain> valueDomains = new LinkedHashSet<ValueDomain>()

    ListWithTotalAndType<Model> getTopLevelModels(Map params) {
        getTopLevelModels(modelCatalogueSecurityService.currentUser?.filteredBy, params)
    }

    ListWithTotalAndType<Model> getTopLevelModels(List<Classification> classifications, Map params) {
        RelationshipType hierarchy      = RelationshipType.hierarchyType
        ElementStatus status            = ElementService.getStatusFromParams(params)
        RelationshipType classification = RelationshipType.classificationType


        if (classifications) {
            // language=HQL
            Lists.fromQuery params, Model, """
            select distinct m
            from Model as m join m.incomingRelationships as rel
            where m.status = :status
                and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                and rel.source in (:classifications)
                and rel.relationshipType = :classificationType
            group by m.name, m.id
            order by m.name
        ""","""
            select count(m.id)
            from Model as m join m.incomingRelationships as rel
            where m.status = :status
                and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                and rel.source in (:classifications)
                and rel.relationshipType = :classificationType
        """, [type: hierarchy, status: status, classifications: classifications, classificationType: classification ]
        } else {
            // language=HQL
            Lists.fromQuery params, Model, """
            select distinct m
            from Model m
            where m.status = :status and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
            group by m.name, m.id
            order by m.name
        ""","""
            select count(m.id)
            from Model m
            where m.status = :status and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
        """, [type: hierarchy, status: status]
        }
    }

    ListWithTotalAndType<Model> getSubModels(Model model) {
        List<Model> models = listChildren(model)
        new ListCountAndType<Model>(count: models.size(), list: models, itemType: Model)

    }

    ListWithTotalAndType<DataElement> getDataElementsFromModels(List<Model> models){
        def results = []
        models.each{ model ->
            results.addAll(model.contains)
        }
        new ListCountAndType<DataElement>(count: results.size(), list: results, itemType: DataElement)
    }


    protected List<Model> listChildren(Model model, results = []){
            if (model && !results.contains(model)) {
                    results += model
                    model.parentOf?.each { child ->
                        results += listChildren(child, results)
                    }
            }
            results.unique()
    }



    def printModels(Model targetModel) {
        StringWriter writer = new StringWriter()
        xmlSchema = Classification.findByName("XMLSchema")
        def xml = new MarkupBuilder(writer)
        def childRelations = targetModel.getOutgoingRelationshipsByType(RelationshipType.hierarchyType)
        def subModels = listChildren(targetModel)

        xml.'xs:schema'('xmlns:xs': 'http://www.w3.org/2001/XMLSchema', 'xmlns:vc': 'http://www.w3.org/2007/XMLSchema-versioning', 'xmlns:gel': 'https://genomicsengland.co.uk/xsd/', 'vc:minVersion': '1.1') {
            'xs:annotation'{
                'xs:documentation'{
                    'test documentation'
                }
            }

//            "xs:element"(name:targetModel.name){
//                'xs:complexType'{
//                    'xs:sequence'{
//                        childRelations.each { Relationship relationship ->
//                            printElements(xml, relationship.destination, relationship.ext.get("Min Occurs"), relationship.ext.get("Min Occurs"))
//                        }
//
//                    }
//                }
//            }
            subModels.each { Model model ->
               printComplexType(xml, model)
            }


//            "$targetModel.name"() {
//
//            }
        }

        println(writer.toString())

    }

    protected printComplexType(MarkupBuilder xml, Model model){

        return xml.'xs:complexType'(name: printXSDFriendlyString(model.name)){
            'xs:sequence'{

                model.getOutgoingRelationshipsByType(RelationshipType.containmentType).each { Relationship relationship ->
                    printDataElements(xml, relationship.destination, relationship.ext.get("Min Occurs"), relationship.ext.get("Max Occurs"))
                }

                model.getOutgoingRelationshipsByType(RelationshipType.hierarchyType).each { Relationship relationship ->
                    printModelElements(xml, relationship.destination, relationship.ext.get("Min Occurs"), relationship.ext.get("Max Occurs"))
                }
            }
        }

    }

    protected printModelElements(MarkupBuilder xml, Model model, String minOccurs, String maxOccurs){


        return xml.'xs:element'(name: printXSDFriendlyString(model.name), type: printXSDFriendlyString(model.name), minOccurs: defaultMinOccurs(minOccurs), maxOccurs: defaultMaxOccurs(maxOccurs)){

        }

    }

    protected printDataElements(MarkupBuilder xml, DataElement dataElement, String minOccurs, String maxOccurs){
        def valueDomain = dataElement?.valueDomain
        def type
        if(!valueDomain){
            type = "xs:string"
            return printDataElementSchemaType(xml, dataElement, type, minOccurs, maxOccurs)
        }else{
            if(valueDomain.classifications.contains(xmlSchema)){
                type = valueDomain.name
                return printDataElementSchemaType(xml, dataElement, type, minOccurs, maxOccurs)
            }else{
                valueDomains.add(valueDomain)
                return printDataElementSimpleType(xml, dataElement, type, minOccurs, maxOccurs)
            }
        }


    }

    protected printDataElementSchemaType(MarkupBuilder xml, DataElement dataElement, String type, String minOccurs = "0", String maxOccurs = "unbounded"){
        return xml.'xs:element'(name: printXSDFriendlyString(dataElement.name), minOccurs: defaultMinOccurs(minOccurs), maxOccurs: defaultMaxOccurs(maxOccurs)){
            'xs:annotation'{
                'xs:documentation'{
                    'xs:description' {
                        dataElement.description
                    }
                }
            }
            'xs:complexType'{
                'xs:simpleContent'{
                    'xs:extension'(base: type){
                        'xs:attribute'(name: 'm', use: 'optional', type: "xs:date")
                    }
                }
            }
        }
    }

    protected printDataElementSimpleType(MarkupBuilder xml, DataElement dataElement, String type, String minOccurs = "0", String maxOccurs = "unbounded"){

    }

    protected printXSDFriendlyString(String string){
        return string.replaceAll(" ", "-").toLowerCase()
    }

    protected defaultMinOccurs(String min){
        if(min==null) min = '0'
        return min
    }

    protected defaultMaxOccurs(String max){
        if(max==null) max = 'unbounded'
        return max
    }



    def gelMasterXML(Model model){
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        builder.context('xmlns:xsi':"http://www.w3.org/2001/XMLSchema-instance", 'xsi:noNamespaceSchemaLocation':"../../../Transformations/form.xsd"){
            setOmitEmptyAttributes(true)
            setOmitNullAttributes(true)
            dataset('fileLocation':"../GEL_RD_Master.xml", 'name':"Master")
            form(id:"${printXSDFriendlyString(model.name)}") {
                name model.name
                instructions model.ext.instructions
                version {
                    major model.versionNumber
                    minor 0
                    patch 0
                }
                versionDescription 'Alpha version'
                revisionNotes 'Alpha version'
                formTitle printXSDFriendlyString(model.name)
                formInitials printXSDFriendlyString(model.name)
                model.outgoingRelationships.each { Relationship rel ->
                    if (rel.relationshipType == RelationshipType.containmentType) this.printQuestion(rel.destination, rel.ext, builder)
                    if (rel.relationshipType == RelationshipType.hierarchyType) this.printSection(rel.destination, rel.ext, builder)
                }
            }
        }
        println(writer.toString())
    }

    def gelDiseaseModelPhenotypes(){
        Long id = params.long('modelId') ?: params.long('id')
        Model model = Model.get(id)
        def builder = new MarkupBuilder()
        builder."rare-diseases" ('xmlns:xsi':"http://www.w3.org/2001/XMLSchema-instance", 'xsi:noNamespaceSchemaLocation':"../Schemas/DiseaseOntology.xsd"){
            setOmitEmptyAttributes(true)
            setOmitNullAttributes(true)
            model.outgoingRelationships.each{ Relationship rel ->
                if(rel.relationshipType==RelationshipType.hierarchyType) this.printDiseaseGroup(rel.destination, builder)
            }
        }

        return "test"
    }

    def printDiseaseGroup(Model model, MarkupBuilder builder){
        return builder."disease-group"(name: model.name, id: model.ext.get("OBO ID")) {
            setOmitEmptyAttributes(true)
            setOmitNullAttributes(true)
            model.outgoingRelationships.each { Relationship rel ->
                if (rel.relationshipType == RelationshipType.hierarchyType) this.printSubGroup(rel.destination, builder)
            }
        }
    }

    def printSubGroup(Model model, MarkupBuilder builder){
        return builder."sub-group"(name: model.name, id: model.ext.get("OBO ID")) {
            setOmitEmptyAttributes(true)
            setOmitNullAttributes(true)
            model.outgoingRelationships.each { Relationship rel ->
                if (rel.relationshipType == RelationshipType.hierarchyType && rel.ext.get("phenotypes") != "shallow-phenotypes") {
                    this.printSpecificDisorder(rel.destination, builder)
                } else {
                    "shallow-phenotypes" {
                        rel.destination.parentOf.each { md ->
                            this.printPhenotypes(md, builder)
                        }
                    }
                }
            }
        }
    }

    def printSpecificDisorder(Model model, MarkupBuilder builder){

        return builder."specific-disorder"(name: model.name, id: model.ext.get("OBO ID")) {
            setOmitEmptyAttributes(true)
            setOmitNullAttributes(true)
            "shallow-phenotypes" {
                model.outgoingRelationships.each { Relationship rel ->
                    rel.destination.parentOf.each { md ->
                        this.printPhenotypes(md, builder)
                    }
                }
            }
        }
    }

    def printPhenotypes(Model model, MarkupBuilder builder){
        return builder."hpo-phenotype"(name: model.name, id: model.ext.get("OBO ID"), "hpo-build":"")
    }

    def printSection(Model model, Map ext, MarkupBuilder builder){
        if(model.ext.repeating=='true') {

            return builder.repeatingGroup(id: printXSDFriendlyString(model.name), minOccurs: ext.get("Min Occurs"), maxOccurs: ext.get("Max Occurs")) {
                setOmitEmptyAttributes(true)
                setOmitNullAttributes(true)
                name model.name

                model.outgoingRelationships.each { Relationship rel ->
                    if (rel.relationshipType == RelationshipType.containmentType) this.printQuestion(rel.destination, rel.ext, builder)
                    if (rel.relationshipType == RelationshipType.hierarchyType) this.printSection(rel.destination, rel.ext, builder)
                }
            }

        }else{

            return builder.section(id: model.name, minRepeat: ext.get("Min Occurs"), maxRepeat: ext.get("Max Occurs")) {
                setOmitEmptyAttributes(true)
                setOmitNullAttributes(true)
                name model.name
                instructions model.ext.instructions

                model.outgoingRelationships.each { Relationship rel ->
                    if (
                    rel.relationshipType == RelationshipType.containmentType) this.printQuestion(rel.destination, rel.ext, builder)
                    if (rel.relationshipType == RelationshipType.hierarchyType) this.printSection(rel.destination, rel.ext, builder)
                }
            }

        }
    }

    def printQuestion(DataElement dataElement, Map ext, MarkupBuilder builder){
        return builder.question(id: "R_${dataElement.id}", minRepeat: ext.get("Min Occurs"), maxRepeat: ext.get("Max Occurs")){
            setOmitEmptyAttributes(true)
            setOmitNullAttributes(true)
            name dataElement.name
            text  dataElement.ext.text
            instructions dataElement.description

            if(dataElement.ext.serviceLookupName){
                'service-lookup'(id: dataElement.ext.serviceLookupId, style: dataElement.ext.serviceLookupStyle){
                    name dataElement.ext.serviceLookupName
                }
            }

            if(dataElement?.valueDomain?.dataType instanceof EnumeratedType) {
                enumeration(id:printXSDFriendlyString(dataElement.valueDomain.name), style:dataElement.valueDomain.ext.style){
                    dataElement.valueDomain.dataType.enumerations.each{ key, val ->
                        value (control: key, val)
                    }
                }
            }else{
                if(dataElement?.valueDomain?.dataType) {
                    simpleType transformDataType(dataElement?.valueDomain.dataType.name)
                }else{
                    simpleType 'string'
                }
            }

        }
    }


    protected  transformDataType(String dataType){
        def dataType2 = dataType.replace('xs:', '')

        if(dataType2=="nonNegativeInteger"){
            dataType2 = "integer"
        }else if(dataType2=="double"){
            dataType2 = "decimal"
        }else if(dataType2=="dateTime"){
            dataType2 = "datetime"
        }

        return printXSDFriendlyString(dataType2)

    }








//    <xs:element name="participant-id" minOccurs="1" maxOccurs="1">
//    <xs:annotation>
//    <xs:appinfo>
//    <question-text>Participant ID</question-text>
//                        </xs:appinfo>
//    <xs:documentation>
//    <description>Genomics England Participant ID</description>
//                        </xs:documentation>
//    </xs:annotation>
//                     <xs:complexType>
//                        <xs:simpleContent>
//                           <xs:extension base="xs:integer">
//                              <xs:attribute name="m" use="optional" type="xs:date"/>
//    </xs:extension>
//                        </xs:simpleContent>
//    </xs:complexType>
//                  </xs:element>

}
