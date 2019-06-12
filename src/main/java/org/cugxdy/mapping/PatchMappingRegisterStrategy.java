package org.cugxdy.mapping;

import java.lang.reflect.Method;

import org.cugxdy.annotation.PatchMapping;


public class PatchMappingRegisterStrategy extends AbstractRequestMappingRegisterStrategy implements RequestMappingRegisterStrategy{

	@Override
	void registerMapping(String url, ControllerMapping controllerMapping) {
		// TODO Auto-generated method stub
		ControllerMappingRegistry.getPatchMappings().put(url, controllerMapping);
	}

	@Override
	String getMethodUrl(Method method) {
		// TODO Auto-generated method stub
        if(method.getAnnotation(PatchMapping.class) != null) {
            return method.getAnnotation(PatchMapping.class).value();
        }
        return "";
	}

	@Override
	String getHttpMethod() {
		// TODO Auto-generated method stub
		return "PATCH";
	}

}
