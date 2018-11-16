package com.latidude99;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import com.latidude99.model.FromView;
import com.latidude99.service.UserService;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter{
	
	@Autowired
	UserService userService;
	
	
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		userService.addAdminRole(); //("latidude99test@gmail.com"); 
		userService.addDemoUserRole("Demo");
		userService.addDemoUserRole("Test");
	}
	
	@Bean
	   public LocaleResolver localeResolver() {
	       SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
	       sessionLocaleResolver.setDefaultLocale(Locale.US);
	       return sessionLocaleResolver;
	   }
	 
   @Bean
   public LocaleChangeInterceptor localeChangeInterceptor() {
       LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
       lci.setParamName("lang");
       return lci;
   }
	 
   @Override
   public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(localeChangeInterceptor());
   }
   
   @Bean //optional?
   public Java8TimeDialect java8TimeDialect() {
       return new Java8TimeDialect();
   }
   
   @Bean
   public FromView fromView() {
	   return new FromView();
   }
	

}












