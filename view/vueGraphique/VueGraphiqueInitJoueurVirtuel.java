package view.vueGraphique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import controller.ControllerInitialisationJoueurVirtuel;
import model.Partie;
import model.joueur.Joueur;
import model.joueur.JoueurVirtuel;
import view.vueTexte.VueTexteInitialiserJoueurVirtuel;


/**
 * Classe repr�sentant l'interface graphique demandant la strat�gie de jeu d'un joueur virtuel pass� en param�tre.
 * Elle cr�e �galement la vue texte associ�e {@code new VueTexteInitialiserJoueurVirtuel();}.
 * Elle est �galement associ�e � un controller d�di� {@link controller.ControllerInitialisationJoueurVirtuel}.
 * Les objets de cette classes sont des observateurs du joueur � initialiser.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 * @see javax.swing
 *
 */
public class VueGraphiqueInitJoueurVirtuel extends JFrame implements Observer {
	
	/**
	 * Attribut statique serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Fenetre d'affichage graphique
	 * @see javax.swing.JFrame
	 */
	private JFrame frame;
	
	/**
	 * ComboBox pour choisir la strat�gie de jeu du joueur virtuel
	 */
	private JComboBox<String> comboBox;

	/**
	 * Bouton pour valider le choix de l'utilisateur
	 * @see controller.ControllerInitialisationJoueurVirtuel
	 */
	private JButton boutonValider;
	
	/**
	 * Joueur virtuel pour lequel on demande la strat�gie de jeu
	 */
	private JoueurVirtuel joueur;

	/**
	 * Num�ro de joueur virtuel dans la liste de tous les joueurs 
	 */
	private int numeroJoueur;
	
	/**
	 * Runnable : thread de l'interface graphique
	 */
	private Runnable t;

	
	/**
	 * Constructeur de la {@code VueGraphiqueInitJoueurVirtuel}.
	 * Permet de cr�er le thread de la classe, et d'ajouter cette classe � la liste d'observers du joueur � initialiser.
	 * @param joueur : Joueur virtuel pour lequel on veut choisir la strat�gie de jeu
	 * @see java.util.Observer
	 */
    public VueGraphiqueInitJoueurVirtuel(JoueurVirtuel joueur) {
    	
    	this.joueur = joueur;
    	this.numeroJoueur = Partie.getInstance().getJoueurs().indexOf(joueur) + 1;
    	
        // Ajout de cette classe en tant qu'Observer de joueur
        joueur.addObserver(this);
    	
    	// Cr�ation du thread qui cr�e l'interface graphique
    	EventQueue.invokeLater(t = creerThread());
	}

    /**
	 * M�thode permettant de cr�er le thread de la classe {@code VueGraphiqueInitJoueuVirtuel}.
	 * Contient la m�thode {@code run()} du thread.
	 * @return Le Runnable t instanci�
	 */
    private Runnable creerThread() {
		
		return new Runnable() {
			
			/**
			 * M�thode permettant d'executer le Thread.
			 * Initialise la fenetre graphique.
			 * Cr�e �galement la vue texte associ�e et le controller associ� � cette classe.
			 * @see view.vueTexte.VueTexteInitialiserJoueurVirtuel
			 * @see controller.controllerInitialisationJoueurVirtuel
			 */
			@Override
			public void run() {
				try {
					// Modifications de la fenetre 
			    	initialize();
			    	frame.setVisible(true);
			    	
			        // Ajout du controller
			       	new ControllerInitialisationJoueurVirtuel(joueur, boutonValider, comboBox);
			        
			        // Creation de la vue Texte
			    	new VueTexteInitialiserJoueurVirtuel(joueur);			    	
			    	
			    	// On donne un nom a ce thread
					Thread.currentThread().setName("Thread GUI initialiser joueur virtuel n�" + numeroJoueur);
				
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		};
	}

	/**
	 * Initialise le contenu de la fenetre d'interface graphique
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelTitre = new JPanel();
		frame.getContentPane().add(panelTitre, BorderLayout.NORTH);
		
		JLabel labelTitre = new JLabel("Joueur n\u00B0" + this.numeroJoueur + " (robot)");
		labelTitre.setForeground(Color.BLACK);
		labelTitre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelTitre.add(labelTitre);
		
		JPanel panelPrincipal = new JPanel();
		frame.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelNom = new JPanel();
		panelNom.setPreferredSize(new Dimension(450, 30));
		panelPrincipal.add(panelNom);
		
		JLabel labelNom = new JLabel("Nom du joueur : " + joueur.getNom());
		panelNom.add(labelNom);
		
		JPanel panelStrategie = new JPanel();
		panelPrincipal.add(panelStrategie);
		
		JLabel labelStrategie = new JLabel("Choisissez la strat\u00E9gie du joueur :");
		panelStrategie.add(labelStrategie);
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Random", "Agressive"}));
		comboBox.setPreferredSize(new Dimension(100,21));
		panelStrategie.add(comboBox);
		
		JPanel panelValider = new JPanel();
		frame.getContentPane().add(panelValider, BorderLayout.SOUTH);
		
		boutonValider = new JButton("Valider");
		panelValider.add(boutonValider);
	}

	
	/**
	 * M�thode h�rit�e de la classe Observer, qui sert � changer de vue.
	 * S'execute � chaque fois que l'Observable {@link model.joueur.JoueurVirtuel} notifie les observateurs d'un changement.
	 * Si la notification concerne le nom d'un joueur ({@code "nom"}) on rend la fenetre actuelle invisible et on appelle {@code Partie.getInstance().initialiserJoueurs();} pour initialiser un nouveau joueur.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		Joueur joueurSuivant;
		
		if (o instanceof JoueurVirtuel) {
			
			// Changement strat�gie joueur = initialisation joueur virtuel
			if (arg == "strategie") {
				
				// On ferme cette interface graphique
				frame.setVisible(false);
				
				// On appelle la fonction pour initialiser le prochain joueur
				Partie.getInstance().initialiserJoueurs();
			}
		}	
	}
}
