import grails.rest.render.RenderContext
import org.modelcatalogue.core.CatalogueElement
import org.modelcatalogue.core.reports.ReportsRegistry
import org.modelcatalogue.core.testapp.Requestmap
import org.modelcatalogue.core.testapp.Role
import org.modelcatalogue.core.testapp.User
import org.modelcatalogue.core.testapp.UserRole
import org.modelcatalogue.core.util.ListWrapper
import org.modelcatalogue.core.util.marshalling.xlsx.XLSXListRenderer
import org.springframework.http.HttpMethod

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


		if(Requestmap.count()==0)
			configureRequestMap()

		//default roles : ROLE_USER, ROLE_ADMIN, ROLE_METADATA_CURATOR
		createRoles()

		environments {
			production {
				createMainAdminUser()
			}
			staging{
			}
			test{
				createUsers()
			}
			development {
				createUsers()
			}
		}
    }

	def configureRequestMap(){
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
				'/register/*','/errors', '/errors/*', '/index'
		]) {
			createRequestmapIfMissing(url,'permitAll',null)
		}

		createRequestmapIfMissing('/api/modelCatalogue/core/*/**', 'IS_AUTHENTICATED_FULLY',  org.springframework.http.HttpMethod.GET)
		createRequestmapIfMissing('/asset/download/*',             'IS_AUTHENTICATED_FULLY',  org.springframework.http.HttpMethod.GET)
		createRequestmapIfMissing('/api/modelCatalogue/core/*/**', 'ROLE_METADATA_CURATOR',   org.springframework.http.HttpMethod.POST)
		createRequestmapIfMissing('/api/modelCatalogue/core/*/**', 'ROLE_METADATA_CURATOR',   org.springframework.http.HttpMethod.PUT)
		createRequestmapIfMissing('/api/modelCatalogue/core/*/**', 'ROLE_METADATA_CURATOR',   org.springframework.http.HttpMethod.DELETE)


		//only permit admin user for Spring Security URIs
		createRequestmapIfMissing('/admin','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/admin/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/securityInfo/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/role','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/role/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/registrationCode','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/registrationCode/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/user','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/user/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/aclClass','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/aclClass/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/aclSid','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/aclSid/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/aclEntry','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/aclEntry/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/aclObjectIdentity','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/requestmap','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)
		createRequestmapIfMissing('/requestmap/**','ROLE_ADMIN, IS_AUTHENTICATED_FULLY',null)

	}

	def createRoles(){
		def roleUser = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
		def roleAdmin = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
		def metadataCurator = Role.findByAuthority('ROLE_METADATA_CURATOR') ?: new Role(authority: 'ROLE_METADATA_CURATOR').save(failOnError: true)
	}

	def createUsers(){

		def roleUser  = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
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
	}

	def createMainAdminUser(){

		def roleUser  = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
		def roleAdmin = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
		def metadataCurator = Role.findByAuthority('ROLE_METADATA_CURATOR') ?: new Role(authority: 'ROLE_METADATA_CURATOR').save(failOnError: true)

		def admin   = User.findByUsername('admin') ?: new User(username: 'admin', enabled: true, password: 'EgnAR@rQvhf8o').save(failOnError: true)

		if (!admin.authorities.contains(roleAdmin)) {
			UserRole.create admin, roleUser
			UserRole.create admin, metadataCurator
			UserRole.create admin, roleAdmin, true
		}
	}

    def destroy = {}

	private static Requestmap createRequestmapIfMissing(String url, String configAttribute, HttpMethod method = null) {
		Requestmap.findOrSaveByUrlAndConfigAttributeAndHttpMethod(url, configAttribute, method, [failOnError: true])
	}

}
