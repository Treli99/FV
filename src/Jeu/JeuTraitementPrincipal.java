package Jeu;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

public class JeuTraitementPrincipal extends BasicGameState {

	public static final int ID = 1;
	private TiledMap map;// contient la map affichée
	private Personnage perso;
	private boolean pause;
	private int choixPause;
	private GameContainer container;
	private int tempsDeath;
	private StateBasedGame game;
	private float frequenceLireInput;
	private float frequenceTesterCollision;
	//private Input toucheMemorisee;
	private int frequenceCheckDeplacement = 0;
	@Override
	public int getID() {
		return ID;
	}

	// initialise les différents éléments graphiques
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.container = container;
		this.game = game;
		this.map = new TiledMap("./src/main/ressources/map/test2Fv.tmx");
		this.perso = new Personnage(200, 120, 100F,50F,
				"./src/main/ressources/perso/testCollision.png", 40, 0.4F,
				0.4F, 100);
		this.tempsDeath = 100;
	}

	// met a jour l'affichage du jeu (60 frames par secondes max)
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(new Color(0, 0, 0, .5f));
		if (this.perso.getPointDeVie() <= 0 && this.tempsDeath <= 0) {
			this.afficherAccueil(g);
		} else {
			if (this.perso.getPointDeVie() <= 0) {
				this.animationMourir(g);
			}
			this.afficherJeu(g);
			if (this.pause) {
				this.afficherPause(g);
			}
		}

	}

	private void afficherJeu(Graphics g) {
		/*
		 * affichage de la map
		 */
		g.translate(container.getWidth() / 2 - (int) perso.getX() - 100,
				container.getHeight() / 2 - (int) perso.getY() - 60); 
		this.map.render(0, 0, 0); // affiche le premier layer de la map
		this.map.render(0, 0, 1);
		
		/*
		 * affichage perso
		 */
		if ((this.perso.getBlessure() % 2) == 0) {	
			if (this.perso.isAccroupi()) {
				g.setColor(new Color(0.3f, 0.3f, 0.1f, .9f));
				Rectangle hitBoxAccroupi = new Rectangle(this.perso.getX(),
						this.perso.getY() + 60, 200, 60);
				g.fill(hitBoxAccroupi);
			}else{
				perso.retournerImagePersonnage().draw(perso.getX(),
						perso.getY()); 
			}
		}
		
	}

	private void animationMourir(Graphics g) {
		if (this.tempsDeath > 0) {
			g.rotate(perso.getX(), perso.getY(), 101 - this.tempsDeath);
			this.tempsDeath -= 10;
		}
	}

	private void afficherAccueil(Graphics g) {
		g.setColor(new Color(0, 0, 0, 1f));
		g.drawRect(0, 0, container.getWidth(), container.getHeight());
		g.setColor(new Color(1, 1, 1, 1f));
		g.drawString("Tu es mort...", container.getWidth() / 2 - 100,
				container.getHeight() / 2);
	}

	private void afficherPause(Graphics g) {
		RoundedRectangle r = new RoundedRectangle(perso.getX() + 100 - 250,
				perso.getY() + 60 - 150, 500F, 300F, 30);
		g.setColor(new Color(0.117F, 0.13f, 0.20f, .8f));
		g.fill(r);
		g.setColor(new Color(1, 1, 1, 1f));
		g.drawString("PAUSE", perso.getX() + 100 - 30,
				perso.getY() + 60 - 100);
		RoundedRectangle rAccueil = new RoundedRectangle(
				perso.getX() + 100 - 150, perso.getY() + 60 - 50, 300F,
				50F, 50);
		RoundedRectangle rQuit = new RoundedRectangle(
				perso.getX() + 100 - 150, perso.getY() + 60 + 50, 300F,
				50F, 50);
		g.setColor(new Color(0.5f, 0.3f, 0.1f, .9f));
		if (this.choixPause != 0) {
			g.fill(rAccueil);
		} else {
			g.fill(rQuit);
		}
		g.setColor(new Color(0, 0, 0, .9f));
		if (this.choixPause == 0) {
			g.fill(rAccueil);
		} else {
			g.fill(rQuit);
		}
		g.setColor(new Color(1, 1, 1, 1f));
		g.drawString("Retour accueil", perso.getX() + 100 - 70,
				perso.getY() + 60 - 35);
		g.drawString("Quitter", perso.getX() + 100 - 35,
				perso.getY() + 60 + 65);
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		if (!container.hasFocus()) {
			this.afficherMenuPause();
		}
		if (!pause) {
				this.perso.seDeplacer(this.map, delta);
		}

	}

	private void attaquerEnnemi(float mettreUnCoup, int delta, Ennemi ennemi) {
		if (mettreUnCoup < 0) {
			if ((this.perso.getX() + mettreUnCoup) < (ennemi.getX() + 200F)) {
				ennemi.prendreUnCoup(delta, "droite", 10);
			}
		} else {
			if ((this.perso.getX() + mettreUnCoup + 200F) > (ennemi.getX())) {
				ennemi.prendreUnCoup(delta, "gauche", 10);
			}
		}
	}

	private void collisionEnnemi(int delta, Ennemi ennemi) {
		if (this.perso.getX() < ennemi.getX()) {
			if ((this.perso.getX() + 200f > ennemi.getX())) {
				this.collisionOrdonnee(delta, "droite", ennemi);
			}
		} else {
			if ((ennemi.getX() + 200f > this.perso.getX())) {
				this.collisionOrdonnee(delta, "gauche", ennemi);
			}
		}
	}

	private void collisionOrdonnee(int delta, String coteAttaque, Ennemi ennemi) {
		if (this.perso.getY() < ennemi.getY()) {
			if ((this.perso.getY() + 120F > ennemi.getY())) {
				this.perso.prendreUnCoup(delta, coteAttaque, 10);
			}
		} else {
			if ((ennemi.getY() + 120F > this.perso.getY())) {
				this.perso.prendreUnCoup(delta, coteAttaque, 10);
			}
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_Q:
		case Input.KEY_LEFT:
			if (this.perso.isAccroche()) {
			}
			this.perso.courir("gauche");
			break;
		case Input.KEY_D:
		case Input.KEY_RIGHT:
			if (!this.perso.isAccroche()) {
			}
			this.perso.courir("droite");
			break;
		case Input.KEY_Z:
		case Input.KEY_UP:
			if (!this.pause) {
				this.perso.sauter();
			} else {
				this.choixPause = (this.choixPause + 1) % 2;
			}
			break;
		case Input.KEY_S:
		case Input.KEY_DOWN:
			if (!this.pause) {
				// se coucher
			} else {
				this.choixPause = (this.choixPause + 1) % 2;
			}
			break;
		case Input.KEY_A:
		case Input.KEY_L:
			this.perso.attaquer();
			break;
		case Input.KEY_LSHIFT:
			this.perso.sAccroupir();
			break;
		case Input.KEY_ENTER:
			if (this.pause) {
				if (this.choixPause == 1) {
					this.game.enterState(0, new FadeOutTransition(Color.black),
							new FadeInTransition(Color.black));
				} else {
					this.container.exit();
				}
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch (key) {
		case Input.KEY_D:
		case Input.KEY_Q:
		case Input.KEY_LEFT:
		case Input.KEY_RIGHT:
			this.perso.onNeBougePlus();
			break;
		case Input.KEY_Z:
		case Input.KEY_UP:
			this.perso.aTerre();
			break;
		case Input.KEY_ESCAPE:
			this.afficherMenuPause();
		}
	}

	public void afficherMenuPause() {
		this.pause = !this.pause;
		this.choixPause = 1;
	}

}

