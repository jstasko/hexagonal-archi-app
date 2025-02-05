package sk.stasko.order.system.domain.entity;

import sk.stasko.order.system.domain.valueObject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    public Customer() {
    }

    public Customer(CustomerId id) {
        super.setId(id);
    }
}
