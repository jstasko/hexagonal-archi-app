package sk.stasko.order.system.service.domain.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import sk.stasko.order.system.domain.valueObject.OrderStatus;

import java.util.UUID;

@Builder
public record CreateOrderResponse(
        @NotNull UUID orderTrackingId,
        @NotNull OrderStatus orderStatus,
        @NotNull String message
) {
}
