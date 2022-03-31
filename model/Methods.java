package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Classe contenant des m�thodes g�n�riques utilis�es tout au long du projet.
 * 
 */
public class Methods{

    //Ouverture du scanner
	/**
	 * Attribut scanner utilis� tout au long du projet permettant de lire les inputs console de l'utilisateur.
	 */
    public static Scanner sc = new Scanner(System.in);

    /**
     * M�thode vidant le buffer du Scanner en recr�ant une instance de scanner dans l'attribut {@code sc}.
     * Utilise pour �tre s�r de n'avoir que les derni�res valeurs entr�es par l'utilisateur lors de la lecture de saisie.
     */
    public static void viderBufferScanner(){
        sc = new Scanner(System.in);
    }
    
    /**
     * M�thode permettant d'attendre un input de l'utilisateur avant de continuer d'afficher la vue Console.
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public static void pause() throws InterruptedException, IOException {
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	if (!Thread.interrupted()) System.out.println("Appuyez sur la touche 'Entrer' pour continuer...");
    	
    	try {
    	    // On attend jusqu'� avoir de quoi completer une Readline()
    	    while (!br.ready()) {
    	    	Thread.sleep(2);
    	    }
    	    br.readLine();
    	       
    	} 
    	catch (InterruptedException e) {
    		
    		throw e;
    	} 
    }
    
    /**
     * M�thode g�n�rique permettant de demander � l'utilisateur de rentrer un nombre entier compris entre {@code min} et {@code max}, en affichant la consigne {@code message}.
     *  
     * @param min Valeur minimale autoris�e
     * @param max Valeur maximale autoris�e
     * @param message Message � afficher lors de la demande de saisie
     * @return la saisie de l'utilisateur
     * @throws IOException
     * @throws InterruptedException
     */
	public static int entrerEntier(int min, int max, String message) throws IOException, InterruptedException {
		
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String input;
    	int value = -1;
    	
    	if (!Thread.interrupted()) System.out.println(message);
    	
    	do {
    	    try {
    	        // On attend jusqu'� avoir de quoi completer une Readline()
    	        while (!br.ready()) {
    	        	Thread.sleep(2);
    	        }
    	        
    	        input = br.readLine();
    	        value = Integer.parseInt(input);
    	        if (value < min || value > max) {
    	        	System.out.println("Entrez un nombre entier entre " + min + " et " + max + ".");
    	        }
    	    } 
    	    catch (InterruptedException e) {
    	        throw e;
    	    } 
    	    catch (NumberFormatException e) {
    	    	System.out.println("Veuillez entrer un entier.");
    	    }
    	} while (value< min || value > max);
    	
    	return value;
    }
	
	/**
	 * M�thode g�n�rique permettant de demander � l'utilisateur de rentrer une cha�ne de caract�res en affichant la consigne {@code message}.
	 * 
	 * @param message Message � afficher lors de la demande de saisie
	 * @return la saisie de l'utilisateur
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String entrerChaine(String message) throws IOException, InterruptedException {
		
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String input;
    	
    	if (!Thread.interrupted()) System.out.println(message);
    	
    	do {
    	    try {
    	        // wait until we have data to complete a readLine()
    	        while (!br.ready()) {
    	        	Thread.sleep(2);
    	        }
    	        input = br.readLine();
    	    }
    	    
    	    catch (InterruptedException e) {
    	        throw e;
    	    }
    	    
    	} while (input == null || input == "");
    	
    	return input;
    }    
    	
}
