package sk.stasko.order.system.service.domain.ports.input.message.listener.payment;

import sk.stasko.order.system.service.domain.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse response);
    void paymentCancelled(PaymentResponse response);
}
