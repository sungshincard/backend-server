package com.sungshincard.backend.domain.member.repository;

import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByMember(Member member);
}
