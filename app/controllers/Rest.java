package controllers;

import citadelles.Game;
import citadelles.domain.Player;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

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
	 public static Result player(String gameId, String playerId) {
		 Player player = Game.getPlayer(gameId, playerId);
		 return ok(Json.toJson(player));
	 }
	
//	 public static Result scores(Long gameId, Long playerId) {
//		 Player p = new Player();
//		 return ok(Json.toJson(p));
//	 }
//
//
//	 
//	 public static Result gameInfos(Long gameId) {
//		 return ok(Json.toJson("TODO"));
//	 }
//
//	 public static Result score(Long gameId, Long playerId) {
//		 return ok(Json.toJson("TODO"));
//	 }
//	 
//	 public static Result playerInfos(Long gameId, Long playerId) {
//		 System.out.println("ffff");
//		 return ok(Json.toJson("{name:'joueur1',id:'1',gold:'4',isAlive:'1',isStolen:'0',canBeStolen:'1',canBeDestroy:'1',canBeKill:'1'}"));
//	 }
//	
//	 public static Result init() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result draw() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result selectCard() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result build() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result assassinKill() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result thiefStole() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result wizardSwitch() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result wizardStole() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result traderHarvest() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result kingHarvest() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result bishopHarvest() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result warlordHarvest() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result warlordDestroy() {
//		 return ok(Json.toJson("TODO"));
//	 }
//	
//	 public static Result endTurn() {
//		 return ok(Json.toJson("TODO"));
//	 }
//
//	 public static Result initRedis() {
//		 GameAdmin.initGameDatas();
//		 return ok(Json.toJson("OK"));
//	 }
}
