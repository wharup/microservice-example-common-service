package microservices.examples.user;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    public List<User> findAll();

    public List<User> findAll(Pageable of);

    public List<User> findAllById(Iterable<String> ids);

    public List<User> findAllByDepartmentId(String departmentId, Pageable of);

    public long countByDepartmentId(String departmentId);
}
