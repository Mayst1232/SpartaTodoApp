package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 기능 성공 테스트")
    void signupSuccessTest() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto("hwang1234", "qwer1234");
        Mockito.when(passwordEncoder.encode(requestDto.getPassword())).thenReturn(new BCryptPasswordEncoder().encode(requestDto.getPassword()));
        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.empty());

        // when
        userService.signup(requestDto);

        // then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("회원가입 시도 시 username의 중복으로 인한 실패 테스트")
    void signupFailTestUserNameIsDuplicate() {
        User user = new User();
        user.setUsername("hwang1234");
        // given
        SignupRequestDto requestDto = new SignupRequestDto("hwang1234", "qwer1234");
        Mockito.when(passwordEncoder.encode(requestDto.getPassword())).thenReturn(new BCryptPasswordEncoder().encode(requestDto.getPassword()));
        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(user));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(requestDto));

        // then
        assertThat("중복된 사용자가 존재합니다.").isEqualTo(exception.getMessage());
    }
}