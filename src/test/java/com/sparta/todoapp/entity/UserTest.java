package com.sparta.todoapp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;

class UserTest {
    @Test
    @DisplayName("User에 username과 password가 전달된 값과 일치해야 합니다")
    public void testUserConstructor(){
        // given
        User user = Mockito.spy(User.builder()
                .username("Mayst")
                .password("1234")
                .build());

        // when
        String username = "Mayst";
        String password = "1234";

        // then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
    }
}