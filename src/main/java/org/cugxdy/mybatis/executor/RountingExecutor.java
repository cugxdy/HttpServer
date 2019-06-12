package org.cugxdy.mybatis.executor;

public class RountingExecutor {
	
	public static Executor selectExecutor(String param) {
		switch(param) {
		case "CacheExecutor" : 
			return new CacheExecutor();
		case "SimpleExecutor" :
			return new SimpleExecutor();
		default :
			return null;
		}
	}
}
