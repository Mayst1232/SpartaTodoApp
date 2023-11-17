package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.dto.StatusResponseDto;
import com.sparta.todoapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult){
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            String message = "필드가 비었습니다.";
            return ResponseEntity.badRequest().body(new StatusResponseDto(message, HttpStatus.BAD_REQUEST.value()));
        }
        try {
            userService.signup(requestDto);
            String message = "가입에 성공했습니다";
            return ResponseEntity.ok().body(new StatusResponseDto(message, HttpStatus.OK.value()));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(new StatusResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
