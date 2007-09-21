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
package fr.paris.lutece.plugins.ods.service.publicationparisfr;


/**
 *
 * PublicationError
 *
 */
public class PublicationError
{
    public static final String MESSAGE_PROBLEME_PARIS_FR = "Problème lors de la publication  vers paris.fr. ";
    public static final String MESSAGE_DELETE_LOCAL_DIRECTORY = "Problème de suppression des fichiers  contenus dans le répertoire locale(localDirectory dans ods.properties)";
    public static final String MESSAGE_ALL_PARAMETERS_REQUIERED = "L’ensemble des paramètres nécessaires  à la publication  vers paris.fr ne sont pas renseignés.Vérifier les paramètres  dans le fichier ods.properties.";
    public static final String MESSAGE_DELETE_VERROU = "Impossible de supprimer le fichier verrou.";
    public static final String MESSAGE_GENERE_XML = "Une erreur est survenu lors de la génération des fichiers xml dans le repertoire locale.";
    public static final String MESSAGE_GENERE_PDF = "Une erreur est survenu lors de la génération des fichiers pdf dans le repertoire locale.";
    public static final String MESSAGE_GENERE_FICHIERS = "Une erreur est survenu lors de la génération des fichiers dans le repertoire locale.";

    /**
     * Concatene au message d'erreur passé en parametre l'en-tete commune aux erreurs  de publications
     * @param strMessage le message d'erreur a loguer
     * @return un message d'erreur complet
     */
    public static String getMessageError( String strMessage )
    {
        return MESSAGE_PROBLEME_PARIS_FR + strMessage;
    }
}
