package utils;

import redis.clients.jedis.Jedis;

public enum Redis {

	JEDIS;
	
	private Jedis jedis = new Jedis("127.0.0.1", 6379);
	
	public Jedis get() {
		if(!jedis.isConnected()){
			jedis.connect();
		}
		return jedis;
	}

}
