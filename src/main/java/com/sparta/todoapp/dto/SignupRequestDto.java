package com.sparta.todoapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
//    @Pattern(regexp = "^[a-z0-9]*$")
//    @Min(4)
//    @Max(10)
    private String username;

    @NotBlank
//    @Pattern(regexp = "^[a-zA-Z0-9]*$")
//    @Min(8)
//    @Min(15)
    private String password;
}
