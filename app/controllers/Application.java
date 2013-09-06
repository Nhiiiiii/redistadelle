package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.redistadelle;
import views.html.admin;

public class Application extends Controller {
  
    public static Result index() {
        return ok(redistadelle.render());
    }
    
    public static Result admin() {
        return ok(admin.render());
    }
    
}
