package view.vueTexte;

import model.joueur.Joueur;

/**
 * Classe représentant l'interface textuelle de la fin de partie. Elle affiche seulement le vainqueur.
 * Elle est créée par la vue graphique associée {@link view.vueGraphique.VueGraphiqueFin}.
 * @author Mathilde COYEN
 * @author Timothee JACOB
 */
public class VueTexteFin {

	/**
	 * Constructeur de la classe, qui affiche à la console le gagnant de la partie
	 * @param joueur Gagnant de la partie
	 */
	public VueTexteFin(Joueur joueur) {
		System.out.println("Bravo ! " + joueur.getNom() + " a gagné !");
	}
}
