package utils;

import redis.clients.jedis.Jedis;

public enum Redis {

	JEDIS;
	
	private Jedis jedis = new Jedis("127.0.0.1", 6379);
	
	public Jedis get() {
		jedis.connect();
		return jedis;
	}

}
