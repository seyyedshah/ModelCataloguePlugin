package mdc.modules

import geb.Module

class TopNavElements extends Module {

	static String searchTextInput = "input#search-term"
	static String searchResultUl = "form[role='search'] ul.dropdown-menu"
	static String loginButton ="form.navbar-form.navbar-right button#loginButton"
	static String logoutButton ="form.navbar-form.navbar-right button#logoutButton"


	static String  classificationLink  = "li a#navbar-classification-menu-item-link"
	static String  modelLink		   = "li a#navbar-model-menu-item-link"
	static String  dataElementLink 	   = "li a#navbar-dataElement-menu-item-link"
	static String conceptualDomainLink = "li a#navbar-classification-menu-item-link"
	static String  valueDomainLink     = "li a#navbar-valueDomain-menu-item-link"
	static String  dataTypeLink    	   = "li a#navbar-dataType-menu-item-link"
	static String  measurementUnitLink = "li a#navbar-measurementUnit-menu-item-link"
	static String assetLink 		   = "li a#navbar-asset-menu-item-link"

	static content = {
		navPresentAndVisible(required:false) {$("div.navbar")}
	}
}