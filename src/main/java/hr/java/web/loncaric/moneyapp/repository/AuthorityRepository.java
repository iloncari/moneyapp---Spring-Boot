package hr.java.web.loncaric.moneyapp.repository;

import hr.java.web.loncaric.moneyapp.model.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface AuthorityRepository extends CrudRepository<Authority, Long>{

    List<Authority> findALLByUsername(String username);

}
