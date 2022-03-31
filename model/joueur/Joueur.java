package model.joueur;

import java.util.ArrayList;
import java.util.Observable;

import model.Partie;
import model.cartes_rumeur.*; 

/**
 * Cette classe est une classe abstraite permettant de regrouper les m�thodes n�cessaires aux classes {@code JoueurReel} et {@code JoueurVirtuel}.
 * 
 * @see JoueurReel
 * @see JoueurVirtuel
 * 
 */
public abstract class Joueur extends Observable {
	/**
	 * Cet attribut permet de compter les points de chaque instance de Joueur.
	 */
	private int points;
	
	/**
	 * Cet attribut contient le r�le du joueur, � partir de l'�num�ration {@link Role}.
	 * 
	 * @see Role
	 */
	private Role role;
	
	/**
	 * Cet attribut contient le nom du Joueur, g�n�r� automatiquement parmi une liste pr�-g�n�r�e s'il est {@code JoueurVirtuel} et choisi par le joueur si il est {@code JoueurReel}
	 */
	private String nom;
	
	/**
	 * Cet attribut permet de savoir si l'identit�e du Joueur a �t� r�v�l�e.
	 * Il contient vrai si son identit�e est r�v�l�e, et faux sinon.
	 */
	private boolean identiteRevelee;
	
	/**
	 * Cet attribut permet de savoir s'il le joueur est encore en capacit� de jouer (i.e. son identit� n'a pas �t� r�v�l�e en tant que Sorci�re)
	 */
	private boolean peutJouer;
	
	/**
	 * Cet attribut permet de savoir si le Joueur a �t� accus�. Cela permet de changer ses actions, pour qu'il puisse jouer ses cartes c�t� Witch, par exemple.
	 */
	private boolean joueurEstAccuse;
	
	/**
	 * Cet attribut est un ArrayList contenant les cartes R�v�l�es du Joueur.
	 */
	private ArrayList<CarteRumeur> cartesReveleesJoueur = new ArrayList<>();
	
	/**
	 * Cet attribut est un ArrayList contenant les cartes Rumeur contenues dans la main du Joueur.
	 */
	private ArrayList<CarteRumeur> cartesRumeurJoueur = new ArrayList<>();
	
	/**
	 * Cet attribut contient le Joueur qui a accus� le joueur accus� durant ce tour.
	 */
	private static Joueur joueurQuiAccuse = null;
	
	/**
	 * Cette m�thode est une m�thode abstraite permettant au joueur de choisir son action.
	 * Elle est red�finie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @see JoueurReel#choisirAction()
	 * @see JoueurVirtuel#choisirAction()
	 */
	public abstract void choisirAction();
	
	/**
	 * Cette m�thode est une m�thode abstraite permettant au joueur de choisir son role.
	 * Elle est red�finie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @see JoueurReel#choisirRole()
	 * @see JoueurVirtuel#choisirRole()
	 */
	public abstract void choisirRole();
	
	/**
	 * Cette m�thode est une m�thode abstraite permettant au joueur de choisir une carte.
	 * Elle est red�finie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @param listeCartes la liste des cartes parmi lesquelles choisir
	 * @param consigne Le texte � afficher lors de la demande de choix.
	 * @return la {@link CarteRumeur} choisie
	 */
	public abstract CarteRumeur choisirCarte (ArrayList<CarteRumeur> listeCartes, String consigne);
	
	/**
	 * Cette m�thode est une m�thode abstraite permettant au joueur apr�s celui qui a jou� la carte Evil Eye d'effectuer l'action n�cessaire.
	 * Elle est red�finie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @see JoueurReel#faireActionEvilEye()
	 * @see JoueurVirtuel#faireActionEvilEye()
	 */
	public abstract void faireActionEvilEye(); // Action sp�ciale pour le joueur qui passe apr�s la carte EvilEye : Il doit accuser quelqu'un obligatoirement, sauf (si possible) le joueur qui a la carte Evil Eye
	
	/**
	 * Cette m�thode est une m�thode abstraite permettant au joueur apr�s celui qui a jou� la carte Ducking Stool d'effectuer l'action n�cessaire.
	 * Elle est red�finie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @see JoueurReel#faireActionDuckingStool()
	 * @see JoueurVirtuel#faireActionDuckingStool()
	 */
	public abstract void faireActionDuckingStool(); // Action sp�ciale pour le joueur qui passe apr�s la carte DuckingStool : Il doit choisir entre jeter une carte ou r�veler son identit�
	
	/**
	 * Cette m�thode est le constructeur de la classe Joueur. Elle prend en param�tre le nom du joueur et met ses attributs dans les conditions standard de d�but de tour.
	 * @param nom Le nom du joueur
	 */
	public Joueur(String nom) {
		this.nom = nom;
		this.points = 0;
		this.identiteRevelee = false;
		this.peutJouer = true;
		this.joueurEstAccuse = false;
	}
	
	/**
	 * Cette m�thode permet d'accuser un joueur.
	 * @param joueurAccuse le joueur � accuser
	 */
	public void accuser(Joueur joueurAccuse) {
		
		// On met la variable "isAccused" du joueur accus� � true
		joueurAccuse.setJoueurEstAccuse(true);
		
		//On d�finit ce joueur comme le joueur qui accuse
		Joueur.setJoueurQuiAccuse(this);
		
		// on d�finit le prochain joueur comme �tant ce joueur :)
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurAccuse); 
		
		//Notifier les observers
		this.setChanged();
		this.notifyObservers("accusation");
		
		
	}
	
	/**
	 * Cette m�thode permet de choisir le prochain Joueur.
	 * @param joueurChoisi le joueur � mettre en suivant
	 */
	public void choisirProchainJoueur(Joueur joueurChoisi) {
		
		// on d�finit le prochain joueur comme �tant ce joueur :)
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurChoisi); 
		
		//Notifier les observers
		this.setChanged();
		this.notifyObservers("choixJoueur");
				
	}

	/**
	 * Cette m�thode permet de faire effectuer l'action au joueur suivant celui qui a jou� la carte AngryMob.
	 */
	public void faireActionAngryMob(){ // Action sp�ciale pour le joueur qui passe apr�s la carte AngryMob : Il doit r�veler son identit�

		// On r�v�le l'identit� du joueur
		this.setIdentiteRevelee(true);

		// On enl�ve l'accusation port�e sur le joueur
		this.setJoueurEstAccuse(false);

		// Si c'�tait une soci�re
		if (this.getRole() == Role.SORCIERE) {

			System.out.println("Bravo ! " + this.getNom() + " �tait une soci�re !\n");

			System.out.println(this.getNom() + ", vous ne pouvez plus jouer jusqu'� la fin du tour :(");
			System.out.println(getJoueurQuiAccuse().getNom() + " gagne 2 points.");


			this.setPeutJouer(false); // Le joueur r�v�l� ne peut plus jouer
			getJoueurQuiAccuse().actualiserPoints(2); // Le joueur qui accuse gagne 2 points

			Partie.getInstance().getTourActuel().setProchainJoueur(getJoueurQuiAccuse()); // Le joueur qui accuse rejoue, on le met donc en prochain joueur
		}

		else { // Si c'�tait un villageois

			System.out.println(this.getNom() + " �tait un villageois.");
			System.out.println(getJoueurQuiAccuse().getNom() + " perd 2 points." + this.getNom() + "  prend la main.");

			getJoueurQuiAccuse().actualiserPoints(-2); // Le joueur qui accuse perd 2 point
			Partie.getInstance().getTourActuel().setProchainJoueur(this); // Le joueur accus� prend le prochain tour
		}
	}


	/**
	 * Cette m�thode permet de modifier une liste de cartes par une autre pass�e en param�tre
	 * @param listeCartes la liste de carte � faire remplacer
	 */
	public void changerCartes(ArrayList<CarteRumeur> listeCartes) {
		this.cartesRumeurJoueur = listeCartes;
	}

	
	/**
	 * Cette m�thode permet d'afficher la liste de cartes pass�e en param�tre
	 * @param cartes la liste de cartes � afficher
	 */
	public void afficherCartes(ArrayList<CarteRumeur> cartes){
		for (int i = 0; i < cartes.size(); i++) {
			System.out.println("["+(i+1)+"] " + this.cartesRumeurJoueur.get(i));
		}
	}
		
	/**
	 * Cette m�thode permet � un joueur de r�v�ler son identit�.
	 */
	public void revelerIdentite() {
        
        // On r�v�le l'identit� du joueur
        this.setIdentiteRevelee(true);
        
        // On enl�ve l'accusation port�e sur le joueur
        this.setJoueurEstAccuse(false);
        
        // Si c'�tait une soci�re
        if (this.getRole() == Role.SORCIERE) {
            
            this.setPeutJouer(false); // Le joueur r�v�l� ne peut plus jouer
            getJoueurQuiAccuse().actualiserPoints(1); // Le joueur qui accuse gagne 1 point
            
            Partie.getInstance().getTourActuel().setProchainJoueur(getJoueurQuiAccuse()); // Le joueur qui accuse rejoue, on le met donc en prochain joueur

        }
        
        else { // Si c'�tait un villageois

            Partie.getInstance().getTourActuel().setProchainJoueur(this); // Le joueur accus� prend le prochain tour
        }
        
        this.setChanged();
        this.notifyObservers("revelerIdentite");

    }
	
	/**
	 * Cette m�thode permet d'obtenir la liste des joueurs pouvant �tre accus�s
	 * @return liste de joueurs pouvant �tre accus�s
	 */
	public ArrayList<Joueur> getJoueursAAccuser() {
		
		int i = 0;
		
		// On r�cup�re la liste des joueurs r�els et virtuels
		ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(Partie.getInstance().getJoueurs()); 
				
		// On enl�ve le joueur qui accuse de la liste des joueurs 
		// car un joueur ne peut pas s'accuser soit-m�me)		
		listeJoueurs.remove(this);
				
		// On parcourt toute la liste de joueurs pour enlever ceux dont l'identit� a �t� r�v�l�e 
		// (on ne peut pas accuser un joueur dont l'identit� a �t� r�v�l�e)
		while (i < listeJoueurs.size()) {
			if (listeJoueurs.get(i).isIdentiteRevelee()) {
				listeJoueurs.remove(i);
			}
			else i++;
		}
		
		return listeJoueurs;
	}
	
	/**
	 * Cette m�thode permet d'actualiser le nombre de points d'un joueur.
	 * @param points le nombre de points � ajouter
	 */
	public void actualiserPoints(int points) {
		this.setPoints(getPoints() + points);
	}
	
	/**
	 * Cette m�thode permet de voir le nombre de points que les joueurs poss�dent.
	 */
	public void voirPointsJoueurs() {
		
		ArrayList<Joueur> joueurs = Partie.getInstance().getJoueurs();

		for (Joueur joueur : joueurs) {
			System.out.println("\t- " + joueur.getNom() + " : " + joueur.getPoints() + " point(s)");
		}
	}
	
	/**
	 * Cette m�thode permet de v�rifier que le joueur peut jouer une certaine carte.
	 * @param carte la carte � tester
	 * @return vrai si il peut la jouer, faux sinon
	 */
	public boolean peutJouerCarte (CarteRumeur carte) {
		
		// Pour la carte AngryMob et la carte The Inquisition (m�mes conditions)
		if ((carte instanceof AngryMob) || (carte instanceof TheInquisition)) {
			// Pour le c�t� Hunt de la carte
			if (!this.joueurEstAccuse) {
				// Si le joueur n'a pas �t� r�v�l� Villageois, il ne peut pas jouer cette carte
				return this.identiteRevelee && this.role != Role.SORCIERE;
			}
			// Pour tous les autres cas de la carte
			return true;
		}
		
		// Pour la carte PointedHat
		else if (carte instanceof PointedHat) {
			// Si le joueur n'a pas de carteR�v�l�e, il ne peut pas jouer cette carte
			return this.getCartesReveleeJoueur().size() != 0;
		}
		
		// Pour toutes les autres cartes 
		return true;
	}

	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
		this.setChanged();
		this.notifyObservers("role");
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
		this.setChanged();
		this.notifyObservers("nom");
	}	
	public boolean isIdentiteRevelee() {
		return identiteRevelee;
	}
	public void setIdentiteRevelee(boolean identiteRevelee) {
		this.identiteRevelee = identiteRevelee;
	}
	public boolean isPeutJouer() {
		return peutJouer;
	}
	public void setPeutJouer(boolean peutJouer) {
		this.peutJouer = peutJouer;
	}
	public boolean isJoueurEstAccuse() {
		return joueurEstAccuse;
	}
	public void setJoueurEstAccuse(boolean joueurEstAccuse) {
		this.joueurEstAccuse = joueurEstAccuse;
	}
	
	public static Joueur getJoueurQuiAccuse() {
		return joueurQuiAccuse;
	}

	public static void setJoueurQuiAccuse(Joueur joueurQuiAccuse) {
		Joueur.joueurQuiAccuse = joueurQuiAccuse;
	}

	public ArrayList<CarteRumeur> getCartesReveleeJoueur() {
		return cartesReveleesJoueur;
	}
	public void setCartesReveleeJoueur(ArrayList<CarteRumeur> cartesReveleeJoueur) {
		this.cartesReveleesJoueur = cartesReveleeJoueur;
	}
	public ArrayList<CarteRumeur> getCartesRumeurJoueur() {
		return cartesRumeurJoueur;
	}
	public void setCartesRumeurJoueur(ArrayList<CarteRumeur> cartesRumeurJoueur) {
		this.cartesRumeurJoueur = cartesRumeurJoueur;
	}
	public void ajouterCarteRumeurJoueur (CarteRumeur carte) {
		this.cartesRumeurJoueur.add(carte);
	}
	public void retirerCarteRumeurJoueur (CarteRumeur carte) {
		this.cartesRumeurJoueur.remove(carte);
	}
	
	public String toString() {
		return this.getNom();
	}
}
