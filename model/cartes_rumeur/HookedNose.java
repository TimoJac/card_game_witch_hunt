package model.cartes_rumeur;

import java.util.ArrayList;
import java.util.Random;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Hooked Nose, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class HookedNose extends CarteRumeur {

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Hooked Nose.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		// Avant son tour, prendre une carte random de la main du prochain joueur pour l'ajouter � celle du joueur actuel
		prendreCarteProchainJoueur(joueurActuel);
	}

	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte Hooked Nose.
	 * @param joueurActuel le joueur choisi par la carte
	 * @param carteChoisie la carte choisie par le joueur
	 */
	public void executerCoteWitch(Joueur joueurActuel, CarteRumeur carteChoisie) {
		
		Joueur joueurQuiAccuse = Joueur.getJoueurQuiAccuse();
		
		// Ajouter cette carte � la main du joueur actuel
		joueurActuel.getCartesRumeurJoueur().add(carteChoisie);
			
		// Supprimer cette carte de la main du joueur qui a accus�
		joueurQuiAccuse.getCartesRumeurJoueur().remove(carteChoisie);
		
		// Mettre ce joueur en prochain joueur
		System.out.println(joueurActuel.getNom() + ", vous pouvez rejouer !");
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		this.notifierObservers();
		
	}
	
	// Constructeur 
	/**
	 * Cette m�thode est le constructeur de la carte Hooked Nose. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public HookedNose() {
		nom = "Hooked Nose";
		descHunt = "Choisissez le prochain joueur. Avant son tour, prenez une carte de sa main au hasard.";
		descWitch = "Prenez une carte de la main de celui qui vous a accus�. Rejouez.";
	}
	
	/**
	 * Cette m�thode permet au joueur ayant jou� cette carte de prendre une carte de la main du joueur choisi.
	 * @param joueurActuel le joueur choisi pour r�cup�rer une carte de sa main.
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
			System.out.println("D�sol�. Vous ne pouvez pas prendre une carte de la main du prochain joueur car il n'a plus de cartes.");
		}
	}
	
	
	// ~~~ Witch ? 
	// Prendre une carte de la main du joueur qui accuse
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Choisir le prochain joueur
	// Avant son tour, lui prendre une carte de sa main al�atoirement et l'ajouter � sa main
}
