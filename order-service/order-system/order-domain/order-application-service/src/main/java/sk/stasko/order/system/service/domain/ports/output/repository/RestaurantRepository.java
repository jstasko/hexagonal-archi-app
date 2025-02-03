package sk.stasko.order.system.service.domain.ports.output.repository;

import sk.stasko.order.system.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findByRestaurantInformation(Restaurant restaurant);
}
