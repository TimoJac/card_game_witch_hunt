package model.cartes_rumeur;

import java.util.ArrayList;
import java.util.Random;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe représente la carte Hooked Nose, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class HookedNose extends CarteRumeur {

	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Hooked Nose.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		// Avant son tour, prendre une carte random de la main du prochain joueur pour l'ajouter à celle du joueur actuel
		prendreCarteProchainJoueur(joueurActuel);
	}

	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte Hooked Nose.
	 * @param joueurActuel le joueur choisi par la carte
	 * @param carteChoisie la carte choisie par le joueur
	 */
	public void executerCoteWitch(Joueur joueurActuel, CarteRumeur carteChoisie) {
		
		Joueur joueurQuiAccuse = Joueur.getJoueurQuiAccuse();
		
		// Ajouter cette carte à la main du joueur actuel
		joueurActuel.getCartesRumeurJoueur().add(carteChoisie);
			
		// Supprimer cette carte de la main du joueur qui a accusé
		joueurQuiAccuse.getCartesRumeurJoueur().remove(carteChoisie);
		
		// Mettre ce joueur en prochain joueur
		System.out.println(joueurActuel.getNom() + ", vous pouvez rejouer !");
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		this.notifierObservers();
		
	}
	
	// Constructeur 
	/**
	 * Cette méthode est le constructeur de la carte Hooked Nose. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public HookedNose() {
		nom = "Hooked Nose";
		descHunt = "Choisissez le prochain joueur. Avant son tour, prenez une carte de sa main au hasard.";
		descWitch = "Prenez une carte de la main de celui qui vous a accusé. Rejouez.";
	}
	
	/**
	 * Cette méthode permet au joueur ayant joué cette carte de prendre une carte de la main du joueur choisi.
	 * @param joueurActuel le joueur choisi pour récupérer une carte de sa main.
	 */
	private void prendreCarteProchainJoueur (Joueur joueurActuel) {
		
		Joueur prochainJoueur = Partie.getInstance().getTourActuel().getProchainJoueur(); // Prochain joueur
		ArrayList<CarteRumeur> mainProchainJoueur = prochainJoueur.getCartesRumeurJoueur(); // Main du prochain joueur
		ArrayList<CarteRumeur> mainJoueurActuel = joueurActuel.getCartesRumeurJoueur(); // Main du joueur actuel
		
		Random rand = new Random(); // Nombre random
		int numCarte ; // Indice de la carte dans la liste "mainProchainJoueur"
		
		
		if (mainProchainJoueur.size() > 0) {
			// Choisir au hasard une carte parmi la main du prochain joueur
			numCarte = rand.nextInt(mainProchainJoueur.size());
			
			// Ajouter cette carte dans la main du joueur actuel
			mainJoueurActuel.add(mainProchainJoueur.get(numCarte));
			
			// On retire la carte en trop de la main du prochain joueur
			mainProchainJoueur.remove(numCarte);
		}
		else {
			System.out.println("Désolé. Vous ne pouvez pas prendre une carte de la main du prochain joueur car il n'a plus de cartes.");
		}
	}
	
	
	// ~~~ Witch ? 
	// Prendre une carte de la main du joueur qui accuse
	// Être le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Choisir le prochain joueur
	// Avant son tour, lui prendre une carte de sa main aléatoirement et l'ajouter à sa main
}
