package org.modelcatalogue.core

import geb.spock.GebReportingSpec
import org.modelcatalogue.core.pages.HomePage
import spock.lang.Specification

/**
 * Created by soheil on 23/09/2014.
 */
class HomePageSpec extends GebReportingSpec{

	def "test"(){

		when:
		to HomePage

		then:
		at HomePage

	}
}