package com.example.lifecaremember.dto.common;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class Paging {
    private int page = 0;
    private int size = 5;
    private String sort;
    private String direction = "asc";

//    public Pageable getPageable() {
//
//        return sort != null ? PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort)) : PageRequest.of(page, size);
//    }
}
