package com.example.lifecaremember.dto.payload;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionAddPayload {
    private long memberNo;
    private List<Long> ids = new ArrayList<>();
}
