package model.cartes_rumeur;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe représente la carte Black Cat, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class BlackCat extends CarteRumeur {

	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Black Cat.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		ArrayList<CarteRumeur> defausse = Partie.getInstance().getTourActuel().getDefausse();
		
		// Ajout d'une carte de la défausse, et défausse de la carte BlackCat
		
		if (defausse.size() > 0) { // Cas classique : Il y a des cartes dans la défausse
			
			System.out.println("Choisissez une carte de la défausse pour l'ajouter à votre main.");
			ajouterCarteDefausse(defausse, joueurActuel);	// Ajouter une carte de la défausse à la main du joueur
			defausserBlackCat(joueurActuel); // On met BlackCat dans la défausse
		}
		
		else { 	// Cas où il n'y a pas de carte dans la défausse 
			
			// le joueur ne choisit pas de carte à ajouter à sa main, mais il défausse "Black Cat" quand même
			System.out.println("Il n'y a pas de carte dans la défausse, vous défaussez seulement votre carte \"Black Cat\".");
			defausserBlackCat(joueurActuel);
		}
		
	}

	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte Black Cat.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {

		System.out.println("Cette carte vous permet de rejouer ! Alors rejouez :) ");

		// Mettre ce joueur en prochain joueur
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		this.notifierObservers();

	}
	
	// Constructeur 
	/**
	 * Cette méthode est le constructeur de la carte Black Cat. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public BlackCat() {
		nom = "Black Cat";
		descHunt = "Ajoutez une carte de la défausse à votre main, puis jetez BlackCat. Rejouez.";
		descWitch = "Rejouez";
	}
	
	// Ajout d'une carte de la défausse
	/**
	 * Cette méthode permet d'ajouter une carte dans la défausse.
	 * @param defausse la défausse où envoyer la carte
	 * @param joueurActuel le joueur qui a choisi de défausser sa carte
	 */
	private void ajouterCarteDefausse(ArrayList<CarteRumeur> defausse, Joueur joueurActuel) {
		
		int numeroChoixCarte = -1;
		CarteRumeur choixCarte;


		if (!joueurActuel.getNom().startsWith("[BOT]")) { //Si ce n'est pas un BOT : On demande le choix de l'utilisateur
			// On liste les cartes de la défausse
			for (int i = 0; i < defausse.size(); i++) {
				System.out.println("\t- [" + (i+1) + "] " + defausse.get(i).getNom());
			}

			// Choix de la carte par l'utilisateur
			System.out.println("Entrez le numéro correspondant à la carte pour l'ajouter à votre main");
			try {
				numeroChoixCarte = Methods.entrerEntier(1, defausse.size(), "");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} 
		else {
			numeroChoixCarte = new Random().nextInt(defausse.size());
			System.out.println(joueurActuel + " a choisi de défausser " + defausse.get(numeroChoixCarte).getNom());
		}
		
		if (numeroChoixCarte != -1) {
			choixCarte = defausse.get(numeroChoixCarte);
			
			// Retirer la carte de la défausse
			Partie.getInstance().getTourActuel().retirerCarteDefausse(choixCarte);
			
			// Ajouter la carte à la main du joueur
			joueurActuel.ajouterCarteRumeurJoueur(choixCarte);
		}
		

	}
	
	/**
	 * Cette méthode permet de défausser la carte Black Cat
	 * @param joueurActuel le joueur qui va défausser la carte
	 */
	private void defausserBlackCat(Joueur joueurActuel) {
				
		//Retirer la carte "Black Cat" de la main du joueur
		joueurActuel.retirerCarteRumeurJoueur(this);
		
		// Ajouter la carte "Black Cat" à la défausse
		Partie.getInstance().getTourActuel().ajouterCarteDefausse(this);

	}
	
	// ~~~ Witch ? 
	// Être le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Ajouter une carte de la défausse dans sa main, et défausser BlackCat
	// Être le prochain joueur
}
