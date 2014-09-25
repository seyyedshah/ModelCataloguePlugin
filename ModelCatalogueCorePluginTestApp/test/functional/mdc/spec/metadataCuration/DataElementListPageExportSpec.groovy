package mdc.spec.metadataCuration

import geb.spock.GebReportingSpec
import mdc.pages.DashboardPage
import mdc.pages.metadataCuration.ListPage.DataElementListPage
import mdc.pages.metadataCuration.ListPage.ModelListPage
import mdc.pages.metadataCuration.ShowPage.AssetShowPage
import mdc.pages.metadataCuration.ShowPage.DataElementShowPage

/**
 * Created by soheil on 17/05/2014.
 * As testing export button using GEB, failed several time
 * I moved testing export into a separate file
 */
class DataElementListPageExportSpec extends GebReportingSpec {

	def "ExportButton in dataElement list page contains several default reports"() {

		setup: "Go to dataElement List page as a List page that contains ExportButton"
		to DashboardPage
		waitFor {
			at DashboardPage
		}
		loginAsViewer()
		waitFor {
			at DashboardPage
		}

		to DataElementListPage

		when: "at DataElementListPage"
		waitFor {
			at DataElementListPage
		}

		//this line will create some sort of delay on the page,
		//as GEB used to fail on accessing 'exportButtonContent',
		// I had to add this check here
		waitFor {
			$("h2", text: contains("Data Element List")).displayed
		}

		waitFor {
			exportButtonContent.displayed
		}
		interact {
			click(exportButtonContent)
		}


		then: "list of available reports will be displayed in a menu"
		waitFor {
			$(DataElementListPage.exportButtonItems).displayed
		}
		waitFor {
			$(DataElementListPage.exportButtonItems).find("li", index).displayed
		}
		waitFor {
			$(DataElementListPage.exportButtonItems).find("li", index).find("a").size() == 1
		}
		waitFor {
			$(DataElementListPage.exportButtonItems).find("li", index).find("a")[0].text() == label
		}

		where:
		index | label
		1     | "Data Elements to Excel"
	}

	def "ExportButton in dataElement list page will export dataElement list as an excel file"() {

		setup: "Go to dataElement page as a List page that contains ExportButton"
		to DashboardPage
		waitFor {
			at DashboardPage
		}
		loginAsViewer()
		waitFor {
			at DashboardPage
		}

		to DataElementListPage

		when: "at dataElementList Page"
		waitFor {
			at DataElementListPage
		}

		//this line will create some sort of delay on the page,
		//as GEB used to fail on accessing 'exportButtonContent',
		// I had to add this check here
		waitFor {
			$("h2", text:contains("Data Element List")).displayed
		}

		waitFor {
			exportButtonContent.displayed
		}
		interact {
			click(exportButtonContent)
		}

		waitFor {
			$(DataElementListPage.exportButtonItems).displayed
		}

		//just try it for the first item in the list
		int index = 1
		waitFor {
			$(DataElementListPage.exportButtonItems).find("li", index).displayed
		}
		waitFor {
			$(DataElementListPage.exportButtonItems).find("li", index).find("a", 0).displayed
		}

		$(DataElementListPage.exportButtonItems).find("li", index).find("a", 0).click()

		then: "it downloads the excel file"
		waitFor {
			at AssetShowPage
		}
	}
}