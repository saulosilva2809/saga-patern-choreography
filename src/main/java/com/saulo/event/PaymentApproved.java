package com.saulo.event;

import java.math.BigDecimal;

public record PaymentApproved(
        String orderId,
        BigDecimal amount
) {
}
