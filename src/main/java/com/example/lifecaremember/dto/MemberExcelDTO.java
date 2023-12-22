package com.example.lifecaremember.dto;

import lombok.Data;

@Data
public class MemberExcelDTO {
    private byte[] exportData;
    private String fileName;
}
