package com.saulo.event;

public record InventoryReservationFailed(
        String orderId,
        String reason
) {
}
