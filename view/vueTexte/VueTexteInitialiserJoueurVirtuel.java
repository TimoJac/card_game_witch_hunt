package view.vueTexte;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import model.Methods;
import model.Partie;
import model.joueur.JoueurVirtuel;
import model.joueur.StrategieAgressive;
import model.joueur.StrategieRandom;

/**
 * Classe représentant l'interface textuelle demandant le nom d'un joueur virtuel.<br>
 * Elle est créée par la vue graphique associée {@link view.vueGraphique.VueGraphiqueInitJoueurVirtuel}.<br>
 * Cette classe est un observateur du {@link model.joueur.JoueurVirtuel} voulant être initialisé.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 */
public class VueTexteInitialiserJoueurVirtuel implements Observer, Runnable {
	
	/**
	 * Numéro de joueur réel dans la liste de tous les joueurs 
	 */
	private int numeroJoueur;
	
	/**
	 * Joueur réel a qui on est en train de demander le nom
	 */
	private JoueurVirtuel joueur;
	
	/**
	 * Thread de la {@code VueTexteInitialiserJoueurReel}<br>
	 * Utile pour mettre en concurrence la console et la vue graphique
	 */
	private Thread t;
	
	/**
	 * Constructeur de la classe<br>
	 * <ul>
	 * <li>Ajoute l'instance créée à la liste d'observers du {@link model.joueur.JoueurVirtuel joueur à initialiser} </li>
	 * <li>Démarre le thread de la {@link VueTexteInitialiserJoueurVirtuel} </li>
	 * </ul>
	 * @param joueur Joueur virtuel pour lequel on souhaite saisir la stratégie de jeu
	 */
	public VueTexteInitialiserJoueurVirtuel(JoueurVirtuel joueur) {
		
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
	 * On demande à l'utilisateur de choisir la stratégie du joueur, et on stocke ce résultat dans le modèle grâce à {@link model.joueur.JoueurVirtuel#setStrategieJoueurVirtuel(model.joueur.StrategieJoueurVirtuel)}. <br><br>
	 * Pour faire la saisie des données, on utilise {@link model.Methods#entrerEntier(int, int, String)} ce qui permet de ne pas blocker le bufferedReader si le thread est interrompu (i.e. si l'utilisateur a utilisé la vue graphique pour faire la saisie)
	 */
	@Override
	public void run() {
		
		boolean threadInterrompu = false;
		int choixStrategie = 0;
		
		try {			
			System.out.println("Choisissez la stratégie de jeu de " + joueur.getNom() +" \u001B[3m(entrez le numéro correspondant)\u001B[0m : ");
			System.out.println("\t- Stratégie Random : Entrez '1'");
			System.out.println("\t- Stratégie Agressive : Entrez '2'");
			
			
			choixStrategie = Methods.entrerEntier(1, 2, "");
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			threadInterrompu = true;
		}
		
		if (threadInterrompu == false) {
			if (choixStrategie == 1) {
				((JoueurVirtuel)joueur).setStrategieJoueurVirtuel(new StrategieRandom());
			}
			else if (choixStrategie == 2) {
				((JoueurVirtuel)joueur).setStrategieJoueurVirtuel(new StrategieAgressive());
			}
		}
		
	}

	/**
	 * Méthode héritée de la classe Observer, qui sert afficher les compte-rendus de notifications émises par les Observables.<br>
	 * S'execute à chaque fois que l'Observable {@link model.joueur.JoueurVirtuel} notifie les observateurs d'un changement.<br>
	 * Si la notification concerne la stratégie d'un joueur virtuel({@code "strategie"}) on affiche un rendu de la notification dans la console, et on arrete le thread de cette vue pour arreter de d'attendre de recevoir des inputs.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		StringBuffer sb = new StringBuffer ();
		
		if (o instanceof JoueurVirtuel) {
			if (arg == "strategie") {
				// On interromp le thread
				this.t.interrupt();
				
				sb.append("Joueur virtuel n°" + this.numeroJoueur);
				sb.append("\n - Nom : " + this.joueur.getNom());
				/*sb.append("\n - Role : ");
				if (this.joueur.getRole() == Role.SORCIERE) sb.append("Sorcière");
				else if (this.joueur.getRole() == Role.VILLAGEOIS) sb.append("Villageois");*/
				sb.append("\n - Strategie de jeu : ");
				if (this.joueur.getStrategieJoueurVirtuel() instanceof StrategieRandom) sb.append("Random");
				else if (this.joueur.getStrategieJoueurVirtuel() instanceof StrategieAgressive) sb.append("Agressive");
				sb.append("\n");

				System.out.println(sb.toString());
			}
		}
		
	}

}
