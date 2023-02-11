package microservices.examples.customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.common.CodeService;
import microservices.examples.system.CacheProxy;
import microservices.examples.system.NotFoundException;

@Slf4j
public class CustomerServiceCachedImpl implements CustomerService {
    CustomerService customerService;

    private CacheProxy<Customer> customerCache; 

    @Autowired
    public CustomerServiceCachedImpl(CustomerService customerService, CacheManager cacheManager) {
        this.customerService = customerService;
        this.customerCache = new CacheProxy<>(cacheManager, "customers");
    }

    public List<Customer> findAll() {
        return customerService.findAll();
    }

    public List<Customer> findByIds(List<String> customerIds) {
        List<Customer> result = new ArrayList<>(customerIds.size());
        Iterator<String> iterator = customerIds.iterator();
        while (iterator.hasNext()) {
            Customer customer = customerCache.get(iterator.next());
            if (customer != null) {
                result.add(customer);
                iterator.remove();
            }
        }

        List<Customer> queried = customerService.findByIds(customerIds);
        for (Customer customer : queried) {
            customerCache.put(customer.getId(), customer);
            result.add(customer);
        }
        return result;
    }

    public Customer findById(String customerId) {
        Customer customer = customerCache.get(customerId);
        if (customer != null) {
            return customer;
        }
        customer = customerService.findById(customerId);
        customerCache.put(customerId, customer);
        return customer;
    }

}
