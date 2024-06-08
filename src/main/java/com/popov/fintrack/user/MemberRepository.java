package com.popov.fintrack.user;

import com.popov.fintrack.user.model.member.Member;
import com.popov.fintrack.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByWalletId(Long walletId);

    void deleteByWalletIdAndUserId(Long walletId, Long userId);

    @Query("SELECT m.wallet FROM Member m WHERE m.user.id = :userId")
    List<Wallet> findSharedWallets(Long userId);
}
