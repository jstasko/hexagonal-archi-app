package sk.stasko.order.system.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sk.stasko.order.system.domain.OrderDomainService;
import sk.stasko.order.system.domain.entity.Customer;
import sk.stasko.order.system.domain.entity.Order;
import sk.stasko.order.system.domain.entity.Restaurant;
import sk.stasko.order.system.domain.event.OrderCreatedEvent;
import sk.stasko.order.system.domain.exception.OrderDomainException;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderCommand;
import sk.stasko.order.system.service.domain.mapper.OrderDataMapper;
import sk.stasko.order.system.service.domain.ports.output.repository.CustomerRepository;
import sk.stasko.order.system.service.domain.ports.output.repository.OrderRepository;
import sk.stasko.order.system.service.domain.ports.output.repository.RestaurantRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent createOrder(CreateOrderCommand cmd) {
        checkCustomer(cmd.customerId());
        Restaurant restaurant = checkRestaurant(cmd);
        Order order = orderDataMapper.createOrderCommandToOrder(cmd);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info("Order created with Id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand cmd) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(cmd);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find restaurant with id {}", restaurant.getId().getValue());
            throw new OrderDomainException("Could not find restaurant with id " + restaurant.getId().getValue());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Customer with id {} not found", customerId);
            throw new OrderDomainException("Customer with id " + customerId + " not found");
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            log.error("Could not save order");
            throw new OrderDomainException("Could not save order");
        }
        log.info("Saved order with id:  {}", order.getId().getValue());
        return orderResult;
    }
}
