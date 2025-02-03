package sk.stasko.order.system.service.domain.ports.output.repository;

import sk.stasko.order.system.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);
}
