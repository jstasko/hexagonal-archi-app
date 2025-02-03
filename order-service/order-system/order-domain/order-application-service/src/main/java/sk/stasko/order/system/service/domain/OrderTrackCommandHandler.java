package sk.stasko.order.system.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.exception.OrderNotFoundException;
import sk.stasko.order.system.domain.valueObject.TrackingId;
import sk.stasko.order.system.service.domain.dto.track.TrackOrderQuery;
import sk.stasko.order.system.service.domain.dto.track.TrackOrderResponse;
import sk.stasko.order.system.service.domain.mapper.OrderDataMapper;
import sk.stasko.order.system.service.domain.ports.output.repository.OrderRepository;

import java.util.Optional;

@Component
@Slf4j
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderDataMapper orderDataMapper, OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> orderResult =
                orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.orderTrackingId()));
        if (orderResult.isEmpty()) {
            log.warn("Order with id {} not found", trackOrderQuery.orderTrackingId());
            throw new OrderNotFoundException("Order with id " + trackOrderQuery.orderTrackingId() + " not found");
        }
        return orderDataMapper.orderToTrackOrderResponse(orderResult.get());
    }
}
