package mdc.spec.authentication;
import geb.spock.GebReportingSpec
import mdc.pages.DashboardPage;

class UserLoginSpec extends GebReportingSpec {

	def "User authentication attempts (bad)"() {

		when:"User logs in by an invalid username/password"
		to DashboardPage
		waitFor {
			at DashboardPage
		}
		showLoginModal()
		modalPromptLogin.username = "badUser"
		modalPromptLogin.password = "badPassord"
		modalPromptLogin.submitButton.click()

		then: 'Then an error message is displayed stating the combination is incorrect and I can try again'
		waitFor{
			modalPromptLogin.messagePanel.displayed
		}
		modalPromptLogin.messagePanel.text() == "Sorry, we were not able to find a user with that username and password."
	}
	
	def "Successful second authentication"() {
		when: 'I enter the incorrect credentials'
		to DashboardPage
		waitFor {
			at DashboardPage
		}
		showLoginModal()
		modalPromptLogin.username = "badUser"
		modalPromptLogin.password = "badPassord"
		modalPromptLogin.submitButton.click()

		then: 'Then an error message is displayed stating the combination is incorrect and I can try again'
		waitFor{
			modalPromptLogin.messagePanel.displayed
		}
		modalPromptLogin.messagePanel.text() == "Sorry, we were not able to find a user with that username and password."


		when: 'I enter the correct credentials'
		modalPromptLogin.username = "viewer"
		modalPromptLogin.password = "viewer"
		modalPromptLogin.submitButton.click()

		then: 'I am taken to the dashboard page'
		waitFor{
			at DashboardPage
		}
	}
}