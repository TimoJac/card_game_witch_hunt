package view.vueGraphique;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;

import model.joueur.Joueur;
import view.vueTexte.VueTexteFin;

/**
 * Classe représentant l'interface graphique de la fin de la partie. Elle affiche le vainqueur
 * Elle crée également la vue texte associée {@code new VueTexteFin(joueur);}.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB

 * @see javax.swing
 *
 */
public class VueGraphiqueFin {

	/**
	 * Fenetre d'affichage graphique
	 * @see javax.swing.JFrame
	 */
	private JFrame frame;
	
	/**
	 * Nom du joueur gagnant
	 */
	private String gagnant;


	/**
	 * Constructeur de la classe.
	 * Initialise le {@link #gagnant}, initilise la fenetre et crée la vue texte associée
	 * @param joueur Joueur gagnant de la partie
	 * @see view.vueTexte.VueTexteFin
	 */
	public VueGraphiqueFin(Joueur joueur) {
		this.gagnant = joueur.getNom();
		
		initialize();
		frame.setVisible(true);
		
		new VueTexteFin(joueur);
	}

	/**
	 * Initialise le contenu de la fenetre graphique qui affiche le gagnant
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel finLabel = new JLabel("Bravo ! " + gagnant + " a gagné !");
		finLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(finLabel, BorderLayout.CENTER);
	}

}
