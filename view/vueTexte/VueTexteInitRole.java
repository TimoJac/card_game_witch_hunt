package view.vueTexte;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import model.Methods;
import model.TourDeJeu;
import model.joueur.Joueur;
import model.joueur.JoueurVirtuel;
import model.joueur.Role;


/**
 * Classe repr�sentant l'interface textuelle demandant le r�le d'un joueur pour un nouveau Tour de jeu.<br>
 * Elle est cr��e par la vue graphique associ�e {@link view.vueGraphique.VueGraphiqueInitRole}.<br>
 * Cette classe est un observateur du {@link model.joueur.Joueur} voulant �tre initialis�.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 */
public class VueTexteInitRole  implements Observer, Runnable{
	
	/**
	 * Joueur r�el a qui on est en train de demander le nom
	 */
	private Joueur joueur;
	
	/**
	 * Thread de la {@code VueTexteInitRole}<br>
	 * Utile pour mettre en concurrence la console et la vue graphique
	 */
	private Thread t;
	
	/**
	 * Boolean servant � savoir si la m�thode {@link #update(Observable, Object)} de cette classe a d�j� �t� appel�e.
	 * Utile pour d�sactiver l'action de la m�thode {@link #update(Observable, Object)} lorsque la vue n'est plus visible.
	 */
	private boolean vueUpdate;

	/**
	 * Constructeur de la classe<br>
	 * <ul>
	 * <li>Ajoute l'instance cr��e � la liste d'observers du {@link model.joueur.Joueur} </li>
	 * <li>D�marre le thread de la {@link VueTexteInitRole} </li>
	 * </ul>
	 * @param joueur Joueur pour lequel on souhaite choisir le r�le
	 */
	public VueTexteInitRole (Joueur joueur ) {
		
		this.joueur = joueur;
		this.vueUpdate = false;
		
		joueur.addObserver(this);
		
		// Cr�ation du Thread pour la vue texte
		t = new Thread(this);
		t.setName("Thread vue texte init role " + joueur.getNom());
		t.start();
	}

	/**
	 * M�thode permettant d'effectuer l'action du thread {@link #t}<br>
	 * 
	 * <ul>
	 * <li>Si le joueur est virtuel, on affiche un texte disant qu'il a choisi son r�le et on demande � l'utlisateur d'appuyer sur Entrer pour passer � la suite</li>
	 * <li>Si le joueur est r�el, on lui demande de choisir son r�le en entrant un entier associ�, puis on stocke le choix dans le Joueur gr�ce � {@link model.joueur.Joueur#setRole(Role)} </li>
	 * </ul>
	 * @see model.Methods#entrerEntier(int, int, String)
	 * @see model.Methods#pause()
	 */
	@Override
	public void run() {
		
		boolean threadInterrompu = false;
		
		// Joueur virtuel
		if (joueur instanceof JoueurVirtuel) {
			System.out.println("Le joueur " + joueur.getNom() + " a choisi son r�le pour le tour n� " + TourDeJeu.getTour() + " ! ");
			
			try {
				Methods.pause();
				joueur.choisirRole();
			} catch (InterruptedException e) {
				threadInterrompu = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Joueur r�el
		else {
			
			int choixRole = 0;
			
			try {			
				System.out.println(joueur.getNom() + ", choisissez votre role pour ce tour \u001B[3m(entrez le num�ro correspondant)\u001B[0m : ");
				System.out.println("\t- Villageois : Entrez '1'");
				System.out.println("\t- Sorci�re : Entrez '2'");
				
				
				choixRole = Methods.entrerEntier(1, 2, "");
			}
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				threadInterrompu = true;
			}
			
			if (threadInterrompu == false) {
				if (choixRole == 1) {
					joueur.setRole(Role.VILLAGEOIS);
				}
				else if (choixRole == 2) {
					joueur.setRole(Role.SORCIERE);
				}
			}
		}	
	}

	/**
	 * M�thode h�rit�e de la classe Observer, qui sert afficher les compte-rendus de notifications �mises par les Observables.<br>
	 * S'execute � chaque fois que l'Observable {@link model.joueur.Joueur} notifie l'observateur d'un changement.<br>
	 * Si la notification concerne le role d'un joueur ({@code "role"}) on affiche un rendu de la notification dans la console, et on arrete le thread de cette vue pour arreter de vouloir recevoir des inputs.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		if (o instanceof Joueur && arg == "role" && vueUpdate == false) {
			// On interromp le thread
			this.t.interrupt();
			
			vueUpdate = true;
				
			System.out.println(joueur + " a choisi son r�le pour le tour n� " + TourDeJeu.getTour() + " !");			
		}
	}
}
