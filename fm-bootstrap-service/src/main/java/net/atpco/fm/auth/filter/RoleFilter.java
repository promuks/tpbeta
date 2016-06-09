package net.atpco.fm.auth.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("roleFilter")
public class RoleFilter implements Filter {

	private static final String HEALTH_URI = "health";
	private static final String AUTH_URI = "/api/bootstrap/authorize";

	private static final Logger logger = LoggerFactory.getLogger(RoleFilter.class); 
	
	@Value(value = "${fm.app.allowed.roles}")
	private String tpRole;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		if(request.getRequestURI().contains(HEALTH_URI) || request.getRequestURI().contains(AUTH_URI)) {
			chain.doFilter(req, res);
		} else {
			Principal userPrincipal = request.getUserPrincipal();
			if(userPrincipal == null) {
				logger.error("User principal not found");
		        throw new ServletException("User principal not found");
			}
			if (request.isUserInRole(tpRole.toUpperCase())) {
				chain.doFilter(req, res);
		    } else {
				logger.error("User not authenticated for role: {}", tpRole);
		        throw new ServletException("Not Authenticated");
		    }
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {		
	}

	@Override
	public void destroy() {		
	}

}
