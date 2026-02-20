package org.example.bank;

import org.example.bank.port.LedgerRepository;
import org.example.bank.port.WalletRepository;
import org.example.common.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.dgc.Lease;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private LedgerRepository ledgerRepository;

    @InjectMocks
    private WalletService walletService;


    @Test
    void requestId_이미처리됨이면_멱등으로_그대로_종료() {
        //given
        Long userId = 1111L;
        long amount = 100L;
        String requestId = "r-1";

        when(ledgerRepository.existsByRequestId(requestId)).thenReturn(true);

        //when
        walletService.debit(userId, amount, requestId);

        //then
        verify(ledgerRepository).existsByRequestId(requestId);
        verify(walletRepository, never()).findById(anyLong());
        verify(ledgerRepository, never()).save(any());

    }

    @Test
    void 잔액부족이면_예외_및_ledger저장없음() {
        //given
        Long userId = 1111L;
        long amount = 100L;
        String requestId = "r-1";

        Wallet wallet = new Wallet(userId, 50L);
        when(ledgerRepository.existsByRequestId(requestId)).thenReturn(false);
        when(walletRepository.findById(userId)).thenReturn(Optional.of(wallet));

        //when
        assertThatThrownBy(() -> walletService.debit(userId, amount, requestId))
                .isInstanceOf(BadRequestException.class);

        verify(ledgerRepository, never()).save(any());
        assertThat(wallet.balance).isEqualTo(50L);
    }

    @Test
    void 정상차감이면_wallet감소_및_ledger저장() {
        //given
        Long userId = 1111L;
        long amount = 100L;
        String requestId = "r-1";

        Wallet wallet = new Wallet(userId, 200L);

        when(ledgerRepository.existsByRequestId(requestId)).thenReturn(false);
        when(walletRepository.findById(userId)).thenReturn(Optional.of(wallet));

        //when

        walletService.debit(userId, amount, requestId);


        ArgumentCaptor<Ledger> ledgerArgumentCaptor = ArgumentCaptor.forClass(Ledger.class);
        verify(ledgerRepository).save(ledgerArgumentCaptor.capture());
        Ledger savedLedger = ledgerArgumentCaptor.getValue();

        assertThat(savedLedger.userId).isEqualTo(userId);
        assertThat(savedLedger.requestId).isEqualTo(requestId);
        assertThat(savedLedger.amount).isEqualTo(amount);
        assertThat(wallet.balance).isEqualTo(100L);


    }


    @Test
    void 정상차감이면_wallet감소_및_ledger저장2() {
        //given
        Long userId = 1111L;
        long amount = 100L;
        String requestId = "r-1";

        Wallet wallet = new Wallet(userId, 200L);

        when(ledgerRepository.existsByRequestId(requestId)).thenReturn(false);
        when(walletRepository.findById(userId)).thenReturn(Optional.of(wallet));

        //when
        walletService.debit(userId, amount, requestId);

        //then
        verify(ledgerRepository).save(argThat(l -> l.amount == 100L && l.userId == 1111L));
        assertThat(wallet.balance).isEqualTo(100L);


    }
}
