package org.cugxdy.mybatis.session;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarFile;

import org.cugxdy.mybatis.annoattion.delete;
import org.cugxdy.mybatis.annoattion.insert;
import org.cugxdy.mybatis.annoattion.select;
import org.cugxdy.mybatis.annoattion.update;
import org.cugxdy.mybatis.beanutil.MappedStatement;

public class Configuation {
	
	private static Map<Object,Object> properties = new HashMap<Object,Object>();
	
	public static Map<String,Class<?>> mapperClass = new HashMap<String,Class<?>>();
	
	public static Map<String, MappedStatement> mappedStatement = new HashMap<String, MappedStatement>();

	
	static {
		Properties props = new Properties();
		ClassLoader load = Thread.currentThread().getContextClassLoader();
		InputStream input = load.getResourceAsStream("org/cugxdy/mybatis/file/initConfig.properties");	
		try {
			props.load(input);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("load failure");
		}	
		
		Iterator<Object> itor = props.keySet().iterator();
	
		System.out.println(props.get("mapping"));
		
		while(itor.hasNext()) {
			Object obj = itor.next();
			properties.put(obj, props.get(obj));
		}
		
		System.out.println(properties.size());

	}
	
	
	public static Configuation configuation = new Configuation();
	
	
	private Configuation() {
		
		System.out.println("初始化...");
		
		String mapping = (String) properties.get("mapping");
		System.out.println(mapping);
		if(mapping != null && !"".equals(mapping) ) {
			parseMappedStatement(mapping);
		}
		
		if(mapperClass.size() != 0) {
			doParseMySql();
		}
		
		
	}
	
	public static String getUrl() {
		return (String) properties.get("jdbc.url");
	}
	
	public static String getDriver() {
		return (String) properties.get("jdbc.driver");
	}
	
	public static String getUserName() {
		return (String) properties.get("jdbc.username");
	}
	
	public static String getPassword() {
		return (String) properties.get("jdbc.password");
	}


	private void doParseMySql() {
		// TODO Auto-generated method stub
		Set<Entry<String,Class<?>>> entrySet = mapperClass.entrySet();
		for(Entry<String,Class<?>> entry : entrySet) {
			doParse(entry.getKey(),entry.getValue()); 
		}
	}


	private void doParse(String key, Class<?> value) {
		// TODO Auto-generated method stub

		Method[] methods = value.getMethods();
		
		for(Method method : methods) {
			
			MappedStatement statement = new MappedStatement();
			String sql;
			if(method.getAnnotation(select.class) != null) {
				sql = method.getAnnotation(select.class).value();
				statement.setSql(sql);
				statement.setMethod(method);
				statement.setClazz(value);
				statement.setType("select");
			}else if(method.getAnnotation(insert.class) != null) {
				sql = method.getAnnotation(insert.class).value();
				statement.setSql(sql);
				statement.setMethod(method);
				statement.setClazz(value);
				statement.setType("insert");
			}else if(method.getAnnotation(update.class) != null) {
				sql = method.getAnnotation(update.class).value();
				statement.setSql(sql);
				statement.setMethod(method);
				statement.setClazz(value);
				statement.setType("update");
			}else if(method.getAnnotation(delete.class) != null) {
				sql = method.getAnnotation(delete.class).value();
				statement.setSql(sql);
				statement.setMethod(method);
				statement.setClazz(value);
				statement.setType("delete");
			}else {
				continue;
			}
			
			String keyValue = key + "." + method.getName();
			System.out.println(keyValue);
			mappedStatement.put(keyValue, statement);
		}
		
	}


	private void parseMappedStatement(String mapping) {
		// TODO Auto-generated method stub
		String[] packages =  mapping.split(";");
		System.out.println("mapper = " + mapping);
		
		for(String element : packages) {
			doParseMappedStatement(element);
		}
	}
	

	private void doParseMappedStatement(String element) {
		// TODO Auto-generated method stub
		String pkgDirName = element.replace('.', '/');
		
		try {
			Enumeration<URL> urls = Configuation.class.getClassLoader().getResources(pkgDirName);
			if(urls.hasMoreElements()) {
				URL url = urls.nextElement();
				String  protol = url.getProtocol();
				System.out.println("protol = " + protol);

				if("file".equals(protol)) {
					String filePath = URLDecoder.decode(url.getFile(),"UTF-8");
					System.out.println(filePath);
					
					findClassByFile(filePath,element);
				}else if("jar".equals(protol)) {
					JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
					findClassByJar(jarFile,element);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	private void findClassByJar(JarFile jarFile,String element) {
		// TODO Auto-generated method stub
		
	}


	private void findClassByFile(String filePath,String element) {
		// TODO Auto-generated method stub
		File file = new File(filePath);
		if(!file.exists() || !file.isDirectory()) {
			return;
		}
		File[] files = file.listFiles(filter -> filter.isDirectory() || filter.getName().endsWith("class"));
		
		for(File f : files) {
			
			if(f.isDirectory()) {
				findClassByFile(f.getPath(),element+"/"+f.getName());
				continue;
			}

			String fileName = f.getName();
			String ClassPath = element + "." + fileName.substring(0,fileName.length() - 6);
			
			System.out.println("ClassPath = " + ClassPath);
			Class<?> clazz;
			
			try {
				
				clazz = Class.forName(ClassPath);
				System.out.println(clazz.getName());
				
				if(clazz != null ) {
					mapperClass.put(clazz.getName(), clazz);
				}
			} catch (Exception e) {
				// TODO: handle exception
				
				System.out.println("异常发生...");
			}
		}
	}


	public boolean hasStatement(String mappingStatement) {
		return mappedStatement.containsKey(mappingStatement);
	}
	
	public MappedStatement getMappedStatement(String MappedStatementId) {
		return mappedStatement.get(MappedStatementId);
	}


	public String getExector() {
		// TODO Auto-generated method stub
		return (String) properties.get("exector");
	}
	
	
}
