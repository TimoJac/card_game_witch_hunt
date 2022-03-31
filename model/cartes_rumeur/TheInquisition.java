package model.cartes_rumeur;

import java.util.ArrayList;

import model.*;
import model.joueur.Joueur;
import model.joueur.Role;

/**
 * Cette classe repr�sente la carte The Inquisition, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class TheInquisition extends CarteRumeur{

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte The Inquisition.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {


		// Choisir le prochain joueur
		// Regarder l'identit� du prochain joueur

		Joueur prochainJoueur = Partie.getInstance().getTourActuel().getProchainJoueur();

		System.out.println("Attention... voici l'identit� de " + prochainJoueur.getNom() + " !");

		if (prochainJoueur.getRole() == Role.VILLAGEOIS)
			System.out.println(prochainJoueur.getNom() + " est un villageois !");
		else if (prochainJoueur.getRole() == Role.SORCIERE)
			System.out.println(prochainJoueur.getNom() + "est une sorci�re !");
	}


	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte The Inquisition.
	 * @param joueurActuel le joueur choisi par la carte
	 * @param carteAJeter la carte � jeter choisir par le joueur
	 */
		public void executerCoteWitch(Joueur joueurActuel, CarteRumeur carteAJeter) {
			
			// Ajouter la carte choisie � la d�fausse
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
		 * Cette m�thode est le constructeur de la carte The Inquisition. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
		 */
	public TheInquisition() {
		nom = "The Inquisition";
		descHunt = "[Jouable seulement si vous avez �t� r�v�l� Villageois] Choisissez le prochain joueur. Avant leur tour, regardez secr�tement son identit�.";
		descWitch = "D�faussez une carte de votre main. Jouez en suivant";
	}
	
	
	// ~~~ Witch ? 
	// D�fausser une carte de sa main
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt ! SEULEMENT JOUABLE SI LE JOUEUR EST UN VILLAGEOIS REVELE
	// Choisir le prochain joueur
	// Avant son tour, regarder secr�tement son identit�
}
