package hr.java.web.loncaric.moneyapp.repository;

import hr.java.web.loncaric.moneyapp.model.Expense;
import hr.java.web.loncaric.moneyapp.model.SearchFormResult;
import hr.java.web.loncaric.moneyapp.model.Wallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {
     List<Expense> findAllByWalletId(Long walletId);

     @Query("delete from Expense e where e.wallet.id = :walletId")
     void reset(@Param("walletId") Long walletId);

     List<Expense> findAllByWalletIdAndNameContaining(Long walletId, String name);
     Expense getById(Long id);
     boolean existsById(Long id);
     List<Expense> findAllByExpenseType(Expense.Type type);
     Expense findFirstByExpenseTypeOrderByPriceDesc(Expense.Type type);
     Expense findFirstByExpenseTypeOrderByPriceAsc(Expense.Type type);

     List<Expense> findAllByWalletIdAndNameContainingAndPriceLessThanEqualAndPriceGreaterThanEqualAndExpenseTypeEquals(Long walletId, String name, Double min, Double max, Expense.Type type);

//7. pretraga troskova - po iznosu i vrsti...sva polja...vrsta putem drop.. iznos po rasponu vrijednostu
}
