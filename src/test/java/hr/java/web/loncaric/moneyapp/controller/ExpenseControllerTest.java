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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    public void showNewExpenseForm() throws Exception {

        List<Wallet> userWallets = walletRepository.findWalletsForUserByUsername("admin");
        if(userWallets.size()==0){
            this.mockMvc.perform(get("/expenses/new").with(user("admin").password("adminpass").roles("USER", "ADMIN")).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("walletTypes"))
                    .andExpect(model().attribute("walletTypes", Matchers.arrayWithSize(3)))
                    .andExpect(model().attributeExists("wallet"))
                    .andExpect(view().name("newWallet"));
        }else{
            this.mockMvc
                    .perform(get("/expenses/new").with(user("admin").password("adminpass")
                            .roles("USER", "ADMIN")).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("expenseTypes"))
                    .andExpect(model().attribute("expenseTypes", Matchers.arrayWithSize(5)))
                    .andExpect(view().name("index"));
        }
    }

    @Test
    public void saveNewExpense() throws Exception {
        Wallet wallet = walletRepository.getById(Long.valueOf(1));

        Expense expense = new Expense();
        expense.setName("Ožujsko pivo");
        expense.setPrice(Double.valueOf(13));
        expense.setCreateDate(LocalDateTime.now());
        expense.setExpenseType(Expense.Type.PIĆE);
        expense.setWallet(wallet);

        this.mockMvc
                .perform(post("/expenses/new")
                        .with(user("admin").password("adminpass").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("expense", expense))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("expense"))
                .andExpect(model().attributeExists("valuta"))
                .andExpect(model().attribute("valuta", " kn"))
                .andExpect(model().attributeExists("totalSum"))
                .andExpect(model().attribute("totalSum", Matchers.equalTo(13d)))
                .andExpect(view().name("expenseConfirmed"));

    }

    @Test
    public void showNewWalletScreen() throws Exception {
        this.mockMvc
                .perform(get("/expenses/newWallet")
                        .with(user("admin").password("adminpass").roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("wallet"))
                .andExpect(model().attributeExists("walletTypes"))
                .andExpect(model().attribute("walletTypes", Matchers.arrayWithSize(3)))
                .andExpect(view().name("newWallet"));
    }


    @Test
    public void showSearchForm() throws Exception {
        this.mockMvc
                .perform(get("/expenses/searchExpenses").with(user("admin").password("adminpass")
                        .roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("searchObject"))
                .andExpect(view().name("searchExpenses"));
    }

    @Test
    public void processSearchForm() throws Exception {
        SearchFormResult searchFormResult = new SearchFormResult();
        searchFormResult.setName("Mobitel");

        this.mockMvc
                .perform(post("/expenses/searchExpenses")
                        .with(user("admin").password("adminpass").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("searchObject", searchFormResult))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("searchObject"))
                .andExpect(view().name("searchExpenses"));
    }
}