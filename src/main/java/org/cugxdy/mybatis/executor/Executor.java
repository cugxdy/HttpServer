package org.cugxdy.mybatis.executor;

import org.cugxdy.mybatis.beanutil.MappedStatement;

public interface Executor {

	Object query(MappedStatement sql, Object[] objects);

	Object update(MappedStatement mappedStatement, Object[] args);

}
