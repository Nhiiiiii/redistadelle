package domain;

import static utils.Redis.JEDIS;
import static utils.Utils.key;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

public class Player {

    private String playerId;
    private String gameId;
    
    public String name;
    public Long gold;
    public Job job;
    public List<District> hand;
    public List<District> city;
    public Long score;
    public Boolean isAlive;
    public Boolean isStolen;
    
    public Player(String gameId, String playerId) {
    	this.playerId = playerId;
    	this.gameId = gameId;
    } 

    /** Get a boolean player info (isFirst, isAlive etc...). */
    public static Boolean getBooleanField(String gameId, String playerId,String field) {
        return Boolean.getBoolean(JEDIS.get().hget(key(gameId, playerId), field));
    }
    
    /** Increment gold value. */
    public static Long incrGold(String gameId, String playerId, Long value) {
        return JEDIS.get().hincrBy(key(gameId, playerId), "gold", value);
    }
    
    // TODO LUA script
    public void construct(String districtKey) { 
        
        Double nbDistrict = JEDIS.get().zscore(key(gameId,playerId,"hand"),districtKey);
        if (nbDistrict == null || nbDistrict <= 0) {
            return;
        }
        
        Map<String,String> district = JEDIS.get().hgetAll(key("district",districtKey));
        Integer cost = Integer.valueOf(district.get("gold"));
        Integer gold = Integer.valueOf(JEDIS.get().hget(key("player",gameId,playerId),"gold"));
        if (cost > gold) return;
        
        if (JEDIS.get().sismember(key(gameId,playerId,"city"), districtKey)) return;
        
        JEDIS.get().hincrBy(key(gameId,playerId),"gold", -cost);
        JEDIS.get().zincrby(key(gameId,playerId,"hand"),-1,districtKey);
        Long citySize = JEDIS.get().sadd(key(gameId,playerId,"city"), districtKey);
        if(citySize >= 8) {
        	JEDIS.get().hset(key("player", gameId, playerId), "canBeDestroy" ,"0");
        }
    }

    

    
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
