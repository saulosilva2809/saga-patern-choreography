# Java — Saga Pattern (Choreography)

Projeto básico desenvolvido em Java com o objetivo de estudar o **Saga Pattern utilizando a abordagem de Choreography**, explorando transações distribuídas, comunicação baseada em eventos e operações compensatórias.

## Objetivo

Simular um cenário de compras em um sistema de e-commerce, onde diferentes serviços são responsáveis por etapas independentes do processo de compra, como criação do pedido, reserva de estoque, processamento do pagamento e solicitação de envio.

O principal objetivo é implementar uma **Saga Coreografada**, onde os serviços se comunicam por meio de eventos, sem a necessidade de um orquestrador central para controlar a sequência das operações.

**Projeto desenvolvido para fins de estudo**, explorando o padrão Saga, Event-Driven Architecture, consistência eventual e estratégias de compensação em sistemas distribuídos.