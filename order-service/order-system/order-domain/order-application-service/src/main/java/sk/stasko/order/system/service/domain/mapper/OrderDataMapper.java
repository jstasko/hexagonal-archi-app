package sk.stasko.order.system.service.domain.mapper;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.entity.OrderItem;
import sk.stasko.order.system.domain.entity.Product;
import sk.stasko.order.system.domain.entity.Restaurant;
import sk.stasko.order.system.domain.valueObject.*;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderCommand;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderResponse;
import sk.stasko.order.system.service.domain.dto.create.OrderAddress;
import sk.stasko.order.system.service.domain.dto.track.TrackOrderResponse;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {
    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand command) {
        return Restaurant.newBuilder()
                .id(new RestaurantId(command.restaurantId()))
                .products(command.items().stream()
                        .map(orderItem -> new Product(new ProductId(orderItem.productId())))
                        .toList()
                ).build();

    }

    public Order createOrderCommandToOrder(CreateOrderCommand command) {
        return Order.builder()
                .customerId(new CustomerId(command.customerId()))
                .restaurantId(new RestaurantId(command.restaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(command.address()))
                .price(new Money(command.price()))
                .items(orderItemsToOrderItemEntities(command.items()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(@NotNull List<sk.stasko.order.system.service.domain.dto.create.OrderItem> items) {
        return items.stream()
                .map(orderItem -> OrderItem.builder()
                        .product(new Product(new ProductId(orderItem.productId())))
                        .price(new Money(orderItem.price()))
                        .quantity(orderItem.quantity())
                        .subTotal(new Money(orderItem.subTotal()))
                        .build())
                .toList();
    }

    private StreetAddress orderAddressToStreetAddress(@NotNull OrderAddress address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.street(),
                address.postalCode(),
                address.city()
        );
    }
}
