package microservices.examples.customer;

import java.util.List;

public interface CustomerService {

    Customer findById(String customerId);

    List<Customer> findAll();

    List<Customer> findByIds(List<String> customerIds);

}