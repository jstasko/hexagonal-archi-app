package sk.stasko.order.system.dataaccess.restaurant.mapper;

import org.springframework.stereotype.Component;
import sk.stasko.order.system.dataaccess.restaurant.entity.RestaurantEntity;
import sk.stasko.order.system.dataaccess.restaurant.exception.RestaurantDataAccessException;
import sk.stasko.order.system.domain.entity.Product;
import sk.stasko.order.system.domain.entity.Restaurant;
import sk.stasko.order.system.domain.valueObject.Money;
import sk.stasko.order.system.domain.valueObject.ProductId;
import sk.stasko.order.system.domain.valueObject.RestaurantId;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .toList();
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(
                        () -> new RestaurantDataAccessException("Restaurant entity not found")
                );

        List<Product> restaurantProducts = restaurantEntities.stream()
                .map(entity ->
                    new Product(
                            new ProductId(entity.getProductId()), entity.getProductName(), new Money(entity.getProductPrice())
                    ))
                .toList();
        return Restaurant.newBuilder()
                .id(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
