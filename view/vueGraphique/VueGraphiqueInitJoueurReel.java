package view.vueGraphique;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.ControllerInitialisationJoueurReel;
import model.Partie;
import model.joueur.JoueurReel;
import view.vueTexte.VueTexteInitialiserJoueurReel;
import java.awt.Font;


/**
 * Classe représentant l'interface graphique demandant le nom d'un joueur réel passé en paramètre.
 * Elle crée également la vue texte associée {@code new VueTexteInitialiserJoueurReel();}.
 * Elle est également associée à un controller dédié {@link controller.ControllerInitialisationJoueurReel}.
 * Les objets de cette classes sont des observateurs du joueur à initialiser.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 * @see javax.swing
 *
 */
public class VueGraphiqueInitJoueurReel extends JFrame implements Observer {
	
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
	 * Champ de saisie de texte pour le nom de joueur choisi par l'utilisateur
	 * @see controller.ControllerInitialisationJoueurReel
	 */
	private JTextField textField;
	
	/**
	 * Bouton pour valider le choix de l'utilisateur.
	 * @see controller.ControllerInitialisationJoueurReel
	 */
	private JButton boutonValider;
	
	/**
	 * Joueur réel a qui on est en train de demander le nom
	 */
	private JoueurReel joueur;
	
	/**
	 * Numéro de joueur réel dans la liste de tous les joueurs 
	 */
	private int numeroJoueur;
	
	/**
	 * Runnable : thread de l'interface graphique
	 */
	private Runnable t;

	/**
	 * Constructeur de la {@code VueGraphiqueInitJoueurReel}.
	 * Permet de créer le thread de la classe, et d'ajouter cette classe à la liste d'observers du joueur à initialiser.
	 * @param joueur : Joueur réel à qui l'on souhaite demander le nom.
	 * @see java.util.Observer
	 */
    public VueGraphiqueInitJoueurReel(JoueurReel joueur) {
    	
    	this.joueur = joueur;
    	this.numeroJoueur = Partie.getInstance().getJoueurs().indexOf(joueur) + 1;
    	
        // Ajout de cette classe en tant qu'Observer de joueur
        joueur.addObserver(this);
    	
    	// Création du thread qui crée l'interface graphique
    	EventQueue.invokeLater(t = creerThread());
	}
    
    /**
	 * Méthode permettant de créer le thread de la classe {@code VueGraphiqueInitJoueurReel}.
	 * Contient la méthode {@code run()} du thread.
	 * @return Le Runnable t instancié
	 */
	private Runnable creerThread() {
		
		return new Runnable() {
			
			/**
			 * Méthode permettant d'executer le Thread.
			 * Initialise la fenetre graphique.
			 * Crée également la vue texte associée et le controller associé à cette classe.
			 * @see view.vueTexte.VueTexteInitialiserJoueurReel
			 * @see controller.controllerInitialisationJoueurReel
			 */
			@Override
			public void run() {
				try {
					// Modifications de la fenetre 
			    	initialize();
			    	frame.setVisible(true);
			    	
			        // Ajout du controller
			       	new ControllerInitialisationJoueurReel(joueur, boutonValider, textField);
			        
			        // Creation de la vue Texte
			    	new VueTexteInitialiserJoueurReel(joueur);			    	
			    	
			    	// On donne un nom a ce thread
					Thread.currentThread().setName("Thread GUI initialiser joueur réel n°" + numeroJoueur);
				
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		};
	}
    
    
	/**
	 * Initialise la fenetre de l'interface graphique
	 */
    private void initialize() {
    	
    	frame = new JFrame();
		frame.setTitle("WitchHunt");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
    	
    	JPanel panelTitre = new JPanel();
		frame.getContentPane().add(panelTitre, BorderLayout.NORTH);
		
		JLabel labelTitre = new JLabel("Joueur n\u00B0" + this.numeroJoueur + " (réel)");
		labelTitre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelTitre.add(labelTitre);
		
		JPanel panelPrincipal = new JPanel();
		frame.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelNom = new JPanel();
		panelNom.setPreferredSize(new Dimension(450, 30));
		panelPrincipal.add(panelNom);
		
		JLabel labelNom = new JLabel("Entrez le nom du joueur");
		panelNom.add(labelNom);
		
		textField = new JTextField();
		textField.setColumns(10);
		panelNom.add(textField);
		
		JPanel panelValider = new JPanel();
		frame.getContentPane().add(panelValider, BorderLayout.SOUTH);
		
		boutonValider = new JButton("Valider");
		panelValider.add(boutonValider);
		
        frame.setVisible(true);
    }
    
	/**
	 * Méthode héritée de la classe Observer, qui sert à changer de vue.
	 * S'execute à chaque fois que l'Observable {@link model.joueur.JoueurReel} notifie les observateurs d'un changement.
	 * Si la notification concerne le nom d'un joueur ({@code "nom"}) on rend la fenetre actuelle invisible et on appelle {@code Partie.getInstance().initialiserJoueurs();} pour initialiser un nouveau joueur.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		// Changement de nom de joueur = initialisation joueur réel
		if (o instanceof JoueurReel && arg == "nom") {
						
			// On ferme cette interface graphique
			frame.setVisible(false);
			
			// On appelle la fonction pour initialiser le prochain joueur
			Partie.getInstance().initialiserJoueurs();
		}
	}

}
