package org.cugxdy.mybatis.mapping;

import java.util.List;

import org.cugxdy.mybatis.annoattion.insert;
import org.cugxdy.mybatis.annoattion.select;
public interface ProduceOperation {
	
	@insert("insert into producer (id,name,producer,number,location) values (?,?,?,?,?)")
	public boolean insert(List<?> arg);
	
	
	@select("select id,name,producer,number,location from producer")
	public List<?> query();
}
