package com.example.lifecaremember.repo;


import com.example.lifecaremember.dto.payload.MemberSearchPayload;
import com.example.lifecaremember.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

    @Query("""
            SELECT m FROM Member m
            WHERE (:id IS NULL OR m.id like concat('%', :id, '%'))
            AND (:name IS NULL OR m.name like concat('%', :name, '%'))
            AND (:phone IS NULL OR m.phone like concat('%', :phone, '%'))
            AND (:fromDate IS NULL OR m.createdDate >= :fromDate)
            AND (:toDate IS NULL OR m.createdDate <= :toDate)
    """)
    Page<Member> search(String id, String name, String phone, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    Optional<Member> findById(String id);

    Optional<Member> findByMemberNo(long memberNo);

    void deleteByMemberNo(long memberNo);
}
