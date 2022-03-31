package model.joueur;

import java.util.ArrayList;
import java.util.Observable;

import model.Partie;
import model.cartes_rumeur.*; 

/**
 * Cette classe est une classe abstraite permettant de regrouper les méthodes nécessaires aux classes {@code JoueurReel} et {@code JoueurVirtuel}.
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
	 * Cet attribut contient le rôle du joueur, à partir de l'énumération {@link Role}.
	 * 
	 * @see Role
	 */
	private Role role;
	
	/**
	 * Cet attribut contient le nom du Joueur, généré automatiquement parmi une liste pré-générée s'il est {@code JoueurVirtuel} et choisi par le joueur si il est {@code JoueurReel}
	 */
	private String nom;
	
	/**
	 * Cet attribut permet de savoir si l'identitée du Joueur a été révélée.
	 * Il contient vrai si son identitée est révélée, et faux sinon.
	 */
	private boolean identiteRevelee;
	
	/**
	 * Cet attribut permet de savoir s'il le joueur est encore en capacité de jouer (i.e. son identité n'a pas été révélée en tant que Sorcière)
	 */
	private boolean peutJouer;
	
	/**
	 * Cet attribut permet de savoir si le Joueur a été accusé. Cela permet de changer ses actions, pour qu'il puisse jouer ses cartes côté Witch, par exemple.
	 */
	private boolean joueurEstAccuse;
	
	/**
	 * Cet attribut est un ArrayList contenant les cartes Révélées du Joueur.
	 */
	private ArrayList<CarteRumeur> cartesReveleesJoueur = new ArrayList<>();
	
	/**
	 * Cet attribut est un ArrayList contenant les cartes Rumeur contenues dans la main du Joueur.
	 */
	private ArrayList<CarteRumeur> cartesRumeurJoueur = new ArrayList<>();
	
	/**
	 * Cet attribut contient le Joueur qui a accusé le joueur accusé durant ce tour.
	 */
	private static Joueur joueurQuiAccuse = null;
	
	/**
	 * Cette méthode est une méthode abstraite permettant au joueur de choisir son action.
	 * Elle est redéfinie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @see JoueurReel#choisirAction()
	 * @see JoueurVirtuel#choisirAction()
	 */
	public abstract void choisirAction();
	
	/**
	 * Cette méthode est une méthode abstraite permettant au joueur de choisir son role.
	 * Elle est redéfinie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @see JoueurReel#choisirRole()
	 * @see JoueurVirtuel#choisirRole()
	 */
	public abstract void choisirRole();
	
	/**
	 * Cette méthode est une méthode abstraite permettant au joueur de choisir une carte.
	 * Elle est redéfinie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @param listeCartes la liste des cartes parmi lesquelles choisir
	 * @param consigne Le texte à afficher lors de la demande de choix.
	 * @return la {@link CarteRumeur} choisie
	 */
	public abstract CarteRumeur choisirCarte (ArrayList<CarteRumeur> listeCartes, String consigne);
	
	/**
	 * Cette méthode est une méthode abstraite permettant au joueur après celui qui a joué la carte Evil Eye d'effectuer l'action nécessaire.
	 * Elle est redéfinie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @see JoueurReel#faireActionEvilEye()
	 * @see JoueurVirtuel#faireActionEvilEye()
	 */
	public abstract void faireActionEvilEye(); // Action spéciale pour le joueur qui passe après la carte EvilEye : Il doit accuser quelqu'un obligatoirement, sauf (si possible) le joueur qui a la carte Evil Eye
	
	/**
	 * Cette méthode est une méthode abstraite permettant au joueur après celui qui a joué la carte Ducking Stool d'effectuer l'action nécessaire.
	 * Elle est redéfinie dans les classes filles {@link JoueurReel} et {@link JoueurVirtuel}
	 * 
	 * @see JoueurReel#faireActionDuckingStool()
	 * @see JoueurVirtuel#faireActionDuckingStool()
	 */
	public abstract void faireActionDuckingStool(); // Action spéciale pour le joueur qui passe après la carte DuckingStool : Il doit choisir entre jeter une carte ou réveler son identité
	
	/**
	 * Cette méthode est le constructeur de la classe Joueur. Elle prend en paramètre le nom du joueur et met ses attributs dans les conditions standard de début de tour.
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
	 * Cette méthode permet d'accuser un joueur.
	 * @param joueurAccuse le joueur à accuser
	 */
	public void accuser(Joueur joueurAccuse) {
		
		// On met la variable "isAccused" du joueur accusé à true
		joueurAccuse.setJoueurEstAccuse(true);
		
		//On définit ce joueur comme le joueur qui accuse
		Joueur.setJoueurQuiAccuse(this);
		
		// on définit le prochain joueur comme étant ce joueur :)
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurAccuse); 
		
		//Notifier les observers
		this.setChanged();
		this.notifyObservers("accusation");
		
		
	}
	
	/**
	 * Cette méthode permet de choisir le prochain Joueur.
	 * @param joueurChoisi le joueur à mettre en suivant
	 */
	public void choisirProchainJoueur(Joueur joueurChoisi) {
		
		// on définit le prochain joueur comme étant ce joueur :)
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurChoisi); 
		
		//Notifier les observers
		this.setChanged();
		this.notifyObservers("choixJoueur");
				
	}

	/**
	 * Cette méthode permet de faire effectuer l'action au joueur suivant celui qui a joué la carte AngryMob.
	 */
	public void faireActionAngryMob(){ // Action spéciale pour le joueur qui passe après la carte AngryMob : Il doit réveler son identité

		// On révèle l'identité du joueur
		this.setIdentiteRevelee(true);

		// On enlève l'accusation portée sur le joueur
		this.setJoueurEstAccuse(false);

		// Si c'était une socière
		if (this.getRole() == Role.SORCIERE) {

			System.out.println("Bravo ! " + this.getNom() + " était une socière !\n");

			System.out.println(this.getNom() + ", vous ne pouvez plus jouer jusqu'à la fin du tour :(");
			System.out.println(getJoueurQuiAccuse().getNom() + " gagne 2 points.");


			this.setPeutJouer(false); // Le joueur révélé ne peut plus jouer
			getJoueurQuiAccuse().actualiserPoints(2); // Le joueur qui accuse gagne 2 points

			Partie.getInstance().getTourActuel().setProchainJoueur(getJoueurQuiAccuse()); // Le joueur qui accuse rejoue, on le met donc en prochain joueur
		}

		else { // Si c'était un villageois

			System.out.println(this.getNom() + " était un villageois.");
			System.out.println(getJoueurQuiAccuse().getNom() + " perd 2 points." + this.getNom() + "  prend la main.");

			getJoueurQuiAccuse().actualiserPoints(-2); // Le joueur qui accuse perd 2 point
			Partie.getInstance().getTourActuel().setProchainJoueur(this); // Le joueur accusé prend le prochain tour
		}
	}


	/**
	 * Cette méthode permet de modifier une liste de cartes par une autre passée en paramètre
	 * @param listeCartes la liste de carte à faire remplacer
	 */
	public void changerCartes(ArrayList<CarteRumeur> listeCartes) {
		this.cartesRumeurJoueur = listeCartes;
	}

	
	/**
	 * Cette méthode permet d'afficher la liste de cartes passée en paramètre
	 * @param cartes la liste de cartes à afficher
	 */
	public void afficherCartes(ArrayList<CarteRumeur> cartes){
		for (int i = 0; i < cartes.size(); i++) {
			System.out.println("["+(i+1)+"] " + this.cartesRumeurJoueur.get(i));
		}
	}
		
	/**
	 * Cette méthode permet à un joueur de révéler son identité.
	 */
	public void revelerIdentite() {
        
        // On révèle l'identité du joueur
        this.setIdentiteRevelee(true);
        
        // On enlève l'accusation portée sur le joueur
        this.setJoueurEstAccuse(false);
        
        // Si c'était une socière
        if (this.getRole() == Role.SORCIERE) {
            
            this.setPeutJouer(false); // Le joueur révélé ne peut plus jouer
            getJoueurQuiAccuse().actualiserPoints(1); // Le joueur qui accuse gagne 1 point
            
            Partie.getInstance().getTourActuel().setProchainJoueur(getJoueurQuiAccuse()); // Le joueur qui accuse rejoue, on le met donc en prochain joueur

        }
        
        else { // Si c'était un villageois

            Partie.getInstance().getTourActuel().setProchainJoueur(this); // Le joueur accusé prend le prochain tour
        }
        
        this.setChanged();
        this.notifyObservers("revelerIdentite");

    }
	
	/**
	 * Cette méthode permet d'obtenir la liste des joueurs pouvant être accusés
	 * @return liste de joueurs pouvant être accusés
	 */
	public ArrayList<Joueur> getJoueursAAccuser() {
		
		int i = 0;
		
		// On récupère la liste des joueurs réels et virtuels
		ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(Partie.getInstance().getJoueurs()); 
				
		// On enlève le joueur qui accuse de la liste des joueurs 
		// car un joueur ne peut pas s'accuser soit-même)		
		listeJoueurs.remove(this);
				
		// On parcourt toute la liste de joueurs pour enlever ceux dont l'identité a été révélée 
		// (on ne peut pas accuser un joueur dont l'identité a été révélée)
		while (i < listeJoueurs.size()) {
			if (listeJoueurs.get(i).isIdentiteRevelee()) {
				listeJoueurs.remove(i);
			}
			else i++;
		}
		
		return listeJoueurs;
	}
	
	/**
	 * Cette méthode permet d'actualiser le nombre de points d'un joueur.
	 * @param points le nombre de points à ajouter
	 */
	public void actualiserPoints(int points) {
		this.setPoints(getPoints() + points);
	}
	
	/**
	 * Cette méthode permet de voir le nombre de points que les joueurs possèdent.
	 */
	public void voirPointsJoueurs() {
		
		ArrayList<Joueur> joueurs = Partie.getInstance().getJoueurs();

		for (Joueur joueur : joueurs) {
			System.out.println("\t- " + joueur.getNom() + " : " + joueur.getPoints() + " point(s)");
		}
	}
	
	/**
	 * Cette méthode permet de vérifier que le joueur peut jouer une certaine carte.
	 * @param carte la carte à tester
	 * @return vrai si il peut la jouer, faux sinon
	 */
	public boolean peutJouerCarte (CarteRumeur carte) {
		
		// Pour la carte AngryMob et la carte The Inquisition (mêmes conditions)
		if ((carte instanceof AngryMob) || (carte instanceof TheInquisition)) {
			// Pour le côté Hunt de la carte
			if (!this.joueurEstAccuse) {
				// Si le joueur n'a pas été révélé Villageois, il ne peut pas jouer cette carte
				return this.identiteRevelee && this.role != Role.SORCIERE;
			}
			// Pour tous les autres cas de la carte
			return true;
		}
		
		// Pour la carte PointedHat
		else if (carte instanceof PointedHat) {
			// Si le joueur n'a pas de carteRévélée, il ne peut pas jouer cette carte
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
