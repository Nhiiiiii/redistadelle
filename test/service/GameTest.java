package service;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import citadelles.domain.Player;
import citadelles.service.Game;

public class GameTest {
	    
    @Test 
    public void playerIdList() {
    	List<String> result = Game.playerIdList("testGame");
    	assertThat(result).isNotEmpty();
    	assertThat(result).contains("titi");
    	assertThat(result).contains("tutu");
    	assertThat(result).contains("toto");
	}
    
    @Test
    public void players() {
    	Player result = Game.getPlayer("testGame", "toto");
    	assertThat(result.name).isEqualTo("toto");
    }
    
    

}
