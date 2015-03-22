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
