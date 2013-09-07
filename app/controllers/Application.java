package controllers;

import static play.data.Form.form;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin;
import views.html.index;
import views.html.redistadelle;

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
		if(session("playerId") == null || session("playerId").isEmpty()) {
			return ok(index.render(form(Login.class)));
		}
		return redirect(routes.Application.redistadelle());
	}
	
	/** Login */
    public static Result login() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(!loginForm.get().playerId.isEmpty()) {
        	 session("playerId", loginForm.get().playerId);
        	 return redirect(routes.Application.redistadelle());
        }
        return badRequest(index.render(loginForm));
        
    }
    
    /** Logout */
    public static Result logout() {
    	session().clear();
    	return redirect(routes.Application.index());
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
