package mdc.spec.metadataCuration

import mdc.pages.DashboardPage;
import geb.spock.GebReportingSpec
import mdc.pages.metadataCuration.ListPage.AssetListPage
import mdc.pages.metadataCuration.ListPage.ConceptualDomainListPage
import mdc.pages.metadataCuration.ListPage.DataElementListPage
import mdc.pages.metadataCuration.ListPage.DataTypeListPage
import mdc.pages.metadataCuration.ListPage.ModelListPage
import spock.lang.Ignore

class DashboardSpec extends GebReportingSpec {

	def "Dashboard readonly items should be displayed for Viewer user"() {

		when: "I have logged in as a viewer"
		to DashboardPage
		waitFor {
			at DashboardPage
		}
		loginAsViewer()


		then: "Readonly items of dashboard should be displayed"
		classificationLink.displayed
		finalizedModelsLink.displayed
		draftModelsLink.displayed


		finalizedDataElementsLink.displayed
		draftDataElementsLink.displayed
		uninstantiatedDataElementsLink.displayed

		conceptualDomainsLink.displayed

		valueDomainLink.displayed
		incompleteValueDomainLink.displayed


		dataTypesLink.displayed


		measurementUnitLink.displayed


		finalizedAssetsLink.displayed

		draftAssetsLink.displayed

		and:"Editable items should not be displayed"
		!createClassificatinLink.displayed
		!createModelLink.displayed
		!createDataElementLink.displayed
		!createConceptualDomainLink.displayed
		!createValueDomainsLink.displayed
		!createDataTypesLink.displayed
		!createMeasurementUnitLink.displayed
		!createAssetsLink.displayed
	}


	def "Dashboard editable items should be displayed for Admin user"() {

		when: "I have logged in as a viewer"
		to DashboardPage
		waitFor {
			at DashboardPage
		}
		loginAsAdmin()


		then: "Readonly items of dashboard should be displayed"
		classificationLink.displayed
		finalizedModelsLink.displayed
		draftModelsLink.displayed


		finalizedDataElementsLink.displayed
		draftDataElementsLink.displayed
		uninstantiatedDataElementsLink.displayed

		conceptualDomainsLink.displayed

		valueDomainLink.displayed
		incompleteValueDomainLink.displayed


		dataTypesLink.displayed


		measurementUnitLink.displayed


		finalizedAssetsLink.displayed

		draftAssetsLink.displayed

		and:"Editable items should be also displayed"
		createClassificatinLink.displayed
		createModelLink.displayed
		createDataElementLink.displayed
		createConceptualDomainLink.displayed
		createValueDomainsLink.displayed
		createDataTypesLink.displayed
		createMeasurementUnitLink.displayed
		createAssetsLink.displayed
	}


 	def "Conceptual Domains subMenu will redirect us to ConceptualDomain List page"(){

		when: "Click on ConceptualDomain List sub-menu"
		to ModelListPage

		waitFor {
			at ModelListPage
		}
		waitFor {
			$(nav.catalogueElementLink).displayed
		}
		$(nav.catalogueElementLink).click()

		waitFor {
			$(nav.conceptualDomainLink).displayed
		}
		$(nav.conceptualDomainLink).click()


		then:"will redirect us to ConceptualDomain List page"
		waitFor {
			at ConceptualDomainListPage
		}
	}

 	def "DataElements subMenu will redirect us to DataElements List page"(){

		when: "Click on DataElements List sub-menu"
		to ModelListPage

		waitFor {
			at ModelListPage
		}
		waitFor {
			$(nav.catalogueElementLink).displayed
		}
		$(nav.catalogueElementLink).click()


		waitFor {
			$(nav.dataElementLink).displayed
		}

		$(nav.dataElementLink).click()


		then:"will redirect us to DataElement List page"
		waitFor {
			at DataElementListPage
		}

	}

 	def "DataType subMenu will redirect us to DataType List page"(){

		when: "Click on DataType List sub-menu"
		to ModelListPage
		waitFor {
			at ModelListPage
		}
		waitFor {
			$(nav.catalogueElementLink).displayed
		}
		$(nav.catalogueElementLink).click()


		waitFor {
			$(nav.dataTypeLink).displayed
		}

		$(nav.dataTypeLink).click()

		then:"will redirect us to DataType List page"
		waitFor {
			at DataTypeListPage
		}
	}

 	def "Model subMenu will redirect us to ModelShowPage"(){

		when: "Click on ModelList page sub-menu"
		to ModelListPage
		waitFor {
			at ModelListPage
		}
		waitFor {
			$(nav.catalogueElementLink).displayed
		}
		$(nav.catalogueElementLink).click()
		waitFor {
			$(nav.modelLink).displayed
		}
		$(nav.modelLink).click()

		then:"it will redirect us to ModelListPage"
		waitFor {
			at ModelListPage
		}
	}


	def "Asset subMenu will redirect us to AssetShowPage"(){

		when: "Click on ModelList page sub-menu"
		to ModelListPage
		waitFor {
			at ModelListPage
		}
		waitFor {
			$(nav.catalogueElementLink).displayed
		}
		$(nav.catalogueElementLink).click()
		waitFor {
			$(nav.assetLink).displayed
		}
		$(nav.assetLink).click()

		then:"it will redirect us to AssetListPage"
		waitFor {
			at AssetListPage
		}
	}
}