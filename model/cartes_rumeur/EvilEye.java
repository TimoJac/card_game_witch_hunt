package model.cartes_rumeur;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe représente la carte Evil Eye, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class EvilEye extends CarteRumeur  {

	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Evil Eye.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		// Mettre EvilEyEActive à true pour le prochain joueur, pour qu'il doive éffectuer l'action de Evil Eeye obligatoire
		TourDeJeu.setEvilEyeActive(true);

	}

	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte Evil Eye.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {

		// Choisir prochain joueur
		//joueurActuel.choisirProchainJoueur("Choisissez le prochain joueur (Entrez le numéro correspondant) :\nLors de son tour, il devra accuser un joueur autre que vous, si possible.");
		
		// Mettre EvilEyEActive à true pour le prochain joueur, pour qu'il doive éffectuer l'action de Evil Eeye obligatoire
		TourDeJeu.setEvilEyeActive(true);
	}
	
	// Constructeur 
	/**
	 * Cette méthode est le constructeur de la carte Evil Eye. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public EvilEye() {
		nom = "Evil Eye";
		descHunt = "Choissez le prochain joueur. Lors le son tour, il doit accuser un joueur autre que vous si possible.";
		descWitch = "Choissez le prochain joueur. Lors le son tour, il doit accuser un joueur autre que vous si possible.";
	}
	
	// ~~~ Witch ? 
	// Choisir le prochain joueur
	// Pendant son tour, doit accuser quelqu'un d'autre que joueur
	//
	//
	// ~~~ Hunt !
	// Choisir le prochain joueur
	// Pendant son tour, doit accuser quelqu'un d'autre que joueur
}
