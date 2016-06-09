package net.atpco.directory.lookup.controller;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;

import net.atpco.directory.lookup.config.AppConfig;
import net.atpco.directory.lookup.services.LookupService;
import net.atpco.directory.lookup.services.UserRoleValidator;
import net.atpco.directory.lookup.util.LookupUtil;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * This class will do directory service lookup
 * 
 */
@Api(name = "LookUpController", description = "Directory service lookup")
@RestController
@RequestMapping("/ws/lookup")
public class LookUpController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LookUpController.class);
	
	@Autowired
	private AppConfig appConfig;
	
	@Autowired
	private LookupService lookupService;
	
	@Autowired
	private UserRoleValidator userRoleValidator;
	
	/*@ApiMethod(
	        path = "/ws/lookup/search/{type}/{value}",
	        description = "Directory lookup for a specific Lookup Type and value. Type can be carriers,cities(Origin and Destination)"+
	        " Value can be a specific carrier code like BA or a specific city like WAS",
	        produces = { MediaType.APPLICATION_JSON_VALUE }
	        )*/
	@RequestMapping(value = "/search/{type}/{value}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getDirectoryLookupDetails(			
			final HttpServletRequest httpRequest,
			final HttpServletResponse httpResponse,
			final @ApiPathParam @PathVariable("type") String searchType,
			final @ApiPathParam @PathVariable("value") String searchValue ) throws Exception {
		String response = null;		

		if(StringUtils.isEmpty(searchType) || StringUtils.isEmpty(searchValue) ){
			throw new BadRequestException("Bad Request");
		}
		
//		if (!userRoleValidator.isUserHaveAccess(httpRequest)) {
//			throw new AuthenticationException("Unauthorized Access") {
//			};
//		}
		if (appConfig.getSupportLookupList().contains(searchType)) {
			final long start = System.nanoTime();
			
			response=lookupService.getLookupResponse(searchType);
			response = LookupUtil.filterValue(response,searchValue.toUpperCase());
			
			final long elapsedTime = TimeUnit.MILLISECONDS.convert((System.nanoTime() - start), TimeUnit.NANOSECONDS);
			
			LOGGER.info("Total Time taken by LookUpController is={} in nanosecond", elapsedTime);
		}
		return response;
	}
	
	
	@ApiMethod(
	        path = "/ws/lookup/search/{type}",
	        description = "Directory lookup for a specific Lookup Type. Type can be carriers,cities(Origin and Destination),"+
	        "passenger types, currencies",
	        produces = { MediaType.APPLICATION_JSON_VALUE }
	        )
	@RequestMapping(value = "/search/{type}", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getDirectoryLookupDetailsByType(			
			final HttpServletRequest httpRequest,
			final HttpServletResponse httpResponse,
			final @ApiPathParam @PathVariable("type") String searchType) throws Exception{
		String response = null;		
		if(StringUtils.isEmpty(searchType)){
			throw new BadRequestException("Bad Request");
		}			
//		if (!userRoleValidator.isUserHaveAccess(httpRequest)) {
//			throw new AuthenticationException("Unauthorized Access") {
//			};
//		}
		if (appConfig.getSupportLookupList().contains(searchType)) {
			final long start = System.nanoTime();
			response=lookupService.getLookupResponse(searchType);
			final long elapsedTime = TimeUnit.MILLISECONDS.convert((System.nanoTime() - start), TimeUnit.NANOSECONDS);
			LOGGER.info("Total Time taken by LookUpController is={} in nanosecond", elapsedTime);
		}
		return response;
	}
	
	
	
	@ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadRequestException.class })
	public @ResponseBody String handleException(Exception ex) {
    	ex.printStackTrace();
		return "{\"code\":\"400\",  \"systemMessage\":" + ex.getMessage() + "}";
	}
	
	@ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ AuthenticationException.class })
	public @ResponseBody String handleUnAuthorizedException(Exception ex) {
    	ex.printStackTrace();
    	LOGGER.debug("Unauthorized Access.");
		return "{\"code\":\"401\",  \"systemMessage\":" + ex.getMessage() + "}";
		
	}
    
    @ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Exception.class })
	public @ResponseBody String handleApplicationException(Exception ex) {
    	ex.printStackTrace();
    	LOGGER.error("Exception occurred {} StackTrace={}", ex.getMessage(),ex.getStackTrace());
		LOGGER.error("Exception Details=", ex);
		return "{\"code\":\"500\",  \"systemMessage\":" + ex.getMessage() + "}";
	}

}
