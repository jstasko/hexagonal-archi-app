package sk.stasko.order.system.domain.entity;

import sk.stasko.order.system.domain.valueObject.Money;
import sk.stasko.order.system.domain.valueObject.OrderId;
import sk.stasko.order.system.domain.valueObject.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    private OrderItem(Builder builder) {
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subTotal;
        super.setId(builder.id);
    }

    public boolean isPriceValid() {
        return price.isGreaterThanZero() &&
                price.equals(product.getPrice()) &&
                price.multiply(quantity).equals(subTotal);
    }

    public void initializeOrderItem(OrderId oderId, OrderItemId orderItemId) {
        this.orderId = oderId;
        super.setId(orderItemId);
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Product product;
        private int quantity;
        private Money price;
        private Money subTotal;
        private OrderItemId id;

        private Builder() {
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subTotal(Money val) {
            subTotal = val;
            return this;
        }

        public Builder id(OrderItemId val) {
            id = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
