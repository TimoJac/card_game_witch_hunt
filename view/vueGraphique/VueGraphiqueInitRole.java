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

import controller.ControllerInitRole;
import model.Partie;
import model.TourDeJeu;
import model.joueur.Joueur;
import model.joueur.JoueurReel;
import model.joueur.JoueurVirtuel;
import view.vueTexte.VueTexteInitRole;


/**
 * Classe repr�sentant l'interface graphique g�rant le r�le du joueur pass� en argument.
 * <ul>
 * <li>Pour un joueur r�el, l'interface demande de choisir le r�le du joueur</li>
 * <li>Pour un joueur virtuel, l'interface dit que le bot vient de choisir son role</li>
 * </ul>
 * Elle cr�e �galement la vue texte associ�e {@code new VueTexteInitRole();}.
 * Elle est �galement associ�e � un controller d�di� {@link controller.ControllerInitRole}.
 * Les objets de cette classes sont des observateurs du joueur dont le r�le est a initialiser.
 * 
 * @author Mathilde COYEN
 * @author Timothee JACOB
 * 
 * @see java.util.Observer
 * @see javax.swing
 *
 */
public class VueGraphiqueInitRole extends JFrame implements Observer {
	
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
	 * Joueur dont on veut initialiser le role
	 */
	private Joueur joueur;
	
	/**
	 * Runnable : thread de l'interface graphique
	 */
	private Runnable t;
	
	/**
	 * Bouton pour valider le choix de l'utilisateur
	 */
	private JButton boutonValider;
	
	/**
	 * ComboBox pour choisir le role du joueur pour ce tour
	 */
	private JComboBox<String> comboBox;
	
	/**
	 * Boolean servant � savoir si la m�thode {@link #update(Observable, Object)} de cette classe a d�j� �t� appel�e.
	 * Utile pour d�sactiver l'action de la m�thode {@link #update(Observable, Object)} lorsque la vue n'est plus visible.
	 */
	private boolean vueUpdate;

	/**
	 * Constructeur de la {@code VueGraphiqueInitRole}.
	 * Permet de cr�er le thread de la classe, et d'ajouter cette classe � la liste d'observers du joueur � initialiser.
	 * @param joueur : Joueur pour lequel on veut choisir le role
	 * @see java.util.Observer
	 */
	public VueGraphiqueInitRole(Joueur joueur) {
		
		this.joueur = joueur;
		this.vueUpdate = false;
		
        // Ajout de cette classe en tant qu'Observer de joueur
        joueur.addObserver(this);
        
        // Cr�ation du thread qui cr�e l'interface graphique
    	EventQueue.invokeLater(t = creerThread());
	}

	/**
	 * M�thode permettant de cr�er le thread de la classe {@code VueGraphiqueInitRole}.
	 * Contient la m�thode {@code run()} du thread.
	 * @return Le Runnable t instanci�
	 */
	private Runnable creerThread() {
		return new Runnable() {
			
			/**
			 * M�thode permettant d'executer le Thread.
			 * Initialise la fenetre graphique selon le joueur a initialiser :
			 * <ul>
			 * <li>{@code initializeJoueurReel();} pour un joueur r�el</li>
			 * <li>{@code initializeJoueurVirtuel();} pour un joueur virtuel</li>
			 * </ul>
			 * Cr�e �galement la vue texte associ�e et le controller associ� � cette classe.
			 * @see view.vueTexte.VueTexteInitRole
			 * @see controller.controllerInitRole
			 */
			@Override
			public void run() {
				try {
					// Modifications de la fenetre 
			    	if (joueur instanceof JoueurReel) {
			    		initializeJoueurReel();
			    	}
			    	else if (joueur instanceof JoueurVirtuel) {
			    		initializeJoueurVirtuel();
			    	}
			    	frame.setVisible(true);
			    	
			        // Ajout du controller
			       	new ControllerInitRole(joueur, boutonValider, comboBox);
			        
			        // Creation de la vue Texte
			    	new VueTexteInitRole(joueur);			    	
			    	
			    	// On donne un nom a ce thread
					Thread.currentThread().setName("Thread GUI init Role " + joueur.getNom());
				
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		};
	}

	/**
	 * Initialise la fenetre pour un joueur r�el
	 */
	private void initializeJoueurReel() {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 450, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelTitre = new JPanel();
		this.frame.getContentPane().add(panelTitre, BorderLayout.NORTH);
		
		JLabel labelTitre = new JLabel(joueur.getNom() + ", choisissez votre r�le pour le tour n� " + TourDeJeu.getTour() +" !");
		labelTitre.setForeground(Color.BLACK);
		labelTitre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelTitre.add(labelTitre);
		
		JPanel panelPrincipal = new JPanel();
		this.frame.getContentPane().add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelRole = new JPanel();
		panelPrincipal.add(panelRole);
		
		JLabel labelRole = new JLabel("R�le :");
		panelRole.add(labelRole);
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Villageois", "Sorci�re"}));
		comboBox.setPreferredSize(new Dimension(100,21));
		panelRole.add(comboBox);
		
		JPanel panelValider = new JPanel();
		frame.getContentPane().add(panelValider, BorderLayout.SOUTH);
		
		boutonValider = new JButton("Valider");
		panelValider.add(boutonValider);
	}
	
	/**
	 * Initialise la fenetre pour un joueur virtuel
	 */
	private void initializeJoueurVirtuel() {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 450, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelTitre = new JPanel();
		this.frame.getContentPane().add(panelTitre, BorderLayout.NORTH);
		
		JLabel labelTitre = new JLabel(joueur.getNom() + " a choisi son r�le pour le tour n� " + TourDeJeu.getTour() +" !");
		labelTitre.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelTitre.add(labelTitre);
	
		JPanel panelValider = new JPanel();
		this.frame.getContentPane().add(panelValider, BorderLayout.SOUTH);
		
		boutonValider = new JButton("Valider");
		panelValider.add(boutonValider);
	}

	/**
	 * M�thode h�rit�e de la classe Observer qui sert � changer de vue.
	 * S'execute � chaque fois que l'Observable {@link model.joueur.Joueur} notifie les observateurs d'un changement consernant le r�le d'un joueur.
	 * Si la notification concerne le r�le d'un joueur ({@code "role"}) on rend la fenetre actuelle invisible et on appelle la m�thode {@code Partie.getInstance().gererPartie();} qui va d�cider de la prochaine action.
	 * @see java.util.Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		Joueur joueurSuivant;
		
		if (o instanceof Joueur && vueUpdate == false) {
			
			// Changement role joueur = initialisation d'un role de joueur
			if (arg == "role") {
				
				vueUpdate = true;
				
				// On ferme cette interface graphique
				frame.setVisible(false);
				
				// On appelle la fonction qui va d�cider quoi faire ensuite
				Partie.getInstance().gererPartie();
			}
		}	
	}		
}
