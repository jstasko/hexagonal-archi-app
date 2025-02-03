package sk.stasko.order.system.service.domain.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import sk.stasko.order.system.domain.valueObject.OrderStatus;

import java.util.List;
import java.util.UUID;

@Builder
public record TrackOrderResponse(
        @NotNull
        UUID orderTrackingId,
        @NotNull
        OrderStatus orderStatus,
        List<String> failureMessages
) {
}
