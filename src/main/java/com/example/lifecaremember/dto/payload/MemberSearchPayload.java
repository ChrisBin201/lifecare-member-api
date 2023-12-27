package com.example.lifecaremember.dto.payload;

import com.example.lifecaremember.dto.common.Paging;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberSearchPayload extends Paging {
    @Pattern(regexp = "^\\d{3,}$", message = "ID must be at least 3 digits long")
    private String id;
//    if letters only, at least 2 characters long (blank is allowed)
    @Pattern(regexp = "^[a-zA-Z]{2,}(?: [a-zA-Z]+)*$", message = "Name must be at least 2 characters long")
    private String name;

    @Pattern(regexp = "^[0-9]+$", message = "Phone must be only numbers ")
    private String phone;
    private long fromDate;
    private long toDate;
}
