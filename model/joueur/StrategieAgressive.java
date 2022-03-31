package model.joueur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import controller.ControllerPartie;
import model.Partie;
import model.cartes_rumeur.CarteRumeur;

/**
 * Cette classe étend {@code StrategieJoueurVirtuel} et contient les méthodes nécessaires pour les BOTs ayant la stratégie Agressive mise en place. 
 * 
 * @see StrategieJoueurVirtuel
 *
 */
public class StrategieAgressive implements StrategieJoueurVirtuel{
	
	@Override
	public void choisirAction() {
		
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel();
		boolean testPeutJouerCarte = false;
		int compteurCartes = 0;
		ArrayList<CarteRumeur> listeCartes = Partie.getInstance().getTourActuel().getJoueurActuel().getCartesRumeurJoueur();
			
		System.out.println("C'est au tour de " + joueurActuel.getNom());
		
		//On teste si le joueur peut jouer au moins une de ses cartes rumeurs
		while ((testPeutJouerCarte == false) && (compteurCartes < listeCartes.size()) ) {
			if (joueurActuel.peutJouerCarte(listeCartes.get(compteurCartes))) {
				testPeutJouerCarte = true;
			}
			compteurCartes++;
		}
							
		// Cas où le joueur actuel n'est pas accusé
		if (!joueurActuel.isJoueurEstAccuse()) {
			
			//Dans cette stratégie, le joueur accuse systématiquement
			Joueur.setJoueurQuiAccuse(joueurActuel);	
			joueurActuel.accuser(((JoueurVirtuel)joueurActuel).choisirQuiAccuser());
			
		}		
		// Cas où le joueur actuel est accusé
		else {
			
			// Si le joueur n'a plus de cartes rumeur ou qu'il ne peut en jouer aucune
			if((joueurActuel.getCartesRumeurJoueur().size() == 0) || (testPeutJouerCarte == false)) {
				System.out.println(joueurActuel.getNom() + " révèle sa carte identité");
				joueurActuel.revelerIdentite();
			}
			
			else {
				// S'il peut, le joueur choisit systématiquement une carte Witch pour se défendre dans cette stratégie
				choisirWitch();
			}
			
		}
	}

	@Override
	public void choisirWitch() {
		
		ArrayList<String> meilleuresCartes = new ArrayList<String> (Arrays.asList("Hooked Nose", "Pointed Hat", "Evil Eye", "Cauldron", "Pet Newt", "Broomstick", "Wart", "Toad", "Black Cat", "Angry Mob", "Ducking Stool", "The Inquisition"));
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel();
		ArrayList <CarteRumeur> cartesJoueur = new ArrayList<CarteRumeur> (joueurActuel.getCartesRumeurJoueur());
		CarteRumeur choixCarte = null;
		int compteur = 0;
		
		// Tant qu'on a pas trouvé la meilleure carte possible dans les cartes du joueur, on continue à chercher
		while ((choixCarte == null) && (compteur < Partie.getInstance().getCartesRumeur().size())) {
			// On regarde pour chaque carte du joueur si elle correspond à la meilleure carte possible à jouer
			for (int i = 0; i < cartesJoueur.size(); i++)  {
				// Si oui (et si le joueur peut jouer cette carte), on choisit cette carte
				if ((cartesJoueur.get(i).getNom() == meilleuresCartes.get(compteur) && (joueurActuel.peutJouerCarte(cartesJoueur.get(i))))) {
					choixCarte = cartesJoueur.get(i);
				}
			}
			// Sinon, on incrémente le compteur pour passer à une carte un peu moins bonne dans la liste "meilleuresCartes"
			compteur++;
		}
		
		//System.out.println("\n" + joueurActuel.getNom() + " joue le côté Witch de la carte "+ choixCarte.getNom() + "\n");

		// Jouer la carte
		// On donne à la méthode le nom de la carte à jouer
		ControllerPartie.utiliserCarte(choixCarte.getNom());

		// On enlève l'accusation pour le prochain Tour
		joueurActuel.setJoueurEstAccuse(false);
	}

	@Override
	public void choisirHunt() {
		
		Joueur joueurActuel = Partie.getInstance().getTourActuel().getJoueurActuel();
		ArrayList <CarteRumeur> cartesJoueur = new ArrayList<CarteRumeur> (joueurActuel.getCartesRumeurJoueur());
		CarteRumeur choixCarte = null;
		
		// On mélange le jeu (pour avoir de l'aléatoire) et on tire la première carte 
		Collections.shuffle(cartesJoueur);
		choixCarte = cartesJoueur.get(0);

		// Tant que le joueur ne peut pas jouer la carte, on passe le choix de la carte à celle d'après dans la liste des cartes Rumeur
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

		//System.out.println("\n" +  joueurActuel.getNom() + " joue le côté Hunt de la carte "+ choixCarte.getNom() + "\n");

		// Jouer la carte
		// On donne à la méthode le nom de la carte à jouer
		ControllerPartie.utiliserCarte(choixCarte.getNom());
		
	}
}
