package com.pentyugov.wflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentyugov.wflow.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DatabaseConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class WflowApplicationTests {

private static final String EMAIL_USER = "login";
private static final String EMAIL_PASSWORD = "password";


	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper mapper;


}