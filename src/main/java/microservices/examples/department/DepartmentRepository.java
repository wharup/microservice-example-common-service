package microservices.examples.department;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, String>{

	public List<Department> findAll();
	
	public List<Department> findAll(Pageable p);

	public List<Department> findAllById(Iterable<String> ids);
	
	public Department findByName(String name);

}
