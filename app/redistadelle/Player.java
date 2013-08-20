package redistadelle;

import static redistadelle.Redis.JEDIS;
import static redistadelle.Utils.key;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

public class Player {

    private String id;
    private String gameId;
    private String name;
    
  
       
    public Player(String name, String id, String gameId) {
    	this.name = name;
    	this.id = id;
    	this.gameId = gameId;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public void init() {
		JEDIS.get().hset(key("player", gameId, id), "name", name);
		JEDIS.get().hset(key("player", gameId, id), "gold", "4");
		JEDIS.get().hset(key("player", gameId, id), "isAlive", "1");
		JEDIS.get().hset(key("player", gameId, id), "isStolen", "0");
		JEDIS.get().hset(key("player", gameId, id), "canBeStolen", "1");
		JEDIS.get().hset(key("player", gameId, id), "canBeDestroy", "1");
		JEDIS.get().hset(key("player", gameId, id), "canBeKill", "1");
    }
    
    public void initTurn() {
		JEDIS.get().hset(key("player", gameId, id), "isAlive", "1");
		JEDIS.get().hset(key("player", gameId, id), "isStolen", "0");
		JEDIS.get().hset(key("player", gameId, id), "canBeStolen", "1");
		JEDIS.get().hset(key("player", gameId, id), "canBeDestroy", "1");
		JEDIS.get().hset(key("player", gameId, id), "canBeKill", "1");
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
        
        Double nbDistrict = JEDIS.get().zscore(key(gameId,id,"hand"),districtKey);
        if (nbDistrict == null || nbDistrict <= 0) {
            return;
        }
        
        Map<String,String> district = JEDIS.get().hgetAll(key("district",districtKey));
        Integer cost = Integer.valueOf(district.get("gold"));
        Integer gold = Integer.valueOf(JEDIS.get().hget(key("player",gameId,id),"gold"));
        if (cost > gold) return;
        
        if (JEDIS.get().sismember(key(gameId,id,"city"), districtKey)) return;
        
        JEDIS.get().hincrBy(key(gameId,id),"gold", -cost);
        JEDIS.get().zincrby(key(gameId,id,"hand"),-1,districtKey);
        Long citySize = JEDIS.get().sadd(key(gameId,id,"city"), districtKey);
        if(citySize >= 8) {
        	JEDIS.get().hset(key("player", gameId, id), "canBeDestroy" ,"0");
        }
    }

    
    public void draw() {
    	JEDIS.get().rpoplpush(key(gameId,"pile"), key(gameId,id,"pile"));
    	JEDIS.get().rpoplpush(key(gameId,"pile"), key(gameId,id,"pile"));
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
    
    public Integer getScore() {
        Integer result = 0;
        Boolean hasLordly = false;
        Boolean hasSacred = false;
        Boolean hasShop = false;
        Boolean hasMilitatry = false;
        Set<String> city = JEDIS.get().smembers(key(gameId,id,"city"));
        for (String districtKey : city) {
            Map<String,String> district = JEDIS.get().hgetAll(key("district",districtKey));
            result += Integer.valueOf(district.get("gold"));
            hasLordly = hasLordly || Boolean.getBoolean(district.get("isLordly"));
            hasSacred = hasSacred || Boolean.getBoolean(district.get("isSacred"));;
            hasShop = hasShop || Boolean.getBoolean(district.get("isShop"));;
            hasMilitatry = hasMilitatry || Boolean.getBoolean(district.get("isMilitatry"));;
        }
        if (hasLordly && hasSacred && hasShop && hasMilitatry) result++;
        if (city.size() >= 8) result++;   
        return result;
    }
    
}
