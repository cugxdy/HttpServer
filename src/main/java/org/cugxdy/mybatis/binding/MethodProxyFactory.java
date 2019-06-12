package org.cugxdy.mybatis.binding;

import java.lang.reflect.Proxy;

import org.cugxdy.mybatis.session.DefaultSqlSession;

public class MethodProxyFactory {
	
	private Class<?> mapperInterface;
	
	private Class<?> mapper;
	
	public MethodProxyFactory(Class<?> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}
	
	public Object newInstance(DefaultSqlSession sqlsession) {
		return Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] {mapperInterface}, new MethodProxy(sqlsession));
	}
	

}
