package mdc.pages.metadataCuration.ListPage
/**
 * Created by soheil on 15/05/2014.
 */
class ConceptualDomainListPage extends ListPage  {

	static url = "#/catalogue/conceptualDomain/all"

	static at = {
		url == "#/catalogue/conceptualDomain/all" &&
		 title == "Conceptual Domains"
	}


	static content = {

		exportButtonContent{  $("span button#exportBtn") }

		newButton(required:false) { $("button#create-catalogue-elementBtn")}

		newCDModelDialogue(required:false)      {$("div.modal-dialog")}
		newCDModelDialogueTitle(required:false) 	   {newCDModelDialogue.find("div.modal-content div.modal-header h4",text:"Create Conceptual Domain")}
		newCDModelDialogueName(required:false)  	   {newCDModelDialogue.find("div.modal-content div.modal-body form input#name")}
		newCDModelDialogueDescription(required:false)  {newCDModelDialogue.find("div.modal-content div.modal-body form input#description")}
		newCDModelDialogueSaveBtn(required:false)      {newCDModelDialogue.find("div.modal-content div.modal-footer div.contextual-actions button#modal-save-elementBtn")}
	}


	@Override
	def getRow(rowIndex){
		def row = ["object":null,"name":null,"desc":null];
		def table =  $("table.dl-table.table")
		waitFor {
			table.displayed
		}

		def object = $("table.dl-table.table").find("tbody tr",rowIndex)

		if(object){
			row = ["object":object,
					"name":$("table.dl-table.table").find("tbody tr",rowIndex).find("td",0).find("a"),
					"desc":$("table.dl-table.table").find("tbody tr",rowIndex).find("td",1).find("span")]
		}
		return row
	}

}