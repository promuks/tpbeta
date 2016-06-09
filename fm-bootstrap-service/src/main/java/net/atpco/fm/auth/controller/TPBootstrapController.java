package net.atpco.fm.auth.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.atpco.exception.ApplicationException;
import net.atpco.fm.auth.model.UIResponse;

import org.apache.catalina.realm.GenericPrincipal;
import org.jsondoc.core.annotation.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(name = "TP Bootstrap Service", description = "Methods to get authorize,get Properties and proxy urls")
@RestController
@RequestMapping("/api/bootstrap/")
public class TPBootstrapController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TPBootstrapController.class);
		
	@Resource(name="propertiesSet")
	private Map<String, String> propertiesSet;
	
	private static final String ALLWD_ROLES = "fm.app.allowed.roles";

		
	public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat(
			"dd MMM yyyy HH:mm z");
	
    @RequestMapping(value = "authorize", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
        public UIResponse authorize(final HttpServletRequest httpRequest,
			final HttpServletResponse httpResponse) {
    		LOGGER.info("Coming here=", httpRequest.toString());
			final UIResponse uiResponse = new UIResponse();
			try {
				uiResponse.setAppProperties(filteredPropertiesSet(propertiesSet));
				if (doesUserHaveAccess(httpRequest, uiResponse)) {
					httpResponse.setStatus(HttpServletResponse.SC_OK);
					/*uiResponse.setAppProps(tpCalcProperties);*/
					//uiResponse.setAppProperties(appProperties/*fmPropertiesServiceImpl.getAppProperties()*/);	
				} else {
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			} catch (Exception e) {
				httpResponse
						.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				LOGGER.error("Exception occure {} StackTrace={}", e.getMessage(),
						e.getStackTrace());
				LOGGER.error("Exception Details=", e);
			}
			return uiResponse;
		}

	public static Map<String, String> filteredPropertiesSet(Map<String, String> propSet) {

		Map<String, String> filteredSet = new HashMap<String, String>();
		for (Iterator<?> it = propSet.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			if (!key.startsWith("/api/") && !key.equalsIgnoreCase(ALLWD_ROLES)) {
				filteredSet.put(key, propSet.get(key));
			}
		}

		return filteredSet;
	}


	@RequestMapping(value = "invalidatesession", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public void invalidateSession(final HttpServletRequest httpRequest,
			final HttpServletResponse httpResponse) {

		try {
			if (httpRequest.getSession(false) != null) {
				LOGGER.info("Invalidate Session={}",
						httpRequest.getSession(false));
				httpRequest.getSession(false).invalidate();
			} else {
				LOGGER.info("No session found");
			}
		} catch (Exception e) {
			httpResponse
					.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			LOGGER.error("Exception occured {} StackTrace={}", e.getMessage(),
					e.getStackTrace());
			LOGGER.error("Exception Details=", e);
		}

	}

	
    /**
	 * Check the user role is one of user roles who have access to tax
	 * calculator application
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean doesUserHaveAccess(final HttpServletRequest httpRequest,
			final UIResponse uiResponse) {

		final Date currentDate = new Date();
		if (httpRequest.getUserPrincipal() != null) {
			LOGGER.info("login user id={}", httpRequest.getUserPrincipal()
					.getName());
			uiResponse.setUserName(httpRequest.getUserPrincipal().getName());
			uiResponse.setAccessTime(DATEFORMAT.format(currentDate));
		} else {
			LOGGER.info("UserPrincipal is null");
		}


		List<String> allowedRoles = new ArrayList<String>(); 
		if(propertiesSet.get(ALLWD_ROLES)!=null){
			LOGGER.info("Allowed Property Roles", propertiesSet.get(ALLWD_ROLES));
			allowedRoles = Arrays.asList(propertiesSet.get(ALLWD_ROLES).split(","));
		}
		final Principal userPrincipal = httpRequest.getUserPrincipal();
	    GenericPrincipal genericPrincipal = (GenericPrincipal) userPrincipal;
	    final String[] roles = genericPrincipal.getRoles();
	    LOGGER.info("All roles from http:"+roles.toString());
		for (final String role : allowedRoles) {
			if (httpRequest.isUserInRole(role.toUpperCase())) {
				uiResponse.setCanAccess(true);
				//uiResponse.setUserRole(role.toUpperCase());
				return true;
			}
		}
		LOGGER.debug("User in role={}", false);
		return false;
	}
	

    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ Exception.class })
	public @ResponseBody String handleException(Exception ex) {
    	ex.printStackTrace();
		return "{\"code\":\"400\",  \"systemMessage\":" + ex.getMessage() + "}";
	}
    
    @ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ ApplicationException.class })
	public @ResponseBody String handleApplicationException(Exception ex) {
    	ex.printStackTrace();
		return "{\"code\":\"500\",  \"systemMessage\":" + ex.getMessage() + "}";
	}
    
}

