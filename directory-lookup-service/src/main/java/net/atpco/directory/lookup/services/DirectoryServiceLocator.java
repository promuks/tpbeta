package net.atpco.directory.lookup.services;

import net.atpco.cluster.support.RestServiceLocator;

/**
 * This is the framework directory service lookup class for load balancing
 * @author atp1ssg
 *
 */
public class DirectoryServiceLocator extends RestServiceLocator<LookupServiceImpl>{

	public DirectoryServiceLocator(final String serviceURL, final String serviceName,
			final boolean cleanup, final Integer servicePollInterval) {
		super(serviceURL, serviceName, cleanup, servicePollInterval);		
	}
	
	@Override
	public LookupServiceImpl getNewService(final String serviceURL) {		
		return  new LookupServiceImpl(serviceURL);
	}
}
