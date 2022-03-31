package model.cartes_rumeur;
import model.Partie;
import model.*;
import model.joueur.Joueur;

/**
 * Cette classe représente la carte Angry Mob, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class AngryMob extends CarteRumeur {

	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte AngryMob.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

        // Demander à un autre joueur de révéler son identité
        //System.out.println("Cette carte permet de forcer un joueur à révéler son identité.\n");

        Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);


        // Cas si le joueur est immunisé (il a révélé la carte Broomstick)
        Joueur prochainJoueur = Partie.getInstance().getTourActuel().getProchainJoueur();
        while (prochainJoueur.getCartesReveleeJoueur().contains(Partie.getInstance().getBroomstick())) {
            System.out.println("Désolé. " + prochainJoueur.getNom() + " a révélé la carte Broomstick. Il est donc immunisé contre cette carte !\n");
            
            Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
            prochainJoueur = Partie.getInstance().getTourActuel().getProchainJoueur();
            this.setChanged();
            this.notifyObservers("ErreurAngryMob");
        }

        // Mettre AngryMobActive à true pour le prochain joueur, pour qu'il doive effectuer l'action de AngryMob obligatoirement
        TourDeJeu.setAngryMobActive(true);
        
        // Définir ce joueur comme étant le joueur qui accuse potentiellement le prochain joueur (permet de savoir à qui donner les points plus facilement)
        Joueur.setJoueurQuiAccuse(Partie.getInstance().getTourActuel().getJoueurActuel());
        
        Partie.getInstance().getTourActuel().getProchainJoueur().faireActionAngryMob();
        
        this.setChanged();
        this.notifyObservers("AngryMob");

    }

	/**
	 * Cette méthode permet d'exécuter le côté Witch de la carte AngryMob.
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
	 * Cette méthode est le constructeur de la carte AngryMob. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public AngryMob() {
		nom = "Angry Mob";
		descHunt = "[Jouable seulement si vous avez été révélé Villageois] Révélez l’identité d’un autre joueur\n\t\tC'est une sorcière : Vous gagnez 2 points. Vous rejouez\n\t\tC'est un villageois : Vous perdez 2 points. L’accusé joue";
		descWitch = "Jouez en suivant";
		
	}
		
	// ~~~ Witch ? 
	// Être le prochain joueur
	//
	//
	// ~~~ Hunt ! SEULEMENT JOUABLE SI LE JOUEUR EST UN VILLAGEOIS REVELE
	// Révéler l'identité d'un autre joueur
	//		• Witch : +2 pts, être le prochain joueur
	//		• Villager : -2 pts, accusé est prochain joueur
}	
