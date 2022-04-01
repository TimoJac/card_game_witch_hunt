package view.vueTexte;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import model.Methods;
import model.Partie;

/**
 * Classe représentant l'interface textuelle demandant le nombre de joueurs réels et virtuels<br>
 * Elle est créée par la vue graphique associée {@link view.vueGraphique.VueGraphiqueDebutPartie}.<br>
 * Cette classe est un observateur de la {@link model.Partie Partie}.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 */
public class VueTexteDebutPartie implements Observer, Runnable {
	
	/**
	 * Thread de la {@code VueTexteDebutPartie}<br>
	 * Utile pour mettre en concurrence la console et la vue graphique
	 */
	Thread t;

	/**
	 * Constructeur de la classe {@link VueTexteDebutPartie}.<br>
	 * <ul>
	 * <li>Ajoute l'instance créée à la liste d'observers de la {@link model.Partie} </li>
	 * <li>Démarre le thread de la {@link VueTexteDebutPartie} </li>
	 * </ul>
	 * 
	 * @see java.util.Observer
	 */
	public VueTexteDebutPartie() {
		
		Partie.getInstance().addObserver(this);
		t = new Thread(this, "Thread vue texte debut partie");
		t.start();
	}

	/**
	 * Méthode permettant d'effectuer l'action du thread {@link #t}<br>
	 * On demande à l'utilisateur d'entrer le nombre de joueurs réels et virtuels, et on met le résultat dans les attributs du model grâce à {@link model.Partie#setNbJoueursReels(int)} et {@link model.Partie#setNbJoueursVirtuels(int)}. <br><br>
	 * Pour faire la saisie des données, on utilise {@link model.Methods#entrerEntier(int, int, String)} ce qui permet de ne pas blocker le bufferedReader si le thread est interrompu (si l'utilisateur a utilisé la vue graphique)
	 */
	@Override
	public void run() {
		
		int nbJoueursReels = 0;
		int nbJoueursVirtuels = 0;
		boolean threadInterrompu = false;
		
		while (((nbJoueursReels + nbJoueursVirtuels) < 3) && ((nbJoueursReels + nbJoueursVirtuels) < 6) && (threadInterrompu == false)) {
			try {
				nbJoueursReels = Methods.entrerEntier(0, 6, "Entrez le nombre de joueurs réels (entre 0 et 6) :");
				if (nbJoueursReels < 6) {
					int minJoueursVirtuels = nbJoueursReels < 3 ? 3 - nbJoueursReels : 0;
					int maxJoueursVirtuels = 6 - nbJoueursReels;
					nbJoueursVirtuels = Methods.entrerEntier( minJoueursVirtuels, maxJoueursVirtuels, "Entrez le nombre de joueurs virtuels (entre " + minJoueursVirtuels + " et " + maxJoueursVirtuels + ") :");
				}				
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Erreur");
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				threadInterrompu = true;
			}
		}
		
		if (!threadInterrompu)  {
			Partie.getInstance().setNbJoueursVirtuels(nbJoueursVirtuels);
			Partie.getInstance().setNbJoueursReels(nbJoueursReels);
		}
	}

	/**
	 * Méthode héritée de la classe Observer, qui sert afficher les compte-rendus de notifications émises par les Observables.
	 * S'execute à chaque fois que l'Observable {@link model.Partie} notifie les observateurs d'un changement.
	 * Si la notification concerne le nombre de joueurs réels {@code "nbJoueursReels"} on affiche un rendu de la notification dans la console, et on arrete le thread de cette vue pour arreter de vouloir recevoir des inputs.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Partie && arg == "nbJoueursReels") {
			System.out.println("L'utilisateur a créé une partie pour " + Partie.getInstance().getNbJoueursReels() + " joueurs réels et " + Partie.getInstance().getNbJoueursVirtuels() + " joueurs virtuels.\n");
			t.interrupt();
	
		}
	}
	

	
}
