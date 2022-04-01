package view.vueTexte;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import model.Methods;
import model.Partie;
import model.joueur.JoueurReel;

/**
 * Classe représentant l'interface textuelle demandant le nom d'un joueur réel.<br>
 * Elle est créée par la vue graphique associée {@link view.vueGraphique.VueGraphiqueInitJoueurReel}.<br>
 * Cette classe est un observateur du {@link model.joueur.JoueurReel} voulant être initialisé.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 */
public class VueTexteInitialiserJoueurReel implements Observer, Runnable {
	
	/**
	 * Numéro de joueur réel dans la liste de tous les joueurs 
	 */
	private int numeroJoueur;
	
	/**
	 * Joueur réel a qui on est en train de demander le nom
	 */
	private JoueurReel joueur;
	
	/**
	 * Nom du joueur saisi
	 */
	private String nom;
	
	/**
	 * Thread de la {@code VueTexteInitialiserJoueurReel}<br>
	 * Utile pour mettre en concurrence la console et la vue graphique
	 */
	private Thread t;

	/**
	 * Constructeur de la classe<br>
	 * <ul>
	 * <li>Ajoute l'instance créée à la liste d'observers du {@link model.joueur.JoueurReel} </li>
	 * <li>Démarre le thread de la {@link VueTexteInitialiserJoueurReel} </li>
	 * </ul>
	 * @param joueur Joueur réel pour lequel on souhaite saisir le nom
	 */
	public VueTexteInitialiserJoueurReel(JoueurReel joueur) {
		
		joueur.addObserver(this);
		
		this.joueur = joueur;
		this.numeroJoueur = Partie.getInstance().getJoueurs().indexOf(joueur) + 1;
		
		// Création du Thread pour la vue texte
		t = new Thread(this);
		t.setName("Thread vue texte init joueur " + numeroJoueur);
		t.start();
	}

	/**
	 * Méthode permettant d'effectuer l'action du thread {@link #t}<br>
	 * On demande à l'utilisateur d'entrer le nom du joueur, et on donne ce nom au joueur grâce à {@link model.joueur.JoueurReel#setNom(String)}. <br><br>
	 * Pour faire la saisie des données, on utilise {@link model.Methods#entrerEntier(int, int, String)} ce qui permet de ne pas blocker le bufferedReader si le thread est interrompu (i.e. si l'utilisateur a utilisé la vue graphique pour faire la saisie)
	 */
	@Override
	public void run() {
		
		boolean threadInterrompu = false;
		
		try {
			// Saisie du nom du joueur
			this.nom = Methods.entrerChaine("Entrez le nom du joueur " + numeroJoueur + " : ");
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			threadInterrompu = true;
		}
		
		if (threadInterrompu == false) {
			joueur.setNom(nom);
		}
	}

	/**
	 * Méthode héritée de la classe Observer, qui sert afficher les compte-rendus de notifications émises par les Observables.<br>
	 * S'execute à chaque fois que l'Observable {@link model.joueur.JoueurReel} notifie les observateurs d'un changement.<br>
	 * Si la notification concerne le nom d'un joueur ({@code "nom"})on affiche un rendu de la notification dans la console, et on arrete le thread de cette vue pour arreter de vouloir recevoir des inputs.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		StringBuffer sb = new StringBuffer ();
		
		if (o instanceof JoueurReel) {
			if (arg == "nom") {
				// On interromp le thread
				this.t.interrupt();
				
				sb.append("Joueur réel n°" + this.numeroJoueur);
				sb.append("\n - Nom : " + this.joueur.getNom());
				
				System.out.println(sb.toString());
			}
		}
		
	}
}
