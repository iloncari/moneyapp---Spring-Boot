package hr.java.web.loncaric.moneyapp.repository;

import hr.java.web.loncaric.moneyapp.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    boolean existsById(Long id);
    User getById(Long id);
}
