package sk.stasko.order.system.messaging.listener.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sk.stasko.order.system.kafka.consumer.KafkaConsumer;
import sk.stasko.order.system.kafka.order.avro.model.OrderApprovalStatus;
import sk.stasko.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import sk.stasko.order.system.messaging.mapper.OrderMessagingDataMapper;
import sk.stasko.order.system.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;

import java.util.List;

@Component
@Slf4j
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseKafkaListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public RestaurantApprovalResponseKafkaListener(RestaurantApprovalResponseMessageListener restaurantApprovalResponseKafkaListener,
                                                   OrderMessagingDataMapper orderMessagingDataMapper) {
        this.restaurantApprovalResponseKafkaListener = restaurantApprovalResponseKafkaListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(
            @Payload List<RestaurantApprovalResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment responses received with keys: {}, partitions {} and offsets: {}",
                messages.size(), keys, partitions, offsets);

        messages.forEach(restaurantApprovalResponseAvroModel -> {
            if (OrderApprovalStatus.APPROVED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                log.info("Processing approved order for order id : {}", restaurantApprovalResponseAvroModel.getOrderId());
                restaurantApprovalResponseKafkaListener.orderApproved(orderMessagingDataMapper
                        .approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel));
            } else if (OrderApprovalStatus.REJECTED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                log.info("Processing rejected order for order id : {} with failureMessages: {}",
                        restaurantApprovalResponseAvroModel.getOrderId()
                , restaurantApprovalResponseAvroModel.getFailureMessages());
                restaurantApprovalResponseKafkaListener.orderRejected(orderMessagingDataMapper
                        .approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel));
            }
        });
    }
}
