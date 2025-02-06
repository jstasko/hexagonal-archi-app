package sk.stasko.order.system.messaging.mapper;

import org.springframework.stereotype.Component;
import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.event.OrderCancelledEvent;
import sk.stasko.order.system.domain.event.OrderCreatedEvent;
import sk.stasko.order.system.domain.event.OrderPaidEvent;
import sk.stasko.order.system.kafka.order.avro.model.*;
import sk.stasko.order.system.service.domain.dto.message.OrderApprovalStatus;
import sk.stasko.order.system.service.domain.dto.message.PaymentResponse;
import sk.stasko.order.system.service.domain.dto.message.PaymentStatus;
import sk.stasko.order.system.service.domain.dto.message.RestaurantApprovalResponse;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {
    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setOrderId(order.getId().getValue())
                .setRestaurantId(order.getRestaurantId().getValue())
                .setOrderId(order.getId().getValue())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(
                        order.getItems().stream()
                                .map(orderItem ->
                                        Product.newBuilder()
                                                .setId(orderItem.getProduct().getId().getValue().toString())
                                                .setQuantity(orderItem.getQuantity())
                                                .build())
                                .toList()
                )
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId().toString())
                .sagaId(paymentResponseAvroModel.getSagaId().toString())
                .paymentId(paymentResponseAvroModel.getPaymentId().toString())
                .customerId(paymentResponseAvroModel.getCustomerId().toString())
                .orderId(paymentResponseAvroModel.getOrderId().toString())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse
    approvalResponseAvroModelToApprovalResponse(RestaurantApprovalResponseAvroModel approvalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(approvalResponseAvroModel.getId().toString())
                .sagaId(approvalResponseAvroModel.getSagaId().toString())
                .restaurantId(approvalResponseAvroModel.getRestaurantId().toString())
                .orderId(approvalResponseAvroModel.getOrderId().toString())
                .createdAt(approvalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(approvalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(approvalResponseAvroModel.getFailureMessages())
                .build();
    }
}
