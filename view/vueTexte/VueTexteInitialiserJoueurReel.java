package view.vueTexte;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import model.Methods;
import model.Partie;
import model.joueur.JoueurReel;

/**
 * Classe repr�sentant l'interface textuelle demandant le nom d'un joueur r�el.<br>
 * Elle est cr��e par la vue graphique associ�e {@link view.vueGraphique.VueGraphiqueInitJoueurReel}.<br>
 * Cette classe est un observateur du {@link model.joueur.JoueurReel} voulant �tre initialis�.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 */
public class VueTexteInitialiserJoueurReel implements Observer, Runnable {
	
	/**
	 * Num�ro de joueur r�el dans la liste de tous les joueurs 
	 */
	private int numeroJoueur;
	
	/**
	 * Joueur r�el a qui on est en train de demander le nom
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
	 * <li>Ajoute l'instance cr��e � la liste d'observers du {@link model.joueur.JoueurReel} </li>
	 * <li>D�marre le thread de la {@link VueTexteInitialiserJoueurReel} </li>
	 * </ul>
	 * @param joueur Joueur r�el pour lequel on souhaite saisir le nom
	 */
	public VueTexteInitialiserJoueurReel(JoueurReel joueur) {
		
		joueur.addObserver(this);
		
		this.joueur = joueur;
		this.numeroJoueur = Partie.getInstance().getJoueurs().indexOf(joueur) + 1;
		
		// Cr�ation du Thread pour la vue texte
		t = new Thread(this);
		t.setName("Thread vue texte init joueur " + numeroJoueur);
		t.start();
	}

	/**
	 * M�thode permettant d'effectuer l'action du thread {@link #t}<br>
	 * On demande � l'utilisateur d'entrer le nom du joueur, et on donne ce nom au joueur gr�ce � {@link model.joueur.JoueurReel#setNom(String)}. <br><br>
	 * Pour faire la saisie des donn�es, on utilise {@link model.Methods#entrerEntier(int, int, String)} ce qui permet de ne pas blocker le bufferedReader si le thread est interrompu (i.e. si l'utilisateur a utilis� la vue graphique pour faire la saisie)
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
	 * M�thode h�rit�e de la classe Observer, qui sert afficher les compte-rendus de notifications �mises par les Observables.<br>
	 * S'execute � chaque fois que l'Observable {@link model.joueur.JoueurReel} notifie les observateurs d'un changement.<br>
	 * Si la notification concerne le nom d'un joueur ({@code "nom"})on affiche un rendu de la notification dans la console, et on arrete le thread de cette vue pour arreter de vouloir recevoir des inputs.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		StringBuffer sb = new StringBuffer ();
		
		if (o instanceof JoueurReel) {
			if (arg == "nom") {
				// On interromp le thread
				this.t.interrupt();
				
				sb.append("Joueur r�el n�" + this.numeroJoueur);
				sb.append("\n - Nom : " + this.joueur.getNom());
				
				System.out.println(sb.toString());
			}
		}
		
	}
}
