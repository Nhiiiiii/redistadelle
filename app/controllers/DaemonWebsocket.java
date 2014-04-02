package controllers;

import models.DaemonActor;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import akka.actor.ActorRef;

public class DaemonWebsocket extends Controller {

	public static Result test() {
		for(ActorRef demonRef: DaemonActor.daemons) {
			demonRef.tell("trolol", null);
		}
		return ok();
	}
	
	public static WebSocket<String> ws() {
	    return new WebSocket<String>() {
	        public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
	        	DaemonActor.addDaemon(out);
	        }

	    };
	}
	
}
