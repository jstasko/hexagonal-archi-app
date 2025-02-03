package sk.stasko.order.system.service.domain.ports.output.message.publisher.payment;

import sk.stasko.order.system.domain.event.OrderCancelledEvent;
import sk.stasko.order.system.domain.event.publisher.DomainEventPublisher;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
