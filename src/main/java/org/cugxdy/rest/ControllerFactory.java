package org.cugxdy.rest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.cugxdy.annotation.DeleteMapping;
import org.cugxdy.annotation.GetMapping;
import org.cugxdy.annotation.PatchMapping;
import org.cugxdy.annotation.PostMapping;
import org.cugxdy.annotation.PutMapping;
import org.cugxdy.annotation.RequestMapping;
import org.cugxdy.annotation.RestController;
import org.cugxdy.mapping.ControllerBean;
import org.cugxdy.mapping.ControllerMappingRegistry;
import org.cugxdy.mapping.DeleteMappingRegisterStrategy;
import org.cugxdy.mapping.GetMappingRegisterStrategy;
import org.cugxdy.mapping.PatchMappingRegisterStrategy;
import org.cugxdy.mapping.PostMappingRegisterStrategy;
import org.cugxdy.mapping.PutMappingRegisterStrategy;
import org.cugxdy.mapping.RequestMappingRegisterContext;
import org.cugxdy.mapping.RequestMappingRegisterStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerFactory {
	private final Logger logger = LoggerFactory.getLogger(ControllerFactory.class);
	
	public static AtomicInteger id = new AtomicInteger(0);
	
	public void registerController(String controllerBasePackage) {
		Set<Class<?>> findClass = findClassesByPackage(controllerBasePackage);
		
		for(Class clazz : findClass) {
			registerClass(clazz);
		}
	}

	private void registerClass(Class clazz) {
		// TODO Auto-generated method stub
		String className = clazz.getName();
		
		System.out.println("Class" + id.getAndIncrement() + " = " + className);
		
		logger.info("Registered REST Controller: {}", className);
		ControllerBean bean = new ControllerBean(clazz,((RestController) clazz.getAnnotation(RestController.class)).singleton());
		// 注册ControllerBean对象
		ControllerMappingRegistry.registerBean(className, bean);
		
		String url = null;
		RequestMapping requestMapping =  (RequestMapping) clazz.getAnnotation(RequestMapping.class);
		if(requestMapping != null) {
			System.out.println("requestMapping = "+ requestMapping.value());
			url = requestMapping.value();
		}
		
		Method[] methods = clazz.getMethods();
		
		for(Method method : methods) {
			RequestMappingRegisterStrategy strategy = null;
			
			if(method.getAnnotation(GetMapping.class) != null ) {
				strategy = new GetMappingRegisterStrategy();
			}else if( method.getAnnotation(PostMapping.class) != null ) {
				strategy = new PostMappingRegisterStrategy();
			}else if(  method.getAnnotation(PutMapping.class) != null  ) {
				strategy = new PutMappingRegisterStrategy();
			}else if( method.getAnnotation(DeleteMapping.class) != null ) {
				strategy = new DeleteMappingRegisterStrategy();
			}else if( method.getAnnotation(PatchMapping.class) != null) {
				strategy = new PatchMappingRegisterStrategy();
			}
			
			if(strategy != null) {
				RequestMappingRegisterContext conetxt = new  RequestMappingRegisterContext(strategy);
				System.out.println("Class = " + conetxt.getClass().getName());
				conetxt.registerMapping(clazz, url, method);
			}
		}
	}


	private Set<Class<?>> findClassesByPackage(String controllerBasePackage) {
		// TODO Auto-generated method stub
		Set<Class<?>> clazz = new LinkedHashSet<Class<?>>(64);
		
		String pkgDirName = controllerBasePackage.replace('.', '/');
		
		try {
			Enumeration<URL> urls = ControllerFactory.class.getClassLoader().getResources(pkgDirName);
			while(urls.hasMoreElements()) {
				
				URL url = urls.nextElement();
				
				String protol = url.getProtocol();
				
				if("file".equals(protol)) {
					
					String filePath = URLDecoder.decode(url.getFile(),"UTF-8");
					findClassesByFile(filePath,controllerBasePackage,clazz);
					
				}else if("jar".equals(protol)) {
					JarFile jarFile = ((JarURLConnection)url.openConnection()).getJarFile();
					findClassesByJar(controllerBasePackage, jarFile, clazz);
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return clazz;
	}

	private void findClassesByJar(String controllerBasePackage, JarFile jarFile, Set<Class<?>> clazz) {
		// TODO Auto-generated method stub

        String pkgDir = controllerBasePackage.replace(".", "/");
        Enumeration<JarEntry> entry = jarFile.entries();
        while (entry.hasMoreElements()) {
            JarEntry jarEntry = entry.nextElement();

            String jarName = jarEntry.getName();
            if (jarName.charAt(0) == '/') {
                jarName = jarName.substring(1);
            }
            if (jarEntry.isDirectory() || !jarName.startsWith(pkgDir) || !jarName.endsWith(".class")) {
                // 非指定包路径， 非class文件
                continue;
            }

            // 获取类名，去掉 ".class" 后缀
            String[] classNameSplit = jarName.split("/");
            String className = controllerBasePackage + "." + classNameSplit[classNameSplit.length - 1];
            if(className.endsWith(".class")) {
                className = className.substring(0, className.length() - 6);
            }

            // 加载类
            Class<?> zlazz = null;
            try {
            	zlazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.error("Class not found, {}", className);
            }
            if (zlazz != null && zlazz.getAnnotation(org.cugxdy.annotation.RestController.class) != null) {
                if (zlazz != null) {
                	clazz.add(zlazz);
                }
            }
        }
	}

	private void findClassesByFile(String filePath, String controllerBasePackage, Set<Class<?>> clazz) {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		if(!file.exists() || !file.isDirectory()) {
			return ;
		}
		File[] files = file.listFiles(filter -> filter.isDirectory() || filter.getName().endsWith("class"));        
	
		for(File f : files) {
			if(f.isDirectory()) {
                findClassesByFile(controllerBasePackage + "." + f.getName(), filePath + "/" + f.getName(), clazz);
                continue;
			}
			
			String className = f.getName();
			className = controllerBasePackage + "." + className.substring(0, className.length() - 6);
			
			Class<?> zlazz = null;
			try {
				zlazz = Class.forName(className);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			if(zlazz != null &&  zlazz.getAnnotation(org.cugxdy.annotation.RestController.class ) != null) {
				if(zlazz != null) {
					clazz.add(zlazz);
				}
			}
		}
		
	}		
}
