package hr.java.web.loncaric.moneyapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.java.web.loncaric.moneyapp.model.Expense;
import hr.java.web.loncaric.moneyapp.repository.ExpenseRepository;
import hr.java.web.loncaric.moneyapp.repository.WalletRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;

import java.net.URI;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseRestControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    public void findAll() throws Exception {
        this.mockMvc
                .perform(get("/api/expenses/")
                        .with(user("admin")
                                .password("adminpass")
                                .roles("USER", "ADMIN"))
                        .with(csrf()))
                .andExpect(jsonPath("$.[0].name").value("Adminov trošak 1"))
                .andExpect(jsonPath("$.[1].name").value("Adminov novi trošak 2"))
                .andExpect(status().isOk());
    }

    @Test
    public void findOne()throws Exception {
        this.mockMvc
                .perform(get("/api/expenses/1")
                        .with(user("admin")
                                .password("adminpass")
                                .roles("USER", "ADMIN"))
                        .with(csrf()))
                .andExpect(jsonPath("$.name").value("Adminov trošak 1"))
                .andExpect(status().isOk());
    }

    @Test
    public void save() throws Exception {
        Expense expense = new Expense();
        expense.setName("Sok");
        expense.setPrice(12d);
        expense.setExpenseType(Expense.Type.PIĆE);

        this.mockMvc
                .perform(post("/api/expenses?walletId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(expense))
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN", "USER"))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    public void update() throws Exception {
        Expense expense = expenseRepository.getById(1l);
        expense.setName("Adminov trošak 1");

        this.mockMvc
                .perform(put("/api/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(expense))
                        .with(user("admin")
                                .password("adminpass")
                                .roles("ADMIN", "USER"))
                        .with(csrf()))
                .andExpect(jsonPath("$.name").value("Adminov trošak 1"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void delete() throws  Exception{
        String uri = "/api/expenses/1";
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(302, status);
    }
}