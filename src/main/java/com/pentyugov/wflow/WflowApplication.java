package com.pentyugov.wflow;

import com.pentyugov.wflow.application.configuration.constant.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
public class WflowApplication extends SpringBootServletInitializer {

	private static Logger logger = LoggerFactory.getLogger(WflowApplication.class);

	public static List<String> allowedOrigins;

	public static void main(String[] args) {
		SpringApplication.run(WflowApplication.class, args);
		createUserFolder();
		logger.info("ALLOWED ORIGINS: " + WflowApplication.allowedOrigins.toString());
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WflowApplication.class);
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList(
				"http://wflow-app.ru",
				"http://wflow-app.ru/ws",
				"http://wflow-app.ru/wss",
				"http://localhost:4200",
				"http://localhost:4200/ws",
				"http://localhost:4200/wss"
		));

		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

		WflowApplication.allowedOrigins = corsConfiguration.getAllowedOrigins();
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

	private static void createUserFolder() {
		new File(ApplicationConstants.File.USER_FOLDER).mkdirs();
	}
}