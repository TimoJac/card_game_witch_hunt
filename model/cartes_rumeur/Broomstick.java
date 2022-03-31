package model.cartes_rumeur;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Broomstick, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class Broomstick extends CarteRumeur {

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Broomstick.
	 * @param prochainJoueur le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur prochainJoueur) {

		// Mettre ce joueur en prochain joueur et ne plus l'accuser
        Partie.getInstance().getTourActuel().setProchainJoueur(prochainJoueur);
        
        //Notifier les observers
        this.notifierObservers();
	}

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte Broomstick.
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
	 * Cette m�thode est le constructeur de la carte Broomstick. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public Broomstick() {
		nom = "Broomstick";
		conditionUtilisation = "[Si cette carte est r�v�l�e, vous ne pouvez pas �tre choisi par la carte Angry Mob]";
		descHunt = "Choisissez le prochain joueur";
		descWitch = "Jouez en suivant";
	}
	
	
	// SI REVELEE, JOUEUR NE PEUT PAS �TRE DESIGNE PAR LA CARTE ANGRY MOB
	//
	//
	// ~~~ Witch ? 
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Choisir le joueur suivant
}
