import grails.rest.render.RenderContext
import org.modelcatalogue.core.actions.Action
import org.modelcatalogue.core.actions.Batch
import org.modelcatalogue.core.actions.CreateCatalogueElement
import org.modelcatalogue.core.reports.ReportsRegistry
import org.modelcatalogue.core.testapp.Requestmap
import org.modelcatalogue.core.testapp.UserRole
import org.modelcatalogue.core.testapp.Role
import org.modelcatalogue.core.testapp.User
import org.modelcatalogue.core.util.ListWrapper
import org.modelcatalogue.core.util.marshalling.xlsx.XLSXListRenderer
import org.modelcatalogue.core.*
import org.modelcatalogue.core.actions.TestAction

class BootStrap {

    def importService
    def domainModellerService
    def initCatalogueService
    def publishedElementService
    def executorService
    def actionService

    XLSXListRenderer xlsxListRenderer
    ReportsRegistry reportsRegistry

    def init = { servletContext ->

        initCatalogueService.initDefaultRelationshipTypes()
        initCatalogueService.initDefaultDataTypes()
        initCatalogueService.initDefaultMeasurementUnits()

        xlsxListRenderer.registerRowWriter('reversed') {
            title "Reversed DEMO Export"
            append metadata
            headers 'Description', 'Name', 'ID'
            when { ListWrapper container, RenderContext context ->
                context.actionName in ['index', 'search'] && container.itemType && CatalogueElement.isAssignableFrom(container.itemType)
            } then { CatalogueElement element ->
                [[element.description, element.name, element.id]]
            }
        }

        def roleUser = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def roleAdmin = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
        def metadataCurator = Role.findByAuthority('ROLE_METADATA_CURATOR') ?: new Role(authority: 'ROLE_METADATA_CURATOR').save(failOnError: true)

        def admin   = User.findByUsername('admin') ?: new User(username: 'admin', enabled: true, password: 'admin').save(failOnError: true)
        def viewer  = User.findByUsername('viewer') ?: new User(username: 'viewer', enabled: true, password: 'viewer').save(failOnError: true)
        def curator = User.findByUsername('curator') ?: new User(username: 'curator', enabled: true, password: 'curator').save(failOnError: true)


        if (!admin.authorities.contains(roleAdmin)) {
            UserRole.create admin, roleUser
            UserRole.create admin, metadataCurator
            UserRole.create admin, roleAdmin, true
        }

        if (!curator.authorities.contains(metadataCurator)) {
            UserRole.create curator, roleUser
            UserRole.create curator, metadataCurator
        }

        if (!viewer.authorities.contains(viewer)) {
            UserRole.create viewer, roleUser
        }

        //permit all for assets and initial pages
        for (String url in [
                '/',
                '/**/favicon.ico',
                '/fonts/**',
                '/assets/**',
                '/plugins/**/js/**',
                '/js/vendor/**',
                '/**/*.less',
                '/**/js/**',
                '/**/css/**',
                '/**/images/**',
                '/**/img/**',
                '/login/','/login.*', '/login/*',
                '/logout', '/logout.*', '/logout/*',
                '/register/*', '/errors', '/errors/*', '/index'
        ]) {
            new Requestmap(url: url, configAttribute: 'permitAll').save(failOnError: true)
        }

        new Requestmap(url: '/api/modelCatalogue/core/*/**', configAttribute: 'IS_AUTHENTICATED_FULLY',   httpMethod: org.springframework.http.HttpMethod.GET).save(failOnError: true)
        new Requestmap(url: '/asset/download/*',             configAttribute: 'IS_AUTHENTICATED_FULLY',   httpMethod: org.springframework.http.HttpMethod.GET).save(failOnError: true)
        new Requestmap(url: '/api/modelCatalogue/core/*/**', configAttribute: 'ROLE_METADATA_CURATOR',    httpMethod: org.springframework.http.HttpMethod.POST).save(failOnError: true)
        new Requestmap(url: '/api/modelCatalogue/core/*/**', configAttribute: 'ROLE_METADATA_CURATOR',    httpMethod: org.springframework.http.HttpMethod.PUT).save(failOnError: true)
        new Requestmap(url: '/api/modelCatalogue/core/*/**', configAttribute: 'ROLE_METADATA_CURATOR',    httpMethod: org.springframework.http.HttpMethod.DELETE).save(failOnError: true)



		//only permit admin user for Spring Security URIs
		new Requestmap(url: '/admin', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/admin/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/securityInfo/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/role', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/role/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/registrationCode', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/registrationCode/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/user', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/user/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/aclClass', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/aclClass/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/aclSid', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/aclSid/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/aclEntry', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/aclEntry/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/aclObjectIdentity', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/requestmap', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/requestmap/**', configAttribute: 'ROLE_ADMIN, IS_AUTHENTICATED_FULLY').save()


		//protect AngularApp
		//user should have login before accessing this page
		new Requestmap(url: '/app',  configAttribute: 'IS_AUTHENTICATED_FULLY').save()
		new Requestmap(url: '/app/**', configAttribute: 'IS_AUTHENTICATED_FULLY').save()

        environments {
            development {
            }
        }


    }

    def destroy = {}

}
