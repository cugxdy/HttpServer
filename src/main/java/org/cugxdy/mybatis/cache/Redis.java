package org.cugxdy.mybatis.cache;

import redis.clients.jedis.Jedis;

public class Redis {

	private static final class RedisHolder {
		static Jedis redis = new Jedis("127.0.0.1");
	}
	
	static Jedis getInstance() {
		return RedisHolder.redis;
	}
}
