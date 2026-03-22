package com.sungshincard.backend.domain.member.repository;

import com.sungshincard.backend.domain.member.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByMemberEmail(String email);
    List<Address> findByMemberId(Long memberId);
    Address findByIdAndMemberEmail(Long id, String email);
}
