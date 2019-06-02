package hr.java.web.loncaric.moneyapp.controller;

import hr.java.web.loncaric.moneyapp.model.Expense;
import hr.java.web.loncaric.moneyapp.model.SearchFormResult;
import hr.java.web.loncaric.moneyapp.model.User;
import hr.java.web.loncaric.moneyapp.model.Wallet;
import hr.java.web.loncaric.moneyapp.repository.ExpenseRepository;
import hr.java.web.loncaric.moneyapp.repository.UserRepository;
import hr.java.web.loncaric.moneyapp.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.boot.spi.InFlightMetadataCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@SessionAttributes({"expenseTypes", "wallet", "walletTypes"})
@RequestMapping("/expenses")
public class ExpenseController {
    private Logger log = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    ExpenseRepository expenseRepository;

    /* @ModelAttribute("wallet")
     public Wallet initializeWallet(){
         log.info("Initialize Wallet");
         return new Wallet("Moja gotovina", Wallet.Type.KUNSKI, new ArrayList<>());
     }*/



    @GetMapping("/new")
    public String showNewExpenseForm(Model model) {

        log.info("GET request - /expenses/new");
        List<Wallet> userWallets = walletRepository.findWalletsForUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userWallets.size() == 0) {
            log.info("GET request - /expenses/new  - showing newWallet screen");
            model.addAttribute("walletTypes", Wallet.Type.values());
            model.addAttribute("wallet", new Wallet());
            return "newWallet";
        }

        log.info("GET request - /expenses/new  - showing index screen");
        model.addAttribute("wallets", userWallets);
        model.addAttribute("expense", new Expense());
        model.addAttribute("expenseTypes", Expense.Type.values());
        return "index";
    }

    @PostMapping("/new")
    public String SaveNewExpense(@Validated Expense expense, Errors errors, Model model) {
        log.info("POST request - /expenses/new");
        if (errors.hasErrors()) {
            log.info("POST request - /expenses/new  - Form has errors - showing index screen again");
            return "index";
        }
        Wallet walletc = walletRepository.findByid(expense.getWallet().getId());
        expense.setWallet(walletc);
        expense.setCreateDate(LocalDateTime.now());
        expenseRepository.save(expense);

        List<Expense> walletExpenses = expenseRepository.findAllByWalletId(walletc.getId());
        Double suma = 0d;
        for (Expense e : walletExpenses)
            suma += e.getPrice();

        model.addAttribute("wallet", walletc);
        model.addAttribute("expense", expense);
        model.addAttribute("totalSum", suma);

        String valuta = "";
        switch (walletc.getWalletType()) {
            case KUNSKI:
                valuta = " kn";
                break;
            case EURO:
                valuta = " €";
                break;
            case DOLAR:
                valuta = " $";
                break;
        }
        model.addAttribute("valuta", valuta);
        log.info("POST request - /expenses/new  - showing index screen");
        return "expenseConfirmed";
    }

    @GetMapping("/newWallet")
    public String showNewWalletScreen(Model model) {
        log.info("GET request - /expenses/newWallet");
        model.addAttribute("walletTypes", Wallet.Type.values());
        model.addAttribute("wallet", new Wallet());
        return "newWallet";
    }

    @PostMapping("/newWallet")
    public String saveNewWallet(@Validated Wallet wallet, Errors errors, Model model) {
        log.info("POST request - /expenses/newWallet");
        if (errors.hasErrors()) {
            log.info("POST request - /expenses/newWallet - Form has errors - showing newWallet screen again");
            return "newWallet";
        }
        wallet.setCreateDate(LocalDateTime.now());
        User curentUser = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        wallet.setUser(curentUser);
        wallet.setExpenses(new ArrayList<Expense>());
        walletRepository.save(wallet);
        return "redirect:/expenses/new";
    }

    @GetMapping("/resetWallet")
    public String resetWallet(@ModelAttribute("wallet") Wallet wallet) {
        log.info("GET request - /expenses/resetWallet - redirect:/expenses/new");
        walletRepository.reset(wallet.getId());
        return "redirect:/expenses/new";
    }


    @GetMapping("/searchExpenses")
    public String showSearchForm(Model model) {
        model.addAttribute("searchObject", new SearchFormResult());
        model.addAttribute("expenseTypes", Expense.Type.values());
        return "searchExpenses";
    }


    @PostMapping("/searchExpenses")
    public String processSearchForm(@Validated SearchFormResult searchFormResult, Errors errors, Model model) {
        if(errors.hasErrors()){
            model.addAttribute("searchObject", new SearchFormResult());
            model.addAttribute("expenseTypes", Expense.Type.values());
            return "searchExpenses";
        }
        List<Wallet> walletsForSearch = walletRepository.findWalletsForUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Expense> expenses = new ArrayList<>();

        for (int i = 0; i < walletsForSearch.size(); i++) {

         List<Expense> expenseList =
                 expenseRepository.findAllByWalletIdAndNameContainingAndPriceLessThanEqualAndPriceGreaterThanEqualAndExpenseTypeEquals(
           walletsForSearch.get(i).getId(), searchFormResult.getName(), searchFormResult.getPriceMax(), searchFormResult.getPriceMin(), searchFormResult.getType());

            if (expenseList != null && expenseList.size() != 0)
                expenses.addAll(expenseList);
        }

        model.addAttribute("searchObject", searchFormResult);
        model.addAttribute("expenses", expenses);
        return "searchExpenses";
    }



}
