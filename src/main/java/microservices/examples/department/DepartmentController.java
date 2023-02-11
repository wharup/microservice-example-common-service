package microservices.examples.department;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("departments")
@Slf4j
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;
	
	@GetMapping
	public ResponseEntity<List<Department>> getAll(WebRequest swr) {
		long lastModified = departmentService.getLastModified();

		if (swr.checkNotModified(lastModified)) {
			log.debug("departments are not changed since {}", lastModified);
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
					.lastModified(lastModified)
					.build();
		}
		log.debug("returning departments {}", lastModified);
		return ResponseEntity.ok()
				.lastModified(lastModified)
				.body(departmentService.findAll());
	}
	
	@GetMapping("/{departmentId}")
	public Department get(@PathVariable String departmentId) {
		return departmentService.findById(departmentId);
	}
	
	@GetMapping(value = "/{departmentIds}", params = "batchapi")
	public List<Department> getMulti(@PathVariable List<String> departmentIds) {
		return departmentService.findByIds(departmentIds);
	}
	
}
