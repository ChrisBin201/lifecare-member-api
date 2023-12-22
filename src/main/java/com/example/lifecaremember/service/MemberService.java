package com.example.lifecaremember.service;

import com.example.lifecaremember.dto.MemberDTO;
import com.example.lifecaremember.dto.MemberExcelDTO;
import com.example.lifecaremember.dto.MemberExportDTO;
import com.example.lifecaremember.dto.payload.MemberPayload;
import com.example.lifecaremember.dto.payload.MemberSearchPayload;
import com.example.lifecaremember.dto.PaginationResult;
import com.example.lifecaremember.dto.payload.PermissionAddPayload;

import java.io.ByteArrayOutputStream;

public interface MemberService {

    PaginationResult<MemberDTO> search(MemberSearchPayload request);

    MemberDTO create(MemberPayload request);

    MemberDTO update(long memberNo, MemberPayload request);

    void delete(long memberNo);

    MemberExcelDTO exportExcelTemplate(MemberSearchPayload request);

    MemberDTO getMemberInfo();

    MemberDTO addPermissions(PermissionAddPayload request);


}
