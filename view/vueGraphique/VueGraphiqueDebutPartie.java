package view.vueGraphique;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import controller.ControllerNbJoueurs;
import model.Partie;
import model.joueur.Joueur;
import model.joueur.JoueurReel;
import model.joueur.JoueurVirtuel;
import view.vueTexte.VueTexteDebutPartie;

import java.util.Observable;
import java.util.Observer;

/**
 * Classe repr�sentant l'interface graphique demandant le nombre de joueurs r�els et virtuels
 * Elle cr�e �galement la vue texte associ�e {@code new VueTexteDebutPartie();}.
 * Elle est �galement associ�e � un controller d�di� {@link controller.ControllerNbJoueurs}.
 * Cette classe est un observateur de la {@link model.Partie Partie}.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 * @see javax.swing
 *
 */
public class VueGraphiqueDebutPartie extends JFrame implements Observer{
	
	/**
	 * Attribut statique serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instance priv�e de {@code VueVueGraphiqueDebutPartie}, utile pour le patron de conception Singleton
	 */
	private static VueGraphiqueDebutPartie instance = null;

	/**
	 * Fenetre d'affichage graphique
	 * @see javax.swing.JFrame
	 */
	private JFrame mainFrame;

	/**
	 * Spinner permettant � l'utilisateur de choisir le nombre de joueurs r�els
	 * @see controller.ControllerNbJoueurs
	 */
	private JSpinner spinnerNbReel;
	/**
	 * Spinner permettant � l'utilisateur de choisir le nombre de joueurs virtuels
	 * @see controller.ControllerNbJoueurs
	 */
	private JSpinner spinnerNbVirtuel;
	/**
	 * Bouton permettant � l'utilisateur de valider son choix et de passer � la vue suivante
	 * @see controller.ControllerNbJoueurs
	 */
	private JButton boutonValider;
	
	/**
	 * Runnable : thread de l'interface graphique
	 */
	private static Runnable t;

	/**
	 * Cette m�thode fait partie du patron de conception Singleton.
	 * Elle cr�e une instance de {@code VueGraphiqueDebutPartie} si celle-ci est inexistante, et retourne l'instance dans tous les cas.
	 * 
	 * @return L'instance de {@code VueGraphiqueDebutPartie}
	 * 
	 * @see #VueGraphiqueDebutPartie()
	 * @see #instance
	 */
	public static VueGraphiqueDebutPartie getInstance() {
		// Patron de conception Singleton
		
		synchronized (VueGraphiqueDebutPartie.class) {
			if (instance == null) {
				instance = new VueGraphiqueDebutPartie();
			}
		}
		
		return instance;
	}
	
	/**
	 * Constructeur priv� de {@code VueGraphiqueDebutPartie}.
	 * Cr�e la vue texte associ�e, ajoute la classe en tant qu'observer de Partie, lance le thread, cr�e le controller associ� et initilise la fenetre.
	 * 
	 * @see controller.ControllerNbJoueurs
	 * @see view.vueTexte.VueTexteDebutPartie
	 * @see #creerThread()
	 */
	private VueGraphiqueDebutPartie() {
		
		// Cr�ation du mod�le
		Partie.getInstance();
		
		// Initialisation de la fen�tre
		initialize();
		
		// Cr�ation de la console - Texte
		new VueTexteDebutPartie();
		
		// Cr�ation du thread qui cr�e l'interface graphique
		EventQueue.invokeLater(t = creerThread());
		
		// Ajout de cet classe comme Observateur de la classe Partie
		Partie.getInstance().addObserver(this);
		
		//Controller pour le d�but de la partie
		new ControllerNbJoueurs(boutonValider, spinnerNbReel, spinnerNbVirtuel);	
	}
	
	/**
	 * M�thode permettant de cr�er le thread de la classe {@code VueGraphiqueDebutPartie}
	 * Contient la m�thode {@code run()} du thread
	 * @return Le Runnable t instanci�
	 */
	private static Runnable creerThread() {

		return new Runnable() {
			
			/**
			 * M�thode permettant d'executer le Thread
			 * Rend la fenetre visible
			 */
			@Override
			public void run() {
				try {
					getInstance().mainFrame.setVisible(true);
					Thread.currentThread().setName("Main Thread");
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		};
	}

	/**
	 * Initialise the contenu de la fenetre
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("WitchHunt");
		mainFrame.setBounds(100, 100, 450, 300);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		mainFrame.getContentPane().add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelNbJoueursReels = new JPanel();
		panel.add(panelNbJoueursReels);
		panelNbJoueursReels.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel labelNbJoueursReels = new JLabel("Nombre de joueurs r\u00E9els");
		panelNbJoueursReels.add(labelNbJoueursReels);
		
		spinnerNbReel = new JSpinner();
		spinnerNbReel.setModel(new SpinnerNumberModel(0, 0, 6, 1));
		panelNbJoueursReels.add(spinnerNbReel);
		
		JPanel panelNbJoueursVirtuels = new JPanel();
		panel.add(panelNbJoueursVirtuels);
		
		JLabel labelNbJoueursVirtuels = new JLabel("Nombre de joueurs virtuels");
		panelNbJoueursVirtuels.add(labelNbJoueursVirtuels);
		
		spinnerNbVirtuel = new JSpinner();
		spinnerNbVirtuel.setModel(new SpinnerNumberModel(0, 0, 6, 1));
		panelNbJoueursVirtuels.add(spinnerNbVirtuel);
		
		JPanel panelTitre = new JPanel();
		mainFrame.getContentPane().add(panelTitre, BorderLayout.NORTH);
		
		JLabel labelTitre = new JLabel("Cr\u00E9ation de la partie");
		panelTitre.add(labelTitre);
		
		JPanel panelValider = new JPanel();
		mainFrame.getContentPane().add(panelValider, BorderLayout.SOUTH);
		
		boutonValider = new JButton("Valider la s\u00E9lection");
		panelValider.add(boutonValider);
	}

	/**
	 * M�thode h�rit�e de la classe Observer, qui sert � changer de vue.
	 * S'execute � chaque fois que l'Observable {@link model.Partie} notifie les observateurs d'un changement.
	 * Si la notification concerne le nombre de joueurs r�els {@code "nbJoueursReels"} on rend la fenetre actuelle invisible et on appelle {@code Partie.getInstance().initialiserJoueurs();} pour initialiser les joueurs.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		if (o instanceof Partie) {
			if (arg == "nbJoueursReels") {
				
				// On ferme cette interface graphique
				mainFrame.setVisible(false);
				
				// On cherche le prochain joueur devant �tre initialis�
				Partie.getInstance().initialiserJoueurs();
			}
		}
	}
}
