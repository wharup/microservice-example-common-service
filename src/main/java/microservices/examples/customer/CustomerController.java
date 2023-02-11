package microservices.examples.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customers")
public class CustomerController {

    //로컬 캐시 사용 여부
	private static final boolean useCache = true;

	CustomerService customerService;

	@Autowired
	public CustomerController(CustomerService customerService, CacheManager cacheManager) {
		super();
        if (useCache) {
            this.customerService = new CustomerServiceCachedImpl(customerService, cacheManager);
        } else {
            this.customerService = customerService;
        }
	}

	@GetMapping
	public List<Customer> getAll(Pageable pageable) {
		return customerService.findAll();
	}

	@GetMapping("/{customerId}")
	public Customer get(@PathVariable String customerId) {
		return customerService.findById(customerId);
	}

	@GetMapping(value = "/{customerIds}", params = "batchapi")
	public List<Customer> getMulti(@PathVariable List<String> customerIds) {
		return customerService.findByIds(customerIds);
	}

}
