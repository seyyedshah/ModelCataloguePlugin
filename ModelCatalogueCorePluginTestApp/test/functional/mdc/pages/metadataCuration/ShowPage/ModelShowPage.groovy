package mdc.pages.metadataCuration.ShowPage

import mdc.pages.BasePageWithNav

/**
 * Created by soheil on 15/05/2014.
 */
class ModelShowPage extends BasePageWithNav{

	static url = "#/catalogue/model/"


	static at = {
		url == "#/catalogue/model/" &&
		title == "Metadata Registry"
	}

	static content = {
		mainLabel(wait:true) { $("h3.ce-name") }

		propertiesTab {waitFor {$("div.tabbable ul li[heading='Properties']")}}
		childOfTab{waitFor { $("div.tabbable ul li[heading='Parent']")}}

		conceptualDomainTab {waitFor { $("div.tabbable ul li[heading='Conceptual Domains']")}}
		dataElementsTab {waitFor { $("div.tabbable ul li[heading='Data Elements']")}}

		metadataTab {waitFor { $("div.tabbable ul li[heading='Metadata']")}}
		parentOfTab {waitFor { $("div.tabbable ul li[heading='Children']")}}

		draftIcon     {waitFor {$("h3 span.label-warning")}}
		finalizedIcon {waitFor {$("h3 span.label-primary")}}
	}
}