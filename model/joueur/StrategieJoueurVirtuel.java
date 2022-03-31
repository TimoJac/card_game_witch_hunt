package model.joueur;

/**
 * Interface permettant d'impl�menter le patron de conception strat�gie, permettant au joueur vituel d'avoir le choix entre plusieurs strat�gies.
 * 
 *
 */
public interface StrategieJoueurVirtuel {
	
	/**
	 * Cette m�thode permet de faire choisir une action au bot. L'action choisie d�pendra de la strat�gie impl�ment�e pour chaque bot. 
	 * 
	 * @see StrategieAgressive#choisirAction()
	 * @see StrategieRandom#choisirAction()
	 */
	public void choisirAction();
	
	/**
	 * Cette m�thode permet de faire jouer une carte c�t� Witch au bot. La carte choisie d�pendra de la strat�gie impl�ment�e pour chaque bot. 
	 * 
	 * @see StrategieAgressive#choisirWitch()
	 * @see StrategieRandom#choisirWitch()
	 */
	public void choisirWitch();
	
	/**
	 * Cette m�thode permet de faire jouer une carte c�t� Hunt au bot. La carte choisie d�pendra de la strat�gie impl�ment�e pour chaque bot. 
	 * 
	 * @see StrategieAgressive#choisirHunt()
	 * @see StrategieRandom#choisirHunt()
	 */
	public void choisirHunt();
}
