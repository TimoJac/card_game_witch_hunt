package model.joueur;

import java.io.IOException;
import java.util.ArrayList;

import model.*;
import model.cartes_rumeur.CarteRumeur;

/**
 * Cette classe repr�sente un Joueur Reel, et extends la classe {@link Joueur}
 *
 *@see Joueur
 */
public class JoueurReel extends Joueur{

	/**
	 * Cette m�thode est le constructeur du JoueurReel, il demande en param�tre le nom � choisir et utilise le constructeur {@link Joueur#Joueur(String)}
	 * @param nom le nom du joueur
	 */
	public JoueurReel(String nom) {
		super(nom);
	}

	// Choisir son role entre villageois et sorciere. Le role du joueur est stock� dans l'attribut role, de type Role (enumeration)
	@Override
	public void choisirRole() {
		
		int choixRole = 0;
		
		System.out.println("\t- Je veux �tre villageois - Entrez '1'");
		System.out.println("\t- Je veux �tre une sorci�re - Entrez '2'");
		
		try {
			choixRole = Methods.entrerEntier(1, 2, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (choixRole == 1) this.setRole(Role.VILLAGEOIS);
		else if (choixRole == 2) this.setRole(Role.SORCIERE);
		
	}
	
	/**
	 * Cette m�thode permet de demander au Joueur actuel qui il veut choisir en tant que prochain Joueur.
	 * @param consigne Le texte � afficher lors du choix
	 */
    public void choisirProchainJoueur(String consigne) {
    	
		ArrayList<Joueur> listeJoueurs = new ArrayList<>(Partie.getInstance().getJoueurs()); // Liste des joueurs qui peuvent potentiellement �tre le prochain joueur
    	int choixEntierJoueur = -1; // Choix de l'utilisateur pour l'entier correspondant au prochain joueur
    	Joueur choixJoueur = null; // Choix de l'utilisateur pour le procahin joueur
    	
    	// On enl�ve le joueur actuel de la liste des potentiels prochains joueurs (car un joueur ne peut pas se choisir lui-m�me)
    	listeJoueurs.remove(Partie.getInstance().getTourActuel().getJoueurActuel());
    	
    	// On enl�ve les joueurs qui ne peuvent plus jouer de la liste des potentiels prochains joueurs
    	for (int i = 0; i < listeJoueurs.size(); i++) {
    		if (!listeJoueurs.get(i).isPeutJouer()) {
    			listeJoueurs.remove(i);
    			i--;
    		}
    	}


		// Affichage de la consigne
		System.out.println(consigne);

		for (int i = 0; i < listeJoueurs.size(); i++) {
			System.out.println("\t- " + listeJoueurs.get(i).getNom() + " : Entrez '" + (i + 1) + "'");
		}

		// On enregistre le choix de l'utilisateur
		try {
			choixEntierJoueur = Methods.entrerEntier(1, listeJoueurs.size(), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		choixJoueur = listeJoueurs.get(choixEntierJoueur - 1);

    	Partie.getInstance().getTourActuel().setProchainJoueur(choixJoueur);
        	
    }

	// Action obligatoire pour le joueur choisi par la carte Evil Eye
	// Lors de son tour, le joueur doit accuser un autre joueur, si possible pas le joueur qui d�tient la carte EvilEye
	
	@Override
	public void faireActionEvilEye() {
		
		ArrayList<Joueur> joueurs = new ArrayList<Joueur> (Partie.getInstance().getJoueurs()); // Liste des joueurs que l'on peut accuser
		CarteRumeur evilEye = Partie.getInstance().getEvilEye(); // Carte Evil Eye
		Joueur joueurQuiAEvilEye = null; // Joueur qui a la carte Evil Eye (si possible, on ne doit pas l'accuser)
		Joueur choixAccuse = null; 	// Joueur accus� par le joueur actuel
		int i = 0; // indice
		
		System.out.println(this.getNom()+ ", c'est � votre tour. \nVous devez accuser un joueur. Si possible n'accusez pas le joueur qui poss�de la carte Evil Eye.\n");
		
		
		// On cherche � quel joueur appartient la carte EvilEye
		while (joueurQuiAEvilEye == null && i < joueurs.size()) {
			if (joueurs.get(i).getCartesReveleeJoueur().contains(evilEye)) {
				joueurQuiAEvilEye = joueurs.get(i);
			}
			i++;
		}
		
		// On retire le joueur qui poss�de la carte des joueurs � accuser (s'il est dans la liste des joueurs � accuser)
		if (joueurs.contains(joueurQuiAEvilEye)) {
			joueurs.remove(joueurQuiAEvilEye);
		}
		
		// On retire aussi le joueur actuel (s'il est dans la liste des joueurs � accuser), car un joueur ne peut pas s'accuser soi-m�me
		if (joueurs.contains(this)) {
			joueurs.remove(this);
		}
		// Puis on retire les joueurs qui ne peuvent pas �tre accus�s
		for (int j = 0; j <joueurs.size(); j++) {
			if (joueurs.get(j).isIdentiteRevelee() == true) {
				joueurs.remove(j);
				j--;
			}
		}
		
		// S'il reste des joueurs � accuser dans la liste, on demande au joueur lequel il veut accuser 
		if (!joueurs.isEmpty()) {

			choixAccuse = demanderQuelJoueurAccuser(joueurs); // On demande quel joueur accuser et on stocke la r�ponse dans "choixAccuse"
			choixAccuse.setJoueurEstAccuse(true); // On met la variable "isAccused" du joueur accus� � true
			Joueur.setJoueurQuiAccuse(this); // On dit que ce joueur est celui qui a accus� le prochain
			Partie.getInstance().getTourActuel().setProchainJoueur(choixAccuse);  // on d�finit le prochain joueur comme �tant ce joueur :)	
		}
		
		// Si la liste de joueurs � accuser est vide, et que l'identit� du joueur qui poss�de EvilEye n'a pas d�j� �t� r�v�l�e
		else if ((joueurs.isEmpty()) && (!joueurQuiAEvilEye.isIdentiteRevelee())) {
			
			// On accuse le joueur qui poss�de EvilEye
			joueurQuiAEvilEye.setJoueurEstAccuse(true); // On met la variable "isAccused" du joueur accus� � true
			Joueur.setJoueurQuiAccuse(this); // On dit que ce joueur est celui qui a accus� le prochain
			Partie.getInstance().getTourActuel().setProchainJoueur(joueurQuiAEvilEye);  // on d�finit le prochain joueur comme �tant ce joueur :)			
		}
		
		// Si la liste de joueurs � accuser est vide, et que l'identit� du joueur qui poss�de EvilEye a d�j� �t� r�v�l�e
		else {
			// On ne peut accuser aucun joueur
			System.out.println("D�sol�. Vous ne pouvez accuser aucun joueur.\nVotre tour est termin�.");
			
			// On d�termine al�atoirement le prochain joueur
			Partie.getInstance().getTourActuel().choisirJoueurRandom();
		}
	}
	
	@Override
	public void faireActionDuckingStool() {
		
		int choix = 0;
		
		System.out.println(this.getNom()+ ", c'est � votre tour.\nVoulez-vous :");
		System.out.println("\t- Jeter une carte de votre main - Entrez '1'");
		System.out.println("\t- R�v�ler votre identit� - Entrez '2'");
		
		try {
			choix = Methods.entrerEntier(1, 2, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<CarteRumeur> mainJoueur = this.getCartesRumeurJoueur();

		if (mainJoueur.size() == 0){
			System.out.println("Votre main est vide, vous ne pouvez pas d�fausser de carte.");
			choix = 2;
		}

		if (choix == 1) { // Le joueur veut jeter une carte
			
			CarteRumeur carteJetee = null;

			// Le joueur choisit la carte � jeter
			carteJetee = choisirCarte(mainJoueur, "Vous avez choisi de jeter une carte de votre main.\nChoisissez la carte � jeter :");
			
			// On ajoute la carte � la d�fausse
			Partie.getInstance().getTourActuel().ajouterCarteDefausse(carteJetee);
			
			// On retire la carte de la main du joueur actuel
			this.retirerCarteRumeurJoueur(carteJetee);
			
			// Le joueur prend la main, on le d�finit prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(this);

		}
		
		else if (choix == 2)  { // Le joueur veut r�v�ler son identit�
	
			// On r�v�le l'identit� du joueur
			this.setIdentiteRevelee(true);
			
			// On enl�ve l'accusation port�e sur le joueur
			this.setJoueurEstAccuse(false);
			
			// Si c'�tait une soci�re
			if (this.getRole() == Role.SORCIERE) {
				
				System.out.println("Bravo ! " + this.getNom() + " �tait une soci�re !\n");
				
				System.out.println(this.getNom() + ", vous ne pouvez plus jouer jusqu'� la fin du tour :(");
				System.out.println(getJoueurQuiAccuse().getNom() + " gagne 1 point.");
				
				
				this.setPeutJouer(false); // Le joueur r�v�l� ne peut plus jouer
				getJoueurQuiAccuse().actualiserPoints(1); // Le joueur qui accuse gagne 1 point
				
				Partie.getInstance().getTourActuel().setProchainJoueur(getJoueurQuiAccuse()); // Le joueur qui accuse rejoue, on le met donc en prochain joueur
			}
			
			else { // Si c'�tait un villageois

				System.out.println(this.getNom() + " �tait un villageois.");	
				System.out.println(getJoueurQuiAccuse().getNom() + " perd 1 point. " + this.getNom() + "  prend la main.");
				
				getJoueurQuiAccuse().actualiserPoints(-1); // Le joueur qui accuse perd 1 point
				Partie.getInstance().getTourActuel().setProchainJoueur(this); // Le joueur accus� prend le prochain tour
			}		
		}	
	}

	
	// Cette m�thode demande � l'utilisateur quel joueur accuser et retourne le joueur Accus�.
	// En param�tre, on passe la liste des joueurs pouvant potentiellement �tre accus�s.
	/**
	 * Cette m�thode demande � l'utilisateur quel joueur accuser parmi une liste de joueur pouvant l'�treet retourne le joueur accus�.
	 * 
	 * @param listeJoueurs la liste des joueurs pouvant �tre accus�s
	 * @return le joueur accus�
	 */
	private Joueur demanderQuelJoueurAccuser (ArrayList<Joueur> listeJoueurs) {
		
		int numeroChoixAccuse;
		Joueur choixAccuse = null;
		
		// On �crit la consigne pour l'accusation
		System.out.println("Quel joueur voulez-vous accuser ?");
		for (int i=0; i<listeJoueurs.size(); i++) { 
			System.out.print("\t- " + listeJoueurs.get(i).getNom());
			System.out.println(" : Entrez '"+ (i+1) + "'");
		}
				
		// On lit l'entier entr� par le joueur correspondant au joueur � accuser
		try {
			numeroChoixAccuse = Methods.entrerEntier(1, listeJoueurs.size(), "") - 1;
			choixAccuse = listeJoueurs.get(numeroChoixAccuse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return choixAccuse;
	}

	
	/**
	 * Cette m�thode permet d'afficher les cartes r�v�l�es des joueurs
	 */
	public void afficherCarteReveleesJoueur(){
		int choix;
		ArrayList<Joueur> listeJoueurs = new ArrayList<>(Partie.getInstance().getJoueurs());

		for(int i = 0; i < listeJoueurs.size(); i++ ){
			System.out.println("[" + (i+1) + "] - " + listeJoueurs.get(i));
		}

		try {
			choix = Methods.entrerEntier(1, listeJoueurs.size(), "");
			
			if (Partie.getInstance().getJoueurs().get(choix - 1).getCartesReveleeJoueur().size() != 0) {
				System.out.println("Les cartes r�v�l�es du joueur " + listeJoueurs.get(choix - 1) + " sont :\n");
				System.out.println(listeJoueurs.get(choix - 1).getCartesReveleeJoueur());
			} 
			else{
				System.out.println(listeJoueurs.get(choix - 1) + " n'a pas de carte r�v�l�e.");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public CarteRumeur choisirCarte (ArrayList<CarteRumeur> listeCartes, String consigne) {
		
		int numeroChoixCarte = -1;
		CarteRumeur carteChoisie = null;
		
		// On �crit la consigne
		System.out.println("\n" + consigne + "\n");
		
		//On liste les cartes
		for (int i=0; i<listeCartes.size(); i++) { 
			System.out.print("\t- " + listeCartes.get(i).getNom());
			System.out.println(" : Entrez '"+ (i+1) + "'");
		}
		
		// On lit l'entier entr� par le joueur correspondant � la carte choisie
		try {
			numeroChoixCarte = Methods.entrerEntier(1, listeCartes.size(), "") - 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (numeroChoixCarte != -1)
			carteChoisie = listeCartes.get(numeroChoixCarte);
				
		return carteChoisie;
		
	}

	@Override
	public void choisirAction() {
		// TODO Auto-generated method stub
		
	}
	
}
