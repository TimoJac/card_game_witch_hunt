package model.cartes_rumeur;

import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Ducking Stool, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class DuckingStool extends CarteRumeur {

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Ducking Stool.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

		
		// Mettre DuckingStoolActive � true pour le prochain joueur, pour qu'il doive �ffectuer l'action de Evil Eeye obligatoire
		TourDeJeu.setDuckingStoolActive(true);
		
		// D�finir ce joueur comme �tant le joueur qui accuse potentiellement le prochain joueur (permet de savoir � qui donner les points plus facilement)
		Joueur.setJoueurQuiAccuse(Partie.getInstance().getTourActuel().getJoueurActuel());
	}

	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte Ducking Stool.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {

		
		// Choisir prochain joueur
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		this.notifierObservers();
		
		
	}
	
	//Constructeur
	/**
	 * Cette m�thode est le constructeur de la carte Ducking Stool. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public DuckingStool() {
		nom = "Ducking Stool";
		descHunt = "Choisissez un joueur. Ils doivent r�v�ler leur identit� ou d�fausser une carte\n\t\tC'est une sorci�re : Vous gagnez 2 points. Vous rejouez\n\t\tC'est un villageois : Vous perdez 2 points. Il joue\n\t\tS�il d�fausse : Il joue";
		descWitch = "Choisissez le prochain joueur";
	}
	
	
	//
	// ~~~ Witch ? 
	// Choisir le prochain joueur
	//
	// ~~~ Hunt !
	// Choisir un joueur
	//		� R�v�le son identit� -> Witch : +1 pt, �tre le prochain joueur
	//		� R�v�le son identit� -> Villager : -1 pt, accus� est le prochain joueur
	//		� D�fausse une carte : accus� est le prochain joueur
}
