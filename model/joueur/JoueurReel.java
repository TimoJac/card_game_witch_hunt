package model.joueur;

import java.io.IOException;
import java.util.ArrayList;

import model.*;
import model.cartes_rumeur.CarteRumeur;

/**
 * Cette classe représente un Joueur Reel, et extends la classe {@link Joueur}
 *
 *@see Joueur
 */
public class JoueurReel extends Joueur{

	/**
	 * Cette méthode est le constructeur du JoueurReel, il demande en paramètre le nom à choisir et utilise le constructeur {@link Joueur#Joueur(String)}
	 * @param nom le nom du joueur
	 */
	public JoueurReel(String nom) {
		super(nom);
	}

	// Choisir son role entre villageois et sorciere. Le role du joueur est stocké dans l'attribut role, de type Role (enumeration)
	@Override
	public void choisirRole() {
		
		int choixRole = 0;
		
		System.out.println("\t- Je veux être villageois - Entrez '1'");
		System.out.println("\t- Je veux être une sorcière - Entrez '2'");
		
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
	 * Cette méthode permet de demander au Joueur actuel qui il veut choisir en tant que prochain Joueur.
	 * @param consigne Le texte à afficher lors du choix
	 */
    public void choisirProchainJoueur(String consigne) {
    	
		ArrayList<Joueur> listeJoueurs = new ArrayList<>(Partie.getInstance().getJoueurs()); // Liste des joueurs qui peuvent potentiellement être le prochain joueur
    	int choixEntierJoueur = -1; // Choix de l'utilisateur pour l'entier correspondant au prochain joueur
    	Joueur choixJoueur = null; // Choix de l'utilisateur pour le procahin joueur
    	
    	// On enlève le joueur actuel de la liste des potentiels prochains joueurs (car un joueur ne peut pas se choisir lui-même)
    	listeJoueurs.remove(Partie.getInstance().getTourActuel().getJoueurActuel());
    	
    	// On enlève les joueurs qui ne peuvent plus jouer de la liste des potentiels prochains joueurs
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
	// Lors de son tour, le joueur doit accuser un autre joueur, si possible pas le joueur qui détient la carte EvilEye
	
	@Override
	public void faireActionEvilEye() {
		
		ArrayList<Joueur> joueurs = new ArrayList<Joueur> (Partie.getInstance().getJoueurs()); // Liste des joueurs que l'on peut accuser
		CarteRumeur evilEye = Partie.getInstance().getEvilEye(); // Carte Evil Eye
		Joueur joueurQuiAEvilEye = null; // Joueur qui a la carte Evil Eye (si possible, on ne doit pas l'accuser)
		Joueur choixAccuse = null; 	// Joueur accusé par le joueur actuel
		int i = 0; // indice
		
		System.out.println(this.getNom()+ ", c'est à votre tour. \nVous devez accuser un joueur. Si possible n'accusez pas le joueur qui possède la carte Evil Eye.\n");
		
		
		// On cherche à quel joueur appartient la carte EvilEye
		while (joueurQuiAEvilEye == null && i < joueurs.size()) {
			if (joueurs.get(i).getCartesReveleeJoueur().contains(evilEye)) {
				joueurQuiAEvilEye = joueurs.get(i);
			}
			i++;
		}
		
		// On retire le joueur qui possède la carte des joueurs à accuser (s'il est dans la liste des joueurs à accuser)
		if (joueurs.contains(joueurQuiAEvilEye)) {
			joueurs.remove(joueurQuiAEvilEye);
		}
		
		// On retire aussi le joueur actuel (s'il est dans la liste des joueurs à accuser), car un joueur ne peut pas s'accuser soi-même
		if (joueurs.contains(this)) {
			joueurs.remove(this);
		}
		// Puis on retire les joueurs qui ne peuvent pas être accusés
		for (int j = 0; j <joueurs.size(); j++) {
			if (joueurs.get(j).isIdentiteRevelee() == true) {
				joueurs.remove(j);
				j--;
			}
		}
		
		// S'il reste des joueurs à accuser dans la liste, on demande au joueur lequel il veut accuser 
		if (!joueurs.isEmpty()) {

			choixAccuse = demanderQuelJoueurAccuser(joueurs); // On demande quel joueur accuser et on stocke la réponse dans "choixAccuse"
			choixAccuse.setJoueurEstAccuse(true); // On met la variable "isAccused" du joueur accusé à true
			Joueur.setJoueurQuiAccuse(this); // On dit que ce joueur est celui qui a accusé le prochain
			Partie.getInstance().getTourActuel().setProchainJoueur(choixAccuse);  // on définit le prochain joueur comme étant ce joueur :)	
		}
		
		// Si la liste de joueurs à accuser est vide, et que l'identité du joueur qui possède EvilEye n'a pas déjà été révélée
		else if ((joueurs.isEmpty()) && (!joueurQuiAEvilEye.isIdentiteRevelee())) {
			
			// On accuse le joueur qui possède EvilEye
			joueurQuiAEvilEye.setJoueurEstAccuse(true); // On met la variable "isAccused" du joueur accusé à true
			Joueur.setJoueurQuiAccuse(this); // On dit que ce joueur est celui qui a accusé le prochain
			Partie.getInstance().getTourActuel().setProchainJoueur(joueurQuiAEvilEye);  // on définit le prochain joueur comme étant ce joueur :)			
		}
		
		// Si la liste de joueurs à accuser est vide, et que l'identité du joueur qui possède EvilEye a déjà été révélée
		else {
			// On ne peut accuser aucun joueur
			System.out.println("Désolé. Vous ne pouvez accuser aucun joueur.\nVotre tour est terminé.");
			
			// On détermine aléatoirement le prochain joueur
			Partie.getInstance().getTourActuel().choisirJoueurRandom();
		}
	}
	
	@Override
	public void faireActionDuckingStool() {
		
		int choix = 0;
		
		System.out.println(this.getNom()+ ", c'est à votre tour.\nVoulez-vous :");
		System.out.println("\t- Jeter une carte de votre main - Entrez '1'");
		System.out.println("\t- Révéler votre identité - Entrez '2'");
		
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
			System.out.println("Votre main est vide, vous ne pouvez pas défausser de carte.");
			choix = 2;
		}

		if (choix == 1) { // Le joueur veut jeter une carte
			
			CarteRumeur carteJetee = null;

			// Le joueur choisit la carte à jeter
			carteJetee = choisirCarte(mainJoueur, "Vous avez choisi de jeter une carte de votre main.\nChoisissez la carte à jeter :");
			
			// On ajoute la carte à la défausse
			Partie.getInstance().getTourActuel().ajouterCarteDefausse(carteJetee);
			
			// On retire la carte de la main du joueur actuel
			this.retirerCarteRumeurJoueur(carteJetee);
			
			// Le joueur prend la main, on le définit prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(this);

		}
		
		else if (choix == 2)  { // Le joueur veut révéler son identité
	
			// On révèle l'identité du joueur
			this.setIdentiteRevelee(true);
			
			// On enlève l'accusation portée sur le joueur
			this.setJoueurEstAccuse(false);
			
			// Si c'était une socière
			if (this.getRole() == Role.SORCIERE) {
				
				System.out.println("Bravo ! " + this.getNom() + " était une socière !\n");
				
				System.out.println(this.getNom() + ", vous ne pouvez plus jouer jusqu'à la fin du tour :(");
				System.out.println(getJoueurQuiAccuse().getNom() + " gagne 1 point.");
				
				
				this.setPeutJouer(false); // Le joueur révélé ne peut plus jouer
				getJoueurQuiAccuse().actualiserPoints(1); // Le joueur qui accuse gagne 1 point
				
				Partie.getInstance().getTourActuel().setProchainJoueur(getJoueurQuiAccuse()); // Le joueur qui accuse rejoue, on le met donc en prochain joueur
			}
			
			else { // Si c'était un villageois

				System.out.println(this.getNom() + " était un villageois.");	
				System.out.println(getJoueurQuiAccuse().getNom() + " perd 1 point. " + this.getNom() + "  prend la main.");
				
				getJoueurQuiAccuse().actualiserPoints(-1); // Le joueur qui accuse perd 1 point
				Partie.getInstance().getTourActuel().setProchainJoueur(this); // Le joueur accusé prend le prochain tour
			}		
		}	
	}

	
	// Cette méthode demande à l'utilisateur quel joueur accuser et retourne le joueur Accusé.
	// En paramètre, on passe la liste des joueurs pouvant potentiellement être accusés.
	/**
	 * Cette méthode demande à l'utilisateur quel joueur accuser parmi une liste de joueur pouvant l'êtreet retourne le joueur accusé.
	 * 
	 * @param listeJoueurs la liste des joueurs pouvant être accusés
	 * @return le joueur accusé
	 */
	private Joueur demanderQuelJoueurAccuser (ArrayList<Joueur> listeJoueurs) {
		
		int numeroChoixAccuse;
		Joueur choixAccuse = null;
		
		// On écrit la consigne pour l'accusation
		System.out.println("Quel joueur voulez-vous accuser ?");
		for (int i=0; i<listeJoueurs.size(); i++) { 
			System.out.print("\t- " + listeJoueurs.get(i).getNom());
			System.out.println(" : Entrez '"+ (i+1) + "'");
		}
				
		// On lit l'entier entré par le joueur correspondant au joueur à accuser
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
	 * Cette méthode permet d'afficher les cartes révélées des joueurs
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
				System.out.println("Les cartes révélées du joueur " + listeJoueurs.get(choix - 1) + " sont :\n");
				System.out.println(listeJoueurs.get(choix - 1).getCartesReveleeJoueur());
			} 
			else{
				System.out.println(listeJoueurs.get(choix - 1) + " n'a pas de carte révélée.");
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
		
		// On écrit la consigne
		System.out.println("\n" + consigne + "\n");
		
		//On liste les cartes
		for (int i=0; i<listeCartes.size(); i++) { 
			System.out.print("\t- " + listeCartes.get(i).getNom());
			System.out.println(" : Entrez '"+ (i+1) + "'");
		}
		
		// On lit l'entier entré par le joueur correspondant à la carte choisie
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
