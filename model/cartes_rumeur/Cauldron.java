package model.cartes_rumeur;

import java.util.ArrayList;
import java.util.Random;

import model.*;
import model.joueur.Joueur;
import model.joueur.Role;

/**
 * Cette classe repr�sente la carte Cauldron, et �tend la classe {@code CarteRumeur}.
 *
 *@see CarteRumeur
 */
public class Cauldron extends CarteRumeur {
	
	
	/**
	 * Cette m�thode permet d'ex�cuter le c�t� Hunt de la carte Cauldron.
	 * @param joueurActuel le joueur choisi par la carte
	 */
    public void executerCoteHunt(Joueur joueurActuel) {

        // Reveler l'identit� du joueur
        //System.out.println("Vous vous appretez � r�v�ler votre identit�.\n");
        this.revelerIdentite(joueurActuel);
        
        //Notifier les observers
        this.notifierObservers();
                
    }

    /**
	 * Cette m�thode permet d'ex�cuter le c�t� Witch de la carte Cauldron.
	 * @param joueurActuel le joueur choisi par la carte
	 */
	public void executerCoteWitch(Joueur joueurActuel) {
		
		Joueur joueurQuiAccuse = Joueur.getJoueurQuiAccuse();
		ArrayList<CarteRumeur> mainJoueurQuiAccuse = joueurQuiAccuse.getCartesRumeurJoueur();
		Random rand = new Random(); // Nombre al�atoire
		int indiceCarteAjeter; // Indice de la carte � jeter dans l'arrayList mainJoueurQuiAccuse
		

		//System.out.println("Le joueur qui vous a accus� doit jeter une carte de sa main au hasard.\n");
		
		// S'il reste des cartes au joueur qui accuse
		if (mainJoueurQuiAccuse.size() > 0) {
			
			// G�n�rer un nombre al�atoire correspondant � l'indice de la carte � jeter
			indiceCarteAjeter = rand.nextInt(mainJoueurQuiAccuse.size());
			
			System.out.println(joueurQuiAccuse.getNom() + " jete la carte " + mainJoueurQuiAccuse.get(indiceCarteAjeter).getNom() + ".\n");
			
			// Mettre la carte dans la d�fausse
			Partie.getInstance().getTourActuel().getDefausse().add(mainJoueurQuiAccuse.get(indiceCarteAjeter));
			
			// Retirer la carte de la main du joueur
			mainJoueurQuiAccuse.remove(indiceCarteAjeter);
		}
		
		else {
			System.out.println("D�sol�. Le joueur qui vous a accus� ne peut pas jeter de carte car il n'en a plus.\n");
		}
		
		// Mettre ce joueur en prochain joueur
		System.out.println("Vous Rejouez !\n");
		Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
		
		//Notifier les observers
        this.notifierObservers();
	
	}
	
	// Constructeur
	/**
	 * Cette m�thode est le constructeur de la carte Cauldron. Elle contient son nom, sa description du c�t� Witch et Hunt, et sa condition d'utilisation si elle en a une.
	 */
	public Cauldron () {
		nom = "Cauldron";
		descHunt = "Revelez votre identit� ! \n\t\tSi vous �tes une soci�re, le joueur � votre gauche prend la main. \n\t\tSi vous �tes villageois, rejouez.";
		descWitch = "Le joueur qui vous a accus� doit jeter une carte de sa main au hasard. Rejouez.";
	}
	
	/**
	 * Cette m�thode permet � un joueur de r�v�ler son identit�.
	 * @param joueurActuel le joueur qui va r�v�ler son identit�
	 */
	public void revelerIdentite(Joueur joueurActuel) {
          
        // On r�v�le l'identit� du joueur
        joueurActuel.setIdentiteRevelee(true);
        
        // On enl�ve l'accusation port�e sur le joueur
        joueurActuel.setJoueurEstAccuse(false);
        
        // Si c'�tait une soci�re
        if (joueurActuel.getRole() == Role.SORCIERE) {
            
            System.out.println(joueurActuel.getNom() + " est une soci�re !");

            joueurActuel.setPeutJouer(false); // Le joueur r�v�l� ne peut plus jouer
            
            // On d�finit le prochain joueur comme �tant le joueur � gauche du joueur actuel
            setProchainJoueurGauche();
            
        }        
        else { // Si c'�tait un villageois

            System.out.println(joueurActuel.getNom() + " est un villageois.");    
            
            // Le joueur choisit le prochain joueur
            Partie.getInstance().getTourActuel().setProchainJoueur(joueurActuel);
        }
        
    }
	
	/**
	 * Cette m�thode permet de d�finir le prochain joueur comme �tant le joueur � la gauche de celui qui vient de jouer.
	 */
	private void setProchainJoueurGauche() {
		
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel(); // Joueur qui joue actuellement
		ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>(Partie.getInstance().getJoueurs()); // Liste des joueurs
		Joueur JoueurAGauche; // Joueur � gauche du joueur actuel = Avant dans la liste des joueurs
		
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
		else if (positionJoueurActuel == 0) { // Si la position du joueur actuel est en premier dans la liste, le joueur � gauche est le dernier de la liste
			JoueurAGauche = listeJoueurs.get(listeJoueurs.size()-1);
			//Le joueur qui accuse rejoue, on le met donc en prochain joueur
			Partie.getInstance().getTourActuel().setProchainJoueur(JoueurAGauche); 
		}
		else {
			System.out.println("Erreur. Le joueur actuel n'a pas �t� trouv� dans la liste des joueurs");
		}
	}
	
	
	//
	// ~~~ Witch ? 
	// Joueur qui a accus� d�fausse une carte al�atoire de sa main
	// �tre le prochain joueur
	//
	// ~~~ Hunt !
	// R�v�ler son identit�
	//		� Witch : Joueur � sa gauche est le prochain joueur
	//		� Village : Choisir le prochain joueur
}
