package org.modelcatalogue.core

import grails.gorm.DetachedCriteria
import grails.transaction.Transactional
import groovy.xml.MarkupBuilder
import org.modelcatalogue.core.util.ClassificationFilter
import org.modelcatalogue.core.util.ListCountAndType
import org.modelcatalogue.core.util.ListWithTotalAndType
import org.modelcatalogue.core.util.Lists

@Transactional
class ModelService {

    SecurityService modelCatalogueSecurityService
    ClassificationService classificationService

    ListWithTotalAndType<Model> getTopLevelModels(Map params) {
        getTopLevelModels(classificationService.classificationsInUse, params)
    }

    ListWithTotalAndType<Model> getTopLevelModels(ClassificationFilter classifications, Map params) {
        RelationshipType hierarchy      = RelationshipType.hierarchyType
        ElementStatus status            = ElementService.getStatusFromParams(params)
        RelationshipType classification = RelationshipType.classificationType

        DetachedCriteria<Model> criteria = new DetachedCriteria<Model>(Model)




        if (classifications.unclassifiedOnly) {
            // language=HQL
            return Lists.fromQuery(params, Model, """
                select distinct m
                from Model as m left join m.incomingRelationships as rel
                where m.status = :status
                    and (
                        (
                            m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                            and rel.relationshipType != :classificationType
                        )
                        or m.incomingRelationships is empty
                     )
                group by m.name, m.id
                order by m.name
            ""","""
                select count(m.id)
                from Model as m left join m.incomingRelationships as rel
                where m.status = :status
                    and (
                        (
                            m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                            and rel.relationshipType != :classificationType
                        )
                        or m.incomingRelationships is empty
                     )
            """, [type: hierarchy, status: status, classificationType: classification ])
        }
        if (classifications.excludes && !classifications.includes) {
            // language=HQL
            return Lists.fromQuery(params, Model, """
                select distinct m
                from Model as m
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:classifications))
                group by m.name, m.id
                order by m.name
            ""","""
                select count(m.id)
                from Model as m
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:classifications))
            """, [type: hierarchy, status: status, classifications: classifications.excludes, classificationType: classification ])
        }
        if (classifications.excludes && classifications.includes) {
            // language=HQL
            return Lists.fromQuery(params, Model, """
                select distinct m
                from Model as m
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:excludes))
                    and m.id in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:includes))
                group by m.name, m.id
                order by m.name
            ""","""
                select count(m.id)
                from Model as m
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:excludes))
                    and m.id in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:includes))
            """, [type: hierarchy, status: status, includes: classifications.includes, excludes: classifications.excludes, classificationType: classification ])
        }
        if (classifications.includes && !classifications.excludes) {
            // language=HQL
            return Lists.fromQuery(params, Model, """
                select distinct m
                from Model as m join m.incomingRelationships as rel
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and rel.source.id in (:classifications)
                    and rel.relationshipType = :classificationType
                group by m.name, m.id
                order by m.name
            ""","""
                select count(m.id)
                from Model as m join m.incomingRelationships as rel
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and rel.source.id in (:classifications)
                    and rel.relationshipType = :classificationType
            """, [type: hierarchy, status: status, classifications: classifications.includes, classificationType: classification ])
        }
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

    def printSection(Model model, Map ext, MarkupBuilder builder){
        if(model.ext.repeating=='true') {

            return builder.repeatingGroup(id: printXSDFriendlyString(model.name), minRepeat: defaultMinOccurs(ext.get("Min Occurs")), maxRepeat: defaultMaxOccurs(ext.get("Max Occurs"))) {
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
            text dataElement.ext.text
            instructions dataElement.description

            if(dataElement.ext.serviceLookupName){
                'service-lookup'(id: dataElement.ext.serviceLookupId, style: dataElement.ext.serviceLookupStyle){
                    name dataElement.ext.serviceLookupName
                }
            }

            if(dataElement?.valueDomain?.dataType instanceof EnumeratedType) {

                enumeration(id:printXSDFriendlyString(dataElement.valueDomain.name), style:dataElement.ext.style){
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

    protected defaultEnumerationStyle(String style){
        if(style==null) style = 'single-select'
        return style
    }


}
