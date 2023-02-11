package microservices.examples.common;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

@Mapper
public interface CodeRepository extends CrudRepository<Code, String>{
	public List<Code> findAll();
	
	public List<Code> findByCodeType(String codeType);

	public Code findByCodeTypeAndCode(String codeType, String code);

	public List<Code> findByCodeTypeIn(List<String> codeTypes);

	public List<Code> findByCodeTypeIn(List<String> codeTypes, Pageable pageable);

}
