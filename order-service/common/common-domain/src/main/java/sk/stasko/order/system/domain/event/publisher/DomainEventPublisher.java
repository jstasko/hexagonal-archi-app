package sk.stasko.order.system.domain.event.publisher;

import sk.stasko.order.system.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);
}
