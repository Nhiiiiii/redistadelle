package redistadelle;

import redis.clients.jedis.Jedis;

public enum Redis {

	JEDIS;
	
	private Jedis jedis = new Jedis("192.168.1.37", 6379);
	
	public Jedis get() {
		return jedis;
	}
}
