package org.example.bank.port;

import org.example.bank.Wallet;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findById(Long userId);
    Wallet save(Wallet wallet);

}
