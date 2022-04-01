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
 * Classe repr�sentant l'interface textuelle demandant le nom d'un joueur virtuel.<br>
 * Elle est cr��e par la vue graphique associ�e {@link view.vueGraphique.VueGraphiqueInitJoueurVirtuel}.<br>
 * Cette classe est un observateur du {@link model.joueur.JoueurVirtuel} voulant �tre initialis�.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 */
public class VueTexteInitialiserJoueurVirtuel implements Observer, Runnable {
	
	/**
	 * Num�ro de joueur r�el dans la liste de tous les joueurs 
	 */
	private int numeroJoueur;
	
	/**
	 * Joueur r�el a qui on est en train de demander le nom
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
	 * <li>Ajoute l'instance cr��e � la liste d'observers du {@link model.joueur.JoueurVirtuel joueur � initialiser} </li>
	 * <li>D�marre le thread de la {@link VueTexteInitialiserJoueurVirtuel} </li>
	 * </ul>
	 * @param joueur Joueur virtuel pour lequel on souhaite saisir la strat�gie de jeu
	 */
	public VueTexteInitialiserJoueurVirtuel(JoueurVirtuel joueur) {
		
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
	 * On demande � l'utilisateur de choisir la strat�gie du joueur, et on stocke ce r�sultat dans le mod�le gr�ce � {@link model.joueur.JoueurVirtuel#setStrategieJoueurVirtuel(model.joueur.StrategieJoueurVirtuel)}. <br><br>
	 * Pour faire la saisie des donn�es, on utilise {@link model.Methods#entrerEntier(int, int, String)} ce qui permet de ne pas blocker le bufferedReader si le thread est interrompu (i.e. si l'utilisateur a utilis� la vue graphique pour faire la saisie)
	 */
	@Override
	public void run() {
		
		boolean threadInterrompu = false;
		int choixStrategie = 0;
		
		try {			
			System.out.println("Choisissez la strat�gie de jeu de " + joueur.getNom() +" \u001B[3m(entrez le num�ro correspondant)\u001B[0m : ");
			System.out.println("\t- Strat�gie Random : Entrez '1'");
			System.out.println("\t- Strat�gie Agressive : Entrez '2'");
			
			
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
	 * M�thode h�rit�e de la classe Observer, qui sert afficher les compte-rendus de notifications �mises par les Observables.<br>
	 * S'execute � chaque fois que l'Observable {@link model.joueur.JoueurVirtuel} notifie les observateurs d'un changement.<br>
	 * Si la notification concerne la strat�gie d'un joueur virtuel({@code "strategie"}) on affiche un rendu de la notification dans la console, et on arrete le thread de cette vue pour arreter de d'attendre de recevoir des inputs.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		StringBuffer sb = new StringBuffer ();
		
		if (o instanceof JoueurVirtuel) {
			if (arg == "strategie") {
				// On interromp le thread
				this.t.interrupt();
				
				sb.append("Joueur virtuel n�" + this.numeroJoueur);
				sb.append("\n - Nom : " + this.joueur.getNom());
				/*sb.append("\n - Role : ");
				if (this.joueur.getRole() == Role.SORCIERE) sb.append("Sorci�re");
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
