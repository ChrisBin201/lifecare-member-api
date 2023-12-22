package com.example.lifecaremember.service.impl;

import com.example.lifecaremember.dto.PermissionDTO;
import com.example.lifecaremember.repo.PermissionRepo;
import com.example.lifecaremember.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepo permissionRepo;

    @Override
    public List<PermissionDTO> getAll() {
        return permissionRepo.findAll().stream().map(PermissionDTO::from).toList();
    }
}
