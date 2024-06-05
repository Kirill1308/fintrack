package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT wm.wallet FROM Member wm WHERE wm.user.id = :userId")
    List<Wallet> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(wm) > 0 FROM Member wm WHERE wm.user.id = :userId AND wm.wallet.id = :walletId")
    boolean existsByUserIdAndId(@Param("userId") Long userId, @Param("walletId") Long walletId);
}
