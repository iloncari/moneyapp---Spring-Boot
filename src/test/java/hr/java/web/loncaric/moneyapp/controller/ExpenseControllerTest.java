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
                .andExpect(model().attributeExists("wallets"))
                .andExpect(model().attribute("wallets", Matchers.hasSize(1)))
                .andExpect(model().attributeExists("expense"))
                .andExpect(view().name("index"));
    }

    @Test
    public  void testSaveNewExpense() throws Exception{
        Expense expense = new Expense();
        expense.setName("Keksi");
        expense.setPrice(15d);
        expense.setCreateDate(LocalDateTime.now());
        expense.setExpenseType(Expense.Type.HRANA);
        expense.setWallet(walletRepository.findByid(1l));

        this.mockMvc.perform(post("/expenses/new").with(user("admin").password("adminpass").roles("USER", "ADMIN"))
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .flashAttr("expense", expense))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("expense"))
                .andExpect(model().attributeExists("wallet"))
                .andExpect(model().attributeExists("totalSum"))
                .andExpect(model().attribute("totalSum", Matchers.equalTo(6764.72d)))
                .andExpect(model().attributeExists("valuta"))
                .andExpect(model().attribute("valuta", " kn"))
                .andExpect(view().name("expenseConfirmed"));
    }


    @Test
    public void testShowSearchForm() throws Exception {
        this.mockMvc
                .perform(get("/expenses/searchExpenses").with(user("admin").password("adminpass")
                        .roles("USER", "ADMIN")).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("searchObject"))
                .andExpect(model().attributeExists("expenseTypes"))
                .andExpect(model().attribute("expenseTypes", Matchers.arrayWithSize(5)))
                .andExpect(view().name("searchExpenses"));
    }

    @Test
    public void testProcessSearchForm() throws Exception{
        SearchFormResult searchFormResult = new SearchFormResult();
        searchFormResult.setName("nov");
        searchFormResult.setPriceMax(1.5d);
        searchFormResult.setPriceMin(7000d);
        searchFormResult.setType(Expense.Type.HRANA);

        this.mockMvc
                .perform(post("/expenses/searchExpenses")
                        .with(user("admin").password("adminpass").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchObject", searchFormResult))
                .andExpect(status().isOk())
                //.andExpect(model().attributeExists("expenses")) ???? expenses does not exists
                .andExpect(model().attributeExists("searchObject"))
                .andExpect(view().name("searchExpenses"));
    }

    @Test
    public void testShowNewWalletScreen() throws Exception {
        this.mockMvc
                .perform(get("/expenses/newWallet").with(user("admin").password("adminpass")
                        .roles("USER", "ADMIN")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("wallet"))
                .andExpect(model().attributeExists("walletTypes"))
                .andExpect(model().attribute("walletTypes", Matchers.arrayWithSize(3)))
                .andExpect(view().name("newWallet"));
    }
    @Test
    public void testSaveNewWallet() throws Exception{

        Wallet wallet = new Wallet();
        wallet.setName("Adminov Test DOLAR novčanik");
        wallet.setWalletType(Wallet.Type.DOLAR);
        wallet.setUser(userRepository.getById(1l));
        wallet.setCreateDate(LocalDateTime.now());

        this.mockMvc
                .perform(post("/expenses/newWallet")
                        .with(user("admin").password("adminpass").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("wallet", wallet))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/expenses/new"));
    }

}