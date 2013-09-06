package citadelles;

import static utils.Redis.JEDIS;
import static utils.Utils.key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import citadelles.domain.Job;
import citadelles.domain.Player;
import citadelles.domain.Power;

import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

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
		}		
		return player;
	}
	
	/**
	 * Draw a card for a player.
	 * @param gameId the game id
	 * @param playerId the player id
	 */
    public void draw(String gameId, String playerId) {
    	JEDIS.get().rpoplpush(key("pile",gameId), key("pile",gameId,playerId));
    	JEDIS.get().rpoplpush(key("pile",gameId), key("pile",gameId,playerId));
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
	
	   /** Increment gold value. */
    public static Long incrGold(String gameId, String playerId, Long value) {
        return JEDIS.get().hincrBy(key(gameId, playerId), "gold", value);
    }
    
    // TODO LUA script
    public void construct(String gameId, String playerId, String districtKey) { 
        
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
    
	private String id;
		
//	public List<Player> getPlayers() {
//		List<Player> players = new ArrayList<Player>();
//		Set<String> playerKeys = JEDIS.get().keys(key("player",id,"*"));
//		for(String playerKey : playerKeys) {
//			players.add(new Player(playerKey, playerKey, id));
//		}
//		return players;
//	}
	

	
	
	
  

    
//    public Game(List<Player> players, List<District> pile) {
//    	Jedis jedis = new Jedis("localhost");
//        this.players = new ArrayList<Player>(players);
//        this.pile = new ArrayList<District>(pile);
//        this.availableJob = EnumSet.allOf(Job.class);
//    }
    
    
    public List<Map<String,String>> getPlayerInfos() {
    	List<Map<String,String>> playersInfos = new ArrayList<Map<String,String>>();
    	for (String player : JEDIS.get().keys(key("player", id, "*"))) {
    		playersInfos.add(JEDIS.get().hgetAll(player));
    	}
    	return playersInfos;
    }
    
    
//    public void initTurn() {
//        this.availableJob = EnumSet.allOf(Job.class);
//        for(int i=0; i<=players.size(); i++) {
//            if(players.get(i).isFirst) {
//                firstPlayer = i;
//                activePlayer = i;
//                break;
//            }
//        }
//    }
    
    
    public void takeJob(Player player, Job job) {
//        if(!availableJob.contains(job)) return;
//        player.init();
//        player.job = job;
//        switch (job) {
//            case ASSASSIN:
//                player.canBeKill = false;
//                player.canBeStolen = false;
//                break;
//            case THIEF:
//                player.canBeStolen = false;
//                break;
//            case KING:
//                player.isFirst = true;
//                break;
//            case BISHOP:
//                player.canBeDestroy = false;
//                break;
//            case WARLORD:
//                player.canBeDestroy = false;
//                break;
//        }
//        
//        if(activePlayer >= players.size()) {
//            activePlayer = 0;
//        } else {
//            activePlayer++;
//        }
//        availableJob.remove(job);
    }

    public void playAbility(Power power, Player player, Player target, String targetDistrict) {
//        if (!player.powers.contains(power)) return;
//        switch (power) {
//            case ASSASSIN_KILL:
//                if (target.canBeKill) {
//                    target.isAlive = false;
//                    player.powers.remove(power);
//                }
//                break;
//            case THIEF_STOLE:
//                if (target.canBeStolen) {
//                    target.isStolen = true;
//                    player.powers.remove(power);
//                }
//                break;
//            case WIZARD_SWITCH:
//                Integer cardsSize = player.cards.size();
//                player.cards.clear();
//                for (int i = 0; i < cardsSize; i++) {
//                    player.cards.add(pile.get(0));
//                    pile.remove(0);
//                }
//                player.powers.remove(power);
//                break;
//            case WIZARD_STOLE:
//                Set<District> switchDistrict = new HashSet<District>(target.cards);
//                target.cards = new HashSet<District>(player.cards);
//                player.cards = switchDistrict;
//                player.powers.clear();
//                break;
//            case KING_HARVEST:
//                for (District district : player.city) {
//                    if (LORDLY.equals(district.type)) {
//                        player.gold++;
//                    }
//                }
//                player.powers.remove(power);
//                break;
//            case BISHOP_HARVEST:
//                for (District district : player.city) {
//                    if (SACRED.equals(district.type)) {
//                        player.gold++;
//                    }
//                }
//                player.powers.remove(power);
//                break;
//            case TRADER_HARVEST:
//                for (District district : player.city) {
//                    if (SHOP.equals(district.type)) {
//                        player.gold++;
//                    }
//                }
//                player.powers.remove(power);
//                break;
//            case WARLORD_HARVEST:
//                for (District district : player.city) {
//                    if (MILITARY.equals(district.type)) {
//                        player.gold++;
//                    }
//                }
//                player.powers.remove(power);
//                break;
//            case WARLORD_DESTROY:
//                if (target.canBeDestroy && player.gold >= (targetDistrict.value - 1)) {
//                    target.city.remove(targetDistrict);
//                    player.gold -= (targetDistrict.value - 1);
//                    player.powers.remove(power);
//                }
//                break;
//        }
    }

//    public Boolean isFinish() {
//        for(Player player : players) {
//            if(player.city.size() >= 8) return true;
//        }
//        return false;
//    }
    
    
}
