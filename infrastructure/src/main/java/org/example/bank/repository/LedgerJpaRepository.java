package org.example.bank.repository;

import org.example.bank.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerJpaRepository extends JpaRepository<Ledger, Long> {
    boolean existsByRequestId(String requestId);
}
