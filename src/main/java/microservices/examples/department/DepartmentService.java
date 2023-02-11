package microservices.examples.department;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.system.CacheProxy;
import microservices.examples.system.NotFoundException;

@Service
@Slf4j
public class DepartmentService {
	DepartmentRepository departmentRepository;

	private CacheProxy<Department> departmentCache;
	
	@Autowired
	public DepartmentService(DepartmentRepository departmentRepository, CacheManager cacheManager) {
		super();
		this.departmentRepository = departmentRepository;
		this.departmentCache = new CacheProxy<>(cacheManager, "departments");
	}

	public List<Department> getAll() {
		return departmentRepository.findAll();
	}

	public Department findById(String id) {
		Optional<Department> result = departmentRepository.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		throw new NotFoundException(id);
	}

	public List<Department> findAll() {
		return departmentRepository.findAll();
	}

	public Department findCachedById(String departmentId) {
		Department department = departmentCache.get(departmentId);
		if (department != null) {
			return department;
		}
		department = findById(departmentId);
		if (department == null) {
			log.info("CANNOT FIND department for {}", departmentId);
		}
		departmentCache.put(departmentId, department);
		return department;
	}

	public List<Department> findByIds(List<String> departmentIds) {
		return departmentRepository.findAllById(departmentIds);
	}

	public Department findByName(String name) {
		return departmentRepository.findByName(name);
	}

	public long getLastModified() {
		List<Department> lastUpdated = departmentRepository.findAll(
				PageRequest.of(0, 1, Sort.by("updated").descending()));
		if (lastUpdated == null || lastUpdated.isEmpty()) {
			return 0L;
		}
		return lastUpdated.get(0).getUpdated().toEpochMilli();
	}
}
