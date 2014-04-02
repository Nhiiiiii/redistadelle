package citadelles.service;

import static utils.Redis.JEDIS;
import static utils.Utils.key;

import java.util.Set;

import citadelles.domain.Job;

import com.google.common.collect.ImmutableMap;

public class Orders {

	/**
	 * Kill a person.
	 * @param gameId the game id
	 * @param job the person job
	 * @param playerId assassin player
	 */
	public static void kill(String gameId, String job, String playerId) {
		String assassinId = JEDIS.get().hget(key("job",gameId,"ASSASSIN"), "who");
		Boolean canKill = Boolean.valueOf(JEDIS.get().hget(key("job",gameId,"ASSASSIN"), "kill"));
		if(canKill && playerId.equals(assassinId)) {
			JEDIS.get().hset(key("job",gameId,"ASSASSIN"), "kill", "0");
			String victimId = JEDIS.get().get(key("job",gameId,job));
			if(victimId != null) {
				JEDIS.get().hmset(key("player",gameId,victimId), ImmutableMap.of("isAlive","0"));
			}
		}
	}
	
	
	/**
	 * Stole a person.
	 * @param gameId the game id
	 * @param job the person job
	 * @param playerId thief player
	 */
	public static void stole(String gameId, String job, String playerId) {
		String thiefId = JEDIS.get().hget(key("job",gameId,"THIEF"), "who");
		Boolean canStole = Boolean.valueOf(JEDIS.get().hget(key("job",gameId,"THIEF"), "stole"));
		if(canStole && playerId.equals(thiefId)) {
			JEDIS.get().hset(key("job",gameId,"THIEF"), "stole", "0");
			String victimId = JEDIS.get().get(key("job",gameId,job));
			if(victimId != null) {
				JEDIS.get().hmset(key("player",gameId,victimId), ImmutableMap.of("isStolen","1"));
			}
		}	
	}
	
	/** Take gold */
	public static void takeGold(String gameId, String playerId) {
		if(Boolean.valueOf(JEDIS.get().hget(key("job",gameId,"KING"), "goldOrCard"))) {
			JEDIS.get().hset(key("job",gameId,"KING"), "goldOrCard", "0");
			if("TRADER".equals(JEDIS.get().hget(key("player",gameId,playerId), "job"))) {
				incrGold(gameId, playerId, 3L);
			} else {
				incrGold(gameId, playerId, 3L);
			}
		}
	}
	

	
	/** tax district */
	public static void tax(String gameId, String playerId) {
		String jobName = JEDIS.get().hget(key("player",gameId,playerId), "job");
		Job job = Job.valueOf(jobName);
		switch (job) {
			case KING :
				if(Boolean.valueOf(JEDIS.get().hget(key("job",gameId,"KING"), "tax"))) {
					JEDIS.get().hset(key("job",gameId,"KING"), "tax", "0");
					incrGold(gameId,playerId,getNbDistrict(gameId, playerId, "lordy"));
				}
				break;
			
			case TRADER :
				if(Boolean.valueOf(JEDIS.get().hget(key("job",gameId,"TRADER"), "tax"))){
					JEDIS.get().hset(key("job",gameId,"KING"), "tax", "0");
					incrGold(gameId,playerId,getNbDistrict(gameId, playerId, "shop"));
				}
				break;
				
			case BISHOP :
				if(Boolean.valueOf(JEDIS.get().hget(key("job",gameId,"BISHOP"), "tax"))){
					JEDIS.get().hset(key("job",gameId,"KING"), "tax", "0");
					incrGold(gameId,playerId,getNbDistrict(gameId, playerId, "sacred"));
				}
				break;
				
			case WARLORD :
				if(Boolean.valueOf(JEDIS.get().hget(key("job",gameId,"WARLORD"), "tax"))){
					JEDIS.get().hset(key("job",gameId,"KING"), "tax", "0");
					incrGold(gameId,playerId,getNbDistrict(gameId, playerId, "military"));
				}
				break;
				
			default :
				break;
		}
			
	}
	
	/** Get nb district for a type. */
	private static Long getNbDistrict(String gameId, String playerId, String type) {
		Long result = 0L;
		Set<String> city = JEDIS.get().smembers(key("city",gameId,playerId));
        for (String districtKey : city) {
        	if(type.equals(JEDIS.get().hget(key("district",districtKey), "t"))) {
        		result++;
        	}
        }
        return result;
	}
	
	/** Increment gold value. */
	private static void incrGold(String gameId, String playerId, Long value) {
	    JEDIS.get().hincrBy(key("player", gameId, playerId), "gold", value);
	}
}
