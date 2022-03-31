package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import model.joueur.Joueur;

/**
 * La classe du controller permettant aux joueurs réels de saisir leur nom.
 * 
 * @see view.vueGraphique.VueGraphiqueInitJoueurReel
 *
 */
public class ControllerInitialisationJoueurReel {
	
		/**
		 * L'attribut contenant le nom choisi par le joueur.
		 */
		private String nomJoueur; 

		/**
		 * Le constructeur de controller incluant un Action Listener sur le {@code boutonValider} permettant au joueur de choisir son nom.
		 * 
		 * @param joueur Le joueur ayant choisi le nom
		 * @param boutonValider le bouton pour valider le choix du nom
		 * @param champNom le champ contenant le nom du joueur
		 */
		public ControllerInitialisationJoueurReel(Joueur joueur, JButton boutonValider, JTextField champNom) {
			
			boutonValider.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					nomJoueur = champNom.getText();
					joueur.setNom(nomJoueur);
				}
			});
			
		}
}
