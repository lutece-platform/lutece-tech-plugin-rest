/*
 * Copyright (c) 2002-2007, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.ods.service.horodatage;

import java.util.List;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.utils.ShaUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * 
 * HorodatageUtils
 * 
 */
public class HorodatageUtils {
	private static final String CONSTANTE_SEPARATEUR_SIGNATURE = "-";

	/**
	 * Retourne la liste des signatures numériques SHA224 des fichiers (les
	 * signatures sont séparées par le caractère '-')
	 * 
	 * @param pdd
	 *            PDD
	 * @param plugin
	 *            le plugin actif
	 * @return La liste des signatures
	 */
	public static String getSignatures(PDD pdd, Plugin plugin) {
		StringBuffer signatures = new StringBuffer();

		List<Fichier> fichiers = getFichiers(pdd, plugin);

		int lFichiers = (fichiers != null) ? fichiers.size() : 0;

		for (int i = 0; i < lFichiers; i++) {
			Fichier fichier = fichiers.get(i);
			FichierPhysique fichierPhysique = FichierPhysiqueHome
					.findByPrimaryKey(fichier.getFichier().getIdFichier(),
							plugin);

			if (fichierPhysique != null) {
				byte[] donnees = fichierPhysique.getDonnees();
				String signature;

				signature = ShaUtils.toSha224String(donnees);

				if (signature == null) {
					signature = "error_" + fichier.getNom();
					AppLogService.error("Erreur lors de la recuperation de la signature SHA 224 du fichier : " + fichier.getNom());
				}

				signatures.append(signature);
				signatures.append(CONSTANTE_SEPARATEUR_SIGNATURE);
			}
		}

		return signatures.toString();
	}

	/**
	 * Retourne la liste des signatures numériques SHA224 des fichiers (les
	 * signatures sont séparées par le caractère '-')
	 * 
	 * @param voeuAmendement
	 *            VoeuAmendement
	 * @param plugin
	 *            le plugin actif
	 * @return La liste des signatures
	 */
	public static String getSignature(VoeuAmendement voeuAmendement,
			Plugin plugin) {
		String signature = new String();

		Fichier fichier = getFichiers(voeuAmendement, plugin);

		if (fichier != null) {
			FichierPhysique fichierPhysique = FichierPhysiqueHome
			.findByPrimaryKey(fichier.getFichier().getIdFichier(),
					plugin);

			if (fichierPhysique != null) {
				byte[] donnees = fichierPhysique.getDonnees();

				signature = ShaUtils.toSha224String(donnees);
				if (signature == null) {
					signature = "error_" + fichier.getNom();
					AppLogService.error("Erreur lors de la recuperation de la signature SHA 224 du fichier : " + fichier.getNom());
				}
			}
		}

		return signature;
	}

	/**
	 * Retourne la signature numériques SHA224 du fichier
	 * 
	 * @param fichier
	 *            Fichier
	 * @param plugin
	 *            le plugin actif
	 * @return la signature
	 */
	public static String getSignature(Fichier fichier, Plugin plugin) {
		String signature = new String();

		FichierPhysique fichierPhysique = FichierPhysiqueHome
		.findByPrimaryKey(fichier.getFichier().getIdFichier(),
				plugin);

		if (fichierPhysique != null) {
			byte[] donnees = fichierPhysique.getDonnees();
			signature = ShaUtils.toSha224String(donnees);
			if (signature == null) {
				signature = "error_" + fichier.getNom();
				AppLogService.error("Erreur lors de la recuperation de la signature SHA 224 du fichier : " + fichier.getNom());
			}

		}

		return signature;
	}

	/**
	 * Retourne la signature numériques SHA224 du contenu
	 * 
	 * @param contenu
	 *            Contenu de la notification
	 * @return la signature
	 */
	public static String getSignature(String contenu) {
		String signature = new String();

		if (contenu != null) {

			signature = ShaUtils.toSha224String(contenu);
			if (signature == null) {
				signature = "error";
			}

		}

		return signature;
	}

	/**
	 * Retourne la liste des pieces annexes du PDD passé en paramètre
	 * 
	 * @param pdd
	 *            PDD
	 * @param plugin
	 *            le plugin actif
	 * @return Liste des pieces annexes appartenant au PDD
	 */
	private static List<Fichier> getFichiers(PDD pdd, Plugin plugin) {
		FichierFilter fichierFilter = new FichierFilter();
		fichierFilter.setPDD(pdd.getIdPdd());

		return FichierHome.findByFilter(fichierFilter, plugin);
	}

	/**
	 * Retourne le fichier appartenant au voeuAmendement
	 * 
	 * @param voeuAmendement
	 *            VoeuAmendement
	 * @param plugin
	 *            le plugin actif
	 * @return fichier appartenant au voeuAmendement
	 */
	private static Fichier getFichiers(VoeuAmendement voeuAmendement,
			Plugin plugin) {
		return FichierHome.findByPrimaryKey(
				voeuAmendement.getFichier().getId(), plugin);
	}
}
