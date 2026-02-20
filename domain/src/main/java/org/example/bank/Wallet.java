package org.example.bank;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wallet")
public class Wallet {
    @Id
    Long userId;
    long balance;
    @Version
    long version; // optimistic lock


    public Wallet(Long userId, long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public void debit(long amount) {
        this.balance = new Money(this.balance).minus(new Money(amount)).value();
    }
}
