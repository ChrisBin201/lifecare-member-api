package com.example.lifecaremember.service.impl;

import com.example.lifecaremember.config.UserDetailsInfo;
import com.example.lifecaremember.dto.*;
import com.example.lifecaremember.dto.payload.MemberPayload;
import com.example.lifecaremember.dto.payload.MemberSearchPayload;
import com.example.lifecaremember.dto.payload.PermissionAddPayload;
import com.example.lifecaremember.handler.CommonErrorCode;
import com.example.lifecaremember.handler.CommonException;
import com.example.lifecaremember.model.Permission;
import com.example.lifecaremember.model.enumerate.EmailDomain;
import com.example.lifecaremember.model.Member;
import com.example.lifecaremember.model.enumerate.MemberStatus;
import com.example.lifecaremember.model.enumerate.PermissionType;
import com.example.lifecaremember.model.enumerate.Role;
import com.example.lifecaremember.repo.MemberRepo;
import com.example.lifecaremember.repo.PermissionRepo;
import com.example.lifecaremember.service.MemberService;
import jakarta.validation.Validator;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MemberServiceImpl implements MemberService {

    private static final String FILE_NAME_EXPORT = "Member_Report";
    private static final String DATA_EXPORT = "data";

    @Value("${template.member.export}")
    private String templateExcelExport;

    @Autowired
    Validator validator;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final MemberRepo memberRepo;

    private final PermissionRepo permissionRepo;

    public MemberServiceImpl(MemberRepo memberRepo, PermissionRepo permissionRepo) {
        this.memberRepo = memberRepo;
        this.permissionRepo = permissionRepo;
    }

    @Override
    public PaginationResult<MemberDTO> search(MemberSearchPayload request) {
        if(request.getFromDate() > request.getToDate()) {
            throw new RuntimeException("From date must be less than to date");
        }
        LocalDate fromDate = null;
        LocalDate toDate = null;
        if(request.getFromDate() == 0 && request.getToDate() == 0) {
            LocalDate now = LocalDate.now();
            fromDate = now.minusYears(1);
            toDate = now;
        } else {
            fromDate = Instant.ofEpochMilli(request.getFromDate()).atZone(ZoneId.systemDefault()).toLocalDate() ;
            toDate = Instant.ofEpochMilli(request.getToDate()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if(request.getSort() == null) {
            request.setSort("memberNo");
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSort()));
        Page<Member> memberPage = memberRepo.search(request.getId(), request.getName(), request.getPhone(), fromDate, toDate, pageable);
        PaginationResult<MemberDTO> result = new PaginationResult<>();
        result.setTotal(memberPage.getTotalElements());
        result.setPage(memberPage.getNumber());
        result.setTotalPage(memberPage.getTotalPages());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();

        result.setList(memberPage.getContent().stream().map(member -> {
                MemberDTO memberDTO = MemberDTO.from(member);
                if(!userLogin.getRole().equals(Role.ADMIN.name())) memberDTO.setPermissions(null);
                return memberDTO;
        }).toList());

        return result;
    }

    @Override
    public MemberDTO create(MemberPayload request) {
        if(!passwordValidated(request.getPassword())) {
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.INVALID_PASSWORD.getCode(), CommonErrorCode.INVALID_PASSWORD.getMessage());

        }
        if(!emailDomainValidated(request.getEmail())) {
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.INVALID_EMAIL.getCode(), CommonErrorCode.INVALID_EMAIL.getMessage());
        }
        if(memberRepo.findById(request.getId()).isPresent()) {
            throw new CommonException(HttpStatus.BAD_REQUEST.value(), CommonErrorCode.MEMBER_ID_EXISTS.getCode(), CommonErrorCode.MEMBER_ID_EXISTS.getMessage());

        }

        Member member = new Member();
        member.setId(request.getId());
        member.setName(request.getName());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setPhone(request.getPhone());
        member.setEmail(request.getEmail());
        member.setRole(Role.MEMBER);
        member.setStatus(MemberStatus.ACTIVE);
        List<Permission> permissions = permissionRepo.findAll()
                .stream()
                .filter(permission -> permission.getCode().name().equals(PermissionType.READ.name()))
                .toList();
        member.setPermissions(permissions);
//        return null;
        return MemberDTO.from(memberRepo.save(member));
    }

    @Override
    public MemberDTO update(long memberNo, MemberPayload request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();

        if(!emailDomainValidated(request.getEmail())) {
            throw new RuntimeException("Email domain is not valid");
        }
        Member member = memberRepo.findByMemberNo(memberNo).orElseThrow(() -> new RuntimeException("Member not found"));
        if(userLogin.getRole().equals(Role.ADMIN.name()))
            member.setId(request.getId());
        member.setName(request.getName());
        if(request.getPassword() != null) {
            if(!passwordValidated(request.getPassword())) {
                throw new RuntimeException("Password is not valid");
            }
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        member.setPhone(request.getPhone());
        member.setEmail(request.getEmail());

        if(request.getStatus() != null) {
            if(!userLogin.getRole().equals(Role.ADMIN.name())) {
                throw new RuntimeException("You don't have permission to update status");
            }
            member.setStatus(request.getStatus());
        }

        if(request.getPermissionIds() != null) {
            if(!userLogin.getRole().equals(Role.ADMIN.name())) {
                throw new RuntimeException("You don't have permission to update member permission");
            }
            List<Permission> permissions = permissionRepo.findAllById(request.getPermissionIds());
            member.setPermissions(permissions);
        }

        return MemberDTO.from(memberRepo.save(member));
    }

    @Override
    public void delete(long memberNo) {
        memberRepo.deleteById(memberNo);
    }

    @Override
    public MemberExcelDTO exportExcelTemplate(MemberSearchPayload request) {
        if(request.getFromDate() > request.getToDate()) {
            throw new RuntimeException("From date must be less than to date");
        }
        LocalDate fromDate = null;
        LocalDate toDate = null;
        if(request.getFromDate() == 0 && request.getToDate() == 0) {
            LocalDate now = LocalDate.now();
            fromDate = now.minusYears(1);
            toDate = now;
        } else {
            fromDate = Instant.ofEpochMilli(request.getFromDate()).atZone(ZoneId.systemDefault()).toLocalDate() ;
            toDate = Instant.ofEpochMilli(request.getToDate()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if(request.getSort() == null) {
            request.setSort("memberNo");
        }

        List<Member> members = memberRepo.search(request.getId(), request.getName(), request.getPhone(), fromDate, toDate, null).getContent();

        try {
            Map<String, Object> dataMapping = new HashMap<>();
            List<MemberExportDTO> dataExports = members.stream().map(MemberExportDTO::from).toList();
            dataMapping.put(DATA_EXPORT, dataExports);
            Resource resource = new ClassPathResource(this.templateExcelExport);
            FileInputStream fip = new FileInputStream(resource.getFile());
            String fileName = FILE_NAME_EXPORT + ".xlsx";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Put data input data into excel file
            JxlsHelper.getInstance().processTemplate(fip, baos, new Context(dataMapping));
            // Close input and output stream
            fip.close();

//            // create a file from byte array
//            String fileNameTest = "test.xlsx";
//            Files.write(Path.of(fileNameTest), baos.toByteArray());

            MemberExcelDTO result = new MemberExcelDTO();
            result.setFileName(fileName);
            result.setExportData(baos.toByteArray());
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MemberDTO getMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsInfo userLogin = (UserDetailsInfo) authentication.getPrincipal();
        MemberDTO memberDTO = MemberDTO.from(memberRepo.findByMemberNo(userLogin.getId()).orElseThrow(() -> new RuntimeException("Member not found")));
        if(memberDTO.getRole().name().equals(Role.ADMIN.name()) ) {
            memberDTO.setPermissions(permissionRepo.findAll().stream().map(PermissionDTO::from).toList());
        }
        return MemberDTO.from(memberRepo.findByMemberNo(userLogin.getId()).orElseThrow(() -> new RuntimeException("Member not found")));
    }

    @Override
    public MemberDTO addPermissions(PermissionAddPayload request) {
        Member member = memberRepo.findById(request.getMemberNo()).orElseThrow(() -> new RuntimeException("Member not found"));
        List<Permission> permissions = permissionRepo.findAllById(request.getIds());
        member.getPermissions().addAll(permissions);
        return MemberDTO.from(memberRepo.save(member));
    }

    private static boolean passwordValidated(String password) {
        // Check if the password length is within the specified range
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        // Check if the password contains only letters, numbers, and allowed special characters
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*]+$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            return false;
        }

        // Check if consecutive numbers are not more than 3 characters
        if (containsConsecutiveNumbers(password, 3)) {
            return false;
        }

        // Check if the password meets the specific length requirements based on combinations
        int combinationCount = countCharacterCombinations(password);
        if ((combinationCount == 2 && (password.length() < 10 || password.length() > 20))
                || (combinationCount == 3 && (password.length() < 8 || password.length() > 20))) {
            return false;
        }

        return true;
    }

    private static boolean containsConsecutiveNumbers(String password, int maxConsecutive) {
        String consecutiveNumbers = "01234567890";
        for(int i =0; i < password.length() - maxConsecutive+1; i++) {
            String subString = password.substring(i, i + maxConsecutive);
            if(consecutiveNumbers.contains(subString)) {
                return true;
            }
        }
        return false;
    }

    private static int countCharacterCombinations(String password) {
        int combinationCount = 0;
        if (password.matches(".*[a-z].*") || password.matches(".*[A-Z].*")) {
            combinationCount++;
        }
        if (password.matches(".*\\d.*")) {
            combinationCount++;
        }
        if (password.matches(".*[!@#$%^&*].*")) {
            combinationCount++;
        }
        return combinationCount;
    }

    private static boolean emailDomainValidated(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        return Arrays.stream(EmailDomain.values()).anyMatch(emailDomain -> emailDomain.getDomain().equals(domain));
    }

}
