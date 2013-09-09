package controllers;

import citadelles.service.GameAdmin;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Rest controller for admin actions.
 * @author ni
 *
 */
public class RestAdmin extends Controller {

	 /** Init redis database */
	 public static Result initRedis() {
		 GameAdmin.initGameDatas();
		 return ok();
	 }
	 
	 /** Init a game */
	 public static Result initGame(String gameId) {
		 GameAdmin.initGame(gameId);
		 return ok();
	 }
}
