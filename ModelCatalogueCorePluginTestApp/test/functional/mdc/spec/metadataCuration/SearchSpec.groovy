package mdc.spec.metadataCuration

import geb.spock.GebReportingSpec
import mdc.pages.DashboardPage
import mdc.pages.metadataCuration.ShowPage.DataElementShowPage
import org.apache.tools.ant.taskdefs.WaitFor

/**
 * Created by soheil on 22/05/2014.
 */
class SearchSpec  extends GebReportingSpec{
	def "Search text box should be displayed on Model List page"(){

		when: "Going to Dashboard Page"
		to DashboardPage
		waitFor {
			at DashboardPage
		}

		then:"It should display the Search text box"
		waitFor {
			$(nav.searchTextInput).displayed
		}
	}

	def "Searching for an existing term will return all related elements"(){

		given: "In Dashboard Page"
		to DashboardPage
		waitFor {
			at DashboardPage
		}

		waitFor {
			$(nav.searchTextInput).displayed
		}

		when:"Searching for a term"
		$(nav.searchTextInput).value("ANATOMICAL")


		then:"it will return all found elements"
		//It may take a bit long to load the search result
		waitFor (30){
			$(nav.searchResultUl).displayed
		}
 		$(nav.searchResultUl).find("li").size() == 10
		$(nav.searchResultUl).find("li",0).text().trim().contains("Search Catalogue Element for ANATOMICAL")
		$(nav.searchResultUl).find("li",1).text().trim().contains("Validate ANATOMICAL")
		$(nav.searchResultUl).find("li",2).text().trim().contains("Convert ANATOMICAL")
		$(nav.searchResultUl).find("li",3).text().trim().contains("IMAGING CODE (NICIP)*")
	}

	def "Clicking on a returned result from search will lead us to that element"(){

		given: "In Dashboard List Page and the search text box is displayed"
		to DashboardPage
		waitFor {
			at DashboardPage
		}


		waitFor {
			$(nav.searchTextInput).displayed
		}

		when:"Searching for a term and clicking on the result"
		$(nav.searchTextInput).value("ANATOMICAL")
		waitFor {
			$(nav.searchResultUl).displayed
		}

		waitFor {
			$(nav.searchResultUl).find("li",3).text().trim().contains("IMAGING CODE (NICIP)*")
		}

		waitFor {
			$(nav.searchResultUl).find("li",3).find("a")
		}

		$(nav.searchResultUl).find("li",3).find("a").click()

		then:"it will lead us to the show page of that element"
		waitFor (20) {
			at DataElementShowPage
		}
	}
}