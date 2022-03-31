package model.joueur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import model.Partie;
import model.cartes_rumeur.CarteRumeur;

/**
 * Cette classe représente un Joueur Virtuel, et extends la classe {@link Joueur}
 *
 *@see Joueur
 */
public class JoueurVirtuel extends Joueur{

	/**
	 * Cet attribut est une instance de la {@link StrategieJoueurVirtuel}, et permet de définir la stratégie du joueur virtuel voulue.
	 */
	private StrategieJoueurVirtuel strategieJoueurVirtuel;

	/**
	 * Cette méthode est le constructeur du JoueurVirtuel, il demande en paramètre le nom à choisir et utilise le constructeur {@link Joueur#Joueur(String)}
	 * @param nom le nom du joueur
	 */
	public JoueurVirtuel(String nom) {
		super(nom);
	}

	@Override
	public void choisirAction() {
		getStrategieJoueurVirtuel().choisirAction();
	}

	/**
	 * Cette méthode permet de choisir aléatoirement un joueur à accuser.
	 * @return le joueur à accuser
	 */
	public Joueur choisirQuiAccuser() {

		int choixAccuse = 0;
		Joueur joueurAccuse;
		ArrayList <Joueur> listeJoueurs = this.getJoueursAAccuser(); // Liste des joueurs a accuser

		// On génère un entier aléatoire qui correspond à l'indice du joueur accusé dans "listeJoueurs"
		choixAccuse = new Random().nextInt(listeJoueurs.size());	//Entre 0 et listeJoueurs.size()
		
		// On détermine donc le joueur accusé
		joueurAccuse = listeJoueurs.get(choixAccuse);
		
		return joueurAccuse;
	}

	
	public void choisirHunt() {
		getStrategieJoueurVirtuel().choisirHunt();
	}

	
	public void choisirWitch() {
		getStrategieJoueurVirtuel().choisirWitch();
	}

	@Override
	public void choisirRole() {
		int choixRole = new Random().nextInt(2);
		
		if (choixRole == 0) this.setRole(Role.VILLAGEOIS);
		else if (choixRole == 1) this.setRole(Role.SORCIERE);
	}
	
	public void choisirNom() {
		
		ArrayList<String> nomsJoueursVirtuels = Partie.getInstance().getNomsJoueursVirtuels();
		String nomJoueur;
		
		//Pour avoir de l'aléatoire, on mélange le tableau 
		Collections.shuffle(nomsJoueursVirtuels);	
		
		// On donne le premier element du tableau mélangé comme nom du joueur
		nomJoueur = nomsJoueursVirtuels.get(0);
		
		// On retire le premier élément du tableau afin de ne pas le donner à un autre joueur
		nomsJoueursVirtuels.remove(0);
		
		this.setNom(nomJoueur);
	}

	@Override
	public CarteRumeur choisirCarte(ArrayList<CarteRumeur> listeCartes, String consigne) {
		int numeroChoixCarte;
		CarteRumeur carteChoisie = null;
		
		numeroChoixCarte = new Random().nextInt(listeCartes.size());
		carteChoisie = listeCartes.get(numeroChoixCarte);
		
		System.out.println(Partie.getInstance().getTourActuel().getJoueurActuel() + " a choisi la carte " + carteChoisie.getNom());

		return carteChoisie;
	}
	
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

    	// On génère un entier aléatoirement, qui correspond au "choix" du bot
    	choixEntierJoueur = new Random().nextInt(listeJoueurs.size());
		
		choixJoueur = listeJoueurs.get(choixEntierJoueur);

    	Partie.getInstance().getTourActuel().setProchainJoueur(choixJoueur);
    	
    	System.out.println(this + " a choisi " + Partie.getInstance().getTourActuel().getProchainJoueur() + " comme étant le prochain joueur.");
    }



	@Override
	public void faireActionEvilEye() {
		
		ArrayList<Joueur> joueurs = new ArrayList<> (Partie.getInstance().getJoueurs()); // Liste des joueurs que l'on peut accuser
		CarteRumeur evilEye = Partie.getInstance().getEvilEye(); // Carte Evil Eye
		Joueur joueurQuiAEvilEye = null; // Joueur qui a la carte Evil Eye (si possible, on ne doit pas l'accuser)
		Joueur choixAccuse = null; 	// Joueur accusé par le joueur actuel
		int numeroChoixAccuse ;

		int i = 0; // indice


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
			if (joueurs.get(j).isIdentiteRevelee()) {
				joueurs.remove(j);
				j--;
			}
		}

		// S'il reste des joueurs à accuser dans la liste, on demande au joueur lequel il veut accuser
		if (!joueurs.isEmpty()) {

			// On demande quel joueur accuser et on stocke la réponse dans "choixAccuse"
			numeroChoixAccuse = new Random().nextInt(joueurs.size());
			choixAccuse = joueurs.get(numeroChoixAccuse); 
			
			choixAccuse.setJoueurEstAccuse(true); // On met la variable "isAccused" du joueur accusé à true
			Joueur.setJoueurQuiAccuse(this); // On dit que ce joueur est celui qui a accusé le prochain
			Partie.getInstance().getTourActuel().setProchainJoueur(choixAccuse);  // on définit le prochain joueur comme étant ce joueur :)
		}

		// Si la liste de joueurs à accuser est vide, et que l'identité du joueur qui possède EvilEye n'a pas déjà été révélée
		else if ((joueurs.isEmpty() == true) && (joueurQuiAEvilEye.isIdentiteRevelee() == false)) {

			// On accuse le joueur qui possède EvilEye
			joueurQuiAEvilEye.setJoueurEstAccuse(true); // On met la variable "isAccused" du joueur accusé à true
			Joueur.setJoueurQuiAccuse(this); // On dit que ce joueur est celui qui a accusé le prochain
			Partie.getInstance().getTourActuel().setProchainJoueur(joueurQuiAEvilEye);  // on définit le prochain joueur comme étant ce joueur :)
		}

		// Si la liste de joueurs à accuser est vide, et que l'identité du joueur qui possède EvilEye a déjà été révélée
		else {
			// On ne peut accuser aucun joueur
			System.out.println("Il n'y a aucun joueur à accuser\nSon tour est terminé.");

			// On détermine aléatoirement le prochain joueur
			Partie.getInstance().getTourActuel().choisirJoueurRandom();
		}
	}

	@Override
	public void faireActionDuckingStool() {
		int choix;

		System.out.println("C'est au tour de " + this.getNom());

		choix = new Random().nextInt(2);

		if (choix == 0) { // Le joueur veut jeter une carte

			CarteRumeur carteJetee = null;
			ArrayList<CarteRumeur> mainJoueur = this.getCartesRumeurJoueur();

			// Le joueur choisit la carte à jeter
			carteJetee = choisirCarte(mainJoueur, this.getNom() + " a choisi de jeter une carte de sa main.");

			// On ajoute la carte à la défausse
			Partie.getInstance().getTourActuel().ajouterCarteDefausse(carteJetee);

			// On retire la carte de la main du joueur actuel
			this.retirerCarteRumeurJoueur(carteJetee);

			// Le joueur prend la main, on le définit prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(this);

		}

		else if (choix == 1)  { // Le joueur veut révéler son identité

			System.out.println(this.getNom() + " a décidé de révéler son identité.");

			// On révèle l'identité du joueur
			this.setIdentiteRevelee(true);

			// On enlève l'accusation portée sur le joueur
			this.setJoueurEstAccuse(false);

			// Si c'était une socière
			if (this.getRole() == Role.SORCIERE) {

				System.out.println("Bravo ! " + this.getNom() + " était une socière !\n");

				System.out.println(this.getNom() + ", ne peut plus jouer jusqu'à la fin du tour :(");
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

	public StrategieJoueurVirtuel getStrategieJoueurVirtuel() {
		return strategieJoueurVirtuel;
	}

	public void setStrategieJoueurVirtuel(StrategieJoueurVirtuel strategieJoueurVirtuel) {
		this.strategieJoueurVirtuel = strategieJoueurVirtuel;
		this.setChanged();
		this.notifyObservers("strategie");
	}

}
