package microservices.examples.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping()
public class CodeController {
	@Autowired
	CodeService service;
	
	@GetMapping("/codes")
	public List<Code> getCodes () {
		return service.findAll();
	}

	@GetMapping("/code-types/{codeType}/codes/{code}")
	public Code getCode(@PathVariable String codeType, @PathVariable String code) {
		return service.find(codeType, code);
	}

	@GetMapping(value = "/code-types/{codeTypes}")
	public ResponseEntity<List<Code>> getMulti(@PathVariable List<String> codeTypes, WebRequest swr) {
		//① 마지막 변경 시간 조회
		long lastModified = service.getLastModified(codeTypes);

		//② 클라이언트가 가진 데이터의 변경 시간 조회
		if (swr.checkNotModified(lastModified)) {
			//③ 데이터 없이 상태 코드 304만 반환
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
		}
		//④ 클라이언트의 시간과 다른 경우, 결과 조회 후 반환
		List<Code> codes = service.findByCodeTypes(codeTypes);
		return ResponseEntity.ok().body(codes);
	}
	
}
