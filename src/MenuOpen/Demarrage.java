package MenuOpen;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;


public class Demarrage {

	public static void main(String[] args) {
		try {
			AppGameContainer container = new AppGameContainer(
					new Menu());
			container.setDisplayMode(1280, 768, false);
			container.setTargetFrameRate(60);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
