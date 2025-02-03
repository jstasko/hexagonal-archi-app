package sk.stasko.order.system.service.domain.dto.message;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record PaymentResponse(
        String id,
        String sagaId,
        String orderId,
        String paymentId,
        String customerId,
        BigDecimal price,
        Instant createdAt,
        PaymentStatus paymentStatus,
        List<String> failureMessages
) {
}
