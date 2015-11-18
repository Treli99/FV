package Jeu;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


public class Ennemi extends Personnage{
	private float longueurAllerRetour;
	private float positionActuelle;
	private String directionDeplacement;
	
	public Ennemi(int largeurHitBox, int hauteurHitBox, float x, float y, String adresseImage, int hauteurMaxSaut, float vitesseDeplacementHorizontal,float vitesseDeplacementVertical, float longueurAllerRetour, int pv) throws SlickException{
		super(largeurHitBox,hauteurHitBox,x,y,adresseImage,hauteurMaxSaut,vitesseDeplacementHorizontal, vitesseDeplacementVertical,pv);
		this.longueurAllerRetour = longueurAllerRetour;
		this.positionActuelle = 0F;
		this.directionDeplacement = "droite";
	}
	
	public void executerAllerRetour(TiledMap map, int delta){	
		if(this.positionActuelle >= this.longueurAllerRetour){
			this.positionActuelle = 0F;
			this.changerDeDirection();
		}
		this.courir(this.directionDeplacement);
		float xPrecedent = this.getX();
		this.seDeplacer(map, delta);
		this.positionActuelle += this.vitesseDeplacementHorizontal;
		if(xPrecedent == this.getX()){
			this.changerDeDirection();
			this.positionActuelle = 0F;
		}
		
	}
	
	public void changerDeDirection(){
		if(this.directionDeplacement.equals("droite")){
			this.directionDeplacement = "gauche";
		}else{
			this.directionDeplacement = "droite";
		}
	}

}
