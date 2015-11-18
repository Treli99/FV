package MenuOpen;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class MenuTraitement extends BasicGameState {
	public static final int ID = 0;

	private int choixMenu = 0;
	private GameContainer container;
	private Image background;
	TrueTypeFont font;

	private int choixFinal = 0;

	@Override
	public int getID() {
		return ID;
	}

	// initialise les différents éléments graphiques
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		this.background = new Image(
				"./src/main/ressources/elementsGraphiques/fondTest1.jpg");
		this.container = container;
		Font awtFont = new Font("Comic Sans MS", Font.BOLD, 24);
		font = new TrueTypeFont(awtFont, false);

	}

	// met a jour l'affichage du jeu (60 frames par secondes max)
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(new Color(0, 0, 0, .5f));
		this.afficherJeu(g);
	}

	private void afficherJeu(Graphics g) {

		background.draw(0, 0, container.getWidth(), container.getHeight());
		try {
			Image logo = new Image(
					"./src/main/ressources/elementsGraphiques/logo1.png");

			logo.draw(container.getWidth() / 2 - 150, 50, 300, 200);
			// g.setFont(this.font);
			// g.drawString("WELCOME", container.getWidth() / 2 - 40,
			// container.getHeight() / 2 - 130);

			Image btnDemarrer ;

			Image btnQuitter;

			switch (Math.abs(this.choixMenu)) {
			case 0:
				btnDemarrer = new Image(
						"./src/main/ressources/elementsGraphiques/bouton1Cursus.png");
				btnDemarrer.draw(container.getWidth() / 2 - 150, 300, 300, 100);
				btnQuitter = new Image(
						"./src/main/ressources/elementsGraphiques/bouton1.png");
				btnQuitter.draw(container.getWidth() / 2 - 150, 500, 300, 100);
				break;
			case 1:
				btnDemarrer = new Image(
						"./src/main/ressources/elementsGraphiques/bouton1.png");
				btnDemarrer.draw(container.getWidth() / 2 - 150, 300, 300, 100);
				btnQuitter = new Image(
							"./src/main/ressources/elementsGraphiques/bouton1Cursus.png");
					btnQuitter.draw(container.getWidth() / 2 - 150, 500, 300, 100);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		switch (this.choixFinal) {
		case 1:
			this.choixFinal = 0;
			game.enterState(1, new FadeOutTransition(Color.black),
					new FadeInTransition(Color.black));
		}

	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_Q:
		case Input.KEY_LEFT:
			break;
		case Input.KEY_D:
		case Input.KEY_RIGHT:
			break;
		case Input.KEY_Z:
		case Input.KEY_UP:
			if (this.choixMenu == 0) {
				this.choixMenu = 1;
			} else {
				this.choixMenu = this.choixMenu - 1;
			}
			break;
		case Input.KEY_S:
		case Input.KEY_DOWN:
			this.choixMenu = (this.choixMenu + 1) % 2;
			break;
		case Input.KEY_ENTER:
			this.validerChoix();
			break;
		case Input.KEY_ESCAPE:
			this.container.exit();
		}
	}

	private void validerChoix() {
		switch (this.choixMenu) {
		case 0:
			this.choixFinal = 1;
			break;
		case 1:
			this.container.exit();
		}
	}

}
