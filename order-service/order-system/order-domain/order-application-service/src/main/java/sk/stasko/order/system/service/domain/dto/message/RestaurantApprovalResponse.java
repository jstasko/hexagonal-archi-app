package sk.stasko.order.system.service.domain.dto.message;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record RestaurantApprovalResponse(
        String id,
        String sagaId,
        String orderId,
        String restaurantId,
        Instant createdAt,
        OrderApprovalStatus orderApprovalStatus,
        List<String> failureMessages
) {
}
