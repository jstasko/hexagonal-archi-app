package sk.stasko.order.system.service.domain.ports.input.message.listener.restaurantapproval;

import sk.stasko.order.system.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalResponseMessageListener {
    void orderApproved(RestaurantApprovalResponse response);
    void orderRejected(RestaurantApprovalResponse response);
}
