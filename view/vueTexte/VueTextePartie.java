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
 * Classe représentant l'interface textuelle principale du jeu WithHunt.
 * L'interface gère l'affichage d'un tour de jeu pour un joueur, qu'il soit réel ou virtuel, accusé ou non.
 * Les instances de cette classes sont des observateurs du joueur à initialiser et de toutes ses cartes rumeurs.
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
	 * Boolean servant à savoir si la méthode {@link #update(Observable, Object)} de cette classe a déjà été appelée.
	 * Utile pour désactiver l'action de la méthode {@link #update(Observable, Object)} lorsque la vue n'est plus visible.
	 */
	private boolean vueUpdate;
	
	/**
	 * Booléen qui est true si le thread actuel a été interrompu. Cela se produit lorsque l'utilisateur joue avec l'interface graphique.
	 */
	private boolean threadInterrompu;
	
	/**
	 * Constructeur de la {@link VueTextePartie}.<br>
	 * <ul>
	 * <li>Ajoute l'instance créée à la liste d'observers du joueur actuel et de toutes ses cartes rumeur </li>
	 * <li>Démarre le thread de la {@link VueTextePartie} </li>
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
	     
	     // Création du Thread pour la vue texte
	     t = new Thread(this);
	     t.setName("Thread vue texte partie");
	     t.start();
	}
	
	/**
	 * Méthode permettant d'effectuer l'action du thread {@link #t}<br>
	 * 
	 * <ul>
	 * <li>Si le joueur est accusé, on lui propose de jouer Witch ou de révéler son identité </li>
	 * <li>Sinon, on lui propose de jouer Hunt ou d'accuser un joueur</li>
	 * </ul>
	 * On appelle ensuite les méthodes adaptées correspondantes à son choix.<br><br>
	 * Pour faire la saisie des données, on utilise {@link model.Methods#entrerEntier(int, int, String)} ce qui permet de ne pas blocker le bufferedReader si le thread est interrompu (si l'utilisateur a utilisé la vue graphique)
	 */
	@Override
	public void run() {
		
		
		int choixAction = 0;
		
		// Si le joueur n'est pas accusé
		if (! joueurActuel.isJoueurEstAccuse()) {
			try {	
				System.out.println(joueurActuel.getNom() + ", c'est à votre tour ! Que voulez-vous faire ?");
				System.out.println("\t- Accuser un joueur : Entrez '1'");
				System.out.println("\t- Jouer une carte côté Hunt : Entrez '2'");
				
				
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
					System.out.println("Le joueur a choisi de jouer le côté Hunt de la carte "+ carteChoisie.getNom());
				}
			}
		}
		else {
			try {	
				System.out.println(joueurActuel.getNom() + ", vous avez été accusé(e) d'être une sorcière ! Que voulez-vous faire ?");
				System.out.println("\t- Réveler votre carte Identité : Entrez '1'");
				System.out.println("\t- Jouer une carte côté Witch : Entrez '2'");
				
				
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
					System.out.println("Le joueur a choisi de jouer le côté Witch de la carte "+ carteChoisie.getNom());
					
				}
			}
		}
	
	}
	
	/**
	 * Méthode appelée lorsque le joueur actuel a choisi d'accuser un autre joueur.<br>
	 * On lui demande quel joueur accuser parmi une liste passée en paramètre, et on accuse le joueur choisi grâce à {@link model.joueur.Joueur#accuser(Joueur)}
	 * @param listeJoueurs liste des joueurs pouvant être accusés
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
	 * Cette méthode est appelée lorsqu'un joueur a choisi de jouer l'effet d'une de ses cartes et que cet effet implique de choisir une carte.<br>
	 * On lui demande de choisir une carte parmi une liste passée en paramètre, et on retourne la carte choisie.
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
	 * Méthode héritée de la classe Observer, qui sert afficher les compte-rendus de notifications émises par les Observables.<br>
	 * S'execute à chaque fois que l'Observable {@link model.joueur.Joueur} ou {@link model.cartes_rumeur.CarteRumeur} notifie les observateurs d'un changement.<br>
	 * Selon le type de la notification (Object arg), on affiche un compte rendu textuel de la notification à la console. Puis on interromp le thread actuel afin d'arreter d'attendre des inputs.
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
                sb.append(" d'être une sorcière.\n");

                System.out.println(sb.toString());
            }
            // Si le joueur vient de jouer une carte rumeur
            if (o instanceof CarteRumeur) {
            	
                    System.out.println(joueurActuel + " a joué la carte " + CarteRumeur.getNomCarteJouee() + ".\n");

            }
            // Si le joueur vient de réveler son identité
            if (o instanceof Joueur && arg == "revelerIdentite") {

                if (joueurActuel.getRole() == Role.SORCIERE) {
                    sb.append("Bravo ! " + joueurActuel.getNom() + " était une sorcière !\n");
                    sb.append(Joueur.getJoueurQuiAccuse().getNom() + " gagne 1 point et peut rejouer.\n");
                }
                else {
                    sb.append("Eh non... " + joueurActuel.getNom() + " était un villageois.\n");
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

