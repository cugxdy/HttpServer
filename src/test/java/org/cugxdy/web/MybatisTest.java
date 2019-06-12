package org.cugxdy.web;

import org.cugxdy.mybatis.binding.MethodProxyFactory;
import org.cugxdy.mybatis.mapping.UserOperation;
import org.cugxdy.mybatis.session.Configuation;
import org.cugxdy.mybatis.session.DefaultSqlSession;
import org.junit.Test;

public class MybatisTest {

	@Test
	public void test() {
		
		Configuation conf = Configuation.configuation;
		
		System.out.println(conf.mapperClass.size());

		System.out.println(conf.mappedStatement.size());
		
		MethodProxyFactory factory = new MethodProxyFactory(UserOperation.class);
		
		UserOperation operation = (UserOperation) factory.newInstance(new DefaultSqlSession());
		
		Object obj = operation.getUser();
		System.out.println(obj.getClass().getName());
		
		
	}
}
