import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Created by soheil on 11/09/2014.
  CustomAuthenticationHandler class will manage users welcome page
  */
public class CustomAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		String appTargetUrl  = "/app"
		getRedirectStrategy().sendRedirect(request, response, appTargetUrl);
	}
}