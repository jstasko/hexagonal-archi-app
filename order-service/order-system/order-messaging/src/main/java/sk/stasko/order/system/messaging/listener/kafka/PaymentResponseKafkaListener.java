package sk.stasko.order.system.messaging.listener.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sk.stasko.order.system.kafka.consumer.KafkaConsumer;
import sk.stasko.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import sk.stasko.order.system.kafka.order.avro.model.PaymentStatus;
import sk.stasko.order.system.messaging.mapper.OrderMessagingDataMapper;
import sk.stasko.order.system.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;

import java.util.List;

@Component
@Slf4j
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public PaymentResponseKafkaListener(PaymentResponseMessageListener paymentResponseMessageListener, OrderMessagingDataMapper orderMessagingDataMapper) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment responses received wit h keys: {}, partitions {} and offsets: {}",
                messages.size(), keys, partitions, offsets);

        messages.forEach(paymentResponseAvroModel -> {
            if (PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()) {
                log.info("Processing successful payment for order id : {}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper
                        .paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
            } else if (PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus() ||
                    PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus()) {
                log.info("Processing unsuccessful payment for order id : {} with failureMessages: {}",
                        paymentResponseAvroModel.getOrderId(), paymentResponseAvroModel.getFailureMessages());
                paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper
                        .paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
            }
        });

    }
}
