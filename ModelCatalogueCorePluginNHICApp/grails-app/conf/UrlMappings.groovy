class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
		}
		"/app"(view:"/app/index")
        "/"(view:"/index")
	}
}
