package Jeu;

public class Attaque {
	
	private String type;
	private int force;
	private enum TypeMagie {FEU,EAU,AIR,TERRE,BASE};
	private TypeMagie typeMagie;
	
	public Attaque(String nom, int  force, TypeMagie typeMagie){
		this.type = nom;
		this.force = force;
		this.typeMagie = typeMagie;
	}

}
