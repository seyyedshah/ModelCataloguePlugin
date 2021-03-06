// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

       debug 'grails.app.services.org.modelcatalogue'
       debug 'grails.app.controllers.org.modelcatalogue'

       debug 'org.modelcatalogue.core.dataarchitect.xsd.XSDImporter'

       debug 'org.modelcatalogue.core.util.builder'
       debug 'org.modelcatalogue.core.publishing'
       debug 'org.modelcatalogue.core.util.test'
       debug 'org.modelcatalogue.core.gel'
       debug 'org.modelcatalogue.discourse'

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}


environments {
       development {
              grails.logging.jul.usebridge = true
              grails.serverURL = "http://localhost:${System.getProperty('server.port') ?: 8080}/ModelCatalogueCorePluginTestApp"
       }
       local {
              grails.logging.jul.usebridge = true
              grails.serverURL =  "http://localhost:${System.getProperty('server.port') ?: 8080}/ModelCatalogueCorePluginTestApp"
       }
       test {
              grails.plugin.console.enabled = true
              grails.serverURL =  "http://localhost:${System.getProperty('server.port') ?: 8080}/ModelCatalogueCorePluginTestApp"
       }
       production {
              grails.logging.jul.usebridge = false
              grails.serverURL = System.getenv('METADATA_SERVER_URL') ?:  "http://localhost:${System.getProperty('server.port') ?: 8080}/ModelCatalogueCorePluginTestApp"
       }
}
