package mdc.pages

class DashboardPage extends BasePageWithNav{
	
	static url = "#/dashboard/"
	
	static at = {
		url == "#/dashboard/" &&
		title == "Model Catalogue"
	}


	static content = {

		classificationLink(required:false) {waitFor {$("a#dataSetsLink")}}
		createClassificatinLink(required:false) {waitFor {$("a#createClassificatinLink")}}

		finalizedModelsLink(required:false) {waitFor {$("a#finalizedModelsLink")}}
		draftModelsLink(required:false) {waitFor {$("a#draftModelsLink")}}
		createModelLink(required:false) {waitFor {$("a#createModelLink")}}


		finalizedDataElementsLink(required:false) {waitFor {$("a#finalizedDataElementsLink")}}
		draftDataElementsLink(required:false) {waitFor {$("a#draftDataElementsLink")}}
		uninstantiatedDataElementsLink(required:false) {waitFor {$("a#uninstantiatedDataElementsLink")}}
		createDataElementLink(required:false) {waitFor {$("a#createDataElementLink")}}


		conceptualDomainsLink(required:false) {waitFor {$("a#conceptualDomainsLink")}}
		createConceptualDomainLink(required:false) {waitFor {$("a#createConceptualDomainLink")}}


		valueDomainLink(required:false) {waitFor {$("a#valueDomainLink")}}
		incompleteValueDomainLink(required:false) {waitFor {$("a#incompleteValueDomainLink")}}
		createValueDomainsLink(required:false) {waitFor {$("a#createValueDomainsLink")}}


		dataTypesLink(required:false) {waitFor {$("a#dataTypesLink")}}
		createDataTypesLink(required:false) {waitFor {$("a#createDataTypesLink")}}


		measurementUnitLink(required:false) {waitFor {$("a#measurementUnitLink")}}
		createMeasurementUnitLink(required:false) {waitFor {$("a#createMeasurementUnitLink")}}


		finalizedAssetsLink(required:false) {waitFor {$("a#finalizedAssetsLink")}}

		draftAssetsLink(required:false) {waitFor {$("a#draftAssetsLink")}}
		createAssetsLink(required:false) {waitFor {$("a#createAssetsLink")}}
	}


	def loginAsAdmin(){
		waitFor {
			$(nav.loginButton).displayed
		}

		$(nav.loginButton).click()

		waitFor {
			modalPromptLogin.displayed()
		}
		modalPromptLogin.loginAdminUser()

		waitFor {
			!modalPromptLogin.displayed()
		}
	}



	def loginAsViewer(){
		waitFor {
			$(nav.loginButton).displayed
		}

		$(nav.loginButton).click()

		waitFor {
			modalPromptLogin.displayed()
		}
		modalPromptLogin.loginViewerUser()

		waitFor {
			!modalPromptLogin.displayed()
		}
	}


	def showLoginModal(){
		waitFor {
			$(nav.loginButton).displayed
		}

		$(nav.loginButton).click()

		waitFor {
			modalPromptLogin.displayed()
		}
	}

}