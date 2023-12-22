package com.example.lifecaremember.dto.payload;

import com.example.lifecaremember.dto.common.Paging;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberSearchPayload extends Paging {
    private String id;
//    if letters only, at least 2 characters long (blank is allowed)
    @Pattern(regexp = "^[a-zA-Z]{2,}$", message = "Name must be at least 2 characters long")
    private String name;
    private String phone;
    private long fromDate;
    private long toDate;
}
