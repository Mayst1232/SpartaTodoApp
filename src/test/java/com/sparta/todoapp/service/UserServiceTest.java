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
}