package model.cartes_rumeur;


import model.*;
import model.joueur.Joueur;

/**
 * Cette classe représente la carte Wart, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class Wart extends CarteRumeur {

	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Wart.
	 * @param prochainJoueur le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur prochainJoueur) {

		// Mettre ce joueur en prochain joueur et ne plus l'accuser
        Partie.getInstance().getTourActuel().setProchainJoueur(prochainJoueur);
        
        //Notifier les observers
        this.notifierObservers();
	}

	
	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte Wart.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {
		
		// Mettre ce joueur en prochain joueur et ne plus l'accuser
        Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
        
        //Notifier les observers
        this.notifierObservers();
        
	}
	
	// Constructeur
	/**
	 * Cette méthode est le constructeur de la carte Wart. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public Wart() {
		nom = "Wart";
		conditionUtilisation = "[Si cette carte est révélée, vous ne pouvez pas être choisi par La chaise du châtiment]";
		descHunt = "Choisissez le prochain joueur";
		descWitch = "Rejouez";
	}
	
	//
	// SI REVELEE, JOUEUR NE PEUT PAS ÊTRE DESIGNE PAR LA CARTE DUCKING STOOL
	//
	//
	// ~~~ Witch ? 
	// Être le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Choisir le prochain joueur
}
