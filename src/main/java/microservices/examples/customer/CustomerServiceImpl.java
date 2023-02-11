package microservices.examples.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.system.NotFoundException;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        super();
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findById(String customerId) {
        Optional<Customer> result = customerRepository.findById(customerId);
        if (result.isPresent()) {
            return result.get();
        }
        log.info("CANNOT FIND customer for {}", customerId);
        throw new NotFoundException(customerId);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }


    @Override
    public List<Customer> findByIds(List<String> customerIds) {
        return customerRepository.findAllById(customerIds);
    }

}
