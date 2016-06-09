package net.atpco.directory.lookup.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * This interface for lookup service
 *
 */
@SuppressWarnings("all")
@Service
public interface LookupService {
	
	public String getLookupResponse(String searchType,String searchValue) throws Exception;
	
	public String getLookupResponse(String searchType)throws Exception;
	
}
