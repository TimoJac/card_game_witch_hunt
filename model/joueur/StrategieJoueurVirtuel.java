package model.joueur;

/**
 * Interface permettant d'implémenter le patron de conception stratégie, permettant au joueur vituel d'avoir le choix entre plusieurs stratégies.
 * 
 *
 */
public interface StrategieJoueurVirtuel {
	
	/**
	 * Cette méthode permet de faire choisir une action au bot. L'action choisie dépendra de la stratégie implémentée pour chaque bot. 
	 * 
	 * @see StrategieAgressive#choisirAction()
	 * @see StrategieRandom#choisirAction()
	 */
	public void choisirAction();
	
	/**
	 * Cette méthode permet de faire jouer une carte côté Witch au bot. La carte choisie dépendra de la stratégie implémentée pour chaque bot. 
	 * 
	 * @see StrategieAgressive#choisirWitch()
	 * @see StrategieRandom#choisirWitch()
	 */
	public void choisirWitch();
	
	/**
	 * Cette méthode permet de faire jouer une carte côté Hunt au bot. La carte choisie dépendra de la stratégie implémentée pour chaque bot. 
	 * 
	 * @see StrategieAgressive#choisirHunt()
	 * @see StrategieRandom#choisirHunt()
	 */
	public void choisirHunt();
}
