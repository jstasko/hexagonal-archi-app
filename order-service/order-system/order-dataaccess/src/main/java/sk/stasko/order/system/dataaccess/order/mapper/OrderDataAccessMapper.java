package sk.stasko.order.system.dataaccess.order.mapper;

import org.springframework.stereotype.Component;
import sk.stasko.order.system.dataaccess.order.entity.OrderAddressEntity;
import sk.stasko.order.system.dataaccess.order.entity.OrderEntity;
import sk.stasko.order.system.dataaccess.order.entity.OrderItemEntity;
import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.entity.OrderItem;
import sk.stasko.order.system.domain.entity.Product;
import sk.stasko.order.system.domain.valueObject.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sk.stasko.order.system.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity entity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ?
                        String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()): "")
                .build();

        entity.getAddress().setOrder(entity);
        entity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrderEntity(entity));

        return entity;
    }

    public Order orderEntityToOrder(OrderEntity entity) {
        return Order.builder()
                .id(new OrderId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .restaurantId(new RestaurantId(entity.getRestaurantId()))
                .deliveryAddress(addressEntityToDeliveryAddress(entity.getAddress()))
                .price(new Money(entity.getPrice()))
                .items(orderItemEntitiesToOrderItems(entity.getItems()))
                .trackingId(new TrackingId(entity.getTrackingId()))
                .orderStatus(entity.getOrderStatus())
                .failureMessages(entity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(entity.getFailureMessages().split(FAILURE_MESSAGE_DELIMITER))))
                .build();
    }

    private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(orderItemEntity -> OrderItem.builder()
                        .id(new OrderItemId(orderItemEntity.getId()))
                        .product(new Product(new ProductId(orderItemEntity.getProductId())))
                        .price(new Money(orderItemEntity.getPrice()))
                        .quantity(orderItemEntity.getQuantity())
                        .subTotal(new Money(orderItemEntity.getSubTotal()))
                        .build())
                .toList();
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(address.getId(), address.getStreet(), address.getPostalCode(), address.getCity());
    }

    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> items) {
        return items.stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .productId(orderItem.getProduct().getId().getValue())
                        .price(orderItem.getPrice().getAmount())
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubTotal().getAmount())
                        .build())
                .toList();
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.id())
                .street(deliveryAddress.street())
                .city(deliveryAddress.city())
                .postalCode(deliveryAddress.postalCode())
                .build();
    }
}
