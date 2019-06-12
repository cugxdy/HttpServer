package org.cugxdy.mybatis.cache;

import java.util.HashMap;
import java.util.Map;

public class MapCache implements Cache {

	private String id;
	
	private Map<Object,Object> map = new HashMap<Object,Object>();

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public int Size() {
		// TODO Auto-generated method stub
		return map.size();
	}

	@Override
	public void putObject(Object key, Object value) {
		// TODO Auto-generated method stub
		map.put(key, value);
	}

	@Override
	public Object getObject(Object key) {
		// TODO Auto-generated method stub
		return map.get(key);
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return map.remove(key);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		map.clear();
	}

}
