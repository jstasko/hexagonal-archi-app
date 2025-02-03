package sk.stasko.order.system.service.domain.ports.output.repository;

import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.valueObject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByTrackingId(TrackingId trackingId);
}
