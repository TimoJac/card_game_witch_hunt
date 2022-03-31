package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import model.joueur.Joueur;
import model.joueur.JoueurReel;
import model.joueur.Role;

/**
 * La classe du controller permettant aux joueurs réels de choisir leur rôle.
 * 
 * @see view.vueGraphique.VueGraphiqueInitRole
 *
 */
public class ControllerInitRole {
	
	/**
	 * L'attribut string correspondant au nom du rôle
	 */
	private String stringRole;
	
	/**
	 * Une instance de {@code Role} permettant de paramétrer le rôle du joueur
	 */
	private Role role;

	/**
	 * Le contructeur du controller contenant un Action Listener afin de faire choisir son rôle au joueur réel
	 * 
	 * @param joueur le joueur choisissant son rôle
	 * @param boutonValider le bouton permettant de valider le choix du rôle
	 * @param choixRole la JComboBox contenant le choix du rôle
	 */
	public ControllerInitRole(Joueur joueur, JButton boutonValider, JComboBox<String> choixRole) {
		
		boutonValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// joueur réel
				if (joueur instanceof JoueurReel) {
					
					stringRole = (String) choixRole.getSelectedItem();
					
					if (stringRole == "Villageois") {
						role = Role.VILLAGEOIS;
					}
					else if (stringRole== "Sorcière") {
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
