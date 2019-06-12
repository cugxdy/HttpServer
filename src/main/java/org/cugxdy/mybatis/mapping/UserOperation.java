package org.cugxdy.mybatis.mapping;

import org.cugxdy.mybatis.annoattion.select;
import org.cugxdy.mybatis.dao.User;


public interface UserOperation {
	
	@select("select id,username,password,email from user")
	public Object getUser();
	
	
	
}
