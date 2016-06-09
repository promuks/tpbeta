package net.atpco.fm.bootstrap.Application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableJSONDoc
@ComponentScan(basePackages = {"net.atpco.fm"})
@SpringBootApplication
@PropertySources({
	@org.springframework.context.annotation.PropertySource(value = "file:${catalina.base}/properties/fm-properties.properties", ignoreResourceNotFound = true),
	@org.springframework.context.annotation.PropertySource(value = "file:${catalina.base}/properties/tp-beta-properties.properties", ignoreResourceNotFound = true) })
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Autowired private Environment env;
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
    @Bean
    public RestTemplate restTemplate() {
    	RestTemplate restTemplate = new RestTemplate();
    	return restTemplate;
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
        MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter(); 
        return jacksonMessageConverter;
    }
    
    @Bean(name="propertiesSet")
    public Map<String, String> propertiesSet() {
    	Map<String, String> appProperties = new HashMap<String, String>();
		for (Iterator<?> it = ((AbstractEnvironment) env).getPropertySources()
				.iterator(); it.hasNext();) {
			PropertySource<?> propertySource = (PropertySource<?>) it.next();
			if (propertySource instanceof ResourcePropertySource) {
				setAppProperties(appProperties, propertySource);
			}else if (propertySource instanceof CompositePropertySource) {
		        for(Iterator<PropertySource<?>> it2 = ((CompositePropertySource) propertySource).getPropertySources().iterator(); it2.hasNext(); ) {
		            PropertySource<?> propertySource2 = (PropertySource<?>) it2.next();
		            if (propertySource2 instanceof ResourcePropertySource) {
		            	setAppProperties(appProperties, propertySource2);
		            }
		        }
		    }
		}
		return appProperties;
	}

	private void setAppProperties(Map<String, String> appProperties,
			PropertySource<?> propertySource) {
		Iterator<Entry<String, Object>> iter = ((MapPropertySource) propertySource)
				.getSource().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry pair = (Map.Entry) iter.next();
			LOGGER.info("Properties:"+(String) pair.getKey()+"::"+
					(String) pair.getValue());
			appProperties.put((String) pair.getKey(),
					(String) pair.getValue());
		}
	}

}
