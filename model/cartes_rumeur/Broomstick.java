package model.cartes_rumeur;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe représente la carte Broomstick, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class Broomstick extends CarteRumeur {

	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Broomstick.
	 * @param prochainJoueur le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur prochainJoueur) {

		// Mettre ce joueur en prochain joueur et ne plus l'accuser
        Partie.getInstance().getTourActuel().setProchainJoueur(prochainJoueur);
        
        //Notifier les observers
        this.notifierObservers();
	}

	
	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte Broomstick.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {

		// Mettre ce joueur en prochain joueur et ne plus l'accuser
        Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
        
        //Notifier les observers
        this.notifierObservers();
        
	}
	//Constructeur
	/**
	 * Cette méthode est le constructeur de la carte Broomstick. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public Broomstick() {
		nom = "Broomstick";
		conditionUtilisation = "[Si cette carte est révélée, vous ne pouvez pas être choisi par la carte Angry Mob]";
		descHunt = "Choisissez le prochain joueur";
		descWitch = "Jouez en suivant";
	}
	
	
	// SI REVELEE, JOUEUR NE PEUT PAS ÊTRE DESIGNE PAR LA CARTE ANGRY MOB
	//
	//
	// ~~~ Witch ? 
	// Être le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Choisir le joueur suivant
}
