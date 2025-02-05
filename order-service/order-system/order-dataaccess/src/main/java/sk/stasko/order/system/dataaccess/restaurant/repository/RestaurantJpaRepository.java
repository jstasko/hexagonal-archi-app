package sk.stasko.order.system.dataaccess.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.stasko.order.system.dataaccess.restaurant.entity.RestaurantEntity;
import sk.stasko.order.system.dataaccess.restaurant.entity.RestaurantEntityId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {
    Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);
}
