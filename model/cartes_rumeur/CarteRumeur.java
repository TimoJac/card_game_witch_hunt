package model.cartes_rumeur;

import java.util.ArrayList;
import java.util.Observable;

import model.*;

/**
 * Cette classe impl�mente les m�thodes communes � toutes les diff�rentes cartes du jeu.
 * 
 * @see AngryMob
 * @see BlackCat
 * @see Broomstick
 * @see Cauldron
 * @see DuckingStool
 * @see EvilEye
 * @see HookedNose
 * @see PetNewt
 * @see PointedHat
 * @see TheInquisition
 * @see Toad
 * @see Wart
 *
 */
public class CarteRumeur extends Observable {
	
	/**
	 * Cet attribut contient le nom de la carte.
	 */
	protected String nom;
	
	/**
	 * Cet attribut contient une String avec les conditions d'application sp�cifique de certaines cartes (f.e. Impossibilit� d'�tre choisi par une autre carte).
	 * 
	 * @see Broomstick
	 * @see Wart
	 */
	protected String conditionUtilisation = null;
	
	/**
	 * Cet attribut contient une string avec la description de l'effet Hunt de la carte, ainsi que ses conditions d'activation si elle en a.
	 * 
	 * @see PointedHat
	 */
	public String descHunt ;
	
	/**
	 * Cet attribut contient une string avec la description de l'effet Witch de la carte, ainsi que ses conditions d'activation si elle en a.
	 * 
	 * @see AngryMob
	 * @see PointedHat
	 * @see TheInquisition
	 */
	public String descWitch ;
	
	/**
	 * Cet attribut contient le nom de la carte jou�e pendant le tour du joueur.
	 */
	private static String nomCarteJouee;

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nom + "\n");
        if (conditionUtilisation != null) {
        	sb.append("\t" + conditionUtilisation + "\n");
        }
        sb.append("\t - Effet Witch : "+ descWitch + "\n");
        sb.append("\t - Effet Hunt : " + descHunt+ "\n\n");

        return sb.toString();
    }
    
   
    // Methode commune utilis�e par toutes les cartes, afin de d�placer chaque carte Rumeur jou�e dans les cartes r�v�l�es du joueur
	/**
	 * Cette m�thode est utilis�e par toutes les cartes afin de faire l'action de r�v�ler une carte rumeur et de la placer dans les cartes r�v�l�es du Joueur.
	 */
    public void revelerCarteRumeur() {
    	
    	ArrayList<CarteRumeur> cartesRumeurJoueur = Partie.getInstance().getTourActuel().getJoueurActuel().getCartesRumeurJoueur();  
    	ArrayList<CarteRumeur> cartesReveleesJoueur = Partie.getInstance().getTourActuel().getJoueurActuel().getCartesReveleeJoueur(); 
    	int indiceCarte;
    	
    	// On r�cup�re l'indice de la carte actuelle dans la liste des cartes Rumeur non r�v�l�es
    	indiceCarte = cartesRumeurJoueur.indexOf(this);
    	
    	if (indiceCarte != -1) { // Si la carte a bien �t� trouv�e dans la liste
    		
    		// On ajoute cette carte aux cartes r�v�l�es du joueur actuel
    		cartesReveleesJoueur.add(cartesRumeurJoueur.get(indiceCarte));
    		
    		// On retire la carte des cartes rumeur non r�v�l�es du joueur
    		cartesRumeurJoueur.remove(indiceCarte);
    	}
    }
    
    /**
     * Cette m�thode centralise toutes les m�thodes {@code notifyObservers("arg")} pour chaque carte en fonction de leur nom.
     * Cela permet de centraliser le code et de seulement appeler cette m�thode dans le code des diff�rentes cartes.
     */
    public void notifierObservers() {
    	//Notifier les observers
    	this.setChanged();
    	Partie.getInstance().getTourActuel().getJoueurActuel().setJoueurEstAccuse(false);
    	
    	switch (this.getNom()) {
    	case "Angry Mob" :
    		this.notifyObservers(Partie.getInstance().getAngryMob());
			break;
		
		case "Black Cat" :
			this.notifyObservers(Partie.getInstance().getBlackCat());
			break;
		
		case "Broomstick" :
			this.notifyObservers(Partie.getInstance().getBroomstick());
			break;
		
		case "Cauldron" :
			this.notifyObservers(Partie.getInstance().getCauldron());
			break;
		
		case "Ducking Stool" :
			this.notifyObservers(Partie.getInstance().getDuckingStool());
			break;
		
		case "Evil Eye" :
			this.notifyObservers(Partie.getInstance().getEvilEye());
			break;
		
		case "Hooked Nose" :
			this.notifyObservers(Partie.getInstance().getHookedNose());
			break;
		
		case "Pet Newt" :
			this.notifyObservers(Partie.getInstance().getPetNewt());
			break;
		
		case "Pointed Hat" :
			this.notifyObservers(Partie.getInstance().getPointedHat());
			break;
		
		case "The Inquisition" :
			this.notifyObservers(Partie.getInstance().getTheInquisition());
			break;
		
		case "Toad" :
			this.notifyObservers(Partie.getInstance().getToad());
			break;
		
		case "Wart" :
			this.notifyObservers(Partie.getInstance().getWart());
			break;
    	}
    			
    }
    
    public String getNom () {
    	return this.nom;
    }
    


	public static String getNomCarteJouee() {
		return nomCarteJouee;
	}


	public static void setNomCarteJouee(String nomCarteJouee) {
		CarteRumeur.nomCarteJouee = nomCarteJouee;
	}

}
