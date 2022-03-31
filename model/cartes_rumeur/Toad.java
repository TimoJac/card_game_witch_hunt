package model.cartes_rumeur;

import java.util.ArrayList;


import model.*;
import model.joueur.Joueur;
import model.joueur.Role;

/**
 * Cette classe représente la carte Toad, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class Toad extends CarteRumeur {


	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Toad.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

        // Reveler l'identité du joueur
        this.revelerIdentite();
        
        //Notifier les observers
        this.notifierObservers();
                
    }
	
	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte Toad.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {

		System.out.println("Cette carte vous permet de rejouer ! Alors rejouez :) ");

		// Mettre ce joueur en prochain joueur
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		//Notifier les observers
		this.notifierObservers();
		
		
	}
	
	// Constructeur
	/**
	 * Cette méthode est le constructeur de la carte Toad. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public Toad() {
		nom = "Toad";
		descHunt = "Révélez votre identité.\n\t\tSi vous êtez une sorcière, le joueur à votre gauche prend la main.\n\t\tSi vous êtes villageois, choisissez le prochain joueur.";
		descWitch = "Rejouez.";
	}

	
	/**
	 * Cette méthode permet au joueur de révéler son identité.
	 */
	public void revelerIdentite() {
        
        Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel(); // Joueur qui joue actuellement
        
        
        // On révèle l'identité du joueur
        joueurActuel.setIdentiteRevelee(true);
        
        // On enlève l'accusation portée sur le joueur
        joueurActuel.setJoueurEstAccuse(false);
        
        // Si c'était une socière
        if (joueurActuel.getRole() == Role.SORCIERE) {
            
            System.out.println(joueurActuel.getNom() + " est une socière !");

            joueurActuel.setPeutJouer(false); // Le joueur révélé ne peut plus jouer
            
            // On définit le prochain joueur comme étant le joueur à gauche du joueur actuel
            setProchainJoueurGauche();
            
        }        
        else { // Si c'était un villageois

            System.out.println(joueurActuel.getNom() + " est un villageois.");    
            
            // Le joueur choisit le prochain joueur
            Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
        }
        
    }
	
	/**
	 * Cette méthode permet de définir le prochain Joueur comme étant le joueur à gauche de celui ayant joué cette carte
	 */
	private void setProchainJoueurGauche() {
		
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel(); // Joueur qui joue actuellement
		ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(Partie.getInstance().getJoueurs()); // Liste des joueurs
		Joueur JoueurAGauche; // Joueur à gauche du joueur actuel = Avant dans la liste des joueurs
		
		// On cherche la position du joueur actuel dans la liste des joueurs
		int positionJoueurActuel = listeJoueurs.indexOf(joueurActuel);
		
		// Si la position du joueur actuel n'est pas en premier dans la liste
		if (positionJoueurActuel > 0) {
			JoueurAGauche = listeJoueurs.get(positionJoueurActuel-1);
			// Le joueur qui accuse rejoue, on le met donc en prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(JoueurAGauche); 
		}
		else if (positionJoueurActuel == 0) { // Si la position du joueur actuel est en premier dans la liste, le joueur à gauche est le dernier de la liste
			JoueurAGauche = listeJoueurs.get(listeJoueurs.size()-1);
			// Le joueur qui accuse rejoue, on le met donc en prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(JoueurAGauche); 
		}
		
		
		
	}
	
	// ~~~ Witch ? 
	// Être le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Joueur révèle sa propre identité
	//		• Witch : joueur à gauche est le prochain joueur
	//		• Villager : choisir le prochain joueur
}
