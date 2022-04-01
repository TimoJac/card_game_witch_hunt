package view.vueTexte;

import model.joueur.Joueur;

/**
 * Classe repr�sentant l'interface textuelle de la fin de partie. Elle affiche seulement le vainqueur.
 * Elle est cr��e par la vue graphique associ�e {@link view.vueGraphique.VueGraphiqueFin}.
 * @author Mathilde COYEN
 * @author Timothee JACOB
 */
public class VueTexteFin {

	/**
	 * Constructeur de la classe, qui affiche � la console le gagnant de la partie
	 * @param joueur Gagnant de la partie
	 */
	public VueTexteFin(Joueur joueur) {
		System.out.println("Bravo ! " + joueur.getNom() + " a gagn� !");
	}
}
