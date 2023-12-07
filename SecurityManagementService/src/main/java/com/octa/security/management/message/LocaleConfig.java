package com.octa.security.management.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocaleConfig extends WebMvcConfigurationSupport {

	@Autowired
    private DataBaseLabelSource labelMessageSource;

    @Autowired
    private DataBaseMessgeSource messageMessageSource;
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/authenticate/**") // Map your endpoints here
                .allowedOrigins("http://localhost:3000"); // Replace with your frontend application's origin
              
    }
    
	@Bean
	@Override
	public LocaleResolver localeResolver() {
		return new RestLocaleResolver();
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }
	
	@Bean
     MessageSource labelMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
       // messageSource.setBasenames("classpath:labels/labels"); // Change the path accordingly
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setParentMessageSource(labelMessageSource);
        return messageSource;
    }
	
	@Bean
     MessageSource messageMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
      //  messageSource.setBasenames("classpath:messages/messages"); // Change the path accordingly
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setParentMessageSource(messageMessageSource);
        return messageSource;
    }

	/*
	 * @Bean
	 * 
	 * @Primary public MessageSource compositeMessageSource() {
	 * CompositeMessageSource compositeMessageSource = new CompositeMessageSource();
	 * compositeMessageSource.addMessageSource(labelMessageSource());
	 * compositeMessageSource.addMessageSource(messageMessageSource()); return
	 * compositeMessageSource; }
	 */
}
