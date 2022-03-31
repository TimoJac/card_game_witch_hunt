package model.cartes_rumeur;

import java.util.ArrayList;

import model.*;
import model.joueur.Joueur;
import model.joueur.Role;

/**
 * Cette classe représente la carte The Inquisition, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class TheInquisition extends CarteRumeur{

	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte The Inquisition.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {


		// Choisir le prochain joueur
		// Regarder l'identité du prochain joueur

		Joueur prochainJoueur = Partie.getInstance().getTourActuel().getProchainJoueur();

		System.out.println("Attention... voici l'identité de " + prochainJoueur.getNom() + " !");

		if (prochainJoueur.getRole() == Role.VILLAGEOIS)
			System.out.println(prochainJoueur.getNom() + " est un villageois !");
		else if (prochainJoueur.getRole() == Role.SORCIERE)
			System.out.println(prochainJoueur.getNom() + "est une sorcière !");
	}


	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte The Inquisition.
	 * @param joueurActuel le joueur choisi par la carte
	 * @param carteAJeter la carte à jeter choisir par le joueur
	 */
		public void executerCoteWitch(Joueur joueurActuel, CarteRumeur carteAJeter) {
			
			// Ajouter la carte choisie à la défausse
			Partie.getInstance().getTourActuel().ajouterCarteDefausse(carteAJeter);

			// Supprimer cette carte de la main du joueur
			joueurActuel.retirerCarteRumeurJoueur(carteAJeter);

			// Mettre ce joueur en prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
			
			//Notifier les observers
	        this.notifierObservers();

		}
	
	// Constructeur
		/**
		 * Cette méthode est le constructeur de la carte The Inquisition. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
		 */
	public TheInquisition() {
		nom = "The Inquisition";
		descHunt = "[Jouable seulement si vous avez été révélé Villageois] Choisissez le prochain joueur. Avant leur tour, regardez secrètement son identité.";
		descWitch = "Défaussez une carte de votre main. Jouez en suivant";
	}
	
	
	// ~~~ Witch ? 
	// Défausser une carte de sa main
	// Être le prochain joueur
	//
	//
	// ~~~ Hunt ! SEULEMENT JOUABLE SI LE JOUEUR EST UN VILLAGEOIS REVELE
	// Choisir le prochain joueur
	// Avant son tour, regarder secrètement son identité
}
