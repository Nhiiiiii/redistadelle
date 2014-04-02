package models;

import java.util.ArrayList;
import java.util.List;

import play.libs.Akka;
import play.mvc.WebSocket;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class DaemonActor extends UntypedActor {
	
	// List of daemon (one per player)
    public static List<ActorRef> daemons = new ArrayList<ActorRef>();
	
    public WebSocket.Out<String> out;

    public DaemonActor(WebSocket.Out<String> out) {
        this.out = out;
    }

    public static void addDaemon(WebSocket.Out<String> out){
    	final ActorRef demonActor = Akka.system().actorOf(Props.create(DaemonActor.class, out));
    	daemons.add(demonActor);
    }
    
    @Override
    public void onReceive(Object message) {
        out.write("ping");
    } 
    
}
