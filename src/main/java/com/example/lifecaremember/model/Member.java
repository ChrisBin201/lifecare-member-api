package com.example.lifecaremember.model;

import com.example.lifecaremember.model.enumerate.MemberStatus;
import com.example.lifecaremember.model.enumerate.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "member"
       , indexes = {@Index(name = "idx_name", columnList = "name")
                   ,@Index(name = "idx_id", columnList = "id")
                    }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberNo;

    @Column(unique = true)
    private String id;

    private String name;

    private String password;
    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_permission",
            joinColumns = @JoinColumn(
                    name = "member_no", referencedColumnName = "memberNo"),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id", referencedColumnName = "id"))
    private List<Permission> permissions;

}
