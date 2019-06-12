package org.cugxdy.mybatis.session;

import org.cugxdy.mybatis.beanutil.MappedStatement;
import org.cugxdy.mybatis.executor.Executor;
import org.cugxdy.mybatis.executor.RountingExecutor;

public class DefaultSqlSession{

	private Configuation configuation;
	private Executor executor;
	
	public DefaultSqlSession() {
		this.configuation = Configuation.configuation;
		this.executor = RountingExecutor.selectExecutor(configuation.getExector());
	}
	
	
	public <T> T selectOne(String clazz, Object... objects) {
		// TODO Auto-generated method stub
		MappedStatement mappedStatement = configuation.getMappedStatement(clazz);
		System.out.println("代理对象执行sql中");
		return (T) executor.query(mappedStatement, objects);
	}


	public Object getConfiguation() {
		// TODO Auto-generated method stub
		return configuation;
	}


	@SuppressWarnings("unchecked")
	public <T> T update(String mappedStatement, Object[] args) {
		// TODO Auto-generated method stub
		MappedStatement statement = configuation.getMappedStatement(mappedStatement);
		System.out.println("代理对象执行sql中");
		return (T) executor.update(statement, args);
	}

}
