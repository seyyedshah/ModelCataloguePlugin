package mdc.spec.metadataCuration

import geb.spock.GebReportingSpec
import spock.lang.Stepwise
import mdc.pages.metadataCuration.ListPage.ConceptualDomainListPage
import mdc.pages.metadataCuration.ListPage.ModelListPage
import mdc.pages.metadataCuration.ShowPage.ConceptualDomainShowPage

/**
 * Created by soheil on 15/05/2014.
 */
class ConceptualDomainShowPageSpec extends GebReportingSpec {

	def "At ConceptualDomainShowPage, it shows properties, models and valueDomains"() {

		when: "at conceptualDomainList Page and clicking on a conceptualDomain name"
		to ConceptualDomainListPage
		waitFor {
			at ConceptualDomainListPage
		}

		waitFor {
			$(ConceptualDomainListPage.elementsTable).displayed
		}
		def nameElement = getRow(0)["name"]
		waitFor {
			nameElement.displayed
		}
		 nameElement.click()

		waitFor {
			at ConceptualDomainShowPage
		}

		then: "it redirects to conceptualDomain show page"
		waitFor {
			at ConceptualDomainShowPage
			mainLabel.displayed
		}
		mainLabel.text().contains("NHIC")
		description.text() == "NHIC conceptual domain i.e. value domains used the NHIC project"
		waitFor {
			valueDomainsTab.displayed
		}
		waitFor{
			modelsTab.displayed
		}
		waitFor{
			valueDomainsTab.displayed
		}

	}


	def "At ConceptualDomainShowPage, clicking on its tabs will show related table"() {

		when: "at conceptualDomainList Page and clicking on a conceptualDomain name"
		to ConceptualDomainListPage
		waitFor {
			at ConceptualDomainListPage
		}
		waitFor {
			$(ConceptualDomainListPage.elementsTable).displayed
		}
		def nameElement = getRow(0)["name"]
		waitFor {
			nameElement.displayed
		}

		nameElement.click()

		then: "it redirects to conceptualDomain show page"
		waitFor {
			at ConceptualDomainShowPage
		}
		waitFor {
			valueDomainsTab.displayed
		}
		when:"Clicking on valueDomains Tab"
		valueDomainsTab.parent().click()

		then:"valueDomains Table will be displayed"
		waitFor {
			valueDomainsTable.displayed
		}

		when:"Clicking on model Tab"
		waitFor {
			 modelsTab.displayed
		}

		modelsTab.parent().click()

		then:"model Table will be displayed"
		waitFor {
			modelsTable.displayed
		}
	}
}