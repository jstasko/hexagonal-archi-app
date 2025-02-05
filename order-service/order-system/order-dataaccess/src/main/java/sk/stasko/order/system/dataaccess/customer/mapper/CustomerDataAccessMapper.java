package sk.stasko.order.system.dataaccess.customer.mapper;

import org.springframework.stereotype.Component;
import sk.stasko.order.system.dataaccess.customer.entity.CustomerEntity;
import sk.stasko.order.system.domain.entity.Customer;
import sk.stasko.order.system.domain.valueObject.CustomerId;

@Component
public class CustomerDataAccessMapper {
    public Customer customerEntityToCustomer(CustomerEntity entity) {
        return new Customer(new CustomerId(entity.getId()));
    }
}
