package model.cartes_rumeur;

import java.util.ArrayList;


import model.*;
import model.joueur.Joueur;
import model.joueur.Role;

/**
 * Cette classe repr�sente la carte Toad, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class Toad extends CarteRumeur {


	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Toad.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

        // Reveler l'identit� du joueur
        this.revelerIdentite();
        
        //Notifier les observers
        this.notifierObservers();
                
    }
	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte Toad.
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
	 * Cette m�thode est le constructeur de la carte Toad. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public Toad() {
		nom = "Toad";
		descHunt = "R�v�lez votre identit�.\n\t\tSi vous �tez une sorci�re, le joueur � votre gauche prend la main.\n\t\tSi vous �tes villageois, choisissez le prochain joueur.";
		descWitch = "Rejouez.";
	}

	
	/**
	 * Cette m�thode permet au joueur de r�v�ler son identit�.
	 */
	public void revelerIdentite() {
        
        Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel(); // Joueur qui joue actuellement
        
        
        // On r�v�le l'identit� du joueur
        joueurActuel.setIdentiteRevelee(true);
        
        // On enl�ve l'accusation port�e sur le joueur
        joueurActuel.setJoueurEstAccuse(false);
        
        // Si c'�tait une soci�re
        if (joueurActuel.getRole() == Role.SORCIERE) {
            
            System.out.println(joueurActuel.getNom() + " est une soci�re !");

            joueurActuel.setPeutJouer(false); // Le joueur r�v�l� ne peut plus jouer
            
            // On d�finit le prochain joueur comme �tant le joueur � gauche du joueur actuel
            setProchainJoueurGauche();
            
        }        
        else { // Si c'�tait un villageois

            System.out.println(joueurActuel.getNom() + " est un villageois.");    
            
            // Le joueur choisit le prochain joueur
            Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
        }
        
    }
	
	/**
	 * Cette m�thode permet de d�finir le prochain Joueur comme �tant le joueur � gauche de celui ayant jou� cette carte
	 */
	private void setProchainJoueurGauche() {
		
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel(); // Joueur qui joue actuellement
		ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(Partie.getInstance().getJoueurs()); // Liste des joueurs
		Joueur JoueurAGauche; // Joueur � gauche du joueur actuel = Avant dans la liste des joueurs
		
		// On cherche la position du joueur actuel dans la liste des joueurs
		int positionJoueurActuel = listeJoueurs.indexOf(joueurActuel);
		
		// Si la position du joueur actuel n'est pas en premier dans la liste
		if (positionJoueurActuel > 0) {
			JoueurAGauche = listeJoueurs.get(positionJoueurActuel-1);
			// Le joueur qui accuse rejoue, on le met donc en prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(JoueurAGauche); 
		}
		else if (positionJoueurActuel == 0) { // Si la position du joueur actuel est en premier dans la liste, le joueur � gauche est le dernier de la liste
			JoueurAGauche = listeJoueurs.get(listeJoueurs.size()-1);
			// Le joueur qui accuse rejoue, on le met donc en prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(JoueurAGauche); 
		}
		
		
		
	}
	
	// ~~~ Witch ? 
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Joueur r�v�le sa propre identit�
	//		� Witch : joueur � gauche est le prochain joueur
	//		� Villager : choisir le prochain joueur
}
