package org.cugxdy.mapping;

import java.util.ArrayList;
import java.util.List;

public class ControllerMapping {
	
	private String url;
	
	private String  className;
	
	private String classMethod;
	
	private List<ControllerMappingParameter> parameters = new ArrayList<ControllerMappingParameter>();
	
    /**
     * 是否输出结果为JSON
     */
    private boolean JsonResponse;
    
    /**
     * 单例类
     */
    private boolean singleton;

	public final String getUrl() {
		return url;
	}

	public final void setUrl(String url) {
		this.url = url;
	}

	public final String getClassName() {
		return className;
	}

	public final void setClassName(String className) {
		this.className = className;
	}

	public final String getClassMethod() {
		return classMethod;
	}

	public final void setClassMethod(String classMethod) {
		this.classMethod = classMethod;
	}

	public final List<ControllerMappingParameter> getParameters() {
		return parameters;
	}

	public final void setParameters(List<ControllerMappingParameter> parameters) {
		this.parameters = parameters;
	}

	public final boolean isJsonResponse() {
		return JsonResponse;
	}

	public final void setJsonResponse(boolean jsonResponse) {
		JsonResponse = jsonResponse;
	}

	public final boolean isSingleton() {
		return singleton;
	}

	public final void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

}
