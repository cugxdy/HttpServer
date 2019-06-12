package org.cugxdy.mapping;

import java.lang.reflect.Method;

import org.cugxdy.annotation.GetMapping;

public class GetMappingRegisterStrategy extends AbstractRequestMappingRegisterStrategy implements RequestMappingRegisterStrategy{

	@Override
	void registerMapping(String url, ControllerMapping controllerMapping) {
		// TODO Auto-generated method stub
		ControllerMappingRegistry.getGetMappings().put(url, controllerMapping);
	}

	@Override
	String getMethodUrl(Method method) {
		// TODO Auto-generated method stub
		if(method.getAnnotation(GetMapping.class) != null) {
			return method.getAnnotation(GetMapping.class).value();
		}
		return null;
	}

	@Override
	String getHttpMethod() {
		// TODO Auto-generated method stub
		return "GET";
	}

}
