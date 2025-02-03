package sk.stasko.order.system.service.domain.ports.output.message.publisher.restaurantapproval;

import sk.stasko.order.system.domain.event.OrderPaidEvent;
import sk.stasko.order.system.domain.event.publisher.DomainEventPublisher;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
