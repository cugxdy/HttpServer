package org.cugxdy.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerMappingRegistry {
	
    private static final Logger logger = LoggerFactory.getLogger(ControllerMappingRegistry.class);

    private static final Map<String, ControllerMapping> getMappings = new HashMap<>(64);

    private static final Map<String, ControllerMapping> postMappings = new HashMap<>(64);

    private static final Map<String, ControllerMapping> putMappings = new HashMap<>(64);

    private static final Map<String, ControllerMapping> deleteMappings = new HashMap<>(64);

    private static final Map<String, ControllerMapping> patchMappings = new HashMap<>(64);
    
    /**
     * 缓存 REST 控制器类
     */
    private static final Map<String, ControllerBean> beans = new HashMap<>(128);
    
    /**
     * 缓存 REST 控制器类单例
     */
    private static final Map<String, Object> singletons = new ConcurrentHashMap<>(128);
    
    /**
     * 注册Controller类的单例
     * 
     * @param name
     * @param singleton
     */
    public static void registerSingleton(String name, Object singleton) {
        singletons.put(name, singleton);
    }
    
    
    public static Object getSingleton(String name) {
    	if(singletons.containsKey(name)) {
    		return singletons.get(name);
    	}
    	
    	Class<?> clazz = null;
    	try {
    		clazz = Class.forName(name);
		} catch (ClassNotFoundException e) {
            logger.error("Class not found: {}", name);
            return null;
		}
    	Object instance = null;
    	try {
    		instance = clazz.newInstance();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	Object result = singletons.putIfAbsent(name, instance);
        if (result == null) {
            return instance;
        }
        return result;
    	
    			
    }
    
    
    
    
    public static void registerBean(String name, ControllerBean bean) {
        beans.put(name, bean);
    }
    
    public static ControllerBean getBean(String name) {
        return beans.get(name);
    }
    
    

	public static Map<String, ControllerMapping> getDeleteMappings() {
		// TODO Auto-generated method stub
		return deleteMappings;
	}

	public static Map<String, ControllerMapping> getGetMappings() {
		// TODO Auto-generated method stub
		return getMappings;
	}

	public static Map<String, ControllerMapping> getPatchMappings() {
		// TODO Auto-generated method stub
		return patchMappings;
	}

	public static Map<String, ControllerMapping> getPostMappings() {
		// TODO Auto-generated method stub
		return postMappings;
	}

	public static Map<String, ControllerMapping> getPutMappings() {
		// TODO Auto-generated method stub
		return putMappings;
	}
	
	
	
    
    
    
}
