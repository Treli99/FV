package MenuOpen;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import Jeu.JeuTraitementPrincipal;


public class Menu extends StateBasedGame{
	private AppGameContainer container; // le conteneur du jeu 
	
	public Menu() 
	{
		super("Beta fv");
	}
	 
	@Override
	public void initStatesList(GameContainer container) throws SlickException 
	{ 
		if (container instanceof AppGameContainer)
		{ 
		     this.container = (AppGameContainer) container; // on stocke le conteneur du jeu ! 
		} 
		container.setShowFPS(true);
		addState(new MenuTraitement());
		addState (new JeuTraitementPrincipal());
	} 
}
	
