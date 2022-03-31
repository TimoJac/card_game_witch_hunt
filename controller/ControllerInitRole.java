package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import model.joueur.Joueur;
import model.joueur.JoueurReel;
import model.joueur.Role;

/**
 * La classe du controller permettant aux joueurs r�els de choisir leur r�le.
 * 
 * @see view.vueGraphique.VueGraphiqueInitRole
 *
 */
public class ControllerInitRole {
	
	/**
	 * L'attribut string correspondant au nom du r�le
	 */
	private String stringRole;
	
	/**
	 * Une instance de {@code Role} permettant de param�trer le r�le du joueur
	 */
	private Role role;

	/**
	 * Le contructeur du controller contenant un Action Listener afin de faire choisir son r�le au joueur r�el
	 * 
	 * @param joueur le joueur choisissant son r�le
	 * @param boutonValider le bouton permettant de valider le choix du r�le
	 * @param choixRole la JComboBox contenant le choix du r�le
	 */
	public ControllerInitRole(Joueur joueur, JButton boutonValider, JComboBox<String> choixRole) {
		
		boutonValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// joueur r�el
				if (joueur instanceof JoueurReel) {
					
					stringRole = (String) choixRole.getSelectedItem();
					
					if (stringRole == "Villageois") {
						role = Role.VILLAGEOIS;
					}
					else if (stringRole== "Sorci�re") {
						role = Role.SORCIERE;
					}
					
					if (role != null) {
						joueur.setRole(role);
					}
				}
				
				// Joueur virtuel
				else {
					joueur.choisirRole();
				}				
			}
		});
	}	
}
