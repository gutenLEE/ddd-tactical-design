package kitchenpos.products.tobe;

import java.math.BigDecimal;

public record Money(
        BigDecimal value
) {
    public Money {
        if (value == null) {
            throw new IllegalArgumentException("금액은 null이 될 수 없습니다.");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 0보다 작을 수 없습니다.");
        }
    }

    public static Money from(Long value) {
        return new Money(BigDecimal.valueOf(value));
    }
}