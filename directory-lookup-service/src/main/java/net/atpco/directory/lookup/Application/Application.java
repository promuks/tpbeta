package net.atpco.directory.lookup.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableJSONDoc
@ComponentScan(basePackages = "net.atpco.directory")
@SpringBootApplication

public class Application extends SpringBootServletInitializer {
	
	public static final String DATE_FORMAT = "ddMMMyy";
	

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
   
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
	@Bean
	public MappingJackson2HttpMessageConverter jackson2Converter() {
		final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper());
		return converter;
	}

	@Bean
	public ObjectMapper objectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
				false);
		final DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		objectMapper.setDateFormat(df);
		return objectMapper;
	}
}
