import org.modelcatalogue.core.security.ajax.AjaxAwareLoginUrlAuthenticationEntryPoint

// Place your Spring DSL code here
beans = {
    authenticationEntryPoint(AjaxAwareLoginUrlAuthenticationEntryPoint) {
        loginFormUrl = '/login/auth' // has to be specified even though it's ignored
//		loginFormUrl = '/?login' // has to be specified even though it's ignored
		portMapper = ref('portMapper')
        portResolver = ref('portResolver')
    }

	//CustomAuthenticationHandler class will manage users welcome page
	//authenticationSuccessHandler is a Spring Security bean for success authenticationHandler
	authenticationSuccessHandler(CustomAuthenticationHandler)

}