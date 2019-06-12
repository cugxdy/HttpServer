package org.cugxdy.mybatis.binding;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.cugxdy.mybatis.beanutil.MappedStatement;
import org.cugxdy.mybatis.session.Configuation;
import org.cugxdy.mybatis.session.DefaultSqlSession;
import org.cugxdy.mybatis.session.SqlSession;

public class MethodProxy implements InvocationHandler{

	private DefaultSqlSession sqlSession;
	
	public MethodProxy(DefaultSqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	
	@Override
	public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		if(method.getName().equals("toString")) {
			method.invoke(obj, args);
		}
		
		String className = method.getDeclaringClass().getName();
		String methodName = method.getName();
		String MappedStatement = className + "." + methodName;
		
		Configuation  configuation = (Configuation) sqlSession.getConfiguation();
        if ( configuation.hasStatement(MappedStatement)) {
        	MappedStatement mapp = configuation.getMappedStatement(MappedStatement);
        	if(mapp.getType().equals("select")) {
        		return sqlSession.selectOne(MappedStatement, args);
        	}else if(mapp.getType().equals("update")) {
        		
        	}else if(mapp.getType().equals("insert")) {
        		return sqlSession.update(MappedStatement, args);
        	}else {
        		
        	}
        }
        
        // 抛出异常
		return null;
	}
	
	
	private boolean isDefaultMethod(Method method) {
	    return (method.getModifiers()
	        & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
	        && method.getDeclaringClass().isInterface();
	}
	
	  private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
	      throws Throwable {
	    final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
	        .getDeclaredConstructor(Class.class, int.class);
	    // 反射时访问私有变量
	    if (!constructor.isAccessible()) {
	      constructor.setAccessible(true);
	    }
	    
	    // 获取Class类型
	    final Class<?> declaringClass = method.getDeclaringClass();
	    
	    // 执行默认方法
	    return constructor
	        .newInstance(declaringClass,
	            MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
	                | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
	        .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
	  }

}
