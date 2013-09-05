package actions;

import java.util.Map;

import static utils.Redis.JEDIS;
import static utils.Utils.key;

import com.google.common.collect.ImmutableMap;

public class GameAdmin {

	public static void initPlayer(String gameId, String playerId, String name) {
		JEDIS.get().hset(key("player", gameId, playerId), "name", name);
		JEDIS.get().hset(key("player", gameId, playerId), "gold", "4");
		JEDIS.get().hset(key("player", gameId, playerId), "isAlive", "1");
		JEDIS.get().hset(key("player", gameId, playerId), "isStolen", "0");
		JEDIS.get().hset(key("player", gameId, playerId), "canBeStolen", "1");
		JEDIS.get().hset(key("player", gameId, playerId), "canBeDestroy", "1");
		JEDIS.get().hset(key("player", gameId, playerId), "canBeKill", "1");
	}

	public void initPlayerTurn(String gameId, String playerId) {
		JEDIS.get().hset(key("player", gameId, playerId), "isAlive", "1");
		JEDIS.get().hset(key("player", gameId, playerId), "isStolen", "0");
		JEDIS.get().hset(key("player", gameId, playerId), "canBeStolen", "1");
		JEDIS.get().hset(key("player", gameId, playerId), "canBeDestroy", "1");
		JEDIS.get().hset(key("player", gameId, playerId), "canBeKill", "1");
	}

	public static void initGameDatas() {
		JEDIS.get().flushAll();
		initDistricts();
		initDeck();
		initJobs();
	}

	private static void initDistricts() {
		// Religieux
		Map<String, String> temple = ImmutableMap.of("g", "1", "vp", "1");
		Map<String, String> eglise = ImmutableMap.of("g", "2", "vp", "1");
		Map<String, String> monastere = ImmutableMap.of("g", "3", "vp", "1");
		Map<String, String> cathedrale = ImmutableMap.of("g", "5", "vp", "1");
		JEDIS.get().hmset(key("district", "temple"), temple);
		JEDIS.get().hmset(key("district", "eglise"), eglise);
		JEDIS.get().hmset(key("district", "monastere"), monastere);
		JEDIS.get().hmset(key("district", "cathedrale"), cathedrale);

		// Nobles
		Map<String, String> manoir = ImmutableMap.of("g", "3", "vp", "3");
		Map<String, String> chateau = ImmutableMap.of("g", "4", "vp", "4");
		Map<String, String> palais = ImmutableMap.of("g", "5", "vp", "5");
		JEDIS.get().hmset(key("district", "manoir"), manoir);
		JEDIS.get().hmset(key("district", "chateau"), chateau);
		JEDIS.get().hmset(key("district", "palais"), palais);

		// Commerces
		Map<String, String> taverne = ImmutableMap.of("g", "1", "vp", "1");
		Map<String, String> echoppe = ImmutableMap.of("g", "2", "vp", "2");
		Map<String, String> marche = ImmutableMap.of("g", "2", "vp", "2");
		Map<String, String> comptoir = ImmutableMap.of("g", "3", "vp", "3");
		Map<String, String> port = ImmutableMap.of("g", "4", "vp", "4");
		Map<String, String> hoteldeville = ImmutableMap.of("g", "5", "vp", "5");
		JEDIS.get().hmset(key("district", "taverne"), taverne);
		JEDIS.get().hmset(key("district", "echoppe"), echoppe);
		JEDIS.get().hmset(key("district", "marche"), marche);
		JEDIS.get().hmset(key("district", "comptoir"), comptoir);
		JEDIS.get().hmset(key("district", "port"), port);
		JEDIS.get().hmset(key("district", "hoteldeville"), hoteldeville);

		// Militaires
		Map<String, String> tourDeGuet = ImmutableMap.of("g", "1", "vp", "1");
		Map<String, String> prison = ImmutableMap.of("g", "2", "vp", "2");
		Map<String, String> caserne = ImmutableMap.of("g", "3", "vp", "3");
		Map<String, String> forteresse = ImmutableMap.of("g", "5", "vp", "5");
		JEDIS.get().hmset(key("district", "tourdeguet"), tourDeGuet);
		JEDIS.get().hmset(key("district", "prison"), prison);
		JEDIS.get().hmset(key("district", "caserne"), caserne);
		JEDIS.get().hmset(key("district", "forteresse"), forteresse);

		// Merveilles
		Map<String, String> courDesMiracles = ImmutableMap.of("g", "2", "vp",
				"2");
		Map<String, String> donjon = ImmutableMap.of("g", "3", "vp", "3");
		Map<String, String> laboratoire = ImmutableMap.of("g", "5", "vp", "5");
		Map<String, String> manufacture = ImmutableMap.of("g", "5", "vp", "5");
		Map<String, String> observatoire = ImmutableMap.of("g", "5", "vp", "5");
		Map<String, String> cimetiere = ImmutableMap.of("g", "5", "vp", "5");
		Map<String, String> bibliotheque = ImmutableMap.of("g", "6", "vp", "6");
		Map<String, String> ecoleDeMagie = ImmutableMap.of("g", "6", "vp", "6");
		Map<String, String> universite = ImmutableMap.of("g", "6", "vp", "8");
		Map<String, String> dracoport = ImmutableMap.of("g", "6", "vp", "8");
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

	private static void initJobs() {
		JEDIS.get().del("job");
		JEDIS.get().zadd("job", new Double(1), "assassin");
		JEDIS.get().zadd("job", new Double(2), "thief");
		JEDIS.get().zadd("job", new Double(3), "wizard");
		JEDIS.get().zadd("job", new Double(4), "king");
		JEDIS.get().zadd("job", new Double(5), "bishop");
		JEDIS.get().zadd("job", new Double(6), "trader");
		JEDIS.get().zadd("job", new Double(7), "architect");
		JEDIS.get().zadd("job", new Double(8), "warlord");
	}

}
