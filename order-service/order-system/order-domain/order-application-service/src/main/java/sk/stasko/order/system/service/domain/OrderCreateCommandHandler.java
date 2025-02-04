package sk.stasko.order.system.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sk.stasko.order.system.domain.event.OrderCreatedEvent;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderCommand;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderResponse;
import sk.stasko.order.system.service.domain.mapper.OrderDataMapper;
import sk.stasko.order.system.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper, OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    public CreateOrderResponse createOrder(CreateOrderCommand cmd) {
        OrderCreatedEvent event = orderCreateHelper.createOrder(cmd);
        log.info("Created is created with id: {}", event.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(event);
        return orderDataMapper.orderToCreateOrderResponse(event.getOrder(), "Order Created Successfully");
    }

}
