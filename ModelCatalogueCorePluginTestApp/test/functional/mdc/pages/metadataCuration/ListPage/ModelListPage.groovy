package mdc.pages.metadataCuration.ListPage

import mdc.pages.BasePageWithNav

/**
 * Created by soheil on 17/05/2014.
 */
class ModelListPage extends BasePageWithNav {

	static url = "#/catalogue/model/all"

	static at = {
				url == "#/catalogue/model/all" &&
				title == "Models"
	}


	static String modelTree 		 = "div.catalogue-element-treeview-list-container[list='list']"
	static String leftActionList     = "h2 span.btn-group button"
	static String leftSubActionList  = "ul#switch-statusBtnItems"



	static content = {
 		mainLabel { $("h3.ng-binding") }
		descriptionLabel {$("blockquote.ce-description") }
		dataElementsTable { $("table.dl-table.table") }
	}


	def getModelInTreeView(index){
		def Item = $(modelTree).find("span.catalogue-element-treeview-labels", index)
		def Icon = Item.find("a.btn.btn-link")
		def Name = Item.find("span.catalogue-element-treeview-name")

		["Item":Item,"Icon":Icon,"Name":Name]
	}

	def getDataElementRow(rowIndex) {
		def object = dataElementsTable.find("tbody tr", rowIndex)
		if (!object) {
			return ["object": null, "name": null, "desc": null];
		}

		def row = ["object": object,
				"name": object.find("td", 0).find("a"),
				"desc": object.find("td", 1).find("span")]
		return row
	}
}