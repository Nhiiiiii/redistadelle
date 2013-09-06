package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin;
import views.html.redistadelle;

/**
 * Application pages controller.
 * @author ni
 *
 */
public class Application extends Controller {
  
	/** Game page */
    public static Result index() {
        return ok(redistadelle.render());
    }
    
    /** Admin page */    
    public static Result admin() {
        return ok(admin.render());
    }
    
}
