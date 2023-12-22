package com.example.lifecaremember;

import com.example.lifecaremember.model.Member;
import com.example.lifecaremember.model.Permission;
import com.example.lifecaremember.model.enumerate.MemberStatus;
import com.example.lifecaremember.model.enumerate.PermissionType;
import com.example.lifecaremember.model.enumerate.Role;
import com.example.lifecaremember.repo.MemberRepo;
import com.example.lifecaremember.repo.PermissionRepo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
@Slf4j
@OpenAPIDefinition(info = @Info(title = "REST API FOR LIFECARE MEMBER", version = "0.1.0", description = "LIFECARE MEMBER API DOCUMENTATION"))
public class LifecareMemberApplication {

    @Autowired
    MemberRepo memberRepo;

    @Autowired
    PermissionRepo permissionRepo;

    public static void main(String[] args) {
        SpringApplication.run(LifecareMemberApplication.class, args);
    }

//    @Bean
//    CommandLineRunner mockPermissionData() {
//        return args -> {
//            Arrays.stream(PermissionType.values()).forEach(permissionType -> {
//                //permissionName capitalize
//                String permissionName = permissionType.name().substring(0, 1).toUpperCase() + permissionType.name().substring(1).toLowerCase();
//                permissionRepo.save(Permission.builder()
//                        .name(permissionName + " Permission")
//                        .code(permissionType)
//                        .build());
//            });
//        };
//    }
//
//    @Bean
//    CommandLineRunner mockMemberData() {
//        return args -> {
//            for (int i = 0; i < 20; i++) {
//                String id = UUID.randomUUID().toString();
//                String name = getRandomName();
//                String password = "1"; // Fixed password as specified
//                String email = getRandomEmail(name);
//                String phone = getRandomPhoneNumber();
//
//                memberRepo.save(Member.builder()
//                        .id(id)
//                        .name(name)
//                        .password(password)
//                        .email(email)
//                        .phone(phone)
//                        .status(MemberStatus.ACTIVE)
//                        .role(Role.MEMBER)
//                        .build());
//            }
//        };
//    }
//    private static String getRandomName() {
//        // Generate random names by combining common first and last names
//        String[] firstNames = {"John", "Jane", "Alice", "Bob", "Charlie", "Emma", "David", "Eva"};
//        String[] lastNames = {"Smith", "Johnson", "Miller", "Davis", "Wilson", "Lee", "Brown"};
//
//        String randomFirstName = firstNames[(int) (Math.random() * firstNames.length)];
//        String randomLastName = lastNames[(int) (Math.random() * lastNames.length)];
//
//        return randomFirstName + " " + randomLastName;
//    }
//
//    private static String getRandomEmail(String name) {
//        // Generate email using the name and a random domain
//        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com", "domain.com"};
//
//        String[] nameParts = name.split(" ");
//        String firstName = nameParts[0].toLowerCase();
//        String lastName = nameParts[1].toLowerCase();
//
//        String randomDomain = domains[(int) (Math.random() * domains.length)];
//
//        return firstName + "." + lastName + "@" + randomDomain;
//    }
//
//    private static String getRandomPhoneNumber() {
//        // Generate a random phone number with only 10 digits
//        return "555" + String.format("%07d", (int) (Math.random() * 10000000));
//    }
//
//
//    @Bean
//    CommandLineRunner logMemberData() {
//        return args -> {
//            memberRepo.findAll().forEach(m -> {
//                // date/time to epoch
//                long epochMilis = m.getCreatedDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                log.info(m.getId() + " {}" ,epochMilis);
//                // epoch to date/time
//                log.info(m.getId() + " {}" , Instant.ofEpochMilli(epochMilis).atZone(ZoneId.systemDefault()).toLocalDate());
//            });
//        };
//    }
}
