package net.atpco.directory.lookup.services;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * This is the service class for the lookup services
 *
 */

@Component
public class LookupServiceImpl implements LookupService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LookupServiceImpl.class);
	
	private static final String URI_FIRST_PART="rest";
	
	private static final String URI_DELIMITER="/";
	
	private static final String URI_SECOND_PART="search";
	
	@Getter @Setter private String serviceURL;	
	
	@Autowired
	@Getter @Setter private RestTemplate restTemplate;
	
	@Autowired
	@Getter @Setter private DirectoryServiceLocator directoryServiceLocator;
	
	/**
	 * Regular constructor
	 */
	public LookupServiceImpl() {
		super();
	}
	
	/**
	 * Constructor to set service url needed by the DirectoryServiceLocator
	 * @param serviceURL
	 */
	public LookupServiceImpl(final String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String getLookupResponse(final String searchType,final String searchValue) throws Exception{
		String response = null;		
		LOGGER.debug("Inside LookupServiceImpl searchType={}, searchValue={}.",searchType,searchValue);
		if(!StringUtils.isEmpty(searchType) && !StringUtils.isEmpty(searchValue)){	
			String lookupURI = URI_DELIMITER+URI_FIRST_PART+URI_DELIMITER+searchType
						+URI_DELIMITER+URI_SECOND_PART+URI_DELIMITER+searchValue;

			final StringBuilder lookUpURL = new StringBuilder(getDirectoryServiceLocator().locate().getServiceURL());			
			lookUpURL.append(lookupURI);		
			
			LOGGER.debug(lookUpURL.toString());
			
			final long start = System.nanoTime();
			response = getRestTemplate().getForObject(lookUpURL.toString(), String.class);	
			final long elapsedTime = TimeUnit.MILLISECONDS.convert((System.nanoTime() - start), TimeUnit.NANOSECONDS);
			
			LOGGER.info("Time taken by lookup service is={} in nanosecond", elapsedTime);
		}
		return response;
	}	
	
	@Cacheable(value="directorycache",key="#searchType")
	public String getLookupResponse(String searchType) throws Exception {
		String response = null;		
		if(!StringUtils.isEmpty(searchType)){
			
			final StringBuilder lookUpURL = new StringBuilder(getDirectoryServiceLocator().locate().getServiceURL());			
			lookUpURL.append(URI_DELIMITER+URI_FIRST_PART+URI_DELIMITER+searchType);
			LOGGER.debug(lookUpURL.toString());
			
			final long start = System.nanoTime();
			response = getRestTemplate().getForObject(lookUpURL.toString(), String.class);	
			final long elapsedTime = TimeUnit.MILLISECONDS.convert((System.nanoTime() - start), TimeUnit.NANOSECONDS);
			
			LOGGER.info("Time taken by lookup service is={} in nanosecond", elapsedTime);
		}
		return response;
	}
						
}