package com.example.lifecaremember.dto;

import com.example.lifecaremember.model.Member;
import com.example.lifecaremember.model.enumerate.MemberStatus;
import com.example.lifecaremember.model.enumerate.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.ZoneId;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDTO extends BaseDTO {
//    @JsonProperty("member_no")
    private long memberNo;

    private String id;

    private String name;

    private String email;

    private String phone;

    private Role role;

    private MemberStatus status;

    private List<PermissionDTO> permissions;

    public static  MemberDTO from(Member member){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberNo(member.getMemberNo());
        memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setRole(member.getRole());
        memberDTO.setStatus(member.getStatus());
        memberDTO.setCreatedDate(member.getCreatedDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        memberDTO.setLastModifiedDate(member.getLastModifiedDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        memberDTO.setPermissions(member.getPermissions().stream().map(PermissionDTO::from).toList());
        return memberDTO;
    }
}
