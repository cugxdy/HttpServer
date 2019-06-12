package org.cugxdy.mybatis.cache;

public class RedisCache implements Cache {

	private String id;
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public int Size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void putObject(Object key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getObject(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

}
