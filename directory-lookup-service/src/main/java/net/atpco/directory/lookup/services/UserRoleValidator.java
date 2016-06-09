package net.atpco.directory.lookup.services;

import javax.servlet.http.HttpServletRequest;

import net.atpco.directory.lookup.config.AppConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class will validate user role 
 * 
 *
 */
@Service
public class UserRoleValidator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserRoleValidator.class);

	@Autowired
	private AppConfig appConfig;


	/**
	 * Check the user role is one of user roles who have access to tax
	 * calculator application
	 * @param httpRequest
	 * @return
	 */
	public boolean isUserHaveAccess(final HttpServletRequest httpRequest){
		LOGGER.debug("User role list:{}", appConfig.getRolesList());
		for (final String role : appConfig.getRolesList()) {
			if (httpRequest.isUserInRole(role.toUpperCase())) {
				return true;
			}
		}
		LOGGER.debug("User in role={}", false);
		return false;
	}
	
	

}
