package view.vueTexte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import model.Methods;
import model.Partie;
import model.cartes_rumeur.CarteRumeur;
import model.joueur.Joueur;
import model.joueur.Role;


/**
 * Classe repr�sentant l'interface textuelle principale du jeu WithHunt.
 * L'interface g�re l'affichage d'un tour de jeu pour un joueur, qu'il soit r�el ou virtuel, accus� ou non.
 * Les instances de cette classes sont des observateurs du joueur � initialiser et de toutes ses cartes rumeurs.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 *
 */
public class VueTextePartie implements Runnable, Observer {

	/**
	 * Joueur dont c'est au tour de jouer
	 */
	private Joueur joueurActuel;
	
	/**
	 * Carte choisie par le joueur
	 */
	private CarteRumeur carteChoisie;
	
	/**
	 * Thread de la {@code VueTexteDebutPartie}<br>
	 * Utile pour mettre en concurrence la console et la vue graphique
	 */
	private Thread t;
	
	/**
	 * Boolean servant � savoir si la m�thode {@link #update(Observable, Object)} de cette classe a d�j� �t� appel�e.
	 * Utile pour d�sactiver l'action de la m�thode {@link #update(Observable, Object)} lorsque la vue n'est plus visible.
	 */
	private boolean vueUpdate;
	
	/**
	 * Bool�en qui est true si le thread actuel a �t� interrompu. Cela se produit lorsque l'utilisateur joue avec l'interface graphique.
	 */
	private boolean threadInterrompu;
	
	/**
	 * Constructeur de la {@link VueTextePartie}.<br>
	 * <ul>
	 * <li>Ajoute l'instance cr��e � la liste d'observers du joueur actuel et de toutes ses cartes rumeur </li>
	 * <li>D�marre le thread de la {@link VueTextePartie} </li>
	 * </ul>
	 * 
	 * @see java.util.Observer
	 * @see model.joueur.Joueur
	 * @see model.cartes_rumeur.CarteRumeur
	 */
	public VueTextePartie() {

		this.threadInterrompu = false;
		this.joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel();
		
		// Ajouts des observers
		joueurActuel.addObserver(this);
        for (int i = 0; i < joueurActuel.getCartesRumeurJoueur().size() ; i++) {
        	joueurActuel.getCartesRumeurJoueur().get(i).addObserver(this);
        }
		
		 this.joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel();
	     
	     // Cr�ation du Thread pour la vue texte
	     t = new Thread(this);
	     t.setName("Thread vue texte partie");
	     t.start();
	}
	
	/**
	 * M�thode permettant d'effectuer l'action du thread {@link #t}<br>
	 * 
	 * <ul>
	 * <li>Si le joueur est accus�, on lui propose de jouer Witch ou de r�v�ler son identit� </li>
	 * <li>Sinon, on lui propose de jouer Hunt ou d'accuser un joueur</li>
	 * </ul>
	 * On appelle ensuite les m�thodes adapt�es correspondantes � son choix.<br><br>
	 * Pour faire la saisie des donn�es, on utilise {@link model.Methods#entrerEntier(int, int, String)} ce qui permet de ne pas blocker le bufferedReader si le thread est interrompu (si l'utilisateur a utilis� la vue graphique)
	 */
	@Override
	public void run() {
		
		
		int choixAction = 0;
		
		// Si le joueur n'est pas accus�
		if (! joueurActuel.isJoueurEstAccuse()) {
			try {	
				System.out.println(joueurActuel.getNom() + ", c'est � votre tour ! Que voulez-vous faire ?");
				System.out.println("\t- Accuser un joueur : Entrez '1'");
				System.out.println("\t- Jouer une carte c�t� Hunt : Entrez '2'");
				
				
				choixAction = Methods.entrerEntier(1, 2, "");
			}
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				threadInterrompu = true;
			}
			
			if (threadInterrompu == false) {
				if (choixAction == 1) {
					//TODO Accuser
					// Appeler nouvelle vue texte
					
					ArrayList<Joueur> listeJoueursAAccuser = joueurActuel.getJoueursAAccuser();
					choisirJoueurAccuse(listeJoueursAAccuser);
				}
				else if (choixAction == 2) {
					carteChoisie = this.choisirCarte();
					System.out.println("Le joueur a choisi de jouer le c�t� Hunt de la carte "+ carteChoisie.getNom());
				}
			}
		}
		else {
			try {	
				System.out.println(joueurActuel.getNom() + ", vous avez �t� accus�(e) d'�tre une sorci�re ! Que voulez-vous faire ?");
				System.out.println("\t- R�veler votre carte Identit� : Entrez '1'");
				System.out.println("\t- Jouer une carte c�t� Witch : Entrez '2'");
				
				
				choixAction = Methods.entrerEntier(1, 2, "");
			}
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				threadInterrompu = true;
			}
			
			if (threadInterrompu == false) {
				if (choixAction == 1) {
					joueurActuel.revelerIdentite();	
				}
				else if (choixAction == 2) {
					carteChoisie = this.choisirCarte();
					System.out.println("Le joueur a choisi de jouer le c�t� Witch de la carte "+ carteChoisie.getNom());
					
				}
			}
		}
	
	}
	
	/**
	 * M�thode appel�e lorsque le joueur actuel a choisi d'accuser un autre joueur.<br>
	 * On lui demande quel joueur accuser parmi une liste pass�e en param�tre, et on accuse le joueur choisi gr�ce � {@link model.joueur.Joueur#accuser(Joueur)}
	 * @param listeJoueurs liste des joueurs pouvant �tre accus�s
	 */
	private void choisirJoueurAccuse(ArrayList<Joueur> listeJoueurs) {
		
		int numeroJoueur = 0;
		boolean threadInterrompu = false;
	
		try {	
			System.out.println("\nQuel joueur voulez-vous accuser ?\n");
			for (int i = 0; i < listeJoueurs.size(); i++) {
				System.out.println("\t- " + listeJoueurs.get(i).getNom() + " : Entrez '" + (i+1) + "'");
			}
			
			numeroJoueur = Methods.entrerEntier(1, listeJoueurs.size(), "");
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			threadInterrompu = true;
		}
		
		if (threadInterrompu == false) {
			// On accuse le joueur choisi
			joueurActuel.accuser(listeJoueurs.get(numeroJoueur-1));
		}
	}
	
	/** 
	 * Cette m�thode est appel�e lorsqu'un joueur a choisi de jouer l'effet d'une de ses cartes et que cet effet implique de choisir une carte.<br>
	 * On lui demande de choisir une carte parmi une liste pass�e en param�tre, et on retourne la carte choisie.
	 * @return la carte choisie par le joueur
	 */
	public CarteRumeur choisirCarte() {
		
		int numeroCarte = 0;
		
		ArrayList<CarteRumeur> cartesJoueur = joueurActuel.getCartesRumeurJoueur();
		
		System.out.println("Voici vos cartes rumeur :");
		for (int i = 0; i < cartesJoueur.size(); i++) {
			System.out.println(cartesJoueur.get(i));
		}
		
		System.out.println("Quelle carte voulez-vous jouer ?");
		
		for (int i = 0; i < cartesJoueur.size(); i++) {
			System.out.println("\t- " + cartesJoueur.get(i).getNom() + " : Entrez '" + (i+1)  + "'");
		}
		
		try {
			numeroCarte = Methods.entrerEntier(1, cartesJoueur.size(), "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			threadInterrompu = true;
		}
		
		return cartesJoueur.get(numeroCarte -1);
	}

	
	/**
	 * M�thode h�rit�e de la classe Observer, qui sert afficher les compte-rendus de notifications �mises par les Observables.<br>
	 * S'execute � chaque fois que l'Observable {@link model.joueur.Joueur} ou {@link model.cartes_rumeur.CarteRumeur} notifie les observateurs d'un changement.<br>
	 * Selon le type de la notification (Object arg), on affiche un compte rendu textuel de la notification � la console. Puis on interromp le thread actuel afin d'arreter d'attendre des inputs.
	 */
	@Override
    public void update(Observable o, Object arg) {

        StringBuffer sb = new StringBuffer ();

        if (vueUpdate == false) {

            vueUpdate = true;

            // On interromp le thread
            this.t.interrupt();

            // Si le joueur vient d'accuser quelqu'un
            if (o instanceof Joueur && arg == "accusation") {
                sb.append(joueurActuel);
                sb.append(" accuse ");
                sb.append(Partie.getInstance().getTourActuel().getProchainJoueur().getNom());
                sb.append(" d'�tre une sorci�re.\n");

                System.out.println(sb.toString());
            }
            // Si le joueur vient de jouer une carte rumeur
            if (o instanceof CarteRumeur) {
            	
                    System.out.println(joueurActuel + " a jou� la carte " + CarteRumeur.getNomCarteJouee() + ".\n");

            }
            // Si le joueur vient de r�veler son identit�
            if (o instanceof Joueur && arg == "revelerIdentite") {

                if (joueurActuel.getRole() == Role.SORCIERE) {
                    sb.append("Bravo ! " + joueurActuel.getNom() + " �tait une sorci�re !\n");
                    sb.append(Joueur.getJoueurQuiAccuse().getNom() + " gagne 1 point et peut rejouer.\n");
                }
                else {
                    sb.append("Eh non... " + joueurActuel.getNom() + " �tait un villageois.\n");
                    sb.append(Joueur.getJoueurQuiAccuse().getNom() + " ne gagne pas de point. " + joueurActuel.getNom() + " prend la main.\n");
                }
                System.out.println(sb.toString());
            }
            
            if (o instanceof Joueur && arg == "choixJoueur") {
                System.out.println(joueurActuel + " a choisi " + Partie.getInstance().getTourActuel().getProchainJoueur() + " comme prochain joueur.\n");
            }
        }
    }
}

