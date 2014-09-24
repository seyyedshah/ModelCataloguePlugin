package mdc.pages

import geb.Page
import mdc.modules.FooterNav
import mdc.modules.ModalPromptLogin
import mdc.modules.TopNavElements

class BasePageWithNav extends Page {

	static at = {
		nav.navPresentAndVisible.displayed
	}

	static content = {
		nav { module TopNavElements }
		footer { module FooterNav }
		modalPromptLogin { module ModalPromptLogin }
	}
}