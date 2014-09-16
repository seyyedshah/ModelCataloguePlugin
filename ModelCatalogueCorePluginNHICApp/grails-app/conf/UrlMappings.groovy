class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
		}
		"/register/changePassword"(controller:"register", action: "changePassword")
		"/register/register"(controller:"register", action: "register")
		"/register/"(uri:"/")
		"/register/index"(uri:"/")
		"/login/auth"(view:"/?login=true")

		"403"(controller: "errors", action: "error403")
		"404"(controller: "errors", action: "error404")
		"500"(controller: "errors", action: "error500")

		"/"(view:"/index")
	}
}