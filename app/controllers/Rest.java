package controllers;

import actions.GameAdmin;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import domain.Player;

public class Rest extends Controller {

	 public static Result scores(Long gameId) {
		 Player p = new Player("toto", "titi");
		 return ok(Json.toJson(p));
	 }

	 public static Result playersInfos(Long gameId) {
		 return ok(Json.toJson("TODO"));
	 }
	 
	 public static Result gameInfos(Long gameId) {
		 return ok(Json.toJson("TODO"));
	 }

	 public static Result score(Long gameId, Long playerId) {
		 return ok(Json.toJson("TODO"));
	 }
	 
	 public static Result playerInfos(Long gameId, Long playerId) {
		 System.out.println("ffff");
		 return ok(Json.toJson("{name:'joueur1',id:'1',gold:'4',isAlive:'1',isStolen:'0',canBeStolen:'1',canBeDestroy:'1',canBeKill:'1'}"));
	 }
	
	 public static Result init() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result draw() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result selectCard() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result build() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result assassinKill() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result thiefStole() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result wizardSwitch() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result wizardStole() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result traderHarvest() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result kingHarvest() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result bishopHarvest() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result warlordHarvest() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result warlordDestroy() {
		 return ok(Json.toJson("TODO"));
	 }
	
	 public static Result endTurn() {
		 return ok(Json.toJson("TODO"));
	 }

	 public static Result initRedis() {
		 GameAdmin.initGameDatas();
		 return ok(Json.toJson("OK"));
	 }
}
