package com.saulo.event;

public record InventoryReserved(
        String orderId,
        String productId,
        int quantity
) {
}
