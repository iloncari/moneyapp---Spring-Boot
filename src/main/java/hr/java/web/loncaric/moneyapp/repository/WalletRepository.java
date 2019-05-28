package hr.java.web.loncaric.moneyapp.repository;

import hr.java.web.loncaric.moneyapp.model.Wallet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    @Query("from Wallet w where w.user.username = :username")
    List<Wallet> findWalletsForUserByUsername(@Param("username") String username);


    Wallet findByid(Long id);

    @Query("from Wallet w where w.user.username = :username")
    List<Wallet> findAllOwned(@Param("username") String username);

    Wallet getById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Expense e where e.wallet.id = :walletId")
    void reset(@Param("walletId") Long walletId);

}
