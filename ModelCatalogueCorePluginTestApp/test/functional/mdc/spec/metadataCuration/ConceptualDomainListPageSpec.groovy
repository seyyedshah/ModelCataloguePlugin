package mdc.spec.metadataCuration

import geb.spock.GebReportingSpec
import mdc.pages.DashboardPage
import mdc.pages.metadataCuration.ListPage.ConceptualDomainListPage
import mdc.pages.metadataCuration.ShowPage.ConceptualDomainShowPage
import spock.lang.Ignore

/**
 * Created by soheil on 17/05/2014.
 */
class ConceptualDomainListPageSpec extends GebReportingSpec {


	def "Clicking on conceptualDomain name will lead to its show page"() {

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
		waitFor {
			modelsTab.displayed
		}
		waitFor {
			propertiesTab.displayed
		}
	}

	def "ConceptualDomain list page does not show administrative menus to readonly users"() {

		setup: "Go to conceptualDomain list page as readonly users"
		to ConceptualDomainListPage

		when: "at conceptualDomain list page"
		waitFor {
			at ConceptualDomainListPage
		}

		then: "it should not show administrative menus"
		!(newButton.displayed)
	}

	def "ConceptualDomain list page shows administrative menus to admin users"() {

		setup: "Go to conceptualDomain list page as an admin user"
		to DashboardPage
		waitFor {
			at DashboardPage
		}
		loginAsAdmin()
		waitFor {
			at DashboardPage
		}

		when: "at conceptualDomain list page"
		to ConceptualDomainListPage
		waitFor {
			at ConceptualDomainListPage
		}

		then: "it should show administrative menus"
		waitFor {
			newButton.displayed
		}
	}

	def "ConceptualDomain List Page can add a new conceptual domain"() {
		setup: "Go to conceptualDomain list page as an admin user and press 'New Conceptual Domain'"
		to DashboardPage
		waitFor {
			at DashboardPage
		}
		loginAsAdmin()
		waitFor {
			at DashboardPage
		}


		to ConceptualDomainListPage
		waitFor {
			at ConceptualDomainListPage
		}
		waitFor {
			newButton.displayed
		}
		newButton.click()


		waitFor {
			newCDModelDialogue.displayed
		}
		waitFor {
			newCDModelDialogueTitle.displayed
		}
		when: "Fill and save the new ConceptualDomain"
		//fill the model dialogue form
		newCDModelDialogueName << "ZNEW-NAME"
		newCDModelDialogueDescription << "ZNEW-DESC"
		//click on Save button
		newCDModelDialogueSaveBtn.click()

		then: "it should add a new ConceptualDomain and show conceptualDomain show page"
		waitFor {
			ConceptualDomainShowPage
		}
	}

}