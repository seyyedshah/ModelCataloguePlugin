class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
		}
		"/app"(view:"/app/index")
		"/register/changePassword"(controller:"register", action: "changePassword")
        "/"(view:"/index")
	}
}
