package sk.stasko.order.system.dataaccess.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import sk.stasko.order.system.domain.valueObject.OrderItemId;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
@IdClass(OrderItemEntityId.class)
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    private Long id;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_entity_id")
    private OrderEntity orderEntity;


    private UUID productId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(orderEntity, that.orderEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderEntity);
    }
}