package mdc.spec.metadataCuration

import geb.spock.GebReportingSpec
import mdc.pages.DashboardPage
import mdc.pages.authentication.LoginPage
import mdc.pages.metadataCuration.ListPage.ConceptualDomainListPage
import mdc.pages.metadataCuration.ListPage.DataElementListPage
import mdc.pages.metadataCuration.ListPage.DataTypeListPage
import mdc.pages.metadataCuration.ListPage.ModelListPage
import mdc.pages.metadataCuration.ShowPage.ConceptualDomainShowPage
import mdc.pages.metadataCuration.ShowPage.DataElementShowPage
import mdc.pages.metadataCuration.ShowPage.DataTypeShowPage

/**
 * Created by soheil on 17/05/2014.
 */
class DataTypeListPageSpec extends GebReportingSpec {

	def "Clicking on dataType name will lead to its show page"() {

		when: "at dataTypeList Page and clicking on a dataType name"
		to DataTypeListPage
		waitFor {
			at DataTypeListPage
		}

		waitFor {
			$(DataTypeListPage.elementsTable).displayed
			getRow(0)["object"].displayed
		}

		def nameElement = getRow(0)["name"]
		waitFor {
			nameElement.displayed
		}
		interact {
			click(nameElement)
		}

		then: "it redirects to dataType show page"
		waitFor {
			at DataTypeShowPage
			mainLabel.displayed
			description.displayed
		}
		mainLabel.text() == "Boolean (Data Type: 4)"
		description.text() == "java.lang.Boolean"
	}
}