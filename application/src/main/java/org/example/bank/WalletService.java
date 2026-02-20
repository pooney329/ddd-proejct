package org.example.bank;

import org.example.bank.port.LedgerRepository;
import org.example.bank.port.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalletService {
    private final WalletRepository walletRepository;
    private final LedgerRepository ledgerRepository;

    WalletService(WalletRepository walletRepository, LedgerRepository ledgerRepository) {
        this.walletRepository = walletRepository;
        this.ledgerRepository = ledgerRepository;
    }

    public void debit(Long userId, long amount, String requestId) {
        // 멱등
        if (ledgerRepository.existsByRequestId(requestId)) return;

        Wallet wallet = walletRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("wallet not found"));

        wallet.debit(amount); // 잔액 부족이면 예외
        ledgerRepository.save(Ledger.debit(userId, requestId, amount));
        // wallet은 JPA dirty checking
    }
}
