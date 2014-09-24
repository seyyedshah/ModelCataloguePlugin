package mdc.pages.metadataCuration.ListPage

import mdc.pages.BasePageWithNav

/**
 * Created by soheil on 17/05/2014.
 */
class ListPage extends  BasePageWithNav{

	static String elementsTable = "table.dl-table.table"

	static String exportButton      = "span button#exportBtn"
	static String exportButtonItems = "span ul#exportBtnItems"

	static content = {
		mainLabel{  $("h2.ng-binding") }
	}

	def getRow(rowIndex){
		def row = ["object":null,"name":null,"desc":null];
		def table =  $(ListPage.elementsTable)
		waitFor {
			table.displayed
		}

		def object = $(ListPage.elementsTable).find("tbody tr",rowIndex)

		if(object){
			row = ["object":object,
					"name" :$(ListPage.elementsTable).find("tbody tr",rowIndex).find("td",0).find("a"),
					"desc":$(ListPage.elementsTable).find("tbody tr",rowIndex).find("td",1).find("span")]
		}
		return row
	}
}
