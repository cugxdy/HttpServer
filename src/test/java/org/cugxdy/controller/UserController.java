package org.cugxdy.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cugxdy.annotation.DeleteMapping;
import org.cugxdy.annotation.GetMapping;
import org.cugxdy.annotation.JsonResponse;
import org.cugxdy.annotation.PathVariable;
import org.cugxdy.annotation.PostMapping;
import org.cugxdy.annotation.PutMapping;
import org.cugxdy.annotation.RequestBody;
import org.cugxdy.annotation.RequestMapping;
import org.cugxdy.annotation.RequestParam;
import org.cugxdy.annotation.RestController;
import org.cugxdy.annotation.UrlEncodedForm;
import org.cugxdy.business.ResultObject;
import org.cugxdy.mybatis.binding.MethodProxyFactory;
import org.cugxdy.mybatis.mapping.UserOperation;
import org.cugxdy.mybatis.session.Configuation;
import org.cugxdy.mybatis.session.DefaultSqlSession;
import org.cugxdy.rest.HttpStatus;
import org.cugxdy.rest.ResponseEntity;
import org.cugxdy.util.XmlParser;
import org.cugxdy.web.User;
import org.dom4j.DocumentException;

import com.alibaba.fastjson.JSONObject;

//默认为单例，singleton = false表示启用多例。
//@RestController(singleton = false)
@RestController
@RequestMapping("/CmsService")
public class UserController {

    @GetMapping("/person")
    public ResponseEntity<User> query(@RequestParam("username") String username , @RequestParam("password") String password) {
        // 查询用户
    	System.out.println("查询用户中");
    	System.out.println(username);
    	System.out.println(password);
    	
    	
		Configuation conf = Configuation.configuation;
		
		System.out.println("mybatis ..." + conf.mapperClass.size());

		System.out.println("mybatis ..." + conf.mappedStatement.size());
    	
    	
		MethodProxyFactory factory = new MethodProxyFactory(UserOperation.class);
		
		UserOperation operation = (UserOperation) factory.newInstance(new DefaultSqlSession());
		
		Object obj = operation.getUser();
		
		System.out.println(obj.getClass().getName());
		
		boolean login = false;
		if(obj instanceof List) {
			List list = (List) obj;
			System.out.println(1);
			Iterator<Object> itor = list.iterator();
			while(itor.hasNext()) {
				Map obj2 = (Map) itor.next();
				System.out.println(2);
				System.out.println("username = " + username + "password = " + password);
				if(username.equals(obj2.get("username").toString()) && password.equals(obj2.get("password").toString())) {
					System.out.println("username = " + username + " password = " + password);
					login = true;
				}
			}
		}
		
		System.out.println(login);
		if(login) {
			System.out.println("成功查询至数据库中。。。");
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
    }
    
    @PostMapping("/person")
    @JsonResponse
    public ResponseEntity<ResultObject> User(@UrlEncodedForm Map<String, String> data , @RequestParam("server") String id) {
    	
        // 查询用户
    	System.out.println("数据提交验证中。。。。。。。。。。。。。。。。。。。。");

    	Set<Entry<String,String>> entrySet = data.entrySet();
    	System.out.println("表单数据 = " + data.size());
    	for(Entry<String,String> entry : entrySet) {
    		System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
    	}
    	System.out.println("Server = "  + id);

    	ResultObject object = new ResultObject();
    	XmlParser parser;
    	String action = null;
		try {
			parser = new XmlParser();
			Map<String,String> map = parser.parseXml();
			Set<Entry<String,String>> entrySet1 = map.entrySet();
			for(Entry<String,String> entry : entrySet1) {
				System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
			}
			action = map.get(id);
			System.out.println(action);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(action == null) {

			
			object.setErrerCode("00001");
			object.setErrerText("服务不存在");
	        return ResponseEntity.ok().build(object);
		}else {
			
			Class<?>[] paramTypes = new Class[2];
			paramTypes[0] = Object.class;
			paramTypes[1] = ResultObject.class;
			
			Object[] paramValues = new Object[2];
			paramValues[0] = data;
			paramValues[1] = object;
			
			try {
				Class<?> clazz = Class.forName(action);
				try {
					Method method = clazz.getDeclaredMethod("process", paramTypes);
					if(!method.isAccessible()) {
						method.setAccessible(true);
					}
					try {
						method.invoke(clazz.newInstance(), paramValues);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return ResponseEntity.ok().build(object);
		}
    }
}
