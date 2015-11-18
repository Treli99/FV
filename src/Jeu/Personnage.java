package Jeu;

import moteur.Moteur2D;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Personnage implements Moteur2D{

	/********************
	 * attributs
	 */
	//taille perso 200x120;
	private boolean accroche;
	private float yAccroche;
	private int largeurHitBox;
	private int hauteurHitBox;
	private float x, y ;//coordonnée à l'affichage du perso (coin haut gauche)
	private enum Direction { GAUCHE, DROITE};
	private boolean enMouvement;
	private Direction direction;
	private boolean saut;
	private boolean chute;
	private Image persoTest; // image du premier perso de test
	private int hauteurSautCourante;
	private int hauteurMaxSaut;
	private int blessure ;
	private boolean attaquer ;
	private Direction coteAttaque;
	protected float vitesseDeplacementHorizontal;
	protected float vitesseDeplacementVertical;
	private boolean invulnerabilite;
	private boolean ejectionInversee;
	private boolean accroupi;
	private int pointDeVie;
	
	/********************
	 * constructeur
	 */
	public Personnage(int largeurHitBox, int hauteurHitBox, float x, float y, String adresseImage, int hauteurMaxSaut, float vitesseDeplacementHorizontal,float vitesseDeplacementVertical,int pv) throws SlickException{
		 persoTest = new Image (adresseImage);
		 this.x = x;
		 this.y =y;
		 this.direction = Direction.DROITE;
		 this.coteAttaque = Direction.DROITE;
		 this.hauteurSautCourante = 0;
		 this.blessure =0 ;
		 this.hauteurMaxSaut = hauteurMaxSaut;
		 this.vitesseDeplacementHorizontal = vitesseDeplacementHorizontal;
		 this.vitesseDeplacementVertical = vitesseDeplacementVertical;
		 this.pointDeVie = pv;
		 this.largeurHitBox = largeurHitBox;
		 this.hauteurHitBox = hauteurHitBox;
	}
	
	/************************
	 * getter et setter
	 */
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getYAccroche() {
		return this.yAccroche;
	}

	public Image retournerImagePersonnage(){
		return persoTest;
	}
	
	public Image retournerImagePersonnageCollision() {
		Image collision = null;
		try {
			collision =  new Image ("./src/main/ressources/perso/collisionAccroche.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return collision;
	}
	
	public int getBlessure() {
		return blessure;
	}

	public boolean isAttaquer() {
		return attaquer;
	}

	public boolean isInvulnerabilite() {
		return invulnerabilite;
	}

	public boolean isAccroupi() {
		return accroupi;
	}

	public int getPointDeVie() {
		return pointDeVie;
	}
	
	public boolean isAccroche(){
		return this.accroche;
	}

	/*************
	 * methodes
	 */
	
	public void courir(String direction) {
		this.enMouvement = true;
		if(direction.equals("gauche")) {
			this.direction = Direction.GAUCHE;
		}
		else {
			this.direction = Direction.DROITE;
		}
	}
	
	public void seDeplacer(TiledMap map,int delta) {
		if(!this.accroche){
			if(!this.isCollision(map, this.x,this.y+10, "sol","solBase")){
				this.chute=true;
				this.accroupi = false;
			}
			float futurX = this.getFuturX(delta);
			float futurY = this.getFuturY(delta);
			if(this.saut && this.isCollision(map, futurX, futurY, "cote", "accroche")){
				if(!this.isCollisionAccroche(map, futurX, futurY, "sol", "solBase") ){
					this.accroche = true;
					this.chute = false;
				}
			}
			this.modifierX(map, futurX, futurY);
			this.modifierY(map, futurX, futurY);
		}else{
			if (this.saut){
				this.remonter();
			}
		}
		
		if(! (this.blessure>0)){
			this.ejectionInversee = false;
		}
		this.invulnerabilite =(this.blessure > 0);	
	}
	
	public void remonter() {
		this.y = this.yAccroche-this.hauteurHitBox -20;
		if(this.direction.equals(Direction.DROITE)){
			this.x += this.hauteurHitBox+30;
		}else{
			this.x -=30;
		}
		this.accroche = false;
	}

	public void modifierY(TiledMap map, float futurX, float futurY) {
		if(!this.isCollision(map, futurX, futurY, "hautBas","solBase")){
			this.y = futurY;
			if(this.saut && this.hauteurSautCourante<this.hauteurMaxSaut){
				this.hauteurSautCourante++;	
			}else{
				this.saut = false;
			}
		}else{
			if(this.blessure >0){
				this.ejectionInversee = true;
			}
		}
	}

	public void modifierX(TiledMap map, float futurX, float futurY) {
		if(!this.isCollision(map, futurX, futurY, "cote", "solBase")){
			this.x = futurX;
		}
	}
	
	public boolean isCollisionAccroche(TiledMap map, float futurX, float futurY, String typeCollision, String layout) {
		int tileW = map.getTileWidth();
	    int tileH = map.getTileHeight();
	    int logicLayer = map.getLayerIndex("solBase");
	    boolean collision = false;
	    float xCollision = futurX;
	    float yCollision = futurY;
	    float xPointSuivant=0F;
	    float yPointSuivant=0F;
	    switch(typeCollision){
	    	case "cote": 
		    	 if(this.direction.equals(Direction.DROITE)){
			    	xCollision += this.largeurHitBox;
			    }
		    	yPointSuivant =this.hauteurHitBox/6;
		    	
		    	 break;
	    	case "hautBas" :
		    	 if(this.chute){
		 	    	yCollision += this.hauteurHitBox;
		 	    }
		    	 xPointSuivant =this.largeurHitBox/6;  
		    	 break;
	    	case "sol":
	    		if(!this.saut){
	    			yCollision += this.hauteurHitBox;
	    			xPointSuivant =this.largeurHitBox/6; 
	    		}else{
	    			return true;
	    		}
	    }
	    collision = this.testerTousPointsCollision(typeCollision, map,xCollision,yCollision,xPointSuivant,yPointSuivant,tileW,tileH,logicLayer);	    
	    if(collision && typeCollision.equals("hautBas")){
	   		 if(this.chute){
	   			 this.chute = false;
	   		 }
	   		 if(this.saut){
	   			 this.aTerre();
	   		 }
	   	 }
		return collision;
	}
	
	public boolean isCollision(TiledMap map, float futurX, float futurY, String typeCollision, String layout) {
		int tileW = map.getTileWidth();
	    int tileH = map.getTileHeight();
	    int logicLayer = map.getLayerIndex("solBase");
	    boolean collision = false;
	    float xCollision = futurX;
	    float yCollision = futurY;
	    float xPointSuivant=0F;
	    float yPointSuivant=0F;
	    switch(typeCollision){
	    	case "cote": 
		    	 if(this.direction.equals(Direction.DROITE)){
			    	xCollision += this.largeurHitBox;
			    }
		    	yPointSuivant =this.hauteurHitBox/6;
		    	
		    	 break;
	    	case "hautBas" :
		    	 if(this.chute){
		 	    	yCollision += this.hauteurHitBox;
		 	    }
		    	 xPointSuivant =this.largeurHitBox/6;  
		    	 break;
	    	case "sol":
	    		if(!this.saut){
	    			yCollision += this.hauteurHitBox;
	    			xPointSuivant =this.largeurHitBox/6; 
	    		}else{
	    			return true;
	    		}
	    }
	    collision = this.testerTousPointsCollision(typeCollision, map,xCollision,yCollision,xPointSuivant,yPointSuivant,tileW,tileH,logicLayer);	    
	    if(collision && typeCollision.equals("hautBas")){
	   		 if(this.chute){
	   			 this.chute = false;
	   		 }
	   		 if(this.saut){
	   			 this.aTerre();
	   		 }
	   	 }
		return collision;
	}

	public boolean testerTousPointsCollision(String typeCollision, TiledMap map, float xCollision, float yCollision, float xPointSuivant, float yPointSuivant, int tileW, int tileH, int logicLayer) {
		boolean collision = false;
		int nombrePointDeCollision = 6;
		if(this.accroupi && typeCollision.equals("cote")){
			
			for(int i = 4; i<nombrePointDeCollision && !collision;i++){
				
				collision = collision || this.testerCollision(map, xCollision, yCollision+ i*yPointSuivant, tileW, tileH, logicLayer);
			}
		}else{
			float yAccroche= 0F;
			for(int i = 1; i<nombrePointDeCollision && !collision;i++){
				yAccroche = yCollision+ i*yPointSuivant;
				collision = collision || this.testerCollision(map, xCollision+ i*xPointSuivant, yAccroche, tileW, tileH, logicLayer);
			}
			if(collision){
				this.yAccroche = yAccroche;
			}
		}
		return collision;
	}

	public boolean testerCollision(TiledMap map, float futurX,float futurY, int tileW, int tileH, int logicLayer){
		//collision à la tuile près
	    Image tile = map.getTileImage((int)futurX / tileW, (int)futurY / tileH, logicLayer);
		boolean collision = tile != null;
		//collision au pixel près
		if (collision) 
		{
		     Color color = tile.getColor((int)futurX % tileW, (int)futurY % tileH);
		     collision = (color.getAlpha() > 0);
		}
		return collision;
	}

	public float getFuturY(int delta) {
		float yFutur = this.y;
		yFutur = this.obtenirYChute(yFutur, delta);
		yFutur = this.obtenirYSaut(yFutur, delta);
		return  yFutur ;
	}
	
	public float obtenirYSaut(float yFutur, int delta) {
		if(this.saut){
			 yFutur = this.y -this.vitesseDeplacementVertical*delta;
			 if(this.blessure > 0){
				yFutur = this.y +this.vitesseDeplacementVertical*delta;
				this.aTerre();
			}
		}
		return yFutur;
	}

	public float obtenirYChute(float yFutur, int delta) {
		if(this.chute){
			yFutur =this.y +this.vitesseDeplacementVertical*delta;
			if(this.blessure > 0){
				yFutur = this.y -0.1F*delta;
			}
		}
		return yFutur;
	}

	public float getFuturX(int delta) {
		float vitesseReelleDeplacementHorizontal = this.vitesseDeplacementHorizontal*delta;
		float xFutur = this.x;
		if(this.accroupi){
			vitesseReelleDeplacementHorizontal = vitesseReelleDeplacementHorizontal/2;
		}
		if(this.enMouvement){
			if(this.direction.equals(Direction.DROITE)){
				xFutur =this.x + vitesseReelleDeplacementHorizontal;
			} else {
				xFutur= this.x -vitesseReelleDeplacementHorizontal;
			}
		}
		if(this.blessure > 0){
			if(this.coteAttaque.equals(Direction.DROITE)){
				xFutur = this.x -0.5f*delta;
			} else {
				xFutur = this.x +0.5f*delta;	
			}
			this.blessure --;
		}
		return xFutur;
	}

	public void onNeBougePlus() {
		this.enMouvement = false;
	}

	public void sauter() {
		if(!this.chute){
			this.saut = true;
			this.accroupi = false;
		}
	}

	public void aTerre() {
		this.saut = false;
		this.chute = true;
		this.accroupi = false;
		this.hauteurSautCourante = 0;
	}

	public void prendreUnCoup(int delta, String coteAttaque, int force) {
		if(!this.invulnerabilite){
			this.pointDeVie -= force;
			if(this.pointDeVie <= 0){
				this.mourir();
			}else{
				this.accroupi = false;
				this.blessure = 10;
				String coteOfficieuxAttaque = "droite";
				if(this.ejectionInversee){
					coteOfficieuxAttaque = "gauche";
				}
				if(coteAttaque.equals(coteOfficieuxAttaque)){
					this.coteAttaque = Direction.DROITE;
				}else{
					this.coteAttaque = Direction.GAUCHE;
				}
			}
		}
			
	}

	public void mourir() {
		
	}

	public void attaquer() {
		this.attaquer = true;
	}

	public float mettreUnCoup(){
		this.attaquer = false;
		if(this.direction.equals(Direction.DROITE)){
			return 100f;
		}else{
			return -100F;
		}
	}
	
	public void sAccroupir() {
		this.accroupi =!this.accroupi;
	}	
}
