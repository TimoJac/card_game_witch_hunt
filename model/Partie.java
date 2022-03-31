package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

import model.joueur.Joueur;
import model.joueur.JoueurReel;
import model.joueur.JoueurVirtuel;
import view.vueGraphique.VueGraphiqueFin;
import view.vueGraphique.VueGraphiqueInitJoueurReel;
import view.vueGraphique.VueGraphiqueInitJoueurVirtuel;
import view.vueGraphique.VueGraphiquePartie;
import model.cartes_rumeur.*;

/**
 * La classe Partie permet de g�rer l'ensemble d'une partie de jeu WitchHunt. 
 * Elle cr�e toutes les cartes du jeu, elle permet de lancer l'initialisation des joueurs, et de g�rer la cr�ation des tours de jeu
 * Elle v�rifie �galement si c'est la fin de la partie � la fin de chaque tour de jeu, et g�re la fin de la partie.
 * 
 * C'est un singleton, donc voici un exemple de code pour cr�er une nouvelle partie ou acc�der � l'instance en cours :
 * <pre> Partie.getInstance(); </pre>
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observable
 * @see model.joueur.Joueur
 * @see model.TourDeJeu
 * @see model.cartes_rumeur.CarteRumeur
 * 
 *
 */
public class Partie extends Observable {
	
	/**
	 * Instance de la Partie. 
	 * Il n'y a qu'une seule instance possible car Partie est un singleton.
	 * @see getInstance()
	 */
	private static Partie partie;
	
	/**
	 * Booleen qui permet de savoir si la partie est lanc�e ou non
	 * Est utile surtout pour les vues du Menu
	 * 
	 * @see view.vueGraphique.vueGraphiqueMenu
	 * @see view.vueTexte.vueTexteMenu
	 * @see setPartieLancee(boolean)
	 */
	private boolean partieLancee;
	
	/**
	 * Liste des joueurs de la partie
	 * 
	 * @see java.util.ArrayList
	 * @see model.joueur.Joueur
	 */
	private ArrayList<Joueur> joueurs;
	
	/**
	 * Liste de toutes les cartes rumeurs du jeu
	 * @see java.util.ArrayList
	 * @see model.cartes_rumeurs.CarteRumeur
	 */
	private ArrayList<CarteRumeur> cartesRumeur;
	
	/**
	 * Liste de 6 noms possibles � donner � des joueurs virtuels
	 * @see java.util.ArrayList
	 */
	private ArrayList<String> nomsJoueursVirtuels;
	
	/**
	 * Nombre de joueurs r�els dans la partie
	 */
	private int nbJoueursReels;
	
	/**
	 * Nombre de joueurs r�els dans la partie
	 */
	private int nbJoueursVirtuels;
	
	/**
	 * R�f�rence sur le tour de jeu actuellement jou�
	 * @see model.TourDeJeu
	 * @see gererPartie()
	 */
	private TourDeJeu tourActuel;

	
	/**
	 * Reference sur la carte AngryMob
	 * @see model.cartes_rumeur.AngryMob
	 */
	private AngryMob angryMob;
	
	/**
	 * Reference sur la carte BlackCat
	 * @see model.cartes_rumeur.BlackCat
	 */
	private BlackCat blackCat;
	
	/**
	 * Reference sur la carte Broomstick
	 * @see model.cartes_rumeur.Broomstick
	 */
	private Broomstick broomstick;
	
	/**
	 * Reference sur la carte Cauldron
	 * @see model.cartes_rumeur.Cauldron
	 */
	private Cauldron cauldron;
	
	/**
	 * Reference sur la carte DuckingStool
	 * @see model.cartes_rumeur.DuckingStool
	 */
	private DuckingStool duckingStool;
	
	/**
	 * Reference sur la carte EvilEye
	 * @see model.cartes_rumeur.EvilEye
	 */
	private EvilEye evilEye;
	
	/**
	 * Reference sur la carte HookedNose
	 * @see model.cartes_rumeur.HookedNose
	 */
	private HookedNose hookedNose;
	
	/**
	 * Reference sur la carte PetNewt
	 * @see model.cartes_rumeur.PetNewt
	 */
	private PetNewt petNewt;
	
	/**
	 * Reference sur la carte PointedHat
	 * @see model.cartes_rumeur.PointedHat
	 */
	private PointedHat pointedHat;
	
	/**
	 * Reference sur la carte TheInquisition
	 * @see model.cartes_rumeur.TheInquisition
	 */
	private TheInquisition theInquisition;
	
	/**
	 * Reference sur la carte Toad
	 * @see model.cartes_rumeur.Toad
	 */
	private Toad toad;
	
	/**
	 * Reference sur la carte Wart
	 * @see model.cartes_rumeur.Wart
	 */
	private Wart wart;
	
	/**
	 * Reference sur la vue graphique g�rant le tour de jeu actuel
	 * @see view.vueGraphique.VueGraphiquePartie
	 */
	private VueGraphiquePartie vuePartie;


	/**
	 *  Constructeur priv� de partie
	 *  Initialise la liste des joueurs, la liste des noms des joueurs virtuels et cr�e toutes les cartes rumeurs.
	 *  Cette m�thode est priv�e car la classe partie est un singleton. Le contructeur est accessible � l'exterieur par la m�thode getInstance()
	 *  
	 *  @see getInstance()
	 *  
	 */
	private Partie() {
		
		partieLancee = false;
		joueurs = new ArrayList<>();
		nomsJoueursVirtuels = new ArrayList<>(Arrays.asList("[BOT] Larsty", "[BOT] Rochijr", "[BOT] Mme_Sparta", "[BOT] Deca", "[BOT] Doulo", "[BOT] Areru", "[BOT] Ibsels", "[BOT] Aegis", "[BOT] Shiorin", "[BOT] Lossix"));

		// Cr�er chaque instance des 12 classes Cartes Rumeurs
		this.creerCartesRumeur();
	}
	
	
	/**
	 * Cette m�thode permet de g�rer l'ensemble de la partie.
	 * 
	 * <ul>
	 *  <li>Si aucun tour n'a �t� cr�� ou que le tour pr�c�dent est fini, elle cr�e un nouveau tour de jeu et le lance</li>
	 *  <li>Si le tour de jeu actuel est fini et qu'un vainqueur a �t� trouv�, elle lance la vue correspondant � la fin de la partie</li>
	 *  <li>Si le tour actuel n'est pas fini, on continue � faire le tour de jeu</li>
	 *  </ul>
	 *  
	 *  @see model.TourDeJeu
	 *  @see view.vueGraphique.VueGraphiqueFin
	 *  
	 */
	public void gererPartie () {
		
		// Si aucun tour n'a �t� cr�� (d�but de la partie) ou que le tour pr�c�dent est fini (sans gagnant), on en cr�e un nouveau
		if ((this.tourActuel == null) || (this.tourActuel.verifierFinTour() && verifierVainqueur() == null)) {
			this.tourActuel = new TourDeJeu();
		}
		
		// Si un vainqueur a �t� trouv� (v�rification seulement � la fin d'un tour de jeu)
		// On fait la fin du tour
		else if ((this.tourActuel.verifierFinTour()) && (verifierVainqueur() != null)) {
			Joueur vainqueur = verifierVainqueur();
			new VueGraphiqueFin(vainqueur);
		}
		
		// Si on est pas � la fin d'un tour (ou que l'on vient d'en cr�er un)
		if (! this.tourActuel.verifierFinTour()){
			//Appel de la m�thode pour g�rer le tour de jeu
			this.tourActuel.faireTourDeJeu();
		}
	}

	/**
	 * Cette m�thode fait partie du patron de conception Singleton de Partie.
	 * Elle cr�e une instance de Partie si celle-ci est inexistante, et retourne l'instance de Partie dans tous les cas.
	 * @return L'instance de Partie
	 * 
	 * @see Partie()
	 */
	public static Partie getInstance() {
		// Patron de conception Singleton
		if (partie == null) {
			partie = new Partie();
		}
		return partie;
	}
	
	/**
	 * M�thode permettant d'initialiser un joueur virtuel ou r�el non initialis�.
	 * 
	 * A chaque fois que l'on entre dans cette m�thode, elle va initialiser le premier joueur non initialis� qu'elle trouve.
	 * 
	 * <ul>
	 * <li>Pour un joueur r�el, on appelle la vue graphique permettant � l'utilisateur de l'initialiser.</li>
	 * <li>Pour un joueur virtuel, on initialise d�j� le nom du joueur, puis on appelle la vue graphique permettant � l'utilisateur d'initialiser son role.</li>
	 * </ul>
	 * 
	 * @see view.vueGraphique
	 * @see model.joueur.JoueurVirtuel
	 * @see joueurs
	 */
	public void initialiserJoueurs() {
		
		Joueur joueurTrouve;
		
		// Si aucun joueur n'a �t� cr��, on les cr�e tous
		if (joueurs.size() == 0) {
			// Cr�ation des joueurs Reels
			for (int i = 0; i < nbJoueursReels; i++) {
				joueurs.add(new JoueurReel(null));
			}
			// Cr�ation des joueursVirtuels
			for (int i = 0; i < nbJoueursVirtuels; i++) {
				joueurs.add(new JoueurVirtuel(null));
			}
		}
		
		// Initialisation de la liste de joueurs
		boolean trouve = false;
		int i = 0;

		// On cherche dans la liste le premier joueur possible n'ayant pas �t� initialis� (pas de nom)
		while (trouve == false &&  i < (nbJoueursReels + nbJoueursVirtuels)) {
			if (joueurs.get(i).getNom() == null) 
				trouve = true;
			else
				i++;
		}
		
		// Si un joueur non initialis� a �t� trouv�
		if (trouve == true) {
		
			joueurTrouve = joueurs.get(i);

			// Si le joueur est un joueur virtuel
			if (joueurTrouve instanceof JoueurVirtuel) {
				
				//On doit d'abord lui donner al�atoirement un nom
				((JoueurVirtuel) (joueurTrouve)).choisirNom();
				
				//Puis on cr�e l'interface graphique pour initialiser le joueur virtuel
				new VueGraphiqueInitJoueurVirtuel((JoueurVirtuel)joueurTrouve);
				
			}
			else if (joueurTrouve instanceof JoueurReel) {
				// On cr�e l'interface graphique pour initialiser le joueur r�el
				new VueGraphiqueInitJoueurReel((JoueurReel)joueurTrouve);
			}
		}
		// Si tous les joueurs ont d�j� �t� initialis�s, on commence la partie
		else {
			Partie.getInstance().gererPartie();
		}
	}
	
	/**
	 * M�thode permettant d'instancier la liste des cartes rumeurs, de cr�er chaque carte rumeur et de l'ajouter � la liste.
	 * @see cartesRumeur
	 */
	public void creerCartesRumeur () {
		
		if (cartesRumeur == null) {
			
			cartesRumeur = new ArrayList<CarteRumeur>();
			cartesRumeur.add(angryMob = new AngryMob());
			cartesRumeur.add(blackCat = new BlackCat());
			cartesRumeur.add(broomstick = new Broomstick());
			cartesRumeur.add(cauldron = new Cauldron());
			cartesRumeur.add(duckingStool = new DuckingStool());
			cartesRumeur.add(evilEye = new EvilEye());
			cartesRumeur.add(hookedNose = new HookedNose());		
			cartesRumeur.add(petNewt = new PetNewt());
			cartesRumeur.add(pointedHat = new PointedHat());
			cartesRumeur.add(theInquisition = new TheInquisition());
			cartesRumeur.add(toad = new Toad());
			cartesRumeur.add(wart = new Wart());
		}
	}
	
	
	/**
	 * Cette m�thode permet de v�rifier si un joueur est le vainqueur de la partie.
	 * Si un joueur a 5 points ou plus, il est d�sign� vainqueur.
	 * Si plusieurs joueurs sont gagnants, c'est le premier joueur de la liste "joueurs" qui est d�sign� vainqueur
	 * @return Le joueur vainqueur s'il existe, ou null si aucun joueur n'est vainqueur
	 * @see joueurs
	 */
	//Tester s'il y a un vainqueur (Si un joueur a 5 points ou plus)
	public Joueur verifierVainqueur() {
		
		Joueur vainqueur = null;
		int compteurJoueur = 0;
				
		while ((vainqueur == null) && (compteurJoueur < this.joueurs.size())) {
			if (this.joueurs.get(compteurJoueur).getPoints() >= 5) {
				vainqueur = this.joueurs.get(compteurJoueur);
			}
			compteurJoueur++;
		}
		
		return vainqueur;
		
	}


// ~~~~~~~~~~~~~ GETTERS/SETTERS ~~~~~~~~~~~~~ //
	
	public TourDeJeu getTourActuel() {
		return tourActuel;
	}

	public void setTourActuel(TourDeJeu tourActuel) {
		this.tourActuel = tourActuel;
	}

	public ArrayList<Joueur> getJoueurs() {
		return joueurs;
	}

	public ArrayList<CarteRumeur> getCartesRumeur() {
		return cartesRumeur;
	}
	
	public int getNbJoueursReels() {
		return nbJoueursReels;
	}
	
	/**
	 * Setter du nombre de joueurs r�els
	 * Notifie �galement les observateurs du changement de nbJoueurs effectu�
	 * @param nbJoueursReels : Nombre de joueurs r�els
	 * 
	 * @see view.vueGraphique.VueGraphiqueDebutPartie
	 * @see view.vueTexte.VueTexteDebutPartie
	 */
	public void setNbJoueursReels(int nbJoueursReels) {
		this.nbJoueursReels = nbJoueursReels;
		this.setChanged();
		this.notifyObservers("nbJoueursReels");
	}

	public int getNbJoueursVirtuels() {
		return nbJoueursVirtuels;
	}
	
	public ArrayList<String> getNomsJoueursVirtuels() {
		return nomsJoueursVirtuels;
	}

	public void setNbJoueursVirtuels(int nbJoueursVirtuels) {
		this.nbJoueursVirtuels = nbJoueursVirtuels;
	}
	
	public VueGraphiquePartie getVuePartie() {
		return vuePartie;
	}

	public void setVuePartie(VueGraphiquePartie vuePartie) {
		this.vuePartie = vuePartie;
	}
	
	public AngryMob getAngryMob() {
		return angryMob;
	}

	public void setAngryMob(AngryMob angryMob) {
		this.angryMob = angryMob;
	}

	public BlackCat getBlackCat() {
		return blackCat;
	}

	public void setBlackCat(BlackCat blackCat) {
		this.blackCat = blackCat;
	}

	public Broomstick getBroomstick() {
		return broomstick;
	}

	public void setBroomstick(Broomstick broomstick) {
		this.broomstick = broomstick;
	}

	public Cauldron getCauldron() {
		return cauldron;
	}

	public void setCauldron(Cauldron cauldron) {
		this.cauldron = cauldron;
	}

	public DuckingStool getDuckingStool() {
		return duckingStool;
	}

	public void setDuckingStool(DuckingStool duckingStool) {
		this.duckingStool = duckingStool;
	}

	public EvilEye getEvilEye() {
		return evilEye;
	}

	public void setEvilEye(EvilEye evilEye) {
		this.evilEye = evilEye;
	}

	public HookedNose getHookedNose() {
		return hookedNose;
	}

	public void setHookedNose(HookedNose hookedNose) {
		this.hookedNose = hookedNose;
	}

	public PetNewt getPetNewt() {
		return petNewt;
	}

	public void setPetNewt(PetNewt petNewt) {
		this.petNewt = petNewt;
	}

	public PointedHat getPointedHat() {
		return pointedHat;
	}

	public void setPointedHat(PointedHat pointedHat) {
		this.pointedHat = pointedHat;
	}

	public TheInquisition getTheInquisition() {
		return theInquisition;
	}

	public void setTheInquisition(TheInquisition theInquisition) {
		this.theInquisition = theInquisition;
	}

	public Toad getToad() {
		return toad;
	}

	public void setToad(Toad toad) {
		this.toad = toad;
	}

	public Wart getWart() {
		return wart;
	}

	public void setWart(Wart wart) {
		this.wart = wart;
	}
	
	public  boolean isPartieLancee() {
		return partieLancee;
	}

	/**
	 * Setter de l'attribut partieLancee
	 * Notifie �galement les observateurs du changement effectu� (utile pour les vues du menu)
	 * 
	 * @param partieLancee : booleen qui indice si la partie est lanc�e ou non 
	 * 
	 * @see view.vueGraphique.VueGraphiqueMenu
	 * @see view.vueTexte.VueTexteMenu
	 */
	public  void setPartieLancee(boolean partieLancee) {
		this.partieLancee = partieLancee;
		
		this.setChanged();
		this.notifyObservers("partieLancee");
		
	}

// ~~~~~~~~~~~~~ GETTERS/SETTERS ~~~~~~~~~~~~~ //


}