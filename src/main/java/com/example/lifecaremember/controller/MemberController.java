package com.example.lifecaremember.controller;

import com.example.lifecaremember.config.UserDetailsInfo;
import com.example.lifecaremember.dto.*;
import com.example.lifecaremember.dto.payload.MemberPayload;
import com.example.lifecaremember.dto.payload.MemberSearchPayload;
import com.example.lifecaremember.dto.payload.PermissionAddPayload;
import com.example.lifecaremember.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PreAuthorize("hasAnyAuthority('READ','ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody MemberSearchPayload request) {
        ResponseData<PaginationResult<MemberDTO>> response = new ResponseData<>();
        PaginationResult<MemberDTO> result = memberService.search(request);
        response.initData(result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CREATE','ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MemberPayload request) {
        ResponseData<MemberDTO> response = new ResponseData<>();
        MemberDTO result = memberService.create(request);
        response.initData(result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE','ADMIN')")
    @PutMapping("/{memberNo}")
    public ResponseEntity<?> update(@PathVariable("memberNo") long memberNo, @Valid @RequestBody MemberPayload request) {
        ResponseData<MemberDTO> response = new ResponseData<>();
        MemberDTO result = memberService.update(memberNo, request);
        response.initData(result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('DELETE','ADMIN')")
    @DeleteMapping("/{memberNo}")
    public ResponseEntity<?> delete(@PathVariable("memberNo") long memberNo) {
        ResponseData<MemberDTO> response = new ResponseData<>();
        memberService.delete(memberNo);
        response.success();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PreAuthorize("hasAnyAuthority('EXPORT','ADMIN')")
    @PostMapping("/export-excel")
    public ResponseEntity<?> exportExcel(@Valid @RequestBody MemberSearchPayload request) {
        MemberExcelDTO result = memberService.exportExcelTemplate(request);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + result.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(result.getExportData());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        ResponseData<MemberDTO> response = new ResponseData<>();
        MemberDTO result = memberService.getMemberInfo();
        response.initData(result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/info/update")
    public ResponseEntity<?> update(@Valid @RequestBody MemberPayload request) {
        ResponseData<MemberDTO> response = new ResponseData<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();
        MemberDTO result = memberService.update(userLogin.getId(), request);
        response.initData(result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


//    @PreAuthorize("hasAnyAuthority('ADMIN')")
//    @PostMapping("/add-permissions")
//    public ResponseEntity<?> addPermissions(@RequestBody PermissionAddPayload request) {
//        ResponseData<MemberDTO> response = new ResponseData<>();
//        MemberDTO result = memberService.addPermissions(request);
//        response.initData(result);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

}
