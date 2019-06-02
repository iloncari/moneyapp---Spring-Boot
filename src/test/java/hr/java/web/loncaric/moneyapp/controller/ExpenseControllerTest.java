package hr.java.web.loncaric.moneyapp.controller;

import hr.java.web.loncaric.moneyapp.model.Expense;
import hr.java.web.loncaric.moneyapp.model.SearchFormResult;
import hr.java.web.loncaric.moneyapp.model.User;
import hr.java.web.loncaric.moneyapp.model.Wallet;
import hr.java.web.loncaric.moneyapp.repository.ExpenseRepository;
import hr.java.web.loncaric.moneyapp.repository.UserRepository;
import hr.java.web.loncaric.moneyapp.repository.WalletRepository;
import org.apache.tomcat.jni.Local;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {
    private Logger log = LoggerFactory.getLogger(ExpenseControllerTest.class);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    public void testShowNewExpenseForm() throws Exception{
        this.mockMvc
                .perform(get("/expenses/new").with(user("admin").password("adminpass")
                        .roles("USER", "ADMIN")).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("expenseTypes"))
                .andExpect(model().attribute("expenseTypes", Matchers.arrayWithSize(5)))
                .andExpect(view().name("index"));
    }

}