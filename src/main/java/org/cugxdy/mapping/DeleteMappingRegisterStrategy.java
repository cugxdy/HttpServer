package org.cugxdy.mapping;

import java.lang.reflect.Method;

import org.cugxdy.annotation.DeleteMapping;


public class DeleteMappingRegisterStrategy extends AbstractRequestMappingRegisterStrategy implements RequestMappingRegisterStrategy{

	@Override
	void registerMapping(String url, ControllerMapping controllerMapping) {
		// TODO Auto-generated method stub
		ControllerMappingRegistry.getDeleteMappings().put(url, controllerMapping);
	}

	@Override
	String getMethodUrl(Method method) {
		// TODO Auto-generated method stub
		if(method.getAnnotation(DeleteMapping.class) != null) {
			return method.getAnnotation(DeleteMapping.class).value();
		}
		return null;
	}

	@Override
	String getHttpMethod() {
		// TODO Auto-generated method stub
		return "DELETE";
	}

}
