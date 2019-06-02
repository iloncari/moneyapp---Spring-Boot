package hr.java.web.loncaric.moneyapp.controller;

import hr.java.web.loncaric.moneyapp.model.Expense;
import hr.java.web.loncaric.moneyapp.model.Wallet;
import hr.java.web.loncaric.moneyapp.repository.ExpenseRepository;
import hr.java.web.loncaric.moneyapp.repository.WalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/wallets", produces="application/json")
@CrossOrigin
public class WalletRestController {
    private final ExpenseRepository expenseRepository;
    private final WalletRepository walletRepository;

    public WalletRestController(ExpenseRepository expenseRepository, WalletRepository walletRepository) {
        this.expenseRepository = expenseRepository;
        this.walletRepository = walletRepository;
    }


    @GetMapping
    public ResponseEntity<Iterable<Wallet>> findAll() {
        Iterable<Wallet> wallets = walletRepository.findAll();
        return new ResponseEntity<>(wallets, HttpStatus.OK);
    }

   @GetMapping("/{id}")
   public ResponseEntity<Wallet> findOne(@PathVariable Long id) {
       Wallet wallet = walletRepository.getById(id);
       if(wallet != null){
           return new ResponseEntity<>(wallet, HttpStatus.OK);
       }else{
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
   }

    @PostMapping(consumes="application/json")
    public ResponseEntity<Wallet> save(@RequestBody Wallet wallet) {
       Wallet aavedWallet =  walletRepository.save(wallet);
       return new ResponseEntity<>(aavedWallet, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Wallet> update(@RequestBody Wallet wallet, @PathVariable Long id) {
        Wallet oldWallet = walletRepository.getById(id);
        oldWallet.setName(wallet.getName());
        oldWallet.setWalletType(wallet.getWalletType());
        oldWallet.setExpenses(wallet.getExpenses());
        oldWallet.setUser(wallet.getUser());
        Wallet newWallet = walletRepository.save(oldWallet);
        return new ResponseEntity<>(newWallet, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if(!walletRepository.existsById(Long.valueOf(id))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        walletRepository.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(HttpStatus.FOUND);
    }
}
