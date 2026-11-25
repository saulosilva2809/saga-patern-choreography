package com.saulo.shipping;

import com.saulo.event.EventBus;
import com.saulo.event.PaymentApproved;
import com.saulo.event.ShippingFailed;
import com.saulo.event.ShippingRequested;

import java.util.HashSet;
import java.util.Set;

public class ShippingService {

    private final EventBus eventBus;

    // guarda os pedidos que tiveram o envio iniciado.
    private final Set<String> shipments = new HashSet<>();

    // permite simular uma falha no envio.
    private boolean shouldFail;

    public ShippingService(EventBus eventBus, boolean shouldFail) {
        this.eventBus = eventBus;
        this.shouldFail = shouldFail;

        registerEvents();
    }

    private void registerEvents() {
        eventBus.subscribe(
                PaymentApproved.class,
                this::onPaymentApproved
        );
    }

    private void onPaymentApproved(PaymentApproved event) {
        System.out.println(
                "ShippingService: pagamento aprovado para "
                        + event.orderId()
        );

        requestShipping(event.orderId());
    }

    private void requestShipping(String orderId) {

        System.out.println(
                "ShippingService: iniciando envio do pedido "
                        + orderId
        );

        if (shouldFail) {
            System.out.println(
                    "ShippingService: falha ao solicitar envio!"
            );

            eventBus.publish(
                    new ShippingFailed(
                            orderId,
                            "Não foi possível solicitar o envio"
                    )
            );

            return;
        }

        shipments.add(orderId);

        System.out.println(
                "ShippingService: envio solicitado com sucesso!"
        );

        eventBus.publish(
                new ShippingRequested(orderId)
        );
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    public boolean hasShipment(String orderId) {
        return shipments.contains(orderId);
    }
}
