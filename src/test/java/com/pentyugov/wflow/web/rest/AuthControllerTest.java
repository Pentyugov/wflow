package com.pentyugov.wflow.web.rest;

import com.google.gson.Gson;
import com.pentyugov.wflow.AbstractTest;
import com.pentyugov.wflow.core.service.ApplicationService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.payload.request.LoginRequest;
import com.pentyugov.wflow.web.payload.request.SignUpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Security.AUTHORIZATION;
import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Security.JWT_TOKEN_HEADER;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest extends AbstractTest {

    @Autowired
    protected AuthController authController;
    @Autowired
    protected ApplicationService applicationService;
    @Autowired
    protected UserService userService;

    protected SignUpRequest signUpRequest;

    @AfterEach
    void tearDown() {
        userService.removeUserByUsername(signUpRequest.getUsername());
    }

    @BeforeEach
    void setUp() {
        String rawPassword = applicationService.generatePassword();

        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("TestUser");
        signUpRequest.setPassword(rawPassword);
        signUpRequest.setConfirmPassword(rawPassword);
        signUpRequest.setEmail("testUser@example.com");
        signUpRequest.setFirstname("TestUserFN");
        signUpRequest.setLastname("TestUserLN");
    }

    @Test
    void authControllerNotNull() {
        assertNotNull(authController);
    }

    @Test
    void registerUser() throws Exception {

        String url = "/api/auth/register";
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(new Gson().toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUser() throws Exception {
        registerUser();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(signUpRequest.getUsername());
        loginRequest.setPassword(signUpRequest.getPassword());

        String url = "/api/auth/login";

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(new Gson().toJson(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists(JWT_TOKEN_HEADER));
    }

    @Test
    void changePasswordWithoutEmail() throws Exception {
        registerUser();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(signUpRequest.getUsername());
        loginRequest.setPassword(signUpRequest.getPassword());

        String loginUrl = "/api/auth/login";
        String changePsqUrl = "/api/auth/change-password";

        String token = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(loginUrl)
                        .content(new Gson().toJson(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getHeader(JWT_TOKEN_HEADER);

        String rawPassword = applicationService.generatePassword();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, token);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(changePsqUrl)
                        .param("password", rawPassword)
                        .param("confirmPassword", rawPassword)
                        .headers(httpHeaders)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void changePasswordWithEmail() throws Exception {
        registerUser();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(signUpRequest.getUsername());
        loginRequest.setPassword(signUpRequest.getPassword());

        String loginUrl = "/api/auth/login";
        String changePsqUrl = "/api/auth/change-password";

        String token = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(loginUrl)
                        .content(new Gson().toJson(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getHeader(JWT_TOKEN_HEADER);

        String rawPassword = applicationService.generatePassword();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, token);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(changePsqUrl)
                        .param("email", signUpRequest.getEmail())
                        .param("password", rawPassword)
                        .param("confirmPassword", rawPassword)
                        .headers(httpHeaders)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void failLoginWithWrongUsername() throws Exception {
        String substring = "\"message\":\"USERNAME / PASSWORD INCORRECT. PLEASE TRY AGAIN\"";

        registerUser();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wrongUsername");
        loginRequest.setPassword(signUpRequest.getPassword());

        String loginUrl = "/api/auth/login";

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(loginUrl)
                        .content(new Gson().toJson(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(substring)));

    }

    @Test
    void failLoginWithWrongPassword() throws Exception {
        String substring = "\"message\":\"USERNAME / PASSWORD INCORRECT. PLEASE TRY AGAIN\"";

        registerUser();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(signUpRequest.getUsername());
        loginRequest.setPassword("wrongPassword");

        String loginUrl = "/api/auth/login";
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(loginUrl)
                        .content(new Gson().toJson(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(substring)));
    }

    @Test
    void registerWithEmptyUsername() throws Exception {
        String substring = "\"Please enter your username\"";

        signUpRequest.setUsername(null);
        String url = "/api/auth/register";
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(new Gson().toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(substring)));
    }

    @Test
    void registerWithEmptyEmail() throws Exception {
        String substring = "\"User email is required\"";

        signUpRequest.setEmail("");
        String url = "/api/auth/register";
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(new Gson().toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(substring)));
    }

    @Test
    void userWithUsernameAlreadyExists() throws Exception {
        String substring = "\"message\":\"USER WITH USERNAME TESTUSER ALREADY EXIST\"";

        registerUser();

        String rawPassword = applicationService.generatePassword();

        SignUpRequest newSignIpRequest = new SignUpRequest();
        newSignIpRequest.setUsername(signUpRequest.getUsername());
        newSignIpRequest.setEmail("newTestEmail@mail.com");
        newSignIpRequest.setPassword(rawPassword);
        newSignIpRequest.setConfirmPassword(rawPassword);
        newSignIpRequest.setFirstname("newTestUserFN");
        newSignIpRequest.setLastname("newTestUserLN");
        String url = "/api/auth/register";
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(new Gson().toJson(newSignIpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(substring)));
    }

    @Test
    void userWithEmailAlreadyExists() throws Exception {
        String substring = "\"message\":\"USER WITH EMAIL TESTUSER@EXAMPLE.COM ALREADY EXIST\"";

        registerUser();

        String rawPassword = applicationService.generatePassword();

        SignUpRequest newSignIpRequest = new SignUpRequest();
        newSignIpRequest.setUsername("newTestUserUN");
        newSignIpRequest.setEmail(signUpRequest.getEmail());
        newSignIpRequest.setPassword(rawPassword);
        newSignIpRequest.setConfirmPassword(rawPassword);
        newSignIpRequest.setFirstname("newTestUserFN");
        newSignIpRequest.setLastname("newTestUserLN");
        String url = "/api/auth/register";
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(new Gson().toJson(newSignIpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(substring)));
    }

    @Test
    void registerWithDifferentPasswords() throws Exception {
        String substring = "\"PasswordMatchers\":\"Passwords do not match\"";

        signUpRequest.setPassword(applicationService.generatePassword());
        signUpRequest.setPassword(applicationService.generatePassword());
        String url = "/api/auth/register";
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(new Gson().toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(substring)));
    }

}