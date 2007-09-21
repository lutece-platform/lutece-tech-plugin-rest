package fr.paris.lutece.plugins.ods.utils;

import org.bouncycastle.jce.provider.JDKMessageDigest;
import org.bouncycastle.util.encoders.Hex;

import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * Classe utilitaire
 *
 */
public class ShaUtils {
    /**
     * Génération de la signature SHA 224 correspondant au tableau de byte passé en paramètre
     * @param input le tableau de byte à signer
     * @return Une chaîne de caractères représentant la signature
     */
	public static String toSha224String(byte[] input) {
		String strResult = null;
		try {
			JDKMessageDigest.SHA224 digest = new JDKMessageDigest.SHA224();
			digest.engineUpdate(input, 0, input.length);
			byte[] empreinte = Hex.encode(digest.engineDigest());
			strResult = new String(empreinte);
		} catch (Exception e) {
			AppLogService.error( e );
		}
		
		return strResult;
	}
	
    /**
     * Génération de la signature SHA 224 correspondant à la chaîne de caractères passée en paramètre
     * @param strInput la chaine à signer
     * @return Une chaîne de caractères représentant la signature
     */
	public static String toSha224String(String strInput) {
		String strResult = null;
		try {
			JDKMessageDigest.SHA224 digest = new JDKMessageDigest.SHA224();
			digest.engineUpdate(strInput.getBytes(), 0, strInput.getBytes().length);
			byte[] empreinte = Hex.encode(digest.engineDigest());
			strResult = new String(empreinte);
		} catch (Exception e) {
			AppLogService.error( e );
		}
		
		return strResult;
	}

}
