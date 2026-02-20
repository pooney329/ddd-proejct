package org.example.bank;

import org.example.bank.Ledger;
import org.example.bank.port.LedgerRepository;
import org.example.bank.port.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
//@AutoConfigureTestDatabase(
//        replace = AutoConfigureTestDatabase.Replace.NONE
//)
public class LedgerRepositoryTest {
    @Autowired
    LedgerRepository ledgerRepository;
    @Autowired
    WalletRepository walletRepository;

    @Test
    void requestId는_유니크() {
        Long userId = 1L;
        walletRepository.save(new Wallet(userId, 1000));
        ledgerRepository.save(Ledger.debit(userId, "req-x", 100));

        assertThatThrownBy(() ->
                ledgerRepository.save(Ledger.debit(userId, "req-x", 200))
        ).isInstanceOf(Exception.class); // 보통 DataIntegrityViolationException 계열
    }


}
