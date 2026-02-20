package org.example.bank.repository;

import lombok.RequiredArgsConstructor;
import org.example.bank.Wallet;
import org.example.bank.port.WalletRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class WalletRepositoryImpl implements WalletRepository {
    private final WalletJpaRepository walletJpaRepository;
    @Override
    public Optional<Wallet> findById(Long userId) {
        return walletJpaRepository.findById(userId);
    }

    @Override
    public Wallet save(Wallet wallet) {
        return walletJpaRepository.save(wallet);
    }
}
