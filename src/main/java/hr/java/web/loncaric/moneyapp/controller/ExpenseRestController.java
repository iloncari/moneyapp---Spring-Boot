package hr.java.web.loncaric.moneyapp.controller;

import hr.java.web.loncaric.moneyapp.model.Expense;
import hr.java.web.loncaric.moneyapp.model.Wallet;
import hr.java.web.loncaric.moneyapp.repository.ExpenseRepository;
import hr.java.web.loncaric.moneyapp.repository.WalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/expenses", produces="application/json")
@CrossOrigin
public class ExpenseRestController {
    private final ExpenseRepository expenseRepository;
    private final WalletRepository walletRepository;

    public ExpenseRestController(ExpenseRepository expenseRepository, WalletRepository walletRepository) {
        this.expenseRepository = expenseRepository;
        this.walletRepository = walletRepository;
    }


    @GetMapping
    public ResponseEntity<Iterable<Expense>> findAll() {
        Iterable<Expense> expenses = expenseRepository.findAll();
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

   @GetMapping("/{id}")
   public ResponseEntity<Expense> findOne(@PathVariable Long id) {
       Expense expense = expenseRepository.getById(id);
       if(expense != null){
           return new ResponseEntity<>(expense, HttpStatus.OK);
       }else{
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
   }

    @PostMapping(consumes="application/json")
    public ResponseEntity<Expense> save(@RequestBody Expense expense, @RequestParam Long walletId) {
        Wallet wallet = walletRepository.getById(walletId);
        expense.setWallet(wallet);
        Expense savedExpense = expenseRepository.save(expense);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@RequestBody Expense expense, @PathVariable Long id) {
        Expense oldExpense = expenseRepository.getById(id);
        oldExpense.setPrice(expense.getPrice());
        oldExpense.setName(expense.getName());
        oldExpense.setExpenseType(expense.getExpenseType());

        Expense newExpense = expenseRepository.save(oldExpense);
        return new ResponseEntity<>(newExpense, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if(!expenseRepository.existsById(Long.valueOf(id))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        expenseRepository.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
