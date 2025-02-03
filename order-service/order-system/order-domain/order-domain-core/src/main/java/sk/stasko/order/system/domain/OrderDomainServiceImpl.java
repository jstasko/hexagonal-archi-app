package sk.stasko.order.system.domain;

import lombok.extern.slf4j.Slf4j;
import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.entity.Product;
import sk.stasko.order.system.domain.entity.Restaurant;
import sk.stasko.order.system.domain.event.OrderCancelledEvent;
import sk.stasko.order.system.domain.event.OrderCreatedEvent;
import sk.stasko.order.system.domain.event.OrderPaidEvent;
import sk.stasko.order.system.domain.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final ZoneId ZONE_ID = ZoneId.of("UTC");

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {}, is initialized", order.getId());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZONE_ID));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {}, paid successfully", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZONE_ID));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {}, approved successfully", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZONE_ID));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order cancelled for order id: {}", order.getId().getValue());
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        Map<Product, Product> restaurantProducts = restaurant.getProducts().stream()
                        .collect(Collectors.toMap(Function.identity(), Function.identity()));

        order.getItems()
                .forEach(orderItem -> {
                    Product currentProduct = orderItem.getProduct();
                    Product restaurantProduct = restaurantProducts.get(currentProduct);
                    if (restaurantProduct != null) {
                        currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
                    }
                });
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() + " is not active");
        }
    }
}
