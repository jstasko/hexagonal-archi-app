package sk.stasko.order.system.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import sk.stasko.order.system.kafka.order.avro.model.PaymentRequestAvroModel;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class OrderKafkaHelper {
    public <T> void handleKafkaFuture(CompletableFuture<SendResult<String, T>> future, String topicName, String orderId, String requestAvroModelName) {
        future.thenAccept(result -> {
                    var metadata = result.getRecordMetadata();
                    log.info("Received successful response from Kafka for order Id: {} Topic: {}, Partition: {}, Offset: {}, Timestamp: {}",
                            orderId,
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            metadata.timestamp()
                    );
                })
                .exceptionally(ex -> {
                    log.error("Error while sending {} message {}, to topic {}", requestAvroModelName,ex.getMessage(), topicName);
                    return null;
                });
    }
}
