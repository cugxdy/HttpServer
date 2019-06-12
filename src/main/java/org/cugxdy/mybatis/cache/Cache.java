package org.cugxdy.mybatis.cache;

public interface Cache {
	
	public String getId();
	
	public int Size();
	
	public void putObject(Object key , Object value);
	
	public Object getObject(Object key);
	
	public Object remove(Object key);
	
	public void clear();
	
}
