package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JSpinner;

import model.Partie;

/**
 * La classe du controller permettant au joueur de définir combien de joueurs réels et virtuels il veut avoir dans sa partie.
 * 
 * @see view.vueGraphique.VueGraphiqueDebutPartie
 */
public class ControllerNbJoueurs {
	
	/**
	 * Les attributs permettant de stocker le nombre de joueur reels {@code nbJoueursReels}, et le nombre de joueurs virtuels {@code nbJoueursVirtuels}.
	 */
	private int nbJoueursReels, nbJoueursVirtuels;

	/**
	 * Le constructeur du controller contenant un Action Listener permettant au joueur de choisir le nombre de joueurs dans sa partie (réels ou virtuels).
	 * @param boutonValider le bouton pour valider son choix
	 * @param spinnerNbReel le JSpinner permettant de sélectionner le nombre de joueurs réels
	 * @param spinnerNbVirtuel le JSpinner permettant de sélectionner le nombre de joueurs virtuels
	 */
	public ControllerNbJoueurs (JButton boutonValider, JSpinner spinnerNbReel, JSpinner spinnerNbVirtuel) {
		
		boutonValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				nbJoueursReels = (int) spinnerNbReel.getValue();
				nbJoueursVirtuels = (int) spinnerNbVirtuel.getValue();
				
				if (nbJoueursReels + nbJoueursVirtuels < 3) {
					System.out.println("Pas assez de joueurs");
				}
				else if (nbJoueursReels + nbJoueursVirtuels > 6) {
					System.out.println("Trop de joueurs");
				}
				else {
					Partie.getInstance().setNbJoueursVirtuels(nbJoueursVirtuels);
					Partie.getInstance().setNbJoueursReels(nbJoueursReels);
				}
				
				System.out.println();
			}
		});
	}
	
}
