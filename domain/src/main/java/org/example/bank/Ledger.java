package org.example.bank;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "ledger", uniqueConstraints = @UniqueConstraint(name = "uk_ledger_req", columnNames = {"requestId"}))
public class Ledger {
    @Id
    @GeneratedValue
    Long id;
    Long userId;
    String requestId;
    long amount;
    String type; // "DEBIT"

    protected Ledger() {
    }

    public static Ledger debit(Long userId, String requestId, long amount) {
        Ledger l = new Ledger();
        l.userId = userId;
        l.requestId = requestId;
        l.amount = amount;
        l.type = "DEBIT";
        return l;
    }
}
