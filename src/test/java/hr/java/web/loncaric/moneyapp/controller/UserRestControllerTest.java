package hr.java.web.loncaric.moneyapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.java.web.loncaric.moneyapp.model.User;
import hr.java.web.loncaric.moneyapp.model.Wallet;
import hr.java.web.loncaric.moneyapp.repository.UserRepository;
import hr.java.web.loncaric.moneyapp.repository.WalletRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void findAll() throws Exception{
        this.mockMvc
                .perform(get("/api/users")
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN"))
                        .with(csrf()))
                .andExpect(jsonPath("$.[0].username").value("admin"))
                .andExpect(status().isOk());
    }

    @Test
    public void findOne() throws Exception {
        this.mockMvc
                .perform(get("/api/users/1")
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN"))
                        .with(csrf()))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(status().isOk());
    }

    @Test
    public void save() throws Exception {
        User user = new User();
        user.setUsername("student");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode("studentpass"));

        this.mockMvc
                .perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user))
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN", "USER"))
                        .with(csrf()))
                .andExpect(jsonPath("$.username").value("student"))
                .andExpect(status().isCreated());
    }

 /*   @Test
    public void update() throws Exception {
        User user = userRepository.getById(1l);
        user.setPassword(passwordEncoder.encode("adminnovipass"));

        this.mockMvc
                .perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user))
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN", "USER"))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    public void delete() throws Exception {
        String uri = "/api/users/1";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(302, status);
    }
}