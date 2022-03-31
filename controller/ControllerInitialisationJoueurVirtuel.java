package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import model.joueur.Joueur;
import model.joueur.JoueurVirtuel;
import model.joueur.Role;
import model.joueur.StrategieAgressive;
import model.joueur.StrategieJoueurVirtuel;
import model.joueur.StrategieRandom;

/**
 * Classe du controller permettant de choisir la strat�gie des bots.
 * 
 * @see view.vueGraphique.VueGraphiqueInitJoueurVirtuel
 *
 */
public class ControllerInitialisationJoueurVirtuel {
	
	/**
	 * L'attribut String contenant la chaine de caract�res correspondant � la strat�gie voulue
	 */
	private String stringStrategie;
	
	/**
	 * Cet attribut est une instance de {@code StrategieJoueurVirtuel} et permet de d�finir la strat�gie d'un bot
	 */
	private StrategieJoueurVirtuel strategie = null;
	
	/**
	 * Le constructeur du controller, contenant un ActionListener sur le {@code boutonValider} afin de choisir la strat�gie du joueur virtuel.
	 * 
	 * @param joueur le joueur virtuel sur qui appliquer la strat�gie
	 * @param boutonValider le bouton pour valider le choix de la strat�gie
	 * @param choixStrategie le choix de la strat�gie ({@code Model.Joueur.StrategieAgressive ou Model.Joueur.StrategieRandom})
	 */
	public ControllerInitialisationJoueurVirtuel(Joueur joueur, JButton boutonValider, JComboBox<String> choixStrategie ) {
		
		boutonValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				stringStrategie = (String) choixStrategie.getSelectedItem();
				if (stringStrategie == "Random") {
					strategie = new StrategieRandom();
				}
				else if (stringStrategie == "Agressive") {
					strategie = new StrategieAgressive();
				}
				
				if (strategie != null) {
					((JoueurVirtuel)joueur).setStrategieJoueurVirtuel(strategie);
				}
				
			}
		});
		
	}

}
