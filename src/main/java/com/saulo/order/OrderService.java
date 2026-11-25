package com.saulo.order;

import com.saulo.event.*;

import java.math.BigDecimal;

public class OrderService {

    private final EventBus eventBus;

    public OrderService(EventBus eventBus) {
        this.eventBus = eventBus;

        registerEvents();
    }

    private void registerEvents() {
        eventBus.subscribe(
                InventoryReservationFailed.class,
                this::onInventoryReservationFailed
        );

        eventBus.subscribe(
                PaymentFailed.class,
                this::onPaymentFailed
        );

        eventBus.subscribe(
                ShippingFailed.class,
                this::onShippingFailed
        );
    }

    private void onInventoryReservationFailed(InventoryReservationFailed event) {
        System.out.println(
                "Pedido cancelado: falha na reserva de estoque. Pedido: " + event.orderId()
        );
    }

    private void onPaymentFailed(PaymentFailed event) {
        System.out.println(
                "Pedido cancelado: pagamento recusado. Pedido: " + event.orderId()
        );
    }

    private void onShippingFailed(ShippingFailed event) {
        System.out.println(
                "Pedido cancelado: falha no envio. Pedido: " + event.orderId()
        );
    }

    public void createOrder(
            String orderId,
            String productId,
            int quantity,
            BigDecimal amount
    ) {
        System.out.println("Criando pedido: " + orderId);

        eventBus.publish(
                new OrderCreated(orderId, productId, quantity, amount)
        );
    }
}
