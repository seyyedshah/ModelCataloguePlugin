package mdc.pages.metadataCuration.ShowPage

import mdc.pages.BasePageWithNav

/**
 * Created by soheil on 15/05/2014.
 */
class ModelShowPage extends BasePageWithNav{

	static url = "#/catalogue/model/"


	static at = {
		url == "#/catalogue/model/" &&
				title ==~ /^Properties of .*$/
	}

	static content = {
		mainLabel(wait:true) { $("h3.ce-name") }



		propertiesTab {waitFor {$("div.tabbable ul li a tab-heading", text:'Properties')}}
		attachmentTab {waitFor {$("div.tabbable ul li a tab-heading", text:'Attachments')}}
		childrenTab {waitFor {$("div.tabbable ul li a tab-heading", text:contains('Children'))}}
		coceptualDomainsTab {waitFor {$("div.tabbable ul li a tab-heading", text:contains('Conceptual Domains'))}}
		dataElementsTab {waitFor {$("div.tabbable ul li a tab-heading", text:'Data Elements')}}
		metadataTab {waitFor {$("div.tabbable ul li a tab-heading", text:'Metadata')}}

		draftIcon     {waitFor {$("h3 span.label-warning")}}
		finalizedIcon {waitFor {$("h3 span.label-primary")}}
	}
}