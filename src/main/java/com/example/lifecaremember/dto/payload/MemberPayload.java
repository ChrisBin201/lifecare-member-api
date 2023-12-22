package com.example.lifecaremember.dto.payload;

import com.example.lifecaremember.model.enumerate.MemberStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberPayload {

    // at least 4 digits long
    @Pattern(regexp = "^\\d{4,}$", message = "ID must be at least 4 digits long")
    @NotBlank(message = "ID is required")
    private String id;

    //only letters and space
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must be letters only")
    @NotBlank(message = "Name is required")
    private String name;

    private String password;
    // email validation
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    // phone number validation
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits long")
    @NotBlank(message = "Phone number is required")
    private String phone;

    private MemberStatus status;

    private List<Long> permissionIds;
}
