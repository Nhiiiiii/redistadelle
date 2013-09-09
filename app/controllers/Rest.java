package controllers;

import java.util.ArrayList;
import java.util.List;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import citadelles.domain.Player;
import citadelles.service.Game;

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
		 List<Player> result = new ArrayList<Player>();
		 result.add(Game.getPlayer(gameId, playerId));
		 for(String id: Game.playerIdList(gameId)) {
			 if(!id.equals(playerId)) {
				 result.add(Game.getPlayer(gameId, id));
			 }
		 }
		 return ok(Json.toJson(result));
	 }

	 /** Kill action */
	 public static Result kill(String gameId, String job) {
		 Game.kill(gameId, job, session("playerId"));
		 return ok();
	 }
	 
	 /** Stole action */
	 public static Result stole(String gameId, String job) {
		 Game.stole(gameId, job, session("playerId"));
		 return ok();
	 }
	 
	 /** Tax action. */
	 public static Result tax(String gameId) {
		 Game.tax(gameId, session("playerId"));
		 return ok();
	 }
	 
	 /** Partial draw two cards. */
	 public static Result draw(String gameId) {
		 return ok();
	 }
	 
	 /** Choose a card between a partial draw. */
	 public static Result drawChoose(String gameId, Integer card) {
		 return ok();
	 }
	 
	 /** Take gold action. */
	 public static Result takeGold(String gameId) {
		 Game.takeGold(gameId, session("playerId"));
		 return ok();
	 }
	 
}
