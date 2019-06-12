package org.cugxdy.mybatis.cache.decorators;

import org.cugxdy.mybatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingCache implements Cache {

	private Logger log = LoggerFactory.getLogger(LoggingCache.class);
	
	private Cache cache;
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return cache.getId();
	}

	@Override
	public int Size() {
		// TODO Auto-generated method stub
		return cache.Size();
	}

	@Override
	public void putObject(Object key, Object value) {
		// TODO Auto-generated method stub
		cache.putObject(key, value);
		log.info("缓存数据添加成功...");
	}

	@Override
	public Object getObject(Object key) {
		// TODO Auto-generated method stub
		Object obj = cache.getObject(key);
		log.info("缓存数据获取成功...");
		return obj;
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		Object obj = cache.remove(key);
		log.info("缓存数据删除成功...");
		return obj;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		cache.clear();
		log.info("缓存数据清空...");
	}

}
