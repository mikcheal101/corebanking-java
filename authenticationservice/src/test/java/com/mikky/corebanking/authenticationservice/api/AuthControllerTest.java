package com.mikky.corebanking.authenticationservice.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordChangeRequest;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordRequest;
import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordResponse;
import com.mikky.corebanking.authenticationservice.command.dto.SigninRequest;
import com.mikky.corebanking.authenticationservice.command.dto.SignupRequest;
import com.mikky.corebanking.authenticationservice.command.repository.RoleCommandRepository;
import com.mikky.corebanking.authenticationservice.command.repository.UserCommandRepository;
import com.mikky.corebanking.authenticationservice.domain.model.Role;
import com.mikky.corebanking.authenticationservice.domain.model.RoleType;
import com.mikky.corebanking.authenticationservice.query.repository.UserQueryRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private UserCommandRepository userCommandRepository;

    @Autowired
    private RoleCommandRepository roleCommandRepository;

    private static final String API_V1 = "/api/v1/auth";
    private static final String SIGNUP_URI = "/signup";
    private static final String FORGOT_PASSWORD_URI = "/forgot-password";
    private static final String SIGNIN_URI = "/signin";
    private static final String SIGNOUT_URI = "/signout";
    private static final String CHANGE_PASSWORD_URI = "/change-forgotten-password/{token}";

    @BeforeEach
    void cleanDatabase() {
        userCommandRepository.deleteAll();
        roleCommandRepository.deleteAll();

        Role customerRole = new Role();
        customerRole.setName("Customer");
        customerRole.setRoleType(RoleType.CUSTOMER);
        roleCommandRepository.save(customerRole);
    }

    // ============== Sign Up Tests ==============
    @Test
    void shouldRegisterNewUserSuccessfully() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("sample@mail.com"); // email username
        request.setPassword("Passcod@101");

        var signupRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mock.perform(signupRequest).andExpect(status().isCreated());
    }

    @Test
    void shouldFailIfUsernameExists() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("sample@mail.com"); // email username
        request.setPassword("Passcod@101");

        var signupRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mock.perform(signupRequest).andExpect(status().isCreated());
        mock.perform(signupRequest).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWithInvalidPasswordInput() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("wample@mail.com"); // email username
        request.setPassword("passcod101");

        var signupRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mock.perform(signupRequest).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWithNoPasswordInput() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("wample@mail.com"); // email username

        var signupRequest = post(API_V1 + SIGNUP_URI)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);
        mock.perform(signupRequest).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWithNoUsernameInput() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setPassword("passcod101");

        var signupRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mock.perform(signupRequest).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectTooShortPassword() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("wample@mail.com"); // email username
        request.setPassword("pd101");

        var signupRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mock.perform(signupRequest).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectPasswordWithoutUppercase() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setUsername("wample@mail.com"); // email username
        request.setPassword("passc@od101");

        var signupRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mock.perform(signupRequest).andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleConcurrentSignupRequest() throws Exception {

        String username = "wample@mail.com";

        SignupRequest request = new SignupRequest();
        request.setUsername(username);
        request.setPassword("P@sscod@101");

        var signupRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();
                    mock.perform(signupRequest);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executorService.shutdown();

        var userCount = userQueryRepository.countByUsername(username);
        Assertions.assertEquals(1, userCount);
    }

    // ============== Forgot Password Tests ==============

    @Test
    void shouldSendForgotPasswordEmail() throws Exception {

        String username = "sampleUsername";

        var signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword("P@ssword@101");

        var signupPost = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest));
        mock.perform(signupPost);

        var forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setUsername(username);

        var forgotPasswordPost = post(API_V1 + FORGOT_PASSWORD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgotPasswordRequest));

        mock.perform(forgotPasswordPost).andExpect(status().isOk());
    }

    @Test
    void shouldFailForgotPasswordForNoneExistentUser() throws Exception {

        String username = "sampleUsername";
        String wrongUsername = "wrongUsername";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword("P@ssword@101");

        var signupPost = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest));
        mock.perform(signupPost);

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setUsername(wrongUsername);

        var forgotPasswordPost = post(API_V1 + FORGOT_PASSWORD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgotPasswordRequest));

        mock.perform(forgotPasswordPost).andExpect(status().isBadRequest());
    }

    @Test
    void shouldResetPasswordSuccessfully() throws Exception {

        String username = "sampleUsername";
        String newPassword = "NewP@ssword@101";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword("P@ssword@101");

        var signupPost = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest));
        mock.perform(signupPost);

        var forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setUsername(username);

        var forgotPasswordPost = post(API_V1 + FORGOT_PASSWORD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgotPasswordRequest));
        var forgotPasswordResult = mock.perform(forgotPasswordPost).andReturn();

        var changePasswordRequest = new ForgotPasswordChangeRequest();
        changePasswordRequest.setPassword(newPassword);
        changePasswordRequest.setRetypePassword(newPassword);

        String responseJson = forgotPasswordResult.getResponse().getContentAsString();
        ForgotPasswordResponse forgotPasswordResponse = objectMapper.readValue(responseJson, ForgotPasswordResponse.class);
        String token = forgotPasswordResponse.getToken();

        var changePasswordPost = post(API_V1 + CHANGE_PASSWORD_URI, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest));
        mock.perform(changePasswordPost).andExpect(status().isOk());
    }

    @Test
    void shouldFailWithInvalidToken() throws Exception {
        String username = "sampleUsername";
        String newPassword = "NewP@ssword@101";
        String invalidToken = "7y847wythrwg8hfgiufiufnbgiufh9h38hv458ht4823332332";

        var signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword("P@ssword@101");

        var signupPost = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest));
        mock.perform(signupPost);

        var forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setUsername(username);

        var forgotPasswordPost = post(API_V1 + FORGOT_PASSWORD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgotPasswordRequest));
        mock.perform(forgotPasswordPost);

        var changePasswordRequest = new ForgotPasswordChangeRequest();
        changePasswordRequest.setPassword(newPassword);
        changePasswordRequest.setRetypePassword(newPassword);

        var changePasswordPost = post(API_V1 + CHANGE_PASSWORD_URI, invalidToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest));
        mock.perform(changePasswordPost).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailWithNoToken() throws Exception {
        String username = "sampleUsername";
        String newPassword = "NewP@ssword@101";
        String token = "";

        var signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword("P@ssword@101");

        var signupPost = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest));
        mock.perform(signupPost);

        var forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setUsername(username);

        var forgotPasswordPost = post(API_V1 + FORGOT_PASSWORD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgotPasswordRequest));
        mock.perform(forgotPasswordPost);

        var changePasswordRequest = new ForgotPasswordChangeRequest();
        changePasswordRequest.setPassword(newPassword);
        changePasswordRequest.setRetypePassword(newPassword);

        var changePasswordPost = post(API_V1 + CHANGE_PASSWORD_URI, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest));
        mock.perform(changePasswordPost).andExpect(status().isNotFound());
    }

    // ============== Signout Tests ==============

    @Test
    void shouldSignoutSuccessfully() throws Exception {
        var logoutRequest = get(API_V1 + SIGNOUT_URI)
                .contentType(MediaType.APPLICATION_JSON);
        mock.perform(logoutRequest)
                .andExpect(status().is2xxSuccessful());
    }

    // ============== Signin Tests ==============

    @Test
    void shouldSigninSuccessfully() throws Exception {

        String username = "sampleUsername";
        String password = "P@ssword@101";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword(password);

        // create user
        var signupHttpRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest));
        mock.perform(signupHttpRequest);

        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUsername(username);
        signinRequest.setPassword(password);

        // authenticate user
        var authenticateUser = post(API_V1 + SIGNIN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signinRequest));
        mock.perform(authenticateUser).andExpect(status().isOk());
    }

    @Test
    void shouldFailWithInvalidSigninPasswordInput() throws Exception {

        String username = "sampleUsername";
        String password = "P@ssword@101";
        String wrongPassword = "passwordEntered";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword(password);

        // create user
        var signupHttpRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest));
        mock.perform(signupHttpRequest);

        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUsername(username);
        signinRequest.setPassword(wrongPassword);

        // authenticate user
        var authenticateUser = post(API_V1 + SIGNIN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signinRequest));
        mock.perform(authenticateUser).andExpect(status().isForbidden());
    }

    @Test
    void shouldFailSigninForNoneExistentUser() throws Exception {

        String username = "sampleUsername";
        String wrongUsername = "SampleUsername";
        String password = "P@ssword@101";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setPassword(password);

        // create user
        var signupHttpRequest = post(API_V1 + SIGNUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest));
        mock.perform(signupHttpRequest);

        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUsername(wrongUsername);
        signinRequest.setPassword(password);

        // authenticate user
        var authenticateUser = post(API_V1 + SIGNIN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signinRequest));
        mock.perform(authenticateUser).andExpect(status().isForbidden());
    }

}
