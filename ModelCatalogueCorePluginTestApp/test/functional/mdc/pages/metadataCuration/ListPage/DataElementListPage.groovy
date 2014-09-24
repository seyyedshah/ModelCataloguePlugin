package mdc.pages.metadataCuration.ListPage
/**
 * Created by soheil on 15/05/2014.
 */
class DataElementListPage extends ListPage {

	static url = "#/catalogue/dataElement/all"

	static String actionListButton = "span.btn-group button"
	static String subActionList    = "ul#switch-statusBtnItems"
	static String dataElementList  = "table.dl-table.table"

	static content = {
		exportButtonContent{  $("span button#exportBtn") }
	}

	static at = {
		url == "#/catalogue/dataElement/all" &&
		title == "Data Elements"
	}

	@Override
	def getRow(rowIndex){

		def row = ["object":null,"name":null,"desc":null];
		def table = waitFor {  $("table.dl-table.table")}

		if(!table)
			return  row

		def object = $("table.dl-table.table tbody tr",rowIndex)


		if(object){
			row = [ "object":$("table.dl-table.table tbody tr",rowIndex),
					"classification" :$("table.dl-table.table tbody tr",rowIndex).find("td",0).find("a"),
					"catalogueId" :$("table.dl-table.table tbody tr",rowIndex).find("td",1).find("a"),
					"name":$("table.dl-table.table tbody tr",rowIndex).find("td",2).find("a"),
					"desc":$("table.dl-table.table tbody tr",rowIndex).find("td",3).find("span")]
		}
		return row
	}


	def getStatusActionButton(){
		$(DataElementListPage.actionListButton,0)
	}

	def getDraftStatusButton(){
		$(subActionList).find("li",0)
	}

}