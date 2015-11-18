package moteur;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


/*
 * Interface gérant le moteur 2D
 */

public interface Moteur2D {
	
	public void courir(String direction) ;
	
	public void seDeplacer(TiledMap map,int delta);
	
	public void remonter();

	public void modifierY(TiledMap map, float futurX, float futurY);

	public void modifierX(TiledMap map, float futurX, float futurY) ;

	public boolean isCollision(TiledMap map, float futurX, float futurY, String typeCollision, String layout) ;

	public boolean testerTousPointsCollision(String typeCollision, TiledMap map, float xCollision, float yCollision, float xPointSuivant, float yPointSuivant, int tileW, int tileH, int logicLayer);

	public boolean testerCollision(TiledMap map, float futurX,float futurY, int tileW, int tileH, int logicLayer);

	public float getFuturY(int delta) ;
	
	public float obtenirYSaut(float yFutur, int delta) ;
	
	public float obtenirYChute(float yFutur, int delta) ;

	public float getFuturX(int delta) ;

	public void onNeBougePlus() ;

	public void sauter() ;
	
	public void aTerre() ;

	public void prendreUnCoup(int delta, String coteAttaque, int force);

	public void mourir() ;

	public void attaquer();

	public float mettreUnCoup();
	
	public void sAccroupir() ;

}
