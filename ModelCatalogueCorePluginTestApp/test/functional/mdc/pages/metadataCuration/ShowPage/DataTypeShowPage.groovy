package mdc.pages.metadataCuration.ShowPage

import mdc.pages.BasePageWithNav

/**
 * Created by soheil on 15/05/2014.
 */
class DataTypeShowPage extends BasePageWithNav {

	static url = "#/catalogue/dataType/"

	static at = {
		url == "#/catalogue/dataType/" &&
				title ==~ /^Properties of .*$/
	}

	static content = {
		mainLabel(wait:true) {  $("h3.ce-name") }
		description(wait:true) { $("blockquote.ce-description")}

		valueDomainsTab{waitFor {$("div.tabbable ul li a tab-heading", text:contains('Value Domains'))}}
		propertiesTab  {waitFor {$("div.tabbable ul li a tab-heading", text:contains('Properties'))}}

		valueDomainsTable {waitFor {$("div#-valueDomains table")}}
		propertiesTable(required:false) {waitFor {$("table#Properties")}}
	}
}