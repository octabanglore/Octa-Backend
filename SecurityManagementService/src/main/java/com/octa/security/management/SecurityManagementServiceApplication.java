package com.octa.security.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages = {"com.octa"})
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class,DataSourceAutoConfiguration.class})
public class SecurityManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityManagementServiceApplication.class, args);
	}

	@Bean
	WebMvcConfigurer crossConfiguration() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/v1/authenticate/*").allowedOrigins("http://localhost:3000");
			}
		};
	}

}
