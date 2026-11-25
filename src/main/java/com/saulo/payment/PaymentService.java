package com.saulo.payment;

import com.saulo.event.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class PaymentService {

    private final EventBus eventBus;

    // simula os pagamentos já aprovados.
    private final Set<String> approvedPayments = new HashSet<>();

    // permite simular uma falha no pagamento.
    private boolean shouldFail;

    public PaymentService(EventBus eventBus, boolean shouldFail) {
        this.eventBus = eventBus;
        this.shouldFail = shouldFail;

        registerEvents();
    }

    private void registerEvents() {
        eventBus.subscribe(
                InventoryReserved.class,
                this::onInventoryReserved
        );

        eventBus.subscribe(
                ShippingFailed.class,
                this::onShippingFailed
        );
    }

    private void onInventoryReserved(InventoryReserved event) {
        System.out.println(
                "PaymentService: recebendo reserva do pedido "
                        + event.orderId()
        );

        processPayment(event.orderId(), new BigDecimal("199.90"));
    }

    private void processPayment(String orderId, BigDecimal amount) {
        System.out.println(
                "PaymentService: processando pagamento de R$ " + amount
        );

        if (shouldFail) {
            System.out.println(
                    "PaymentService: pagamento recusado!"
            );

            eventBus.publish(
                    new PaymentFailed(
                            orderId,
                            "Pagamento recusado"
                    )
            );

            return;
        }

        approvedPayments.add(orderId);

        System.out.println(
                "PaymentService: pagamento aprovado!"
        );

        eventBus.publish(
                new PaymentApproved(
                        orderId,
                        amount
                )
        );
    }

    private void onShippingFailed(ShippingFailed event) {
        System.out.println(
                "PaymentService: envio falhou. Estornando pagamento."
        );

        refund(event.orderId());
    }

    private void refund(String orderId) {
        boolean wasApproved = approvedPayments.remove(orderId);

        if (!wasApproved) {
            return;
        }

        System.out.println(
                "PaymentService: pagamento estornado para "
                        + orderId
        );
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }
}
