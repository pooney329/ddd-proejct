package org.example.bank;

import org.example.common.exception.BadRequestException;

public record Money(
    Long value
) {

    public Money {
        if(value < 0) throw new IllegalArgumentException("Money cannot be negative");
    }

    public Money minus(Money other){
        if(this.value < other.value()) throw new BadRequestException("other money is greater than current money");
        return new Money(this.value - other.value);
    }
}
