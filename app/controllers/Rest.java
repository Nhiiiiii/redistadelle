package controllers;

import java.util.ArrayList;
import java.util.List;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import citadelles.domain.Player;
import citadelles.service.Game;
import citadelles.service.GameAdmin;

/**
 * Rest controller.
 * @author ni
 *
 */
public class Rest extends Controller {

	 /**
	  * Retrieve player's data
	  * @param gameId the game id
	  * @param playerId the player id
	  * @return player's data
	  */
	 public static Result players(String gameId) {
		 String playerId = session("playerId");
		 if(playerId.isEmpty()) {
			 return forbidden();
		 }
		 
		 List<Player> result = new ArrayList<Player>();
		 result.add(Game.getPlayer(gameId, playerId));
		 for(String id: Game.playerIdList(gameId)) {
			 if(!id.equals(playerId)) {
				 result.add(Game.getPlayer(gameId, id));
			 }
		 }
		 return ok(Json.toJson(result));
	 }

	 public static Result kill(String gameId, String job) {
		 Game.kill(gameId, job);
		 return ok();
	 }
	 
	 public static Result stole(String gameId, String job) {
		 Game.stole(gameId, job);
		 return ok();
	 }
	 
	 /** Init redis database */
	 public static Result initRedis() {
		 GameAdmin.initGameDatas();
		 return ok(Json.toJson("OK"));
	 }
}
