


import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

public class WindowGame extends BasicGame 
{
	 private AppGameContainer container; // le conteneur du jeu 
	 private TiledMap map;
	 
	 private float x = 64, y = 128;
	 private int direction = 0;
	 private boolean moving = false;
	 private boolean saut = false;
	 private static int hauteurMax = 50;
	 private int hauteurSaut = 00;
	 private Animation[] animations = new Animation[8];
	
    public WindowGame() 
    {
        super("teeeeeeeeest");
    }

   
    
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException 
    {
      	 g.translate(container.getWidth() / 2 - (int)this.x, container.getHeight() / 2 - (int)this.y);
      	 this.map.render(0, 0,0);
         this.map.render(0, 0,1);
      	 this.map.render(0, 0,2);
      	 g.setColor(new Color(0, 0, 0, .5f));
    	 g.fillOval(x - 16, y - 8, 32, 16);
    	 g.drawAnimation(animations[direction + (moving ? 4 : 0)], x-32, y-60); 
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException 
    {
    	//saut
    	if(this.saut  && !isCollision(this.x , this.y - 0.2f * delta, "mur"))
  		{
  			this.y -= 0.2f * delta;
  		}
    	else
    	{
    		//chute
	     	if(!isCollision(this.x , this.y + 0.2f * delta, "sol") && !this.saut )
	     	{
	          	this.y += 0.2f * delta;
	        }
	     	//
	        if(isCollision(this.x , this.y + 0.2f * delta, "sol"))
	     	{
	     		 this.saut = true;
	     	}
    	}
    	if (this.moving) 
    	{
    		float futurX = getFuturX(delta);
            float futurY = getFuturY(delta);
            
            boolean collision = isCollision(futurX, futurY, "mur");
            
            if (collision) 
            {
            	this.moving = false;
            } 
            else 
            {	
                this.x = futurX;
                this.y = futurY;
               
                switch(this.direction)
                {
                	case 1 : 
                		this.x -= 0.2f * delta;
                		break;
                	case 3 : 
                		this.x += 0.2f * delta; 
                	
               	}
               
            }
        }
    	
    	
    }
 
    @Override
    public void init(GameContainer container) throws SlickException 
    {
    	//container.setTargetFrameRate(60);
        this.container = (AppGameContainer) container;
        this.map = new TiledMap("./src/main/ressources/map/mapTest2.tmx");
        SpriteSheet spriteSheet = new SpriteSheet("./src/main/ressources/perso/perso.png", 64, 64);
        this.animations[0] = loadAnimation(spriteSheet, 0, 1, 0);
        this.animations[1] = loadAnimation(spriteSheet, 0, 1, 1);
        this.animations[2] = loadAnimation(spriteSheet, 0, 1, 2);
        this.animations[3] = loadAnimation(spriteSheet, 0, 1, 3);
        this.animations[4] = loadAnimation(spriteSheet, 1, 9, 0);
        this.animations[5] = loadAnimation(spriteSheet, 1, 9, 1);
        this.animations[6] = loadAnimation(spriteSheet, 1, 9, 2);
        this.animations[7] = loadAnimation(spriteSheet, 1, 9, 3);
    }

    
    private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) 
    {
        Animation animation = new Animation();
        for (int x = startX; x < endX; x++) 
        {
            animation.addFrame(spriteSheet.getSprite(x, y), 100);
        }
        return animation;
    }
    
    
    
    
    private boolean isCollision(float x, float y, String nomLayer) 
    {
        int tileW = this.map.getTileWidth();
        int tileH = this.map.getTileHeight();
        int logicLayer = this.map.getLayerIndex(nomLayer);
        Image tile = this.map.getTileImage((int)x / tileW, (int)y / tileH, logicLayer);
        boolean collision = tile != null;
        if (collision) 
        {
            Color color = tile.getColor((int)x % tileW, (int)y % tileH);
            collision = (color.getAlpha() > 0);
        }
        return collision;
    }

    private float getFuturX(int delta) 
    {
        float futurX = this.x;
        switch(this.direction)
        {
        	case 1 : futurX -= 0.2f * delta ; break;
        	case 3 : futurX += 0.2f * delta ;
        }
        return futurX;
    }

    private float getFuturY(int delta) {
        float futurY = this.y;
        if (this.saut) 
        {
        	futurY -= 0.2f * delta;
        }
        return futurY;
    }
    
    @Override
    public void keyPressed(int key, char c) 
    {
        switch (key) 
        {
            case Input.KEY_UP:  if(this.saut && (this.hauteurSaut < hauteurMax)){ this.hauteurSaut +=1;}
            					if(this.hauteurSaut >= hauteurMax){this.saut = false;}break;
            case Input.KEY_LEFT:  this.direction = 1; this.moving = true; break;
            case Input.KEY_RIGHT: this.direction = 3; this.moving = true; break;
        }
    }
    
    
    @Override
    public void keyReleased(int key, char c) 
    {
        switch (key) 
        {
        	case Input.KEY_ESCAPE: container.exit(); break;
            case Input.KEY_UP:    this.hauteurSaut =0; this.saut= false; break;
            case Input.KEY_LEFT:  this.direction = 0; this.moving = false; break;
            case Input.KEY_RIGHT: this.direction = 0; this.moving = false; break;
        }
    }
    
    public static void main(String[] args) throws SlickException 
    {
        new AppGameContainer(new WindowGame(), 640, 480, false).start();
    }
}
