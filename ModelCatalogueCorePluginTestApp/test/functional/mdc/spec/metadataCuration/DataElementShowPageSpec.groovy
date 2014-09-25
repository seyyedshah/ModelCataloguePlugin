package mdc.spec.metadataCuration

import geb.spock.GebReportingSpec
import mdc.pages.metadataCuration.ListPage.DataElementListPage
import mdc.pages.metadataCuration.ShowPage.DataElementShowPage

/**
 * Created by soheil on 15/05/2014.
 */
class DataElementShowPageSpec extends GebReportingSpec {
	def "At dataElementShowPage, it shows properties, metadata, values and relations"() {


		when: "Click on a dataElement"
		to DataElementListPage
		waitFor {
			at DataElementListPage
		}
		waitFor {
			$(DataElementListPage.elementsTable).displayed
		}

		def elementCatalogueId = getRow(0)["catalogueId"]
		waitFor {
			elementCatalogueId.displayed
		}

		elementCatalogueId.click()

		then: "it redirects to dataElement show page"
		waitFor{
			at DataElementShowPage
			mainLabel.displayed
		}
		mainLabel.text().contains("ANATOMICAL SIDE (IMAGING)* FINALIZED")
		description.text().contains("*IMAGING CODE (NICIP)")

		waitFor {
			propertiesTab.displayed
		}
		waitFor {
			metadataTab.displayed
		}
		waitFor {
			modelsTab.displayed
		}
		waitFor {
			relationshipsTab.displayed
		}
	}


	def "At dataElementShowPage, clicking on its tabs will show related table"() {


		given:"on dataElementShowPage"
		to DataElementListPage
		waitFor {
			at DataElementListPage
		}
		waitFor {
			$(DataElementListPage.elementsTable).displayed
		}


		def elementCatalogueId = getRow(0)["catalogueId"]
		waitFor {
			elementCatalogueId.displayed
		}

		elementCatalogueId.click()


		waitFor{
			at DataElementShowPage
			mainLabel.displayed
		}
		mainLabel.text().contains("ANATOMICAL SIDE (IMAGING)* FINALIZED")
		description.text().contains("*IMAGING CODE (NICIP)")


		when:"Clicking on metadata Tab"
		waitFor {
			metadataTab.displayed
			metadataTab.parent()
		}
		interact {
			click(metadataTab.parent())
		}

		then:"metadata Table will be displayed"
		waitFor {
			metadataTable.displayed
		}

		when:"Clicking on relatedTo Tab"
		//metadataTab.parent() this item is <a> tag
		relationshipsTab.parent().click()

		then:"relatedTo Table will be displayed"
		waitFor {
			relationshipsTable.displayed
		}
	}
}