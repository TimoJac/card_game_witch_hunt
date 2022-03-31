package model.cartes_rumeur;
import model.Partie;
import model.*;
import model.joueur.Joueur;

/**
 * Cette classe repr�sente la carte Angry Mob, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class AngryMob extends CarteRumeur {

	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte AngryMob.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteHunt(Joueur joueurActuel) {

        // Demander � un autre joueur de r�v�ler son identit�
        //System.out.println("Cette carte permet de forcer un joueur � r�v�ler son identit�.\n");

        Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);


        // Cas si le joueur est immunis� (il a r�v�l� la carte Broomstick)
        Joueur prochainJoueur = Partie.getInstance().getTourActuel().getProchainJoueur();
        while (prochainJoueur.getCartesReveleeJoueur().contains(Partie.getInstance().getBroomstick())) {
            System.out.println("D�sol�. " + prochainJoueur.getNom() + " a r�v�l� la carte Broomstick. Il est donc immunis� contre cette carte !\n");
            
            Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
            prochainJoueur = Partie.getInstance().getTourActuel().getProchainJoueur();
            this.setChanged();
            this.notifyObservers("ErreurAngryMob");
        }

        // Mettre AngryMobActive � true pour le prochain joueur, pour qu'il doive effectuer l'action de AngryMob obligatoirement
        TourDeJeu.setAngryMobActive(true);
        
        // D�finir ce joueur comme �tant le joueur qui accuse potentiellement le prochain joueur (permet de savoir � qui donner les points plus facilement)
        Joueur.setJoueurQuiAccuse(Partie.getInstance().getTourActuel().getJoueurActuel());
        
        Partie.getInstance().getTourActuel().getProchainJoueur().faireActionAngryMob();
        
        this.setChanged();
        this.notifyObservers("AngryMob");

    }

	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte AngryMob.
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
	 * Cette m�thode est le constructeur de la carte AngryMob. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public AngryMob() {
		nom = "Angry Mob";
		descHunt = "[Jouable seulement si vous avez �t� r�v�l� Villageois] R�v�lez l�identit� d�un autre joueur\n\t\tC'est une sorci�re : Vous gagnez 2 points. Vous rejouez\n\t\tC'est un villageois : Vous perdez 2 points. L�accus� joue";
		descWitch = "Jouez en suivant";
		
	}
		
	// ~~~ Witch ? 
	// �tre le prochain joueur
	//
	//
	// ~~~ Hunt ! SEULEMENT JOUABLE SI LE JOUEUR EST UN VILLAGEOIS REVELE
	// R�v�ler l'identit� d'un autre joueur
	//		� Witch : +2 pts, �tre le prochain joueur
	//		� Villager : -2 pts, accus� est prochain joueur
}	
