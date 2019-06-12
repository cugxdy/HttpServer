package org.cugxdy.mapping;

import java.lang.reflect.Method;

public interface RequestMappingRegisterStrategy {
	
	void register(Class<?> clazz, String baseUrl, Method method);
}
