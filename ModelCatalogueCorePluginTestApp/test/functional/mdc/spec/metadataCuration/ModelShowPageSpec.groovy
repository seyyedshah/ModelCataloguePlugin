package mdc.spec.metadataCuration

import geb.spock.GebReportingSpec
import mdc.pages.metadataCuration.ListPage.ModelListPage
import mdc.pages.metadataCuration.ShowPage.ModelShowPage

/**
 * Created by soheil on 15/05/2014.
 */
class ModelShowPageSpec extends GebReportingSpec {

	def "At modelShowPage, it shows model properties, conceptualDomains, metadata and dataElements"() {

		when: "Click on a model"
		to ModelListPage
		waitFor {
			at ModelListPage
		}
		//NHIC Datasets
		waitFor {
			getModelInTreeView(0)["Name"].displayed
		}
		def element = getModelInTreeView(0)
		interact {
			doubleClick(element["Name"])
		}

		then: "its properties, conceptualDomains and dataElements will be displayed"
		waitFor {
			at ModelShowPage
		}
		waitFor {
			propertiesTab.displayed
		}
		waitFor {
			childrenTab.displayed
		}
		waitFor {
			coceptualDomainsTab.displayed
		}
		waitFor {
			dataElementsTab.displayed
		}
		waitFor {
			metadataTab.displayed
		}
		waitFor {
			propertiesTab.displayed
		}
	}
}