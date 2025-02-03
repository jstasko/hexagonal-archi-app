package sk.stasko.order.system.domain.valueObject;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {
    public OrderId(UUID id) {
        super(id);
    }
}
