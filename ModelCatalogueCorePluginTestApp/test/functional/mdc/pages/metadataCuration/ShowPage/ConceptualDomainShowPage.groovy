package mdc.pages.metadataCuration.ShowPage

import mdc.pages.BasePageWithNav

/**
 * Created by soheil on 15/05/2014.
 */
class ConceptualDomainShowPage extends BasePageWithNav{

	static url = "#/catalogue/conceptualDomain/"

	static at = {
		url == "#/catalogue/conceptualDomain/" &&
		title ==~ /^Properties of .*$/
	}

	static content = {
		mainLabel { waitFor { $("h3.ce-name") }}
		description {waitFor { $("blockquote.ce-description")}}

		propertiesTab {waitFor {$("div.tabbable ul li a tab-heading", text:'Properties')}}
		modelsTab   {waitFor {$("div.tabbable ul li a tab-heading", text:contains('Models'))}}
		valueDomainsTab   {waitFor {$("div.tabbable ul li a tab-heading", text:contains('Value Domains'))}}

		propertiesTable(required:false) {waitFor {$("table#Properties")}}
		modelsTable {waitFor {$("div#-isContextFor table")}}
		valueDomainsTable {waitFor {$("div#-valueDomains table")}}

	}
}