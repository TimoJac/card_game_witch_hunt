package model.cartes_rumeur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Pet Newt, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class PetNewt extends CarteRumeur {

	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Pet Newt.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		// R�cup�rer une carte Rumeur r�v�l�e de n'importe quel joueur et la mettre dans sa main
		System.out.println("Choisissez parmi les cartes rumeur revel�es de tous les autres joueurs, et ajoutez la carte choisie � votre main !");
		ajouterCarteMain(joueurActuel);
		
		// Choisir le prochain joueur
		//joueurActuel.choisirProchainJoueur("Choisissez le prochain joueur (Entrez le num�ro correspondant) :");
		
		// Mettre la carte PetNewt dans les cartes r�v�l�es du joueur
		revelerCarteRumeur();
	}

	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte PetNewt.
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
	 * Cette m�thode est le constructeur de la carte Pet Newt. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public PetNewt() {
		nom = "Pet Newt";
		descHunt = "Prenez une carte r�v�l�e de n'importe quel autre joueur pour l'ajouter � votre main. Rejouez.";
		descWitch = "Rejouez.";
	}
	
	// Ajouter une des cartes r�v�l�es d'un autre joueur � sa main
	/**
	 * Cette m�thode permet au joueur ayant jou� cette carte de r�cup�rer une des cartes r�v�l�es d'un joueur choisi.
	 * @param joueurActuel le joueur choisi
	 */
	private void ajouterCarteMain (Joueur joueurActuel) {
	
		ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(Partie.getInstance().getJoueurs()); // Liste des joueurs
		listeJoueurs.remove(joueurActuel); // On enl�ve le joueur actuel de la liste des joueurs
		HashMap<Integer,CarteRumeur> listeCartesTotale = new HashMap<Integer,CarteRumeur>();
		
		Integer compteurChoixCarte = 0;
		Integer numeroChoixCarte = -1 ;
		CarteRumeur choixCarte;

		if (!joueurActuel.getNom().startsWith("[BOT]")) { //Si ce n'est pas un BOT : On demande le choix de l'utilisateur
			for (int i = 0; i < listeJoueurs.size(); i++) {
				ArrayList<CarteRumeur> cartesReveleesJoueur = listeJoueurs.get(i).getCartesReveleeJoueur();
			
				if (cartesReveleesJoueur.size() > 0 ) {
					System.out.println("\nCartes r�v�l�es de " + listeJoueurs.get(i).getNom()+ " :");
				}
						
				for (int j = 0; j< cartesReveleesJoueur.size(); j++) {
					System.out.println("\t- [" + compteurChoixCarte + "] "+ cartesReveleesJoueur.get(j).getNom());
					listeCartesTotale.put(compteurChoixCarte, cartesReveleesJoueur.get(j));
					compteurChoixCarte ++;
				}
			}
		
			if (listeCartesTotale.size() > 0) { // S'il y a au moins une carte r�v�l�e chez les autres joueurs

				System.out.println("\nEntrez le num�ro d'une des cartes ci-dessus pour l'ajouter � votre main");
				try {
					numeroChoixCarte = Methods.entrerEntier(0, listeCartesTotale.size(), "");
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			} 
			
		}
		else {
			
			if (listeCartesTotale.size() > 0) {
				numeroChoixCarte = new Random().nextInt(listeCartesTotale.size());
			}
			
		}
		
		if (listeCartesTotale.size() > 0 ) {
			
			// Ajouter la carte � la main du joueur actuel
			choixCarte = listeCartesTotale.get(numeroChoixCarte);
			Partie.getInstance().getTourActuel().getJoueurActuel().ajouterCarteRumeurJoueur(choixCarte);
			
			// Enlever la carte des cartes r�v�l�es de l'autre joueur
			
			// Pour cela, on doit d�j� trouver � quel joueur appartient la carte	
			// Il s'appelera "joueurCarteEnMoins"
			Joueur joueurCarteEnMoins = null;
			int compteurCartes = listeJoueurs.get(0).getCartesReveleeJoueur().size();
			int compteurJoueurs = 0;
		
			while (compteurCartes < numeroChoixCarte+1) {
				compteurJoueurs ++;
				compteurCartes += listeJoueurs.get(compteurJoueurs).getCartesReveleeJoueur().size();
			}
			
			joueurCarteEnMoins =  listeJoueurs.get(compteurJoueurs);
			
			//Ensuite, on enl�ve la carte de la liste des cartes r�v�l�es du joueur "joueurCarteEnMoins"
		
			joueurCarteEnMoins.getCartesReveleeJoueur().remove(choixCarte);
		
		}
		
	}
	
	
	// ~~~ Witch ? 
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt !
	
	// Choisir le prochain joueur
}
