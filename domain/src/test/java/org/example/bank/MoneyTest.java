package org.example.bank;


import org.example.common.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MoneyTest {
    @Test
    void 음수는_생성불가() {
        assertThatThrownBy(() -> {
            new Money(-1L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 잔액보다_큰_금액을_빼면_예외() {
        Money balance = new Money(100L);
        assertThatThrownBy(() -> {
                    balance.minus(new Money(200L));
                }
        ).isInstanceOf(BadRequestException.class);
    }

    @Test
    void 정상차감() {
        Money balance = new Money(100L);
        Money result = balance.minus(new Money(50L));
        assertThat(result.value()).isEqualTo(50L);
    }


}
