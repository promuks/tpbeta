package net.atpco.directory.lookup.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
@ConfigurationProperties("dirservice")
@PropertySources({
	@PropertySource(value = "file:${catalina.base}/properties/cluster.properties", ignoreResourceNotFound = true)
	//--This is for local only
	//@PropertySource(value = "classpath:config/cluster.properties")
})
@Getter @Setter
public class LookupServiceSettings extends WebMvcConfigurerAdapter {
	
	@Value("${http.rest.service.hosts.directory}")
	private String hostDirectory;
	
	@Value("${http.rest.service.ping.directory}")
	private String pingDirectory;
	
	@Value("${http.rest.serice.poll.interval:600000}")
	private String poolInterval;
	
	private List<String> supportLookupList;
	
	private List<String> rolesList;

}
