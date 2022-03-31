package model.cartes_rumeur;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe représente la carte Ducking Stool, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class DuckingStool extends CarteRumeur {

	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Ducking Stool.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		
		// Mettre DuckingStoolActive à true pour le prochain joueur, pour qu'il doive éffectuer l'action de Evil Eeye obligatoire
		TourDeJeu.setDuckingStoolActive(true);
		
		// Définir ce joueur comme étant le joueur qui accuse potentiellement le prochain joueur (permet de savoir à qui donner les points plus facilement)
		Joueur.setJoueurQuiAccuse(Partie.getInstance().getTourActuel().getJoueurActuel());
	}

	
	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte Ducking Stool.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {

		
		// Choisir prochain joueur
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		this.notifierObservers();
		
		
	}
	
	//Constructeur
	/**
	 * Cette méthode est le constructeur de la carte Ducking Stool. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public DuckingStool() {
		nom = "Ducking Stool";
		descHunt = "Choisissez un joueur. Ils doivent révéler leur identité ou défausser une carte\n\t\tC'est une sorcière : Vous gagnez 2 points. Vous rejouez\n\t\tC'est un villageois : Vous perdez 2 points. Il joue\n\t\tS’il défausse : Il joue";
		descWitch = "Choisissez le prochain joueur";
	}
	
	
	//
	// ~~~ Witch ? 
	// Choisir le prochain joueur
	//
	// ~~~ Hunt !
	// Choisir un joueur
	//		• Révèle son identité -> Witch : +1 pt, être le prochain joueur
	//		• Révèle son identité -> Villager : -1 pt, accusé est le prochain joueur
	//		• Défausse une carte : accusé est le prochain joueur
}
