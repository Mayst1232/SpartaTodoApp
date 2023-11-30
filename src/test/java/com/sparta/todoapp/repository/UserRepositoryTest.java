package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveTest(){
        User user = User.builder()
                .username("test")
                .password("test1234")
                .build();

        User saveUser = userRepository.save(user);

        assertThat(user.getUsername()).isEqualTo(saveUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(saveUser.getPassword());
    }
}