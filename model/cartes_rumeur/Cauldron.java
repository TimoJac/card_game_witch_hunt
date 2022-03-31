package model.cartes_rumeur;

import java.util.ArrayList;
import java.util.Random;

import model.*;
import model.joueur.Joueur;
import model.joueur.Role;

/**
 * Cette classe représente la carte Cauldron, et étend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class Cauldron extends CarteRumeur {
	
	
	/**
	 * Cette méthode permet d'exécuter le côté Hunt de la carte Cauldron.
	 * @param joueurActuel le joueur choisi par la carte
	 */
    public void executerCoteHunt(Joueur joueurActuel) {

        // Reveler l'identité du joueur
        //System.out.println("Vous vous appretez à révéler votre identité.\n");
        this.revelerIdentite(joueurActuel);
        
        //Notifier les observers
        this.notifierObservers();
                
    }

    /**
	 * Cette méthode permet d'exécuter le côté Witch de la carte Cauldron.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {
		
		Joueur joueurQuiAccuse = Joueur.getJoueurQuiAccuse();
		ArrayList<CarteRumeur> mainJoueurQuiAccuse = joueurQuiAccuse.getCartesRumeurJoueur();
		Random rand = new Random(); // Nombre aléatoire
		int indiceCarteAjeter; // Indice de la carte à jeter dans l'arrayList mainJoueurQuiAccuse
		

		//System.out.println("Le joueur qui vous a accusé doit jeter une carte de sa main au hasard.\n");
		
		// S'il reste des cartes au joueur qui accuse
		if (mainJoueurQuiAccuse.size() > 0) {
			
			// Générer un nombre aléatoire correspondant à l'indice de la carte à jeter
			indiceCarteAjeter = rand.nextInt(mainJoueurQuiAccuse.size());
			
			System.out.println(joueurQuiAccuse.getNom() + " jete la carte " + mainJoueurQuiAccuse.get(indiceCarteAjeter).getNom() + ".\n");
			
			// Mettre la carte dans la défausse
			Partie.getInstance().getTourActuel().getDefausse().add(mainJoueurQuiAccuse.get(indiceCarteAjeter));
			
			// Retirer la carte de la main du joueur
			mainJoueurQuiAccuse.remove(indiceCarteAjeter);
		}
		
		else {
			System.out.println("Désolé. Le joueur qui vous a accusé ne peut pas jeter de carte car il n'en a plus.\n");
		}
		
		// Mettre ce joueur en prochain joueur
		System.out.println("Vous Rejouez !\n");
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		//Notifier les observers
        this.notifierObservers();
	
	}
	
	// Constructeur
	/**
	 * Cette méthode est le constructeur de la carte Cauldron. Elle contient son nom, sa description du côté Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public Cauldron () {
		nom = "Cauldron";
		descHunt = "Revelez votre identité ! \n\t\tSi vous êtes une socière, le joueur à votre gauche prend la main. \n\t\tSi vous êtes villageois, rejouez.";
		descWitch = "Le joueur qui vous a accusé doit jeter une carte de sa main au hasard. Rejouez.";
	}
	
	/**
	 * Cette méthode permet à un joueur de révéler son identité.
	 * @param joueurActuel le joueur qui va révéler son identité
	 */
	public void revelerIdentite(Joueur joueurActuel) {
          
        // On révèle l'identité du joueur
        joueurActuel.setIdentiteRevelee(true);
        
        // On enlève l'accusation portée sur le joueur
        joueurActuel.setJoueurEstAccuse(false);
        
        // Si c'était une socière
        if (joueurActuel.getRole() == Role.SORCIERE) {
            
            System.out.println(joueurActuel.getNom() + " est une socière !");

            joueurActuel.setPeutJouer(false); // Le joueur révélé ne peut plus jouer
            
            // On définit le prochain joueur comme étant le joueur à gauche du joueur actuel
            setProchainJoueurGauche();
            
        }        
        else { // Si c'était un villageois

            System.out.println(joueurActuel.getNom() + " est un villageois.");    
            
            // Le joueur choisit le prochain joueur
            Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
        }
        
    }
	
	/**
	 * Cette méthode permet de définir le prochain joueur comme étant le joueur à la gauche de celui qui vient de jouer.
	 */
	private void setProchainJoueurGauche() {
		
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel(); // Joueur qui joue actuellement
		ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(Partie.getInstance().getJoueurs()); // Liste des joueurs
		Joueur JoueurAGauche; // Joueur à gauche du joueur actuel = Avant dans la liste des joueurs
		
		System.out.println("[DEBUG] Liste joueurs :  " + listeJoueurs);
		
		// On cherche la position du joueur actuel dans la liste des joueurs
		int positionJoueurActuel = listeJoueurs.indexOf(joueurActuel);
		System.out.println("[DEBUG] Position joueur actuel = "+ positionJoueurActuel);
		
		// Si la position du joueur actuel n'est pas en premier dans la liste
		if (positionJoueurActuel > 0) {
			JoueurAGauche = listeJoueurs.get(positionJoueurActuel-1);
			//Le joueur qui accuse rejoue, on le met donc en prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(JoueurAGauche); 
		}
		else if (positionJoueurActuel == 0) { // Si la position du joueur actuel est en premier dans la liste, le joueur à gauche est le dernier de la liste
			JoueurAGauche = listeJoueurs.get(listeJoueurs.size()-1);
			//Le joueur qui accuse rejoue, on le met donc en prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(JoueurAGauche); 
		}
		else {
			System.out.println("Erreur. Le joueur actuel n'a pas été trouvé dans la liste des joueurs");
		}
	}
	
	
	//
	// ~~~ Witch ? 
	// Joueur qui a accusé défausse une carte aléatoire de sa main
	// Être le prochain joueur
	//
	// ~~~ Hunt !
	// Révéler son identité
	//		• Witch : Joueur à sa gauche est le prochain joueur
	//		• Village : Choisir le prochain joueur
}
