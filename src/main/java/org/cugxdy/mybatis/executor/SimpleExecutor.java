package org.cugxdy.mybatis.executor;

import org.cugxdy.mybatis.beanutil.MappedStatement;
import org.cugxdy.mybatis.parameter.StatementHandler;

public class SimpleExecutor implements Executor {

	@Override
	public Object query(MappedStatement mappedStatement, Object[] objects) {
		// TODO Auto-generated method stub
		
		System.out.println("SimpleExecutor");
		String sql = mappedStatement.getSql();
		String type = mappedStatement.getType();
		StatementHandler statementHandler = new StatementHandler(sql,type);
		return statementHandler.execute(objects);
	}

	@Override
	public Object update(MappedStatement mappedStatement, Object[] args) {
		// TODO Auto-generated method stub
		System.out.println("SimpleExecutor");
		String sql = mappedStatement.getSql();
		String type = mappedStatement.getType();
		StatementHandler statementHandler = new StatementHandler(sql,type);
		return statementHandler.executeUpate(args);
	}

}
