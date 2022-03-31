package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import model.Partie;
import model.cartes_rumeur.CarteRumeur;
import model.joueur.Joueur;
import model.joueur.JoueurVirtuel;
import model.joueur.Role;
import view.vueGraphique.VueGraphiquePartie;

/**
 * Classe du controller permettant de gérer toute la partie
 * 
 * @see view.vueGraphique.VueGraphiquePartie
 */
public class ControllerPartie {
	
	/**
	 * JComboBox permettant de choisir un joueur (seulement affichée quand une carte le demande)
	 */
	private JComboBox<String> choixJoueur;
	
	/**
	 * JComboBox permettant de choisir une carte (seulement affichée quand une carte le demande)
	 */
	private JComboBox<String> choixCarte;
	
	/**
	 * Une instance statique de joueur, permettant de connaître le joueur dont c'est le tour
	 */
	private static Joueur joueur;
	
	/**
	 * Une ArrayList contenant les joueurs pouvant être accusés
	 */
	private ArrayList<Joueur> listeJoueursAAccuser;
	
	/**
	 * Une ArrayList contenant tous les joueurs sauf le joueur actuel
	 */
	private ArrayList<Joueur> listeAutresJoueurs;
	
	/**
	 * Une ArrayList contenant la liste des cartes dans la main du joueur actuel
	 */
	private static ArrayList<CarteRumeur> listeCartesJoueur;
	
	/**
	 * Une ArrayList contenant la liste des cartes révélées du joueur actuel
	 */
	private static ArrayList<CarteRumeur> listeCartesReveleesJoueur;
	
	/**
	 * Une ArrayList contenant la liste des cartes du joueur qui a accusé le joueur actuel
	 */
	private static ArrayList<CarteRumeur> listeCartesJoueurQuiAccuse;
	
	/**
	 * Une liste des noms des joueurs pouvant être accusés
	 */
	private String[] nomsJoueursAAccuser;
	
	/**
	 * Une liste des noms de tous les joueurs sauf du joueur actuel
	 */
	private static String[] nomsAutresJoueurs;
	
	/**
	 * Une liste du nom des cartes dans la main du joueur actuel
	 */
	private static String[] nomsCartes;
	
	/**
	 * Une liste du nom des cartes révélées du joueur actuel
	 */
	private static String[] nomsCartesRevelees;
	
	/**
	 * Une liste du nom des cartes du joueur qui a accusé le joueur actuel
	 */
	private static String[] nomsCartesJoueurQuiAccuse;
	
	/**
	 * Le nom de la carte jouée par le joueur actuel
	 */
	private String nomCarteJouee;
	
	/**
	 * Un boolean permettant de savoir si le joueur a accusé quelqu'un
	 */
	private boolean joueurAccuse;
	
	/**
	 * Le JButton permettant de révéler son identité
	 */
	private JButton boutonIdentite;
	
	
	/**
	 * Le premier constructeur du controller, implémente un ActionListener permettant au joueur d'accuser quelqu'un.
	 * 
	 * @param boutonIdentite le bouton sur lequel appuyer pour accuser quelqu'un
	 * @param boutonChoix le bouton pour valider son choix
	 * @param choixJoueur la JComboBox permettant de choisir le joueur à accuser
	 */
	public ControllerPartie(JButton boutonIdentite, JButton boutonChoix, JComboBox<String> choixJoueur) {
		
		this.joueur = Partie.getInstance().getTourActuel().getJoueurActuel();
		this.choixJoueur = choixJoueur;
		this.joueurAccuse = false;

		boutonIdentite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				// Si le joueur n'est pas accusé et qu'il ne joue pas de carte, le bouton sert à accuser quelqu'un
				if (!joueur.isJoueurEstAccuse()) {
					
					boutonIdentite.setEnabled(false);
					SwingUtilities.updateComponentTreeUI(Partie.getInstance().getVuePartie().getFrame());
					
					listeJoueursAAccuser = joueur.getJoueursAAccuser();
					nomsJoueursAAccuser = new String[listeJoueursAAccuser.size()];
					
					
					for (int i = 0; i < listeJoueursAAccuser.size(); i++) {
						nomsJoueursAAccuser[i] = listeJoueursAAccuser.get(i).getNom();
					}
					
					joueurAccuse = true;
					
					Partie.getInstance().getVuePartie().afficheJoueursAccuse(nomsJoueursAAccuser, "Quel joueur voulez vous accuser ?");
				}
			}
		});
		
		boutonChoix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				
				// Le joueur est en train d'accuser quelqu'un, on déclenche le code de l'event listener pour accuser le joueur
				if (joueurAccuse == true) {
					accuserJoueur();
				}
			}
		});
	}
	
	//Controller pour révéler son identité
	/**
	 * Deuxième constructeur du controller, implémentant un Action Listener permettant de révéler son identité.
	 * @param boutonId le bouton pour révéler son identité.
	 */
	public ControllerPartie(JButton boutonId) {
			
		this.boutonIdentite = boutonId;
		this.joueur = Partie.getInstance().getTourActuel().getJoueurActuel();
			
		boutonIdentite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				joueur.revelerIdentite();
			}
		});
	}
	
	// Controller pour les joueurs virtuels
	/**
	 * Le troisième constructeur du controller, implémente un Action Listener permettant de valider à chaque action d'un bot avant de passer à la suivante, et permet à un joueur virtuel de choisir une action.
	 * @param boutonId le bouton de validation
	 * @param joueur le joueur virtuel
	 */
	public ControllerPartie(JButton boutonId, Joueur joueur) {
        
        boutonId.setVisible(true);
        VueGraphiquePartie.getPanelCartes().setVisible(false);
        VueGraphiquePartie.getBoutonIdentite().setVisible(false);
        this.boutonIdentite = boutonId;
        this.joueur = joueur;
        
        boutonIdentite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (joueur instanceof JoueurVirtuel) {
                    ((JoueurVirtuel) joueur).getStrategieJoueurVirtuel().choisirAction();
                }
            }
        });
}
	
	//Controller pour choisir une carte
	/**
	 * Le quatrième constructeur du controller, implémente un Action Listener pour que le joueur puisse choisir une carte à jouer, en fonction de s'il doit jouer un côté witch ou hunt.
	 * 
	 * @param boutonChoix Le bouton permettant de valider son choix
	 * @param choixJoueur Une JComboBox permettant de choisir un joueur si la carte le demande
	 * @param choixCarte Une JComboBox permettant de choisir une carte si la carte le demande
	 * @param boutonsCartes la liste des cartes du joueur, sous la forme de boutons
	 */
	public ControllerPartie(JButton boutonChoix, JComboBox<String> choixJoueur, JComboBox<String> choixCarte, ArrayList<JButton>boutonsCartes) {
		
		this.joueur = Partie.getInstance().getTourActuel().getJoueurActuel();	
		this.choixJoueur = choixJoueur;
		this.choixCarte = choixCarte;
		
		// Liste des autres joueurs
		this.listeAutresJoueurs = new ArrayList<Joueur>(Partie.getInstance().getJoueurs());
		this.listeAutresJoueurs.remove(Partie.getInstance().getTourActuel().getJoueurActuel());
		
		nomsAutresJoueurs = new String[listeAutresJoueurs.size()];
		for (int i = 0; i < listeAutresJoueurs.size(); i++) {
			nomsAutresJoueurs[i] = listeAutresJoueurs.get(i).getNom();
		}
		
		// Cartes du joueur
		this.listeCartesJoueur = new ArrayList<CarteRumeur> (this.joueur.getCartesRumeurJoueur());
		
		// Cartes revelees du joueur
		this.listeCartesReveleesJoueur = new ArrayList<CarteRumeur> (this.joueur.getCartesReveleeJoueur());
		
		// Liste des cartes du joueur qui accuse
		if (joueur.isJoueurEstAccuse()) {
			this.listeCartesJoueurQuiAccuse = joueur.getJoueurQuiAccuse().getCartesRumeurJoueur();
		}
		
		for (int i = 0; i < boutonsCartes.size(); i++) {
			JButton boutonActuel = boutonsCartes.get(i);
			
			boutonActuel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					nomCarteJouee = boutonActuel.getText();
					CarteRumeur.setNomCarteJouee(nomCarteJouee);
					
					utiliserCarte(nomCarteJouee);
				}
				
			});
			
		}
			
			boutonChoix.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					Joueur prochainJoueur;

						// Le joueur est en train de choisir quelqu'un ou une carte, on déclenche le code de l'event listener pour faire l'action qui correspond
						// Si le joueur est accusé, on appelle la méthode witch des cartes
						if (joueur.isJoueurEstAccuse()) {
							Partie.getInstance().getTourActuel().getJoueurActuel().setJoueurEstAccuse(false);
							
							switch (nomCarteJouee) {
							
							case "Ducking Stool" :
								prochainJoueur = choisirJoueur();
								Partie.getInstance().getDuckingStool().executerCoteWitch(prochainJoueur);
								break;
							
			
							
							case "Hooked Nose" :
								
								System.out.println(choixCarte.getSelectedItem());
								int positionCarteJoueurQuiAccuse = Arrays.asList(nomsCartesJoueurQuiAccuse).indexOf(choixCarte.getSelectedItem());
								CarteRumeur carteChoisieJoueurQuiAccuse = listeCartesJoueurQuiAccuse.get(positionCarteJoueurQuiAccuse);
								
								Partie.getInstance().getHookedNose().executerCoteWitch(joueur, carteChoisieJoueurQuiAccuse);
							
								break;
							
							case "Pointed Hat" :
								
								System.out.println(choixCarte.getSelectedItem());
								int positionCarteRevelee = Arrays.asList(nomsCartes).indexOf(choixCarte.getSelectedItem());
								CarteRumeur carteReveleeChoisie = listeCartesJoueur.get(positionCarteRevelee);
								
								Partie.getInstance().getPointedHat().executerCoteWitch(joueur, carteReveleeChoisie);
								
								break;
							
							case "The Inquisition" :
								
								System.out.println(choixCarte.getSelectedItem());
								int positionCarte = Arrays.asList(nomsCartes).indexOf(choixCarte.getSelectedItem());
								CarteRumeur carteChoisie = listeCartesJoueur.get(positionCarte);
								
								Partie.getInstance().getTheInquisition().executerCoteWitch(joueur, carteChoisie);
								
								break;
								
							default : break;
							
							}
						}
						
						// Si le joueur n'est pas accusé, on appelle la méthode hunt des cartes
						else if (!joueur.isJoueurEstAccuse()) {
							
							switch (nomCarteJouee) {
								case "Angry Mob" :
	                                prochainJoueur = choisirJoueur();
	                                Partie.getInstance().getAngryMob().executerCoteHunt(prochainJoueur);
	                                break;
								
								case "Broomstick" :
									prochainJoueur = choisirJoueur();
									Partie.getInstance().getBroomstick().executerCoteHunt(prochainJoueur);
									
									break;
								
								case "Cauldron" :
                                    prochainJoueur = choisirJoueur();
                                    Partie.getInstance().getCauldron().executerCoteHunt(prochainJoueur);
                                    break;
								
								case "Toad" :
									prochainJoueur = choisirJoueur();
                                    Partie.getInstance().getToad().executerCoteHunt(prochainJoueur);
                                    break;
									
								
								case "Wart" : 
									prochainJoueur = choisirJoueur();
                                    Partie.getInstance().getWart().executerCoteHunt(prochainJoueur);
									
									break;
									
								default : break;
							}
						}
					}
			});
	}
	

	/**
	 * Une méthode permettant d'accuser un joueur
	 */
	private void accuserJoueur() {
		
		int positionJoueurAAccuser = Arrays.asList(nomsJoueursAAccuser).indexOf(choixJoueur.getSelectedItem());
		Joueur joueurAAccuser = listeJoueursAAccuser.get(positionJoueurAAccuser);
		
		joueur.accuser(joueurAAccuser);
	
	}
	
	/**
	 * Une méthode permettant de choisir un joueur
	 * @return
	 */
	private Joueur choisirJoueur() {
		
		int positionJoueurChoisi = Arrays.asList(nomsAutresJoueurs).indexOf(choixJoueur.getSelectedItem());
		Joueur joueurChoisi = listeAutresJoueurs.get(positionJoueurChoisi);
		
		return joueurChoisi;
	}
	
	/**
	 * Une méthode permettant au programme de savoir quelle carte a été choisie et d'exécuter l'effet de la carte.
	 * @param nomCarteJouee le nom de la carte choisie, sous forme de String
	 */
	public static void utiliserCarte(String nomCarteJouee) {
		// Si le joueur est accusé, on appelle la méthode witch des cartes
		if (joueur.isJoueurEstAccuse()) {
			
			switch (nomCarteJouee) {
			
			case "Angry Mob" :
				Partie.getInstance().getAngryMob().revelerCarteRumeur();
                Partie.getInstance().getAngryMob().executerCoteWitch(joueur);
				break;
			
			case "Black Cat" :
				Partie.getInstance().getBlackCat().revelerCarteRumeur();
				Partie.getInstance().getBlackCat().executerCoteWitch(joueur);
				break;
			
			case "Broomstick" :
				Partie.getInstance().getBroomstick().revelerCarteRumeur();
                Partie.getInstance().getBroomstick().executerCoteWitch(joueur);
				break;
			
			case "Cauldron" :
                Partie.getInstance().getCauldron().revelerCarteRumeur();
                Partie.getInstance().getCauldron().executerCoteWitch(joueur);
                break;
			
			case "Ducking Stool" :
				Partie.getInstance().getDuckingStool().revelerCarteRumeur();
				Partie.getInstance().getVuePartie().afficheJoueursEffet(nomsAutresJoueurs, "Quel joueur voulez vous choisir ?");
				break;
		
			case "Hooked Nose" :
				Partie.getInstance().getHookedNose().revelerCarteRumeur();

				// On convertit l'arrayList en String[] pour l'affichage dans la comboBox
				nomsCartesJoueurQuiAccuse = new String[listeCartesJoueurQuiAccuse.size()];
				for (int i = 0; i < listeCartesJoueurQuiAccuse.size(); i++) {
					nomsCartesJoueurQuiAccuse[i] = listeCartesJoueurQuiAccuse.get(i).getNom();
				}
				Partie.getInstance().getVuePartie().afficheCartes(nomsCartesJoueurQuiAccuse, "Quelle carte voulez-vous prendre ?");
				
				break;
			
			case "Pet Newt" :
				Partie.getInstance().getPetNewt().revelerCarteRumeur();
				Partie.getInstance().getPetNewt().executerCoteWitch(joueur);
				break;
			
			case "Pointed Hat" :
				Partie.getInstance().getPointedHat().revelerCarteRumeur();
				
				// On convertit l'arrayList en String[] pour l'affichage dans la comboBox
				nomsCartesRevelees = new String[listeCartesReveleesJoueur.size()];
				for (int i = 0; i < listeCartesReveleesJoueur.size(); i++) {
					nomsCartesRevelees[i] = listeCartesReveleesJoueur.get(i).getNom();
				}
				Partie.getInstance().getVuePartie().afficheCartes(nomsCartesRevelees, "Quelle carte voulez-vous ajouter à votre main ?");
				
				break;
			
			case "The Inquisition" :
				
				Partie.getInstance().getTheInquisition().revelerCarteRumeur();
				
				// On enlève TheInquisition des cartes révélées
				listeCartesJoueur.remove(Partie.getInstance().getTheInquisition());
				
				// On convertit l'arrayList en String[] pour l'affichage dans la comboBox
				nomsCartesRevelees = new String[listeCartesReveleesJoueur.size()];
				for (int i = 0; i < listeCartesReveleesJoueur.size(); i++) {
					nomsCartesRevelees[i] = listeCartesReveleesJoueur.get(i).getNom();
				}
				Partie.getInstance().getVuePartie().afficheCartes(nomsCartesRevelees, "Laquelle de vos cartes révélées voulez-vous ajouter à votre main ?");
				
				break;
			
			case "Toad" :
				Partie.getInstance().getToad().revelerCarteRumeur();
				Partie.getInstance().getToad().executerCoteWitch(joueur);
				break;
			
			case "Wart" :
				Partie.getInstance().getWart().revelerCarteRumeur();
				Partie.getInstance().getWart().executerCoteWitch(joueur);
				break;
				
			default : break;
			
			}
		}
		
		// Si le joueur n'est pas accusé, on appelle la méthode hunt des cartes
		else if (!joueur.isJoueurEstAccuse()) {
			
			switch (nomCarteJouee) {
				case "Angry Mob" :
                    Partie.getInstance().getAngryMob().revelerCarteRumeur();
                    Partie.getInstance().getVuePartie().afficheJoueursEffet(nomsAutresJoueurs, "Quel joueur voulez vous choisir ?");
                    break;

				
				case "Broomstick" :
					Partie.getInstance().getBroomstick().revelerCarteRumeur();
					Partie.getInstance().getVuePartie().afficheJoueursEffet(nomsAutresJoueurs, "Quel joueur voulez vous choisir ?");
					
					break;
					
				case "Cauldron" :
                    Partie.getInstance().getCauldron().revelerCarteRumeur();
                    if (joueur.getRole() == Role.SORCIERE) {
                        Partie.getInstance().getCauldron().executerCoteHunt(joueur);
                    } else {
                        Partie.getInstance().getVuePartie().afficheJoueursEffet(nomsAutresJoueurs, "Quel joueur voulez vous choisir ?");
                    }
                    break;
				
				case "Toad" :
					 Partie.getInstance().getToad().revelerCarteRumeur();
                     if (joueur.getRole() == Role.SORCIERE) {
                         Partie.getInstance().getToad().executerCoteHunt(joueur);
                     } else {
                    	 Partie.getInstance().getVuePartie().afficheJoueursEffet(nomsAutresJoueurs, "Quel joueur voulez vous choisir ?");
                     }
                     break;
				
				case "Wart" : 
					Partie.getInstance().getWart().revelerCarteRumeur();
					Partie.getInstance().getVuePartie().afficheJoueursEffet(nomsAutresJoueurs, "Quel joueur voulez vous choisir ?");
					
					break;
				
				default : break;
			}
		}
	}
}
