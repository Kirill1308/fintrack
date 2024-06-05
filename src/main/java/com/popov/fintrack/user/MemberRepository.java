package com.popov.fintrack.user;

import com.popov.fintrack.user.model.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserId(Long userId);

    List<Member> findByWalletId(Long walletId);

    void deleteByWalletIdAndUserId(Long walletId, Long userId);
}
