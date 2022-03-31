package model.cartes_rumeur;
import java.util.ArrayList;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe représente la carte Pointed Hat, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class PointedHat extends CarteRumeur{

	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Pointed Hat.
	 * @param prochainJoueur le joueur choisi par la carte
	 * @param carteChoisie la carte choisir par le joueur
	 */
	public void executerCoteHunt(Joueur prochainJoueur, CarteRumeur carteChoisie) {

		ArrayList<CarteRumeur> cartesReveleesJoueur = prochainJoueur.getCartesReveleeJoueur();
		ArrayList<CarteRumeur> mainJoueur = prochainJoueur.getCartesRumeurJoueur();
		
		// Ajouter cette carte à la main du joueur et la retirer des cartes révélées
		mainJoueur.add(carteChoisie);
		cartesReveleesJoueur.remove(carteChoisie);
		
		// Le joueur rejoue
		Partie.getInstance().getTourActuel().setProchainJoueur(prochainJoueur);
		
		this.notifierObservers();
	}

	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte PointedHat.
	 * @param joueurActuel le joueur choisi par la carte
	 * @param carteChoisie la carte choisie par le joueur
	 */
	public void executerCoteWitch(Joueur joueurActuel, CarteRumeur carteChoisie ) {
		
		ArrayList<CarteRumeur> cartesReveleesJoueur = joueurActuel.getCartesReveleeJoueur();
		ArrayList<CarteRumeur> mainJoueur = joueurActuel.getCartesRumeurJoueur();
		
		// Ajouter cette carte à la main du joueur et la retirer des cartes révélées
		mainJoueur.add(carteChoisie);
		cartesReveleesJoueur.remove(carteChoisie);
		
		// Le joueur rejoue
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		this.notifierObservers();

	}
	
	// Constructeur 
	/**
	 * Cette méthode est le constructeur de la carte Pointed Hat. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public PointedHat() {
		nom = "Pointed Hat";
		descHunt = "[Jouable seulement si vous avez été révélé Villageois] Reprenez une de vos propres cartes rumeur révélées dans votre main. Choisissez le prochain joueur.";
		descWitch = "[Jouable seulement si vous avez révélé une carte Rumeur] Reprenez une de vos propres cartes rumeur révélées dans votre main. Rejouez.";

	}
	
	
	// ~~~ Witch ?  SEULEMENT JOUABLE SI JOUEUR A DEJA REVELE UNE CARTE RUMEUR
	// Joueur prend une de ses propre carte rumeur révélée et l'ajoute dans sa main
	// Être le prochain joueur
	//
	//
	// ~~~ Hunt ! SEULEMENT JOUABLE SI LE JOUEUR EST UN VILLAGEOIS REVELE
	// Joueur prend une de ses propre carte rumeur révélée et l'ajoute dans sa main
	// Choisir le prochain joueur
}
