import actions.GameAdmin;
import domain.Player;

public class InitData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameAdmin.initGameDatas();
		Player toto = new Player("toto","toto","testGame");
		Player titi = new Player("titi","titi","testGame");
		Player tata = new Player("tata","tata","testGame");
		Player tutu = new Player("tutu","tutu","testGame");
		toto.init();
		titi.init();
		tata.init();
		tutu.init();
	}

}
