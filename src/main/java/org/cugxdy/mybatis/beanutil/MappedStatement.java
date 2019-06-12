package org.cugxdy.mybatis.beanutil;

import java.lang.reflect.Method;

public class MappedStatement {
	
	private String sql;
	
	private Method method;
	
	private Class<?> clazz;
	
	private String type;

	
	public final String getType() {
		return type;
	}

	public final void setType(String type) {
		this.type = type;
	}

	public final String getSql() {
		return sql;
	}

	public final void setSql(String sql) {
		this.sql = sql;
	}

	public final Method getMethod() {
		return method;
	}

	public final void setMethod(Method method) {
		this.method = method;
	}

	public final Class<?> getClazz() {
		return clazz;
	}

	public final void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	
	
	
}
