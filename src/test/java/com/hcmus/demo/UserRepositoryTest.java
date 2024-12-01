package com.hcmus.demo;

import com.hcmus.demo.model.User;
import com.hcmus.demo.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    public void getUserByUsername() {
        // Write your test code here
    }

    @Test
    public void getUserByEmail() {
        // Write your test code here
        String email = "john.doe@example.com";
        User user = userRepository.findByEmail(email);
        System.out.println(user);
    }
}
