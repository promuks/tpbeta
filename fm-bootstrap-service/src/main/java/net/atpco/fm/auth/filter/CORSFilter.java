package net.atpco.fm.auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component("corsFilter")
public class CORSFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(CORSFilter.class); 
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Auth-Token");
	    response.addHeader("Access-Control-Expose-Headers", "X-Auth-Token");
	    if (((HttpServletRequest) req).getMethod().equals(HttpMethod.OPTIONS.name())) {
	        try {
	            response.getWriter().print("OK");
	            response.getWriter().flush();
	        } catch (IOException e) {
	        	logger.error("Error in CORSFilter - {} ",  e.getMessage());
	        }
	    } else {
	    	chain.doFilter(req, res);
	    }		
	}

	@Override
	public void init(FilterConfig filterConfig) {		
	}

	@Override
	public void destroy() {		
	}

}