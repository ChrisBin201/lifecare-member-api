package com.example.lifecaremember.controller;

import com.example.lifecaremember.dto.MemberDTO;
import com.example.lifecaremember.dto.PermissionDTO;
import com.example.lifecaremember.dto.ResponseData;
import com.example.lifecaremember.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasAnyAuthority('READ','ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        ResponseData<List<PermissionDTO>> response = new ResponseData<>();
        List<PermissionDTO> result = permissionService.getAll();
        response.initData(result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
