package sk.stasko.order.system.service.domain.ports.input.service;

import jakarta.validation.Valid;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderCommand;
import sk.stasko.order.system.service.domain.dto.create.CreateOrderResponse;
import sk.stasko.order.system.service.domain.dto.track.TrackOrderQuery;
import sk.stasko.order.system.service.domain.dto.track.TrackOrderResponse;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand command);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
