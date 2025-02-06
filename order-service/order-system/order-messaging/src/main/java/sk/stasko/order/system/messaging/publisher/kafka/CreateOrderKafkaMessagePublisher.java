package sk.stasko.order.system.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import sk.stasko.order.system.domain.event.OrderCreatedEvent;
import sk.stasko.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import sk.stasko.order.system.messaging.mapper.OrderMessagingDataMapper;
import sk.stasko.order.system.service.domain.config.OrderServiceConfigData;
import sk.stasko.order.system.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;

import java.util.concurrent.*;

@Component
@Slf4j
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaTemplate<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderKafkaHelper orderKafkaHelper;

    public CreateOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                            OrderServiceConfigData orderServiceConfigData,
                                            KafkaTemplate<String, PaymentRequestAvroModel> kafkaProducer,
                                            OrderKafkaHelper orderKafkaHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderKafkaHelper = orderKafkaHelper;
    }

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCreatedEvent for orderId: {}", orderId);

        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderCreatedEventToPaymentRequestAvroModel(domainEvent);

            CompletableFuture<SendResult<String, PaymentRequestAvroModel>> future =
                    kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(), orderId, paymentRequestAvroModel);

            orderKafkaHelper
                    .handleKafkaFuture(
                            future,
                            orderServiceConfigData.getPaymentResponseTopicName(),
                            orderId,
                            "PaymentRequestAvroModel"
                    );
            log.info("PaymentRequestAvroModel sent to Kafka for order id: {}", paymentRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message to kafka with orderId {}, error {}",orderId, e.getMessage());
        }
    }
}
