package com.example.lifecaremember.dto;

import com.example.lifecaremember.model.Member;
import com.example.lifecaremember.model.enumerate.MemberStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberExportDTO {

    private long memberNo;

    private String id;

    private String name;

    private String email;

    private String phone;

    private String joinDate;

    private MemberStatus status;


    public static MemberExportDTO from (Member member) {
        return MemberExportDTO.builder()
                .memberNo(member.getMemberNo())
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .joinDate(member.getCreatedDate().toString())
                .status(member.getStatus())
                .build();
    }
}
