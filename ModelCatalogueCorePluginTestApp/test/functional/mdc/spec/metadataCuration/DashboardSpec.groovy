package mdc.spec.metadataCuration

import mdc.pages.DashboardPage;
import mdc.pages.authentication.LoginPage;
import geb.spock.GebReportingSpec

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


}
