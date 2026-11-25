package com.saulo.inventory;

import com.saulo.event.*;

import java.util.HashMap;
import java.util.Map;

public class InventoryService {

    private final EventBus eventBus;

    private final Map<String, Integer> stock = new HashMap<>();

    private final Map<String, ReservationDto> reservations = new HashMap<>();

    public InventoryService(EventBus eventBus) {
        this.eventBus = eventBus;

        stock.put("product-001", 10);
        stock.put("product-002", 5);

        registerEvents();
    }

    private void registerEvents() {
        eventBus.subscribe(
                OrderCreated.class,
                this::onOrderCreated
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

    // reverte a quantidade em estoque em caso de falha
    private void releaseReservation(String orderId) {
        ReservationDto reservation = reservations.remove(orderId);

        if (reservation == null) {
            return;
        }

        int currentQuantity = stock.getOrDefault(
                reservation.productId(),
                0
        );

        stock.put(
                reservation.productId(),
                currentQuantity + reservation.quantity()
        );

        System.out.println(
                "InventoryService: reserva liberada para " + orderId
        );
    }

    // quando uma nova order for criada
    private void onOrderCreated(OrderCreated event) {
        System.out.println(
                "InventoryService: recebendo pedido " + event.orderId()
        );

        reserve(event);
    }

    private void reserve(OrderCreated event) {
        String productId = event.productId();
        int requestedQuantity = event.quantity();

        int availableQuantity = stock.getOrDefault(productId, 0);

        if (availableQuantity < requestedQuantity) {
            System.out.println(
                    "InventoryService: estoque insuficiente para " + event.orderId()
            );

            eventBus.publish(
                    new InventoryReservationFailed(
                            event.orderId(),
                            "Estoque insuficiente"
                    )
            );

            return;
        }

        // retira a quantidade do estoque
        stock.put(productId, availableQuantity - requestedQuantity);

        // registra a venda
        reservations.put(event.orderId(), new ReservationDto(productId, requestedQuantity));

        System.out.println(
                "InventoryService: estoque reservado para " + event.orderId()
        );

        // comunica que a venda foi efetuada
        eventBus.publish(
                new InventoryReserved(
                        event.orderId(),
                        productId,
                        requestedQuantity
                )
        );
    }

    private void onPaymentFailed(PaymentFailed event) {
        System.out.println(
                "InventoryService: pagamento falhou. Liberando estoque."
        );

        releaseReservation(event.orderId());
    }

    private void onShippingFailed(ShippingFailed event) {
        System.out.println(
                "InventoryService: envio falhou. Liberando estoque"
        );

        releaseReservation(event.orderId());
    }

    public int getAvailableStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }
}
