package org.example.bank.repository;

import lombok.RequiredArgsConstructor;
import org.example.bank.Ledger;
import org.example.bank.port.LedgerRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LedgerRepositoryImpl implements LedgerRepository {
    private final LedgerJpaRepository ledgerJpaRepository;
    @Override
    public boolean existsByRequestId(String requestId) {
        return ledgerJpaRepository.existsByRequestId(requestId);
    }

    @Override
    public Ledger save(Ledger entity) {
        return ledgerJpaRepository.save(entity);
    }
}
