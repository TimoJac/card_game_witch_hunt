package view.vueTexte;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import model.Methods;
import model.Partie;


/**
 * Classe représentant l'interface textuelle du menu/début de la partie de WitchHunt<br>
 * Elle est créée par la vue graphique associée {@link view.vueGraphique.VueGraphiqueMenu}.<br>
 * Cette classe est un observateur de la {@link model.Partie Partie}.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 */
public class VueTexteMenu  implements Observer, Runnable {
	
	/**
	 * Thread de la {@code VueTexteMenu}<br>
	 * Utile pour mettre en concurrence la console et la vue graphique
	 */
	Thread t;

	/**
	 * Constructeur de la classe {@link VueTexteMenu}.<br>
	 * <ul>
	 * <li>Ajoute l'instance créée à la liste d'observers de la {@link model.Partie} </li>
	 * <li>Démarre le thread de la {@link VueTexteMenu} </li>
	 * </ul>
	 * 
	 * @see java.util.Observer
	 */
	public VueTexteMenu() {
		
		Partie.getInstance().addObserver(this);
		t = new Thread(this, "Thread vue texte Menu");
		t.start();
	}

	
	/**
	 * Méthode permettant d'effectuer l'action du thread {@link #t}<br>
	 * On affiche à l'utilisateur le texte de bienvenue dans le jeu, et on lui demande d'appuyer sur 'Entrer' pour passer à la suite.<br>
	 * S'il le fait, on lance la partie en faisant {@code Partie.getInstance().setPartieLancee(true)}
	 * @see model.Methods#pause()
	 * @see model.Partie#setPartieLancee(boolean)
	 */
	@Override
	public void run() {
		
		boolean threadInterrompu = false;
		
		System.out.println("-------------------------------------------------");
		System.out.println("---------- Bienvenue dans WitchHunt ! -----------");
		System.out.println("-------------------------------------------------\n");
		
		try {
			Methods.pause();
		} catch (InterruptedException e) {
			threadInterrompu = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (threadInterrompu == false) {
			Partie.getInstance().setPartieLancee(true);
		}
		
	}

	/**
	 * Méthode héritée de la classe Observer, qui sert afficher les compte-rendus de notifications émises par les Observables.
	 * S'execute à chaque fois que l'Observable {@link model.Partie} notifie les observateurs d'un changement.
	 * Si la notification concerne la partie lancee {@code "partieLancee"} on arrete le thread de cette vue pour arreter de vouloir recevoir des inputs.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Partie  && arg == "partieLancee") {
			
			t.interrupt();
		}
		
	}
}
