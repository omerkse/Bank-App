package com.ata.bankapp;

import com.ata.bankapp.controller.UserController;
import com.ata.bankapp.model.User;
import com.ata.bankapp.request.UserLoginRequest;
import com.ata.bankapp.request.UserRegistrationRequest;
import com.ata.bankapp.security.JwtUtil;
import com.ata.bankapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    private User user;
    private UserRegistrationRequest registrationRequest;
    private UserLoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");

        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setPassword("password");
        registrationRequest.setEmail("testuser@example.com");

        loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
    }

    @Test
    public void testRegisterUser() throws Exception {
        given(userService.registerUser(any(User.class))).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\",\"email\":\"testuser@example.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully with ID: 1"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testLoginUser() throws Exception {
        given(userDetailsService.loadUserByUsername("testuser")).willReturn(new org.springframework.security.core.userdetails.User("testuser", "password", new ArrayList<>()));
        given(jwtUtil.generateToken(any(UserDetails.class))).willReturn("jwt-token");
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(new UsernamePasswordAuthenticationToken("testuser", "password", new ArrayList<>()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").value("jwt-token"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testGetUser() throws Exception {
        given(userService.getUser(1L)).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));

        verify(userService, times(1)).getUser(1L);
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("newpassword");
        updatedUser.setEmail("updateduser@example.com");

        given(userService.updateUser(1L, updatedUser)).willReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updateduser\",\"password\":\"newpassword\",\"email\":\"updateduser@example.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("updateduser"));

        verify(userService, times(1)).updateUser(1L, updatedUser);
    }
}
