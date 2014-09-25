package mdc.spec.metadataCuration

import geb.spock.GebReportingSpec
import mdc.pages.metadataCuration.ListPage.ModelListPage
import mdc.pages.metadataCuration.ShowPage.DataElementShowPage
import mdc.pages.metadataCuration.ShowPage.ModelShowPage
import spock.lang.Ignore

/**
 * Created by soheil on 15/05/2014.
 */

class ModelListPageSpec extends GebReportingSpec{

	def "Navigating to ModelShowPage, it shows TreeModel"(){

		when: "I'm at main Metadata showPage"
		to ModelListPage

		waitFor {
			at ModelListPage
		}

		then: "it shows model treeView"
		waitFor {
			$(ModelListPage.modelTree).displayed
			mainLabel.displayed
		}
		waitFor {
			mainLabel.text().contains("NHIC Datasets")
		}
	}

	def "Clicking on a collapse icon top level model will show its sub models"(){

		when: "Click on a top level parent node"
		to ModelListPage
		waitFor {
			at ModelListPage
		}

		waitFor {
			getModelInTreeView(0)["Item"].displayed
		}

		(getModelInTreeView(0)["Icon"]).click()


		then: "it shows its sub model"
		waitFor {
			(getModelInTreeView(1)["Item"]).displayed
		}
		waitFor {
			(getModelInTreeView(1)["Name"]).displayed
		}
	}

	def"Clicking on a model name, its name and description will be displayed on the main label"(){

		when: "Click on a model"
		to ModelListPage
		waitFor {
			at ModelListPage
		}
		waitFor {
			$(ModelListPage.modelTree).displayed
		}
		waitFor {
			getModelInTreeView(0)["Icon"].displayed
		}
		(getModelInTreeView(0)["Icon"]).click()


		waitFor {
			getModelInTreeView(1)["Item"].displayed
		}
		(getModelInTreeView(1)["Name"]).click()


		then: "its name will be displayed on the main label"
		waitFor {
			mainLabel.displayed
		}
		waitFor {
			mainLabel.text() == "Ovarian Cancer"
		}
	}

	def "Clicking on a model name, its dataElements will be displayed on the table"(){

		when: "Click on a model"
		to ModelListPage
		waitFor {
			at ModelListPage
		}
		waitFor {
			$(ModelListPage.modelTree).displayed
		}

		//NHIC Datasets
		waitFor {
			getModelInTreeView(0)["Icon"].displayed
		}
		(getModelInTreeView(0)["Icon"]).click()


		//Ovarian Cancer
		waitFor {
			getModelInTreeView(1)["Icon"].displayed
		}
		(getModelInTreeView(1)["Icon"]).click()


		//CUH
		waitFor {
			getModelInTreeView(2)["Icon"].displayed
		}
		(getModelInTreeView(2)["Icon"]).click()


		//Round 1
		waitFor {
			getModelInTreeView(3)["Icon"].displayed
		}
		(getModelInTreeView(3)["Icon"]).click()


		//MAIN
		waitFor {
			getModelInTreeView(4)["Icon"].displayed
		}
		(getModelInTreeView(4)["Icon"]).click()




		//PATIENT IDENTITY DETAILS
		waitFor {
			getModelInTreeView(5)["Icon"].displayed
		}
		(getModelInTreeView(5)["Icon"]).click()



		waitFor {
			dataElementsTable.displayed
			getDataElementRow(0)["name"]
			getDataElementRow(0)["desc"]
		}

		then: "its dataElements will be displayed on the table"
		waitFor {
			dataElementsTable.displayed
			(getDataElementRow(0)["name"]).text() == "NHS NUMBER*"
			(getDataElementRow(0)["desc"]).text().contains("*For linkage purposes NHS NUMBER")
		}
	}

 	def "DbClicking on a model name, will show the model page"(){

		when: "Click on a model"
		to ModelListPage
		waitFor {
			at ModelListPage
		}
		waitFor {
			getModelInTreeView(0)["Icon"].displayed
		}

		waitFor {
			getModelInTreeView(0)["Name"].displayed
		}


		def element = getModelInTreeView(0)
		interact {
			doubleClick(element["Name"])
		}

		then: "it will redirect to the model show page"
		waitFor(20) {
			at ModelShowPage
		}

	}

	def "Clicking on a dataElement, will redirect us to dataElement show page"(){

		when: "Click on a model"
		to ModelListPage
		waitFor {
			at ModelListPage
		}
		waitFor {
			$(ModelListPage.modelTree).displayed
		}

		//NHIC Datasets
		waitFor {
			getModelInTreeView(0)["Icon"].displayed
		}
		(getModelInTreeView(0)["Icon"]).click()


		//Ovarian Cancer
		waitFor {
			getModelInTreeView(1)["Icon"].displayed
		}
		(getModelInTreeView(1)["Icon"]).click()


		//CUH
		waitFor {
			getModelInTreeView(2)["Icon"].displayed
		}
		(getModelInTreeView(2)["Icon"]).click()


		//Round 1
		waitFor {
			getModelInTreeView(3)["Icon"].displayed
		}
		(getModelInTreeView(3)["Icon"]).click()


		//MAIN
		waitFor {
			getModelInTreeView(4)["Icon"].displayed
		}
		(getModelInTreeView(4)["Icon"]).click()




		//PATIENT IDENTITY DETAILS
		waitFor {
			getModelInTreeView(5)["Icon"].displayed
		}
		(getModelInTreeView(5)["Icon"]).click()



		waitFor {
			dataElementsTable.displayed
			getDataElementRow(0)["name"]
			getDataElementRow(0)["desc"]
		}


		getDataElementRow(0)["name"].click()

		then: "it will redirect to dataElement show page"
		waitFor {
			at DataElementShowPage
		}
	}



}