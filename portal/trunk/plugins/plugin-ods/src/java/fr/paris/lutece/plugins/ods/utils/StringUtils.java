package fr.paris.lutece.plugins.ods.utils;


/**
 * Classe utilitaire
 *
 */
public class StringUtils {

    /* Compare deux chaine de caractère en considérant les suites de caractères numériques 
     * comme des nombres et non comme une chaîne de caractère ascii
    * @param chaine1 première chaîne à comparer
    * @param chaine2 deuxième chaîne à comparer
    * @return entier : < 0 si chaine1<chaine2
    *                   0 si les deux chaînes sont égales
    *                   > 0 si chaine2>chaine1
    */
	public static int compare(String chaine1, String chaine2) {
		int minSize = (chaine1.length() <= chaine2.length()) ? chaine1.length()
				: chaine2.length();

		int chaine1FirstIndex = 0;
		int chaine2FirstIndex = 0;

		int compare = 0;

		String chaine1Block = chaine1;
		String chaine2Block = chaine2;

		boolean stop = false;
		while (compare == 0 && !stop) {
			for (int i = chaine1FirstIndex; i < minSize; i++) {
				char currentChar = chaine1.charAt(i);
				if( i<(minSize-1) ) {
					char nextChar = chaine1.charAt(i+1);
					if( Character.getType(currentChar) != Character.getType(nextChar)) {
						chaine1Block = chaine1.substring(chaine1FirstIndex,i+1);
						chaine1FirstIndex = i+1;
						break;
					}
				} else {
					stop = true;
					chaine1Block = chaine1.substring(chaine1FirstIndex);
				}
			}

			for (int i = chaine2FirstIndex; i < minSize; i++) {
				char currentChar = chaine2.charAt(i);
				if( i<(minSize-1) ) {
					char nextChar = chaine2.charAt(i+1);
					if( Character.getType(currentChar) != Character.getType(nextChar)) {
						chaine2Block = chaine2.substring(chaine2FirstIndex,i+1);
						chaine2FirstIndex = i+1;
						break;
					}
				} else {
					stop = true;
					chaine2Block = chaine2.substring(chaine2FirstIndex);
				}
			}
			
			
			if( org.apache.commons.lang.StringUtils.isNumeric(chaine1Block) && org.apache.commons.lang.StringUtils.isNumeric(chaine2Block)) {
				compare = Integer.parseInt(chaine1Block) - Integer.parseInt(chaine2Block);
			} else {
				compare = chaine1Block.compareTo(chaine2Block);	
			}
			
		}
		return compare;
	}
}
