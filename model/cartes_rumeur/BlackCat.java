package model.cartes_rumeur;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Black Cat, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class BlackCat extends CarteRumeur {

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Black Cat.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		ArrayList<CarteRumeur> defausse = Partie.getInstance().getTourActuel().getDefausse();
		
		// Ajout d'une carte de la d�fausse, et d�fausse de la carte BlackCat
		
		if (defausse.size() > 0) { // Cas classique : Il y a des cartes dans la d�fausse
			
			System.out.println("Choisissez une carte de la d�fausse pour l'ajouter � votre main.");
			ajouterCarteDefausse(defausse, joueurActuel);	// Ajouter une carte de la d�fausse � la main du joueur
			defausserBlackCat(joueurActuel); // On met BlackCat dans la d�fausse
		}
		
		else { 	// Cas o� il n'y a pas de carte dans la d�fausse 
			
			// le joueur ne choisit pas de carte � ajouter � sa main, mais il d�fausse "Black Cat" quand m�me
			System.out.println("Il n'y a pas de carte dans la d�fausse, vous d�faussez seulement votre carte \"Black Cat\".");
			defausserBlackCat(joueurActuel);
		}
		
	}

	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte Black Cat.
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
	 * Cette m�thode est le constructeur de la carte Black Cat. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public BlackCat() {
		nom = "Black Cat";
		descHunt = "Ajoutez une carte de la d�fausse � votre main, puis jetez BlackCat. Rejouez.";
		descWitch = "Rejouez";
	}
	
	// Ajout d'une carte de la d�fausse
	/**
	 * Cette m�thode permet d'ajouter une carte dans la d�fausse.
	 * @param defausse la d�fausse o� envoyer la carte
	 * @param joueurActuel le joueur qui a choisi de d�fausser sa carte
	 */
	private void ajouterCarteDefausse(ArrayList<CarteRumeur> defausse, Joueur joueurActuel) {
		
		int numeroChoixCarte = -1;
		CarteRumeur choixCarte;


		if (!joueurActuel.getNom().startsWith("[BOT]")) { //Si ce n'est pas un BOT : On demande le choix de l'utilisateur
			// On liste les cartes de la d�fausse
			for (int i = 0; i < defausse.size(); i++) {
				System.out.println("\t- [" + (i+1) + "] " + defausse.get(i).getNom());
			}

			// Choix de la carte par l'utilisateur
			System.out.println("Entrez le num�ro correspondant � la carte pour l'ajouter � votre main");
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
			System.out.println(joueurActuel + " a choisi de d�fausser " + defausse.get(numeroChoixCarte).getNom());
		}
		
		if (numeroChoixCarte != -1) {
			choixCarte = defausse.get(numeroChoixCarte);
			
			// Retirer la carte de la d�fausse
			Partie.getInstance().getTourActuel().retirerCarteDefausse(choixCarte);
			
			// Ajouter la carte � la main du joueur
			joueurActuel.ajouterCarteRumeurJoueur(choixCarte);
		}
		

	}
	
	/**
	 * Cette m�thode permet de d�fausser la carte Black Cat
	 * @param joueurActuel le joueur qui va d�fausser la carte
	 */
	private void defausserBlackCat(Joueur joueurActuel) {
				
		//Retirer la carte "Black Cat" de la main du joueur
		joueurActuel.retirerCarteRumeurJoueur(this);
		
		// Ajouter la carte "Black Cat" � la d�fausse
		Partie.getInstance().getTourActuel().ajouterCarteDefausse(this);

	}
	
	// ~~~ Witch ? 
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Ajouter une carte de la d�fausse dans sa main, et d�fausser BlackCat
	// �tre le prochain joueur
}
