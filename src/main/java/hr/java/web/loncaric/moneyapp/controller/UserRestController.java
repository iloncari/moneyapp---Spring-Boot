package hr.java.web.loncaric.moneyapp.controller;

import hr.java.web.loncaric.moneyapp.model.Authority;
import hr.java.web.loncaric.moneyapp.model.User;
import hr.java.web.loncaric.moneyapp.repository.AuthorityRepository;
import hr.java.web.loncaric.moneyapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path="/api/users", produces="application/json")
@CrossOrigin
public class UserRestController {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserRestController(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }


    @GetMapping
    public ResponseEntity<Iterable<User>> findAll() {
        Iterable<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findOne(@PathVariable Long id) {
        User user = userRepository.getById(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<User> save(@RequestBody User user) {

        User savedUser = userRepository.save(user);
        Authority savedAuthority = authorityRepository.save(new Authority("USER", user.getUsername()));
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable Long id) {

        User oldUser = userRepository.getById(id);
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setWallets(user.getWallets());

        User newUser = userRepository.save(oldUser);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!userRepository.existsById(Long.valueOf(id))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User deletedUser = userRepository.getById(Long.valueOf(id));
        userRepository.deleteById(Long.valueOf(id));
        if (authorityRepository.existsById(Long.valueOf(id))) {
            List<Authority> userAuthorities = authorityRepository.findALLByUsername(deletedUser.getUsername());
            for (Authority a : userAuthorities)
                authorityRepository.deleteById(a.getId());
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}