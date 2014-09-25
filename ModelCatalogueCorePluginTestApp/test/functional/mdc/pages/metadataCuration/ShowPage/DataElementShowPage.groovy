package mdc.pages.metadataCuration.ShowPage

import mdc.pages.BasePageWithNav

/**
 * Created by soheil on 15/05/2014.
 */
class DataElementShowPage extends BasePageWithNav {

	static url = "#/catalogue/dataElement/"

	static at = {
		url == "#/catalogue/dataElement/" &&
		title ==~ /^Properties of .*$/
	}

	static content = {
		mainLabel(wait:true) {  $("h3.ce-name") }
		description(wait:true) { $("blockquote.ce-description")}


		propertiesTab {waitFor {$("div.tabbable ul li a tab-heading", text:'Properties')}}
		modelsTab   {waitFor {$("div.tabbable ul li a tab-heading", text:contains('Models'))}}
		relationshipsTab   {waitFor {$("div.tabbable ul li a tab-heading", text:contains('Relationships'))}}
		metadataTab   {waitFor {$("div.tabbable ul li a tab-heading", text:contains('Metadata'))}}

		propertiesTable(required:false) {waitFor {$("table#Properties")}}
		modelsTable {waitFor {$("div#-isContextFor table")}}
		relationshipsTable {waitFor {$("div#-relationships table")}}
		metadataTable {waitFor {$("table#Metadata")}}

	}
}