package citadelles.service;

import static utils.Redis.JEDIS;
import static utils.Utils.key;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import citadelles.domain.Job;
import citadelles.domain.Player;

import com.google.common.collect.ImmutableMap;

/**
 * Game mechanics.
 * @author ni
 *
 */
public class Game {

	/**
	 * Retrieve player data.
	 * @param gameId the game id
	 * @param playerId the player id
	 * @return player's data
	 */
	public static Player getPlayer(String gameId, String playerId) {
		Player player = new Player();
		Map<String,String> playerMap = JEDIS.get().hgetAll(key("player",gameId,playerId));
		if (!playerMap.isEmpty()) {
			player.num = Long.parseLong(playerMap.get("num"));
			player.name = playerMap.get("name");
			player.gold = Long.parseLong(playerMap.get("gold"));
			player.isAlive = "1".equals(playerMap.get("isAlive"));
			player.isStolen = "1".equals(playerMap.get("isStolen"));
			player.job = Job.valueOf(playerMap.get("job").toUpperCase());
			player.score = score(gameId, playerId);
			player.city = new ArrayList<String>(JEDIS.get().smembers(key("city",gameId,playerId)));
			player.hand = new ArrayList<String>(JEDIS.get().lrange(key("hand",gameId,playerId), 0, -1));
			player.partialDraw = new ArrayList<String>(JEDIS.get().lrange(key("partialDraw",gameId,playerId), 0, -1));
		}		
		return player;
	}
	
	/**
	 * Retrieve players id of a game.
	 * @param gameId the game id
	 * @return Players id of the game
	 */
	public static List<String> playerIdList(String gameId) {
		List<String> result = new ArrayList<String>();
		for(String id : JEDIS.get().keys(key("player",gameId,"*"))) {
			result.add(id.split(":")[2]);
		}
		return result;
	}
	
    /**
     * Get a player score.
     * @param gameId the game id
     * @param playerId the player id
     * @return player's score
     */
	private static Long score(String gameId, String playerId) {
        Long result = 0L;
        Boolean hasLordly = false;
        Boolean hasSacred = false;
        Boolean hasShop = false;
        Boolean hasMilitatry = false;
        Set<String> city = JEDIS.get().smembers(key("city",gameId,playerId));
        for (String districtKey : city) {
            Map<String,String> district = JEDIS.get().hgetAll(key("district",districtKey));
            result += Long.valueOf(district.get("vp"));
            hasLordly = hasLordly || "lordy".equals(district.get("t"));
            hasSacred = hasSacred || "sacred".equals(district.get("t"));
            hasShop = hasShop || "shop".equals(district.get("t"));
            hasMilitatry = hasMilitatry || "military".equals(district.get("t"));
        }
        if (hasLordly && hasSacred && hasShop && hasMilitatry) result++;
        if (city.size() >= 8) result++;   
        return result;
    }
	
	/**
	 * Kill a person in a game.
	 * @param gameId the game id
	 * @param job the person job
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
	 * Stole a person in a game.
	 * @param gameId the game id
	 * @param job the person job
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
	
	/** Increment gold value. */
	public static void incrGold(String gameId, String playerId, Long value) {
	    JEDIS.get().hincrBy(key("player", gameId, playerId), "gold", value);
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
	
//	/**
//	 * Draw a card for a player.
//	 * @param gameId the game id
//	 * @param playerId the player id
//	 */
//    public void draw(String gameId, String playerId) {
//    	JEDIS.get().rpoplpush(key("pile",gameId), key("pile",gameId,playerId));
//    	JEDIS.get().rpoplpush(key("pile",gameId), key("pile",gameId,playerId));
//    }
//    

//    
//    // TODO LUA script
//    public void construct(String gameId, String playerId, String districtKey) { 
//        
//        Double nbDistrict = JEDIS.get().zscore(key(gameId,playerId,"hand"),districtKey);
//        if (nbDistrict == null || nbDistrict <= 0) {
//            return;
//        }
//        
//        Map<String,String> district = JEDIS.get().hgetAll(key("district",districtKey));
//        Integer cost = Integer.valueOf(district.get("gold"));
//        Integer gold = Integer.valueOf(JEDIS.get().hget(key("player",gameId,playerId),"gold"));
//        if (cost > gold) return;
//        
//        if (JEDIS.get().sismember(key(gameId,playerId,"city"), districtKey)) return;
//        
//        JEDIS.get().hincrBy(key(gameId,playerId),"gold", -cost);
//        JEDIS.get().zincrby(key(gameId,playerId,"hand"),-1,districtKey);
//        Long citySize = JEDIS.get().sadd(key(gameId,playerId,"city"), districtKey);
//        if(citySize >= 8) {
//        	JEDIS.get().hset(key("player", gameId, playerId), "canBeDestroy" ,"0");
//        }
//    }
//
//    
//
    
    public Boolean choosePartialDraw(String gameId, String playerId, String districtKey) {
        Transaction t = JEDIS.get().multi();
        Response<List<String>> result = t.lrange(key(gameId,playerId,"pile"), 0, -1);
        t.del(key(gameId,playerId,"pile"));
        t.exec();
        for(String key : result.get()) {
            if(key.equals(districtKey)) {
            	JEDIS.get().zincrby(key(gameId,playerId,"hand"),1,key);
                return true;
            }
        }
        JEDIS.get().lpush(key(gameId,playerId,"pile"), (String[]) result.get().toArray());
        return false;
    }
    
     
}
