package model;

import java.util.*;
import model.cartes_rumeur.CarteRumeur;
import model.joueur.Joueur;
import model.joueur.Role;
import view.vueGraphique.VueGraphiqueInitRole;
import view.vueGraphique.VueGraphiquePartie;

/**
 * Classe repr�sentant un tour de jeu complet, du choix du r�le et de la distribution des cartes � l'annonce du gagnant du tour.
 *
 */
public class TourDeJeu extends Observable {
	/** Attribut repr�sentant le joueur dont c'est le tour de jeu.	 */
	private Joueur joueurActuel;
	/** Attribut repr�sentant le joueur dont c'est le tour en suivant. Initialis� � null car pas de prochain Joueur au d�but du tour.	 */
	private Joueur prochainJoueur = null;

	/** Liste des cartes rumeurs pr�sentes dans la d�fausse. Initialis�e vide car pas de cartes dans la d�fausse au d�but du tour.	 */
	private ArrayList <CarteRumeur> defausse = new ArrayList<>();
	/** Num�ro du tour actuel	 */
	private static int tour;
	/** Attribut repr�sentant le nombre de joueurs dont l'identit� est encore cach�e. Utile pour v�rifier la fin du tour, car la condition est qu'il ne reste plus qu'un seul joueur avec son identit� cach�e.
	 * @see TourDeJeu#faireFinTour()
	 */
	private int nbIdentiteCachee;
	
	/** Attribut repr�sentant le nombre de joueurs ayant choisi leur identit�. Utile pour v�rifier si tous les joueurs ont bien choisi une identit�.
	 * @see TourDeJeu#faireTourDeJeu()
	 * @see TourDeJeu#initRoles()
	 */
	private int nbJoueursAvecRole;
	
	/**
	 * Attribut permettant de savoir si la carte Evil Eye a �t� jou�e au tour d'avant pour pouvoir appliquer son effet sp�cial au joueur suivant.
	 */
	private static boolean evilEyeActive = false;
	
	/**
	 * Attribut permettant de savoir si la carte Ducking Stool a �t� jou�e au tour d'avant pour pouvoir appliquer son effet sp�cial au joueur suivant.
	 */
	private static boolean duckingStoolActive = false;
	
	/**
	 * Attribut permettant de savoir si la carte Angry Mob a �t� jou�e au tour d'avant pour pouvoir appliquer son effet sp�cial au joueur suivant.
	 */
	public static boolean angryMobActive = false;


	/**
	 * Constructeur de la classe {@code TourDeJeu}.
	 * Incr�mente le num�ro du tour, distribue les cartes, choisis le premier joueur, et r�initialise les attributs � leur valeur standard.
	 * 
	 * @see TourDeJeu#distribuerCartes()
	 * @see #choisirJoueurRandom()
	 */
	public TourDeJeu () {
		
		TourDeJeu.tour++;
		
		// On enl�ve les messages des pr�c�dents tours de jeu
		VueGraphiquePartie.setMessageProchaineVue("");
		
		this.nbJoueursAvecRole = 0;
		
		// Distribution des cartes aux joueurs
		this.distribuerCartes();
		
		// Choix du premier joueur
		if (prochainJoueur == null)  {
			this.choisirJoueurRandom();
		}
		
		// Pour chaque joueur, recacher leur identit�, les autoriser � jouer, remettre leur role � null
		for (int i = 0; i < Partie.getInstance().getJoueurs().size(); i++) {
			Partie.getInstance().getJoueurs().get(i).setIdentiteRevelee(false);
			Partie.getInstance().getJoueurs().get(i).setPeutJouer(true);
			Partie.getInstance().getJoueurs().get(i).setRole(null);
		}
	}

	/**
	 * M�thode permettant de r�aliser un tour de jeu complet.
	 * Demande leur r�le aux joueurs, et g�re le {@code prochainJoueur} tant que le tour n'est pas fini. Dans ce cas, fait la fin de tour.
	 * 
	 * @see #initRoles()
	 * @see #faireFinTour()
	 */
	public void faireTourDeJeu() {
		
		// Si tous les joueurs n'ont pas encore choisi leur role
		if (nbJoueursAvecRole != Partie.getInstance().getJoueurs().size()) {
			// On initialise le role d'un joueur
			initRoles();
		}
		
		// Si tous les joueurs ont un r�le et si on est pas � la fin d'un tour 
		//(et si le joueur qui va jouer n'est pas accus� : car ce n'est pas un vrai tour dans ce cas)
		else if ((!verifierFinTour()) || (prochainJoueur.isJoueurEstAccuse())) {
			
			joueurActuel = prochainJoueur;

			Partie.getInstance().setVuePartie(new VueGraphiquePartie()); 
			
		}
		
		// Sinon, on c'est la fin du tour
		else {
			faireFinTour();
		}
	}

	
	/**
	 * M�thode permettant aux joueurs de choisir leur r�le pour le tour actuel.
	 */
	public void initRoles() {
		
		Joueur joueurTrouve;
		boolean trouve = false;
		int i = 0;
		int nbJoueurs = Partie.getInstance().getNbJoueursReels() + Partie.getInstance().getNbJoueursVirtuels();
		
		// On cherche dans la liste le premier joueur possible n'ayant pas de role
		while (trouve == false &&  i < nbJoueurs) {
			if (Partie.getInstance().getJoueurs().get(i).getRole() == null) 
				trouve = true;
			else
				i++;
		}
		
		// Si un joueur n'ayant pas de role a �t� trouv� 
		if (trouve == true) {
			
			this.nbJoueursAvecRole ++;
			
			joueurTrouve = Partie.getInstance().getJoueurs().get(i);
			// On cr�e une nouvelle vue pour initialiser son role
			new VueGraphiqueInitRole(joueurTrouve);		
		}
	}
	
	
	/**
	 * M�thode permettant de distribuer les cartes du tour al�atoirement � tous les joueurs.
	 * 4 possibilit�s en fonction du nombre de joueurs (de 3 � 6).
	 * 
	 * @see Joueur#changerCartes(ArrayList)
	 */
	public void distribuerCartes() {
		// Cartes rumeurs du jeu
		ArrayList<CarteRumeur> cartesRumeur = Partie.getInstance().getCartesRumeur();
		// Liste des joueurs r�els et virtuels
		ArrayList <Joueur> listeJoueurs = Partie.getInstance().getJoueurs();
		// Nombre de joueurs
		int nbJoueurs = Partie.getInstance().getNbJoueursReels() + Partie.getInstance().getNbJoueursVirtuels();
				
		//Pour distribuer les cartes al�atoirement, on m�lange le tableau de cartes
		Collections.shuffle(cartesRumeur);
		
		
		// R�partition des cartes diff�rente en fonction du nombre de joueurs
		switch (nbJoueurs) {
		case 3 : 
			
			// On distribue 4 cartes � chaque joueur 
			// (les 4 premieres cartes de la liste m�lang�e vont au joueur 1, les 4 suivantes au joueur 2, etc)
			for (int indice1 = 0; indice1 < nbJoueurs; indice1++) {
				
				int indice2 = indice1 * 4; // Indice qui sert � r�cup�rer les cartes Rumeur qui vont aller dans la main du Joueur "indice1"
				ArrayList <CarteRumeur> cartesJoueur = new ArrayList <CarteRumeur> (); // Liste des cartes Rumeurs distribu�es au Joueur "indice 1"
				
				// On ajoute les 4 cartes � la main du joueur
				cartesJoueur.add(cartesRumeur.get(indice2)); 
				cartesJoueur.add(cartesRumeur.get(indice2+1));
				cartesJoueur.add(cartesRumeur.get(indice2+2));
				cartesJoueur.add(cartesRumeur.get(indice2+3));
				
				// On change les cartes du Joueur en lui donnant la liste de ses nouvelles cartes
				listeJoueurs.get(indice1).changerCartes(cartesJoueur); 
			}
			
			break;
		
		case 4 : 
			
			// On distribue 3 cartes � chaque joueur
			for (int indice1 = 0; indice1 < nbJoueurs; indice1++) {
				
				int indice2 = indice1 * 3; // Indice qui sert � r�cup�rer les cartes Rumeur qui vont aller dans la main du Joueur "indice1"
				ArrayList <CarteRumeur> cartesJoueur = new ArrayList <> (); // Liste des cartes Rumeurs distribu�es au Joueur "indice 1"
				
				// On ajoute les 3 cartes � la main du joueur
				cartesJoueur.add(cartesRumeur.get(indice2)); 
				cartesJoueur.add(cartesRumeur.get(indice2+1));
				cartesJoueur.add(cartesRumeur.get(indice2+2));
				
				// On change les cartes du Joueur en lui donnant la liste de ses nouvelles cartes
				listeJoueurs.get(indice1).changerCartes(cartesJoueur); 
			}
			
			break;
			
		case 5 : 
			
			// On distribue 2 cartes � chaque joueur
			for (int indice1 = 0; indice1 < nbJoueurs; indice1++) {
				
				int indice2 = indice1 * 2; // Indice qui sert � r�cup�rer les cartes Rumeur qui vont aller dans la main du Joueur "indice1"
				ArrayList <CarteRumeur> cartesJoueur = new ArrayList <> (); // Liste des cartes Rumeurs distribu�es au Joueur "indice 1"
				
				// On ajoute les 2 cartes � la main du joueur
				cartesJoueur.add(cartesRumeur.get(indice2)); 
				cartesJoueur.add(cartesRumeur.get(indice2+1));

				// On change les cartes du Joueur avec la liste de ses nouvelles cartes
				listeJoueurs.get(indice1).changerCartes(cartesJoueur); 
			}
			
			// On met les deux derni�res cartes dans la d�fausse
			this.defausse.add(cartesRumeur.get(10));
			this.defausse.add(cartesRumeur.get(11));
			
			break;
			
		case 6 : 
			
			// On distribue 2 cartes � chaque joueur
			for (int indice1 = 0; indice1 < nbJoueurs; indice1++) {
					
				int indice2 = indice1 * 2; // Indice qui sert � r�cup�rer les cartes Rumeur qui vont aller dans la main du Joueur "indice1"
				ArrayList <CarteRumeur> cartesJoueur = new ArrayList <> (); // Liste des cartes Rumeurs distribu�es au Joueur "indice 1"
						
				// On ajoute les 2 cartes � la main du joueur
				cartesJoueur.add(cartesRumeur.get(indice2)); 
				cartesJoueur.add(cartesRumeur.get(indice2+1));

				// On change les cartes du Joueur avec la liste de ses nouvelles cartes
				listeJoueurs.get(indice1).changerCartes(cartesJoueur); 
			}
			
			break;
			
		default : 
			System.out.println("Erreur lors de la distribution des cartes. Le nombre de joueurs est invalide.");
		}
		
	}
	
	
	/**
	 * M�thode permettant de choisir al�atoirement un joueur, et de le d�finir en tant que prochain joueur.
	 */
	public void choisirJoueurRandom() {
		
		int nbJoueurs = Partie.getInstance().getJoueurs().size();
		ArrayList<Joueur> joueursPouvantJouer = new ArrayList<> (Partie.getInstance().getJoueurs());
		
		// On retire les joueurs qui ne peuvent plus jouer de la liste des joueurs
		for (int i = 0; i < nbJoueurs ; i++) {
			if (!joueursPouvantJouer.get(i).isPeutJouer()) {
				joueursPouvantJouer.remove(i);
				nbJoueurs--;
				i--;
			}
		}
		
		// On g�n�re un nombre al�atoire correspondant � un indice dans le tableau de joueurs
		int nombreRandom = new Random().nextInt(nbJoueurs);
		// On d�termine le prochain joueur comme �tant le joueur dont la position dans le tableau est "nombreRandom" 
		this.prochainJoueur = Partie.getInstance().getJoueurs().get(nombreRandom);
	}
	
	
	/**
	 * M�thode permettant de v�rifier si la fin du tour est atteinte (i.e. Il n'y a plus qu'un seul joueur avec son identit�e cach�e.
	 * 
	 * @return vrai s'il n'y a plus qu'un joueur avec son identit�e cach�e, faux sinon.
	 */
	public boolean verifierFinTour() {
				
		// On r�cup�re la liste des joueurs r�els et virtuels
		ArrayList <Joueur> listeJoueurs = Partie.getInstance().getJoueurs();
		int compteurJoueurRevele = 0;

		// On compte le nombre de joueurs dont l'identit� a �t� r�v�l�e
		for (int i = 0; i < listeJoueurs.size(); i++) {
			if (listeJoueurs.get(i).isIdentiteRevelee()) {
				compteurJoueurRevele++;
			}
		}

		// S'il ne reste plus qu'un seul joueur dont l'identit� n'a pas �t� r�v�l�e, c'est la fin du tour
		return compteurJoueurRevele >= listeJoueurs.size() - 1;
	}
	
	/**
	 * M�thode permettant de r�aliser les actions � faire � chaque fin de tour.
	 * Rechercher le gagnant, annonce la fn du tour visuellement, et annonce le gagnant, ainsi que les points gagn�s par ce dernier.
	 * 
	 * @see Joueur#actualiserPoints(int)
	 */
	public void faireFinTour() {
		
		ArrayList <Joueur> listeJoueurs = Partie.getInstance().getJoueurs();
		Joueur gagnant = null;
		boolean gagnantTrouve = false;
		int compteur = 0;
		
		// Recherche du gagnant
		while ((!gagnantTrouve) && (compteur < listeJoueurs.size())) {
			if (!listeJoueurs.get(compteur).isIdentiteRevelee()) {
				gagnant = listeJoueurs.get(compteur);
				gagnantTrouve = true;
			}
			compteur++;
		}
		
		if (gagnant != null) {
			
			System.out.println("\nC'est la fin du tour !\n");
			System.out.println("F�licitations ! " + gagnant.getNom() + " a gagn� le tour !");
			
			// Ajout des points au gagnant en fonction de son r�le
			if (gagnant.getRole() == Role.SORCIERE) {
				System.out.println("C'�tait une sorci�re, " + gagnant.getNom() + " gagne donc 2 points.");
				gagnant.actualiserPoints(2);
			}
			else {
				System.out.println("C'�tait un villageois, " + gagnant.getNom() + " gagne donc 1 point.");
				gagnant.actualiserPoints(1);
			}
			
			System.out.println("\n-------------------------------------------------------------------------");
			System.out.println("-------------------------------------------------------------------------\n");
		}
		
	}

	
	public Joueur getJoueurActuel() {
		return joueurActuel;
	}

	public void setJoueurActuel(Joueur joueurActuel) {
		this.joueurActuel = joueurActuel;
	}

	public Joueur getProchainJoueur() {
		return prochainJoueur;
	}

	public void setProchainJoueur(Joueur prochainJoueur) {
		this.prochainJoueur = prochainJoueur;
	}
	
	public ArrayList <CarteRumeur> getDefausse() {
		return defausse;
	}

	public void setDefausse(ArrayList <CarteRumeur> defausse) {
		this.defausse = defausse;
	}
	
	public void retirerCarteDefausse (CarteRumeur carte) {
		this.defausse.remove(carte);
	}
	
	public void ajouterCarteDefausse (CarteRumeur carte) {
		this.defausse.add(carte);
	}

	public static int getTour() {
		return tour;
	}

	public static void setTour(int tour) {
		TourDeJeu.tour = tour;
	}

	public int getNbIdentiteCachee() {
		return nbIdentiteCachee;
	}

	public void setNbIdentiteCachee(int nbIdentiteCachee) {
		this.nbIdentiteCachee = nbIdentiteCachee;
	}
	public static boolean isEvilEyeActive() {
		return evilEyeActive;
	}
	public static void setEvilEyeActive(boolean evilEyeActive) {
		TourDeJeu.evilEyeActive = evilEyeActive;
	}
	
	public static boolean isDuckingStoolActive() {
		return duckingStoolActive;
	}
	public static void setDuckingStoolActive(boolean duckingStoolActive) {
		TourDeJeu.duckingStoolActive = duckingStoolActive;
	}
	public static boolean isAngryMobActive() {
		return angryMobActive;
	}
	public static void setAngryMobActive(boolean angryMobActive) {
		TourDeJeu.angryMobActive = angryMobActive;
	}
	
	
}
