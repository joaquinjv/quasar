package com.ml.quasar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Quasar configuration class.
 * 
 */
@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = "quasar")
public class QuasarConfig implements WebMvcConfigurer {

	 /**
     * Set up the bean ResourceBundleMessageSource
     * 
     * @return
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.setBasenames("messages/messages");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addRedirectViewController("/", "/index.html");
    }
	
	@Bean
	public Map<String, String> dataProperties() {
		Map<String, String> hashMap = new HashMap<String, String>();
		try (InputStream input = new FileInputStream("src/main/resources/data.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            
            hashMap.put("kenobi", properties.get("kenobi").toString());
            hashMap.put("skywalker", properties.get("skywalker").toString());
            hashMap.put("sato", properties.get("sato").toString());
            
            hashMap.put("kenobi_testdistance", properties.get("kenobi_testdistance").toString());
            hashMap.put("skywalker_testdistance", properties.get("skywalker_testdistance").toString());
            hashMap.put("sato_testdistance", properties.get("sato_testdistance").toString());
        } catch (IOException io) {
            io.printStackTrace();
        }	
		return hashMap;
	}

}
