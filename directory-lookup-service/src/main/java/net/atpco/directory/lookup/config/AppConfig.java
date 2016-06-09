package net.atpco.directory.lookup.config;

import java.util.List;

import net.atpco.directory.lookup.services.DirectoryServiceLocator;
import net.atpco.directory.lookup.services.LookupService;
import net.atpco.directory.lookup.services.LookupServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
public class AppConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
	
	@Autowired
	private LookupServiceSettings userSettings;
	
	@Bean
	public CacheManager getEhCacheManager(){
	  return  new EhCacheCacheManager(getEhCacheFactory().getObject());
	}
	
	@Bean
	public EhCacheManagerFactoryBean getEhCacheFactory(){
		EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
		factoryBean.setConfigLocation(new ClassPathResource("config/ehcache.xml"));
		factoryBean.setShared(true);
		return factoryBean;
	}
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}	
	
	@Bean
	public DirectoryServiceLocator directoryServiceLocator(){
		LOGGER.info("DirectoryServiceLocator Values are hostDirectory={} , pingDirectory={}, poolInterval={}"
				,userSettings.getHostDirectory(),userSettings.getPingDirectory(),userSettings.getPoolInterval());
		return new DirectoryServiceLocator(userSettings.getHostDirectory(),userSettings.getPingDirectory(),
					true,Integer.parseInt(userSettings.getPoolInterval()));
	}
	
	@Bean
	public LookupService lookupService() {
		return new LookupServiceImpl();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	public List<String> getSupportLookupList() {
		return userSettings.getSupportLookupList();
	}
	
	public List<String> getRolesList() {
		return userSettings.getRolesList();
	}
}
