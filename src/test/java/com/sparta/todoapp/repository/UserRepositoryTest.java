package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


class UserRepositoryTest extends RepositoryTest{
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("입력한 username과 password에 해당하는 user Entity db에 저장시키기")
    public void saveTest(){
        // given
        User user = User.builder()
                .username("test")
                .password("test1234")
                .build();

        // when
        User saveUser = userRepository.save(user);

        // then
        assertThat(user.getUsername()).isEqualTo(saveUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(saveUser.getPassword());
    }

    @Test
    @DisplayName("찾고있는 username으로 user 데이터 찾아오기 성공")
    public void findByUsernameTest() throws IllegalAccessException {
        // given
        User user = User.builder()
                .username("test")
                .password("test1234")
                .build();
        User saveUser = userRepository.save(user);

        // when
        User findUser = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalAccessException("해당하는 사용자가 존재하지않습니다.")
        );

        // then
        assertThat(saveUser.getUsername()).isEqualTo(findUser.getUsername());
        assertThat(saveUser.getPassword()).isEqualTo(findUser.getPassword());
    }

    @Test
    @DisplayName("찾고있는 username으로 user 데이터 찾아오기 실패")
    public void findByUsernameFailTest() throws IllegalAccessException {
        // given
        User user = User.builder()
                .username("test")
                .password("test1234")
                .build();

        User wrongUser = new User();
        wrongUser.setUsername("Wrong");

        User saveUser = userRepository.save(user);

        // when
        Exception exception = assertThrows(NullPointerException.class,
                () -> userRepository.findByUsername(wrongUser.getUsername()).orElseThrow(
                        () -> new NullPointerException("해당 유저는 존재하지 않습니다.")
                )
        );

        // then
        assertEquals("해당 유저는 존재하지 않습니다.", exception.getMessage());
    }
}