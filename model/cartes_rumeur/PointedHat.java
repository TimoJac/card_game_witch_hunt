package model.cartes_rumeur;
import java.util.ArrayList;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Pointed Hat, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class PointedHat extends CarteRumeur{

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Pointed Hat.
	 * @param prochainJoueur le joueur choisi par la carte
	 * @param carteChoisie la carte choisir par le joueur
	 */
	public void executerCoteHunt(Joueur prochainJoueur, CarteRumeur carteChoisie) {

		ArrayList<CarteRumeur> cartesReveleesJoueur = prochainJoueur.getCartesReveleeJoueur();
		ArrayList<CarteRumeur> mainJoueur = prochainJoueur.getCartesRumeurJoueur();
		
		// Ajouter cette carte � la main du joueur et la retirer des cartes r�v�l�es
		mainJoueur.add(carteChoisie);
		cartesReveleesJoueur.remove(carteChoisie);
		
		// Le joueur rejoue
		Partie.getInstance().getTourActuel().setProchainJoueur(prochainJoueur);
		
		this.notifierObservers();
	}

	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte PointedHat.
	 * @param joueurActuel le joueur choisi par la carte
	 * @param carteChoisie la carte choisie par le joueur
	 */
	public void executerCoteWitch(Joueur joueurActuel, CarteRumeur carteChoisie ) {
		
		ArrayList<CarteRumeur> cartesReveleesJoueur = joueurActuel.getCartesReveleeJoueur();
		ArrayList<CarteRumeur> mainJoueur = joueurActuel.getCartesRumeurJoueur();
		
		// Ajouter cette carte � la main du joueur et la retirer des cartes r�v�l�es
		mainJoueur.add(carteChoisie);
		cartesReveleesJoueur.remove(carteChoisie);
		
		// Le joueur rejoue
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		this.notifierObservers();

	}
	
	// Constructeur 
	/**
	 * Cette m�thode est le constructeur de la carte Pointed Hat. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public PointedHat() {
		nom = "Pointed Hat";
		descHunt = "[Jouable seulement si vous avez �t� r�v�l� Villageois] Reprenez une de vos propres cartes rumeur r�v�l�es dans votre main. Choisissez le prochain joueur.";
		descWitch = "[Jouable seulement si vous avez r�v�l� une carte Rumeur] Reprenez une de vos propres cartes rumeur r�v�l�es dans votre main. Rejouez.";

	}
	
	
	// ~~~ Witch ?  SEULEMENT JOUABLE SI JOUEUR A DEJA REVELE UNE CARTE RUMEUR
	// Joueur prend une de ses propre carte rumeur r�v�l�e et l'ajoute dans sa main
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt ! SEULEMENT JOUABLE SI LE JOUEUR EST UN VILLAGEOIS REVELE
	// Joueur prend une de ses propre carte rumeur r�v�l�e et l'ajoute dans sa main
	// Choisir le prochain joueur
}
