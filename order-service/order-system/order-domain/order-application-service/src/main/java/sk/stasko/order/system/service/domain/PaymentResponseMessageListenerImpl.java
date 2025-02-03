package sk.stasko.order.system.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import sk.stasko.order.system.service.domain.dto.message.PaymentResponse;
import sk.stasko.order.system.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {
    @Override
    public void paymentCompleted(PaymentResponse response) {

    }

    @Override
    public void paymentCancelled(PaymentResponse response) {

    }
}
