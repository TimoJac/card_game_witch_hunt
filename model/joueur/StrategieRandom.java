package model.joueur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import controller.ControllerPartie;
import model.Partie;
import model.cartes_rumeur.CarteRumeur;

public class StrategieRandom implements StrategieJoueurVirtuel{
	
	@Override
	public void choisirAction() {
		
		int choixJoueur = new Random().nextInt(2);
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel();
		boolean testPeutJouerCarte = false;
		int compteurCartes = 0;
		ArrayList<CarteRumeur> listeCartes = Partie.getInstance().getTourActuel().getJoueurActuel().getCartesRumeurJoueur();
			
		System.out.println("C'est au tour de " + joueurActuel.getNom());
		
		//On teste si le joueur peut jouer au moins une de ses cartes rumeurs
		while ((!testPeutJouerCarte) && (compteurCartes < listeCartes.size()) ) {
			if (joueurActuel.peutJouerCarte(listeCartes.get(compteurCartes))) {
				testPeutJouerCarte = true;
			}
			compteurCartes++;
		}
							
		// Cas o� le joueur actuel n'est pas accus�
		if (!joueurActuel.isJoueurEstAccuse()) {
			
			// Si le joueur n'a plus de cartes rumeur ou qu'il ne peut en jouer aucune
			if((joueurActuel.getCartesRumeurJoueur().size() == 0) || (!testPeutJouerCarte)) {
				System.out.println("Vous n'avez plus de carte rumeur ou vous ne remplissez pas les conditions pour les jouer. Vous �tes donc oblig� d'accuser quelqu'un.");
				Joueur.setJoueurQuiAccuse(joueurActuel);	
				joueurActuel.accuser(((JoueurVirtuel)joueurActuel).choisirQuiAccuser());
			}
			
			else {
				if (choixJoueur == 0) {
					Joueur.setJoueurQuiAccuse(joueurActuel);	
					joueurActuel.accuser(((JoueurVirtuel)joueurActuel).choisirQuiAccuser());
				}
				else if (choixJoueur == 1) {
					choisirHunt();
				}
			}
			
		}
		
		// Cas o� le joueur actuel est accus�
		else {
			
			// Si le joueur n'a plus de cartes rumeur ou qu'il ne peut en jouer aucune
			if((joueurActuel.getCartesRumeurJoueur().size() == 0) || (!testPeutJouerCarte)) {
				System.out.println("Vous n'avez plus de carte rumeur ou vous ne remplissez pas les conditions pour les jouer. Vous �tes donc oblig� de r�v�ler votre identit�.");
				System.out.println(joueurActuel.getNom() + " r�v�le sa carte identit�");
				joueurActuel.revelerIdentite();
			}
			
			else {
				if (choixJoueur == 0) {
					System.out.println(joueurActuel.getNom() + " r�v�le sa carte identit�");
					joueurActuel.revelerIdentite();
				}
				else if (choixJoueur == 1) {
					choisirWitch();
				}
			}
			
		}
	}

	@Override
	public void choisirWitch() {
		
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel();
		ArrayList <CarteRumeur> cartesJoueur = new ArrayList<CarteRumeur> (joueurActuel.getCartesRumeurJoueur());
		CarteRumeur choixCarte = null;
		
		// On m�lange le jeu (pour avoir de l'al�atoire) et on tire la premi�re carte 
		Collections.shuffle(cartesJoueur);
		choixCarte = cartesJoueur.get(0);

		// Tant que le joueur ne peut pas jouer la carte, on passe le choix de la carte � celle d'apr�s dans la liste des cartes Rumeur
		int compteurCartes = 1;
		while ( (! joueurActuel.peutJouerCarte(choixCarte)) && (compteurCartes < cartesJoueur.size())) {
			choixCarte = cartesJoueur.get(compteurCartes);
			compteurCartes ++;
		}
		
		// Si le joueur ne peut jouer aucune carte
		if (compteurCartes > cartesJoueur.size()) {
			System.out.println("Erreur. Vous ne pouvez pas jouer vos cartes Rumeur car vous ne remplissez pas les conditions requises.\nVous devez donc accuser un joueur.");
			joueurActuel.accuser(((JoueurVirtuel)joueurActuel).choisirQuiAccuser());
		}
		// Si le joueur peut jouer une carte
		else {
			System.out.println("\n" + joueurActuel.getNom() + " joue le c�t� Witch de la carte "+ choixCarte.getNom() + "\n");

			// Jouer la carte
			// On donne � la m�thode le nom de la carte � jouer
			ControllerPartie.utiliserCarte(choixCarte.getNom());

			// On enl�ve l'accusation pour le prochain Tour
			joueurActuel.setJoueurEstAccuse(false);
		}

		
	}

	@Override
	public void choisirHunt() {
		
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel();
		ArrayList <CarteRumeur> cartesJoueur = new ArrayList<> (joueurActuel.getCartesRumeurJoueur());
		CarteRumeur choixCarte = null;
		
		// On m�lange le jeu (pour avoir de l'al�atoire) et on tire la premi�re carte 
		Collections.shuffle(cartesJoueur);
		choixCarte = cartesJoueur.get(0);

		// Tant que le joueur ne peut pas jouer la carte, on passe le choix de la carte � celle d'apr�s dans la liste des cartes Rumeur
		int compteurCartes = 1;
		while ( (! joueurActuel.peutJouerCarte(choixCarte)) && (compteurCartes < cartesJoueur.size())) {
			choixCarte = cartesJoueur.get(compteurCartes);
			compteurCartes ++;
		}
				
		// Si le joueur ne peut jouer aucune carte
		if (compteurCartes > cartesJoueur.size()) {
			System.out.println("Erreur. Vous ne pouvez pas jouer vos cartes Rumeur car vous ne remplissez pas les conditions requises.\nVous devez donc accuser un joueur.");
			joueurActuel.accuser(((JoueurVirtuel)joueurActuel).choisirQuiAccuser());
		}

		System.out.println("\n" +  joueurActuel.getNom() + " joue le c�t� Hunt de la carte "+ choixCarte.getNom() + "\n");

		// Jouer la carte
		// On donne � la m�thode le nom de la carte � jouer
		ControllerPartie.utiliserCarte(choixCarte.getNom());
		
	}

}
