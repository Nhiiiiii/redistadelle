package citadelles.service;

import static utils.Redis.JEDIS;
import static utils.Utils.key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

import com.google.common.collect.ImmutableMap;

public class GameAdmin {

//	public static void initPlayer(String gameId, String playerId, String name) {
//		JEDIS.get().hset(key("player", gameId, playerId), "name", name);
//		JEDIS.get().hset(key("player", gameId, playerId), "gold", "4");
//		JEDIS.get().hset(key("player", gameId, playerId), "isAlive", "1");
//		JEDIS.get().hset(key("player", gameId, playerId), "isStolen", "0");
//	}
//
//	public static void initPlayerTurn(String gameId, String playerId) {
//		JEDIS.get().hset(key("player", gameId, playerId), "isAlive", "1");
//		JEDIS.get().hset(key("player", gameId, playerId), "isStolen", "0");
//	}
//

	
	public static void initGame(String gameId) {
		JEDIS.get().hset(key("game",gameId), "turn", "0");
		createPile(gameId);
//		setPlayerOrder(gameId);
	}

	
	/** Create a pile for a game. */
	private static void createPile(String gameId){
		JEDIS.get().del(key("pile",gameId));
		List<String> gameDeck = new ArrayList<String>();
		Set<Tuple> deck = JEDIS.get().zrangeWithScores("deck", 0, -1);
		for (Tuple tuple : deck) {
			for(int i=0; i<tuple.getScore();i++) {
				gameDeck.add(tuple.getElement());
			}
		}
		Collections.shuffle(gameDeck);
		for(String card: gameDeck) {
			JEDIS.get().lpush(key("pile",gameId), card);
		}
	}
	
	private static void setPlayerOrder(String gameId) {
		String turn = JEDIS.get().hget(key("game",gameId), "turn");
		Set<String> players = JEDIS.get().keys(key("player",gameId,"*"));
		for (String playerId : players) {
			JEDIS.get().lpush(key("order", gameId), playerId);
		}
	}
	
	/**
	 * Init game data (district description, deck etc ...).
	 */
	public static void initGameDatas() {
		initDistricts();
		initDeck();
	}

	/** Init district description in redis db. */
	private static void initDistricts() {
		// Religieux
		Map<String, String> temple = ImmutableMap.of("g", "1", "vp", "1", "t", "sacred");
		Map<String, String> eglise = ImmutableMap.of("g", "2", "vp", "1", "t", "sacred");
		Map<String, String> monastere = ImmutableMap.of("g", "3", "vp", "1", "t", "sacred");
		Map<String, String> cathedrale = ImmutableMap.of("g", "5", "vp", "1", "t", "sacred");
		JEDIS.get().hmset(key("district", "temple"), temple);
		JEDIS.get().hmset(key("district", "eglise"), eglise);
		JEDIS.get().hmset(key("district", "monastere"), monastere);
		JEDIS.get().hmset(key("district", "cathedrale"), cathedrale);

		// Nobles
		Map<String, String> manoir = ImmutableMap.of("g", "3", "vp", "3", "t", "lordy");
		Map<String, String> chateau = ImmutableMap.of("g", "4", "vp", "4", "t", "lordy");
		Map<String, String> palais = ImmutableMap.of("g", "5", "vp", "5", "t", "lordy");
		JEDIS.get().hmset(key("district", "manoir"), manoir);
		JEDIS.get().hmset(key("district", "chateau"), chateau);
		JEDIS.get().hmset(key("district", "palais"), palais);

		// Commerces
		Map<String, String> taverne = ImmutableMap.of("g", "1", "vp", "1", "t", "shop");
		Map<String, String> echoppe = ImmutableMap.of("g", "2", "vp", "2", "t", "shop");
		Map<String, String> marche = ImmutableMap.of("g", "2", "vp", "2", "t", "shop");
		Map<String, String> comptoir = ImmutableMap.of("g", "3", "vp", "3", "t", "shop");
		Map<String, String> port = ImmutableMap.of("g", "4", "vp", "4", "t", "shop");
		Map<String, String> hoteldeville = ImmutableMap.of("g", "5", "vp", "5", "t", "shop");
		JEDIS.get().hmset(key("district", "taverne"), taverne);
		JEDIS.get().hmset(key("district", "echoppe"), echoppe);
		JEDIS.get().hmset(key("district", "marche"), marche);
		JEDIS.get().hmset(key("district", "comptoir"), comptoir);
		JEDIS.get().hmset(key("district", "port"), port);
		JEDIS.get().hmset(key("district", "hoteldeville"), hoteldeville);

		// Militaires
		Map<String, String> tourDeGuet = ImmutableMap.of("g", "1", "vp", "1", "t", "military");
		Map<String, String> prison = ImmutableMap.of("g", "2", "vp", "2", "t", "military");
		Map<String, String> caserne = ImmutableMap.of("g", "3", "vp", "3", "t", "military");
		Map<String, String> forteresse = ImmutableMap.of("g", "5", "vp", "5", "t", "military");
		JEDIS.get().hmset(key("district", "tourdeguet"), tourDeGuet);
		JEDIS.get().hmset(key("district", "prison"), prison);
		JEDIS.get().hmset(key("district", "caserne"), caserne);
		JEDIS.get().hmset(key("district", "forteresse"), forteresse);

		// Merveilles
		Map<String, String> courDesMiracles = ImmutableMap.of("g", "2", "vp","2", "t", "wonder");
		Map<String, String> donjon = ImmutableMap.of("g", "3", "vp", "3", "t", "wonder");
		Map<String, String> laboratoire = ImmutableMap.of("g", "5", "vp", "5", "t", "wonder");
		Map<String, String> manufacture = ImmutableMap.of("g", "5", "vp", "5", "t", "wonder");
		Map<String, String> observatoire = ImmutableMap.of("g", "5", "vp", "5", "t", "wonder");
		Map<String, String> cimetiere = ImmutableMap.of("g", "5", "vp", "5", "t", "wonder");
		Map<String, String> bibliotheque = ImmutableMap.of("g", "6", "vp", "6", "t", "wonder");
		Map<String, String> ecoleDeMagie = ImmutableMap.of("g", "6", "vp", "6", "t", "wonder");
		Map<String, String> universite = ImmutableMap.of("g", "6", "vp", "8", "t", "wonder");
		Map<String, String> dracoport = ImmutableMap.of("g", "6", "vp", "8", "t", "wonder");
		JEDIS.get().hmset(key("district", "courDesMiracles"), courDesMiracles);
		JEDIS.get().hmset(key("district", "donjon"), donjon);
		JEDIS.get().hmset(key("district", "laboratoire"), laboratoire);
		JEDIS.get().hmset(key("district", "manufacture"), manufacture);
		JEDIS.get().hmset(key("district", "observatoire"), observatoire);
		JEDIS.get().hmset(key("district", "cimetiere"), cimetiere);
		JEDIS.get().hmset(key("district", "bibliotheque"), bibliotheque);
		JEDIS.get().hmset(key("district", "ecoledemagie"), ecoleDeMagie);
		JEDIS.get().hmset(key("district", "universite"), universite);
		JEDIS.get().hmset(key("district", "dracoport"), dracoport);
	}

	/** Init deck description in redis db */
	private static void initDeck() {
		JEDIS.get().del("deck");
		JEDIS.get().zadd("deck", new Double(3), "temple");
		JEDIS.get().zadd("deck", new Double(4), "eglise");
		JEDIS.get().zadd("deck", new Double(3), "monastere");
		JEDIS.get().zadd("deck", new Double(2), "cathedrale");
		JEDIS.get().zadd("deck", new Double(5), "manoir");
		JEDIS.get().zadd("deck", new Double(4), "chateau");
		JEDIS.get().zadd("deck", new Double(2), "palais");
		JEDIS.get().zadd("deck", new Double(5), "taverne");
		JEDIS.get().zadd("deck", new Double(3), "echoppe");
		JEDIS.get().zadd("deck", new Double(4), "marche");
		JEDIS.get().zadd("deck", new Double(3), "comptoir");
		JEDIS.get().zadd("deck", new Double(3), "port");
		JEDIS.get().zadd("deck", new Double(2), "hoteldeville");
		JEDIS.get().zadd("deck", new Double(3), "tourdeguet");
		JEDIS.get().zadd("deck", new Double(3), "prison");
		JEDIS.get().zadd("deck", new Double(3), "caserne");
		JEDIS.get().zadd("deck", new Double(2), "fortresse");
		JEDIS.get().zadd("deck", new Double(1), "courdesmiracles");
		JEDIS.get().zadd("deck", new Double(2), "donjon");
		JEDIS.get().zadd("deck", new Double(1), "laboratoire");
		JEDIS.get().zadd("deck", new Double(1), "manufacture");
		JEDIS.get().zadd("deck", new Double(1), "observatoire");
		JEDIS.get().zadd("deck", new Double(1), "cimetiere");
		JEDIS.get().zadd("deck", new Double(1), "bibliotheque");
		JEDIS.get().zadd("deck", new Double(1), "ecoleDeMagie");
		JEDIS.get().zadd("deck", new Double(1), "universite");
		JEDIS.get().zadd("deck", new Double(1), "dracoport");
	}



}
