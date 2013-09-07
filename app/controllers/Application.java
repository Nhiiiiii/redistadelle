package controllers;

import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.data.*;
import views.html.admin;
import views.html.index;
import views.html.redistadelle;

import static play.data.Form.*;

/**
 * Application pages controller.
 * @author ni
 *
 */
public class Application extends Controller {
  
	public static class Login {
        public String playerId;
    }
	
	/** Index/Login page */
	public static Result index() {
		if(Context.current().session().get("playerId").isEmpty()) {
			return ok(index.render(form(Login.class)));
		}
		return redirect(routes.Application.redistadelle());
	}
	
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(!loginForm.get().playerId.isEmpty()) {
        	 session("playerId", loginForm.get().playerId);
        	 return redirect(routes.Application.redistadelle());
        }
        return badRequest(index.render(loginForm));
        
    }
	
	/** Game page */
    public static Result redistadelle() {
        return ok(redistadelle.render());
    }
    
    /** Admin page */    
    public static Result admin() {
        return ok(admin.render());
    }
    
}
