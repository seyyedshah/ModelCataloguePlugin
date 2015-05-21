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
		RelationshipType hierarchy = RelationshipType.hierarchyType
		ElementStatus status = ElementService.getStatusFromParams(params)
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
            """, """
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
            """, [type: hierarchy, status: status, classificationType: classification])
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
            """, """
                select count(m.id)
                from Model as m
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:classifications))
            """, [type: hierarchy, status: status, classifications: classifications.excludes, classificationType: classification])
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
            """, """
                select count(m.id)
                from Model as m
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:excludes))
                    and m.id in (select distinct r.destination.id from Relationship r where r.relationshipType = :classificationType and r.source.id in (:includes))
            """, [type: hierarchy, status: status, includes: classifications.includes, excludes: classifications.excludes, classificationType: classification])
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
            """, """
                select count(m.id)
                from Model as m join m.incomingRelationships as rel
                where m.status = :status
                    and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
                    and rel.source.id in (:classifications)
                    and rel.relationshipType = :classificationType
            """, [type: hierarchy, status: status, classifications: classifications.includes, classificationType: classification])
		}
		// language=HQL
		Lists.fromQuery params, Model, """
            select distinct m
            from Model m
            where m.status = :status and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
            group by m.name, m.id
            order by m.name
        """, """
            select count(m.id)
            from Model m
            where m.status = :status and m.id not in (select distinct r.destination.id from Relationship r where r.relationshipType = :type)
        """, [type: hierarchy, status: status]
	}

	ListWithTotalAndType<Model> getSubModels(Model model) {
		List<Model> models = listChildren(model)
		new ListCountAndType<Model>(count: models.size(), list: models, itemType: Model)

	}

	ListWithTotalAndType<DataElement> getDataElementsFromModels(List<Model> models) {
		def results = []
		models.each { model ->
			results.addAll(model.contains)
		}
		new ListCountAndType<DataElement>(count: results.size(), list: results, itemType: DataElement)
	}


	protected List<Model> listChildren(Model model, results = []) {
		if (model && !results.contains(model)) {
			results += model
			model.parentOf?.each { child ->
				results += listChildren(child, results)
			}
		}
		results.unique()
	}

	def gelMasterXML(Model model) {
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.context('xmlns:xsi': "http://www.w3.org/2001/XMLSchema-instance", 'xsi:noNamespaceSchemaLocation': "../../../Transformations/form.xsd") {
			setOmitEmptyAttributes(true)
			setOmitNullAttributes(true)
			dataset('fileLocation': "../GEL_CAN_Master.xml", 'name': "Master")
			form(id: "${printXSDFriendlyString("Master")}") {
				name model.name
				instructions model?.ext?.instructions
				version {
					major model.versionNumber
					minor 0
					patch 0
				}
				versionDescription 'Alpha version'
				revisionNotes 'Alpha version'
				formTitle printXSDFriendlyString(model.name)
				formInitials printXSDFriendlyString(model.name)

				//order all elements based on their ext.order
				//these are actually CRFs
				model.outgoingRelationships.sort({ it.ext?.order }).each { Relationship rel ->
//                    if (rel.relationshipType == RelationshipType.containmentType) {
//						this.printQuestion(rel.destination, rel.ext, builder)
//					}

					if (rel.relationshipType == RelationshipType.hierarchyType &&
							rel.source.status != ElementStatus.DEPRECATED &&
							rel.destination.status != ElementStatus.DEPRECATED) {
						this.printSection(rel.destination, rel.ext, builder)

						//create Form xml
						gelCreateFormXML(rel.destination)
					}
				}
			}
		}
		//println(writer.toString())

		FileWriter fw = new FileWriter("XML/SourceModels/GEL_CAN_Master.xml");
		String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" + writer.toString()
		fw.write(xmlText);
		fw.close();
	}


	def gelCreateFormXML(Model model) {


		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.context('xmlns:xsi': "http://www.w3.org/2001/XMLSchema-instance", 'xsi:noNamespaceSchemaLocation': "../../../../Transformations/form.xsd") {
			setOmitEmptyAttributes(true)
			setOmitNullAttributes(true)
			dataset('fileLocation': "../GEL_CAN_Master.xml", 'name': "Master")
			form(id: "GEL_CAN_${model.name}") {
				name model.name
				version {
					major model.versionNumber
					minor 0
					patch 0
				}
				versionDescription model.ext.get('versionDescription')
				revisionNotes model.ext.get('revisionNotes')
				formTitle printXSDFriendlyString(model.ext.get('formTitle'))
				formInitials model.ext.get('formInitials')

				//if it does not have any section, just add its questions
				def outgoingHierarchyCount = 0
				model.outgoingRelationships.each {

					if (it.relationshipType == RelationshipType.hierarchyType &&
							it.source.status != ElementStatus.DEPRECATED &&
							it.destination.status != ElementStatus.DEPRECATED)
						outgoingHierarchyCount++
				}
				if (outgoingHierarchyCount == 0) {
					section(id: model.name) {
						setOmitEmptyAttributes(true)
						setOmitNullAttributes(true)
						reference(dataset: "Master", type: "section") {
							value(field: "id", model.name)
						}
					}
				} else {
					model.outgoingRelationships.sort({ it.ext?.order }).each { Relationship rel ->
						if (rel.relationshipType == RelationshipType.hierarchyType &&
								rel.source.status != ElementStatus.DEPRECATED &&
								rel.destination.status != ElementStatus.DEPRECATED) {

							section(id: rel.destination.name, minRepeat: rel.destination.ext.get("Min Occurs"), maxRepeat: rel.destination.ext.get("Max Occurs")) {
								setOmitEmptyAttributes(true)
								setOmitNullAttributes(true)
								reference(dataset: "Master", type: "section") {
									value(field: "id",rel.destination.name)
								}
							}
						}
					}
				}
			}
		}
		println(writer.toString())
		FileWriter fw = new FileWriter("XML/SourceModels/OpenClinicaForms/${printXSDFriendlyString(model.name)}.xml");
		String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" + writer.toString()
		fw.write(xmlText);
		fw.close();
	}


	def printSection(Model model, Map ext, MarkupBuilder builder) {
		if (model.ext?.repeating == 'true') {

			return builder.repeatingGroup(id: printXSDFriendlyString(model.name), minRepeat: defaultMinOccurs(model.ext.get("Min Occurs")), maxRepeat: defaultMaxOccurs(model.ext.get("Max Occurs"))) {
				setOmitEmptyAttributes(true)
				setOmitNullAttributes(true)
				name model.name

				model.outgoingRelationships.sort({ it.ext?.order }).each { Relationship rel ->

					if (rel.relationshipType == RelationshipType.containmentType) {
						this.printQuestion(model,rel.destination, rel.ext, builder)
					}

					if (rel.relationshipType == RelationshipType.hierarchyType) {
						this.printSection(rel.destination, rel.ext, builder)
					}
				}
			}

		} else {


			return builder.section(id: model.name, minRepeat: ext.get("Min Occurs"), maxRepeat: ext.get("Max Occurs")) {
				setOmitEmptyAttributes(true)
				setOmitNullAttributes(true)
				name model.name
				instructions model?.ext?.instructions

				//it.ext?.order
				model.outgoingRelationships.sort({ it.outgoingIndex  }).each { Relationship rel ->

					if (rel.relationshipType == RelationshipType.containmentType) {
						this.printQuestion(model,rel.destination, rel.ext, builder)
					}
					if (rel.relationshipType == RelationshipType.hierarchyType) {
						this.printSection(rel.destination, rel.ext, builder)
					}
				}
			}

		}
	}

	def printQuestion(Model model, DataElement dataElement, Map ext, MarkupBuilder builder) {

		//try to use id from ext on relationship, if it was Not available
		//try to use id from ext, if it was NOT available use main dataElement.id
		def modelName = model.name?.replaceAll(" ", "_").toLowerCase()
		def crfElmId
		if( ext?.id || ext?.get("Id")){
			crfElmId = "${modelName}_${ext?.id}"
		} else if (dataElement.ext?.id || dataElement.ext?.get("Id")) {
			crfElmId = "${modelName}_${dataElement.ext?.id}"
		}else{
			crfElmId = "${modelName}_${dataElement.id}"
		}


		def crfElmHidden = dataElement?.ext?.hidden?.toLowerCase()
		if (crfElmHidden != 'true')
			crfElmHidden = null


		def anonymisation = dataElement?.ext?.anonymisation?.toLowerCase()
		if (anonymisation != 'true')
			anonymisation = null

		//if it is hidden and it's id doesn't start with hidden, add 'Hidden_' into its beginning
		if (crfElmHidden == 'true' && !crfElmId.toLowerCase().startsWith("hidden")) {
			crfElmId = "Hidden_${crfElmId}"
		}

		return builder.question(id: "${crfElmId}", minRepeat: ext.get("Min Occurs"), maxRepeat: ext.get("Max Occurs"), hidden: crfElmHidden, anonymisation: anonymisation) {
			setOmitEmptyAttributes(true)
			setOmitNullAttributes(true)

			name dataElement.name

			//Right Text
			instructions dataElement?.ext.instructions

			//Left text
			def displayText = dataElement?.ext.text
			if(!displayText){
				displayText = dataElement.name
			}
			text displayText



			if (dataElement?.ext.serviceLookupName) {
				'service-lookup'(id: dataElement.ext.serviceLookupId, style: dataElement.ext.serviceLookupStyle) {
					name dataElement.ext.serviceLookupName
				}
			}

			if (dataElement?.valueDomain?.dataType instanceof EnumeratedType) {

				def RESPONSE_LABEL = "${printXSDFriendlyString(dataElement.valueDomain.name)}-${defaultEnumerationStyle(dataElement.ext.get("style"))}"
				enumeration(id: RESPONSE_LABEL, style: dataElement.ext.style) {
					dataElement.valueDomain.dataType.enumerations.each { key, val ->
						value(control: key, val)
					}
				}
			} else {

				def attributes = [:]

				attributes['validation'] = dataElement?.ext.validation
				attributes['validation-error-message'] = dataElement?.ext['validation-error-message']


				if (dataElement?.valueDomain?.dataType) {
					simpleType(attributes, transformDataType(dataElement?.valueDomain.dataType.name))
				} else {
					simpleType(attributes, 'string')
				}
			}

		}
	}


	protected transformDataType(String dataType) {
		def dataType2 = dataType.replace('xs:', '')

		def basicOnes = [
				"string", "boolean",
				"integer", "decimal",
				"float", "date",
				"pdate", "an10 date",
				"time", "datetime",
				"textarea", "file",
				"email", "phone",
				"NHSNumber"];

		if (dataType2.toLowerCase() == "nonnegativeinteger" || dataType2.toLowerCase() == "positiveinteger") {
			dataType2 = "integer"
		} else if (dataType2.toLowerCase() == "double") {
			dataType2 = "decimal"
		} else if (dataType2.toLowerCase() == "dateTime") {
			dataType2 = "datetime"
		} else if (dataType2.toLowerCase() == "base64binary") {
			dataType2 = "file"
		} else if (!basicOnes.contains(dataType2.toLowerCase())) {
			dataType2 = "string"
		}

		return printXSDFriendlyString(dataType2)

	}

	protected printXSDFriendlyString(String string) {
		if (!string)
			return null;
		return string?.replaceAll(" ", "-").toLowerCase()
	}

	protected defaultMinOccurs(String min) {
		if (min == null) min = '0'
		return min
	}

	protected defaultMaxOccurs(String max) {
		if (max == null) max = 'unbounded'
		return max
	}

	protected defaultEnumerationStyle(String style) {
		if (style == null) style = 'single-select'
		return style
	}


}
