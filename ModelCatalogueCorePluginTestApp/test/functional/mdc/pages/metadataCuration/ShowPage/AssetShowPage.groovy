package mdc.pages.metadataCuration.ShowPage

import mdc.pages.BasePageWithNav

/**
 * Created by soheil on 15/05/2014.
 */
class AssetShowPage extends BasePageWithNav{

	static url = "#/catalogue/asset/"

	static String actionButtons = "div.contextual-actions button"

	static at = {
		url == "#/catalogue/asset/" &&
		title ==~ /^Properties of .*$/
	}

	static content = {
		mainLabel   {waitFor { $("h3.ce-name") }}
		description {waitFor { $("blockquote.ce-description")}}

		propertiesTab {waitFor {$("div.tabbable ul li a tab-heading", text:'Properties')}}
		metadataTab   {waitFor {$("div.tabbable ul li a tab-heading", text:'Metadata')}}
	}
}