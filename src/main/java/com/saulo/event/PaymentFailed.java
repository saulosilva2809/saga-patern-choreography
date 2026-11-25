package com.saulo.event;

public record PaymentFailed (
        String orderId,
        String reason
) {
}
