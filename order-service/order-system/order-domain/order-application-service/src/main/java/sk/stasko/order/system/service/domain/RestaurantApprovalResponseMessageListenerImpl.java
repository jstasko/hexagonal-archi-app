package sk.stasko.order.system.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import sk.stasko.order.system.service.domain.dto.message.RestaurantApprovalResponse;
import sk.stasko.order.system.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {
    @Override
    public void orderApproved(RestaurantApprovalResponse response) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse response) {

    }
}
