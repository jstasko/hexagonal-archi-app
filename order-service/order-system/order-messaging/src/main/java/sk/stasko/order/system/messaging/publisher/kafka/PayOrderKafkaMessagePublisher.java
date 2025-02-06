package sk.stasko.order.system.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import sk.stasko.order.system.domain.event.OrderPaidEvent;
import sk.stasko.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import sk.stasko.order.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import sk.stasko.order.system.messaging.mapper.OrderMessagingDataMapper;
import sk.stasko.order.system.service.domain.config.OrderServiceConfigData;
import sk.stasko.order.system.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaTemplate<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderKafkaHelper orderKafkaHelper;


    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                         OrderServiceConfigData orderServiceConfigData,
                                         KafkaTemplate<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
                                         OrderKafkaHelper orderKafkaHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderKafkaHelper = orderKafkaHelper;
    }

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderPaidEvent for orderId: {}", orderId);

        try {
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper
                    .orderPaidEventToRestaurantRequestAvroModel(domainEvent);

            CompletableFuture<SendResult<String, RestaurantApprovalRequestAvroModel>> future =
                    kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(), orderId, restaurantApprovalRequestAvroModel);

            orderKafkaHelper
                    .handleKafkaFuture(future,
                            orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            orderId,
                            "RestaurantApprovalRequestAvroModel"
                    );
            log.info("RestaurantApprovalRequestAvroModel sent to Kafka for order id: {}", restaurantApprovalRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalRequestAvroModel message to kafka with orderId {}, error {}",orderId, e.getMessage());
        }
    }
}
