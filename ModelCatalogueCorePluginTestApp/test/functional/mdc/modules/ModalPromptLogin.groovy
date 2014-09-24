package mdc.modules

import geb.Module

/**
 * Created by soheil on 23/09/2014.
 */
class ModalPromptLogin extends Module {

	static content = {
		loginForm(required: false) { $("form#modalPromptLogin") }
		username { loginForm.find('#username') }
		password { loginForm.find('#password') }
		rememberMe { loginForm.find('#remember_me') }
		submitButton { find("div.modal-dialog div.modal-footer button[type='submit']") }

		messagePanel { $("div.messages-panel div.alert span.ng-binding") }

	}

	def displayed() {
		loginForm.displayed
	}

	def loginAdminUser() {
		loginUser("admin", "admin")
	}

	def loginViewerUser() {
		loginUser("viewer", "viewer")
	}

	def loginCuratorUser() {
		loginUser("curator", "curator")
	}

	def loginUser(String user, String pass) {
		username = user
		password = pass
		submitButton.click()
	}
}
