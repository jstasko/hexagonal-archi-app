package sk.stasko.order.system.dataaccess.customer.adapter;

import org.springframework.stereotype.Component;
import sk.stasko.order.system.dataaccess.customer.mapper.CustomerDataAccessMapper;
import sk.stasko.order.system.dataaccess.customer.repository.CustomerJpaRepository;
import sk.stasko.order.system.domain.entity.Customer;
import sk.stasko.order.system.service.domain.ports.output.repository.CustomerRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository, CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return this.customerJpaRepository.findById(customerId)
                .map(customerDataAccessMapper::customerEntityToCustomer);
    }
}
