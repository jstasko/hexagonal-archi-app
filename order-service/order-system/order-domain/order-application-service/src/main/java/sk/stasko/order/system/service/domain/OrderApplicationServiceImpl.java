package sk.stasko.order.system.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderCommand;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderResponse;
import sk.stasko.order.system.service.domain.dto.track.TrackOrderQuery;
import sk.stasko.order.system.service.domain.dto.track.TrackOrderResponse;
import sk.stasko.order.system.service.domain.ports.input.service.OrderApplicationService;

@Slf4j
@Service
@Validated
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler createCommandHandler;
    private final OrderTrackCommandHandler trackCommandHandler;

    public OrderApplicationServiceImpl(OrderCreateCommandHandler createCommandHandler,
                                       OrderTrackCommandHandler trackCommandHandler) {
        this.createCommandHandler = createCommandHandler;
        this.trackCommandHandler = trackCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand command) {
        return this.createCommandHandler.createOrder(command);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return this.trackCommandHandler.trackOrder(trackOrderQuery);
    }
}
