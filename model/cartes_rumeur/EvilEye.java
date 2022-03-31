package model.cartes_rumeur;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Evil Eye, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class EvilEye extends CarteRumeur  {

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Evil Eye.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		// Mettre EvilEyEActive � true pour le prochain joueur, pour qu'il doive �ffectuer l'action de Evil Eeye obligatoire
		TourDeJeu.setEvilEyeActive(true);

	}

	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte Evil Eye.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {

		// Choisir prochain joueur
		//joueurActuel.choisirProchainJoueur("Choisissez le prochain joueur (Entrez le num�ro correspondant) :\nLors de son tour, il devra accuser un joueur autre que vous, si possible.");
		
		// Mettre EvilEyEActive � true pour le prochain joueur, pour qu'il doive �ffectuer l'action de Evil Eeye obligatoire
		TourDeJeu.setEvilEyeActive(true);
	}
	
	// Constructeur 
	/**
	 * Cette m�thode est le constructeur de la carte Evil Eye. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
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
