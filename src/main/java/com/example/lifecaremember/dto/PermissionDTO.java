package com.example.lifecaremember.dto;

import com.example.lifecaremember.model.Permission;
import lombok.Data;

@Data
public class PermissionDTO {
    private long id;
    private String code;
    private String name;

    public static PermissionDTO from (Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(permission.getId());
        permissionDTO.setCode(permission.getCode().name());
        permissionDTO.setName(permission.getName());
        return permissionDTO;
    }
}
