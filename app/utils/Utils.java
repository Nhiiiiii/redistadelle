package utils;

import static utils.Redis.JEDIS;

public class Utils {

	public static String key(String... ids) {
		StringBuffer result = new StringBuffer();
		for (String id : ids){
			result.append(id).append(":");
		}
		return result.deleteCharAt(result.length()-1).toString();
	}
	
}
