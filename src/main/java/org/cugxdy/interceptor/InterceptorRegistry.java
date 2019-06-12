package org.cugxdy.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InterceptorRegistry {
	
	private final static Set<Interceptor> interceptor = new LinkedHashSet<Interceptor>(8);
	
    private final static Map<String, List<String>> excludeMappings = new HashMap<String, List<String>>(8);
    
    public static void addInterceptor(Interceptor intercept) {
    	interceptor.add(intercept);
    }
    
    public static void addInterceptor(Interceptor intercept,String... excludeMapping) {
    	interceptor.add(intercept);
    	List<String> list = new ArrayList<String>();
    	for(String str : excludeMapping) {
    		list.add(str);
    	}
    	InterceptorRegistry.excludeMappings.put(intercept.getClass().getName(), list);
    }

	public static final Set<Interceptor> getInterceptor() {
		return interceptor;
	}

	public static final List<String> getExcludemappings(Interceptor intercept) {
		return InterceptorRegistry.excludeMappings.get(intercept.getClass().getName());
	}
    
    
    

}
