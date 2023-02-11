package microservices.examples.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.system.CacheProxy;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

	private static final boolean useCache = true;

	UserService userService;

	private CacheProxy<User> userCache;

	@Autowired
	public UserController(UserService userService, CacheManager cacheManager) {
		super();
		this.userService = userService;
		this.userCache = new CacheProxy<>(cacheManager, "users");
	}

	@GetMapping
	public List<User> getAll() {
		return userService.findAll();
	}

	@GetMapping("/{userId}")
	public User get(@PathVariable String userId) {
		if (useCache) {
			User user = userCache.get(userId);
			if (user != null) {
				return user;
			}
			user = userService.findById(userId);
			userCache.put(userId, user);
			return user;
		} else {
			return userService.findById(userId);
		}
	}

	@GetMapping(value = "/{userIds}", params = "batchapi")
	public List<User> getByIds(@PathVariable List<String> userIds) {
		if (useCache) {
			List<User> result = new ArrayList<>(userIds.size());

			Iterator<String> iterator = userIds.iterator();
			while (iterator.hasNext()) {
				User user = userCache.get(iterator.next());
				if (user != null) {
					result.add(user);
					iterator.remove();
				}
			}

			List<User> queried = userService.findByIds(userIds);
			for (User user : queried) {
				userCache.put(user.getId(), user);
				result.add(user);
			}
			return result;
		} else {
			return userService.findByIds(userIds);
		}
	}
}
