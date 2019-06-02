package hr.java.web.loncaric.moneyapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.java.web.loncaric.moneyapp.model.Wallet;
import hr.java.web.loncaric.moneyapp.repository.ExpenseRepository;
import hr.java.web.loncaric.moneyapp.repository.WalletRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class WalletRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;



    @Test
    public void findAll() throws Exception {
        this.mockMvc
                .perform(get("/api/wallets")
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN"))
                        .with(csrf()))
                .andExpect(jsonPath("$.[0].name").value("Adminov KUNSKI novčanik"))
                .andExpect(status().isOk());
    }

    @Test
    public void findOne() throws Exception {
        this.mockMvc
                .perform(get("/api/wallets/1")
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN"))
                        .with(csrf()))
                .andExpect(jsonPath("$.name").value("Adminov KUNSKI novčanik"))
                .andExpect(status().isOk());
    }

    @Test
    public void save() throws Exception{
        Wallet wallet = new Wallet();
        wallet.setName("Adminov novi DOLAR novčanik");
        wallet.setWalletType(Wallet.Type.DOLAR);

        this.mockMvc
                .perform(post("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wallet))
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN", "USER"))
                        .with(csrf()))
                .andExpect(jsonPath("$.name").value("Adminov novi DOLAR novčanik"))
                .andExpect(status().isCreated());
    }

    @Test
    public void update() throws Exception{
        Wallet wallet = walletRepository.getById(1l);
        wallet.setName("Adminov novi KUNSKI novčanik");

        this.mockMvc
                .perform(put("/api/wallets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wallet))
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN", "USER"))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete() throws Exception{
        String uri = "/api/expenses/1";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(302, status);
    }
}