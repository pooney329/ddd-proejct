package org.example.bank.port;

import org.example.bank.Ledger;

public interface LedgerRepository {
    boolean existsByRequestId(String requestId);
    Ledger save(Ledger entity);
}
