package mdc.pages.metadataCuration.ListPage
/**
 * Created by soheil on 15/05/2014.
 */
class AssetListPage extends ListPage {

	static url = "#/catalogue/asset/all"

	static at = {
		url == "#/catalogue/asset/all" &&
		title == "Assets"
	}

	static String actionListButton = "span.btn-group button"
	static String subActionList    = "ul#switch-statusBtnItems"
	static String assetList  = "table.dl-table.table"


	def getStatusActionButton(){
		$(AssetListPage.actionListButton,0)
	}

	def getDraftStatusButton(){
		$(subActionList).find("li",0)
	}

}