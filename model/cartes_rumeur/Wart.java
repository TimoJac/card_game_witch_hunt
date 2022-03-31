package model.cartes_rumeur;


import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Wart, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class Wart extends CarteRumeur {

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Wart.
	 * @param prochainJoueur le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur prochainJoueur) {

		// Mettre ce joueur en prochain joueur et ne plus l'accuser
        Partie.getInstance().getTourActuel().setProchainJoueur(prochainJoueur);
        
        //Notifier les observers
        this.notifierObservers();
	}

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte Wart.
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
	 * Cette m�thode est le constructeur de la carte Wart. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public Wart() {
		nom = "Wart";
		conditionUtilisation = "[Si cette carte est r�v�l�e, vous ne pouvez pas �tre choisi par La chaise du ch�timent]";
		descHunt = "Choisissez le prochain joueur";
		descWitch = "Rejouez";
	}
	
	//
	// SI REVELEE, JOUEUR NE PEUT PAS �TRE DESIGNE PAR LA CARTE DUCKING STOOL
	//
	//
	// ~~~ Witch ? 
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt !
	// Choisir le prochain joueur
}
