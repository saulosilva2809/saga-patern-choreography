package com.saulo;

import com.saulo.event.EventBus;
import com.saulo.inventory.InventoryService;
import com.saulo.order.OrderService;
import com.saulo.payment.PaymentService;
import com.saulo.shipping.ShippingService;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        EventBus eventBus = new EventBus();

        OrderService orderService = new OrderService(eventBus);

        InventoryService inventoryService =
                new InventoryService(eventBus);

        PaymentService paymentService =
                new PaymentService(eventBus, false);

        ShippingService shippingService =
                new ShippingService(eventBus, false);

        System.out.println("\n===== INICIANDO SAGA =====\n");

        orderService.createOrder(
                "order-001",
                "product-001",
                3,
                new BigDecimal("199.90")
        );

        System.out.println("\n===== RESULTADO =====\n");

        System.out.println(
                "Estoque disponível: "
                        + inventoryService.getAvailableStock("product-001")
        );

        System.out.println(
                "Envio iniciado: "
                        + shippingService.hasShipment("order-001")
        );
    }
}
