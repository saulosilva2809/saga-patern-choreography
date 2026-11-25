package com.saulo.event;

public record ShippingFailed(
        String orderId,
        String reason
) {
}
