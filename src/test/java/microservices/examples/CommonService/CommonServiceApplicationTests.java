package microservices.examples.CommonService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.CommonServiceApplication;
import microservices.examples.common.Code;
import microservices.examples.customer.Customer;
import microservices.examples.department.Department;
import microservices.examples.user.User;

@SpringBootTest(classes = CommonServiceApplication.class)
@AutoConfigureMockMvc
@Slf4j
class CommonServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    
	@Test
	void shouldGetAllCustomers() throws Exception {
	    MvcResult result = mockMvc.perform(get("/customers"))
				.andExpect(status().isOk())
				.andReturn();
	    
        List<Customer> readValue = extracted(result, new TypeReference<List<Customer>>(){});
        log.error("{}", readValue.size());
        for(Customer c : readValue) {
            log.error("{}", c);
        }
	}
	
	@Test
    void shouldGetAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(get("/users")).andExpect(status().isOk()).andReturn();

        List<User> readValue = extracted(result, new TypeReference<List<User>>(){});
        log.error("{}", readValue.size());
        for (User u : readValue) {
            log.error("{}", u);
        }
    }
	
	@Test
	void shouldGetAllDepartments() throws Exception {
	    MvcResult result = mockMvc.perform(get("/departments"))
	                .andExpect(status().isOk())
	                .andReturn();
	    
	    List<Department> readValue = extracted(result, new TypeReference<List<Department>>(){});
	    log.error("{}", readValue.size());
        for(Department d : readValue) {
            log.error("{}", d);
        }
	}

	@Test
	void shouldGetAllCodes() throws Exception {
	    MvcResult result = mockMvc.perform(get("/codes"))
	                .andExpect(status().isOk())
	                .andReturn();
	    
        List<Code> readValue = extracted(result, new TypeReference<List<Code>>(){});
	    log.error("{}", readValue.size());
	    for(Code c : readValue) {
	        log.error("{}", c);
	    }
	}
	
    private <T> List<T> extracted(MvcResult result, TypeReference<List<T>> valueTypeRef)
                throws IOException, JsonParseException, JsonMappingException {
        byte[] data = result.getResponse().getContentAsByteArray();
        List<T> readValue = objectMapper.readValue(data, valueTypeRef);
        return readValue;
    }
    

}

