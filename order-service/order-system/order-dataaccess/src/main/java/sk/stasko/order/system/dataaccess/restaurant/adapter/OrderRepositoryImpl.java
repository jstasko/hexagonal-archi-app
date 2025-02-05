package sk.stasko.order.system.dataaccess.restaurant.adapter;

import org.springframework.stereotype.Component;
import sk.stasko.order.system.dataaccess.restaurant.mapper.OrderDataAccessMapper;
import sk.stasko.order.system.dataaccess.restaurant.repository.OrderJpaRepository;
import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.valueObject.TrackingId;
import sk.stasko.order.system.service.domain.ports.output.repository.OrderRepository;

import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        return this.orderDataAccessMapper.orderEntityToOrder(
          orderJpaRepository.save(orderDataAccessMapper.orderToOrderEntity(order))
        );
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return this.orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}
