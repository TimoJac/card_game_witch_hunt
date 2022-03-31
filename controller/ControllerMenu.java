package controller;

import view.vueGraphique.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Partie;

/**
 * La classe du controller permettant au joueur de lancer une partie
 * 
 * @see view.vueGraphique.VueGraphiqueMenu
 */
public class ControllerMenu {
	
	/**
	 * Le JButton permettant de lancer la partie
	 */
	private JButton boutonStart;

	/**
	 * Le constructeur du controller contenant un Action Listener permettant de commencer la partie en cliquant sur le {@code boutonId}
	 * @param boutonId le bouton permettant de commencer la partie
	 */
	public ControllerMenu(JButton boutonId) {
		
		this.boutonStart = boutonId;
		
		boutonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Partie.getInstance().setPartieLancee(true);
				VueGraphiqueMenu.getInstance().getFrame().setVisible(false);
			}
		});
		
		
	}
}
