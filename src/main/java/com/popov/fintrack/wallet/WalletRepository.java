package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findAllByUserId(Long userId);

    boolean existsByUserIdAndId(Long userId, Long walletId);

    boolean existsByUserIdAndIdIn(Long userId, List<Long> walletIds);
}
