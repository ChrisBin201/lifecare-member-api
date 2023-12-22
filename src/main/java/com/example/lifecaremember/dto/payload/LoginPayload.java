package com.example.lifecaremember.dto.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginPayload {

    @NotBlank(message = "ID is required")
    private String id;

    @NotBlank(message = "Password is required")
    private String password;
}
