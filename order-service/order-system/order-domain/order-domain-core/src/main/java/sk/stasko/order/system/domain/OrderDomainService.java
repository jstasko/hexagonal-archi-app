package sk.stasko.order.system.domain;

import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.entity.Restaurant;
import sk.stasko.order.system.domain.event.OrderCancelledEvent;
import sk.stasko.order.system.domain.event.OrderCreatedEvent;
import sk.stasko.order.system.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
