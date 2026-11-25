package com.saulo.event;

import java.math.BigDecimal;

public record OrderCreated(
        String orderId,
        String productId,
        int quantity,
        BigDecimal amount
) {
}
