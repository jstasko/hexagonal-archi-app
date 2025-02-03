package sk.stasko.order.system.service.domain.ports.output.message.publisher.payment;

import sk.stasko.order.system.domain.event.OrderCreatedEvent;
import sk.stasko.order.system.domain.event.publisher.DomainEventPublisher;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
