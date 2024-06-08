package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT wm.wallet FROM Member wm WHERE wm.user.id = :userId")
    List<Wallet> findAllByUserId(Long userId);

    @Query("SELECT COUNT(wm) > 0 FROM Member wm WHERE wm.user.id = :userId AND wm.wallet.id = :walletId")
    boolean existsByUserIdAndId(Long userId, Long walletId);

    @Query("SELECT w FROM Wallet w WHERE w.owner.id = :userId")
    List<Wallet> findOwnedWallets(Long userId);

    boolean existsByOwner_IdAndId(Long userId, Long walletId);
}
