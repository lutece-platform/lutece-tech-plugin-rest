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
package fr.paris.lutece.plugins.ods.business.utilisateur;

import java.sql.Timestamp;


/**
 * Utilisateur
 */
public class Utilisateur
{
    private String _strIdUtilisateur;
    private String _strNom;
    private String _strPrenom;
    private String _strMail;
    private String _strMailCopie1;
    private String _strMailCopie2;
    private Timestamp _tsDerniereConnexion;
    private String _strDerniereIdSession;
    
    /**
     * Constructeur vide
     */
    public Utilisateur( ) 
    { 
    	super( );
    }
    
    /**
     * Constructeur qui va cloner un utilisateur
     * @param utilisateur l'utilisateur à cloner
     */
    public Utilisateur( Utilisateur utilisateur ) 
    {
		_strIdUtilisateur = utilisateur.getIdUtilisateur();
		_strNom = utilisateur.getNom();
		_strPrenom = utilisateur.getPrenom();
		_strMail = utilisateur.getMail();
		_strMailCopie1 = utilisateur.getMailCopie1();
		_strMailCopie2 = utilisateur.getMailCopie2();
		_tsDerniereConnexion = utilisateur.getDerniereConnexion();
		_strDerniereIdSession = utilisateur.getDerniereIdSession();
	}

	/**
     * Retourne l'identifiant de l'utilisateur
     * @return l'identifiant de l'utilisateur
     */
    public String getIdUtilisateur(  )
    {
        return _strIdUtilisateur;
    }

    /**
     * Fixe l'identifiant de l'utilisateur
     * @param strIdUtilisateur l'identifiant de l'utilisateur
     */
    public void setIdUtilisateur( String strIdUtilisateur )
    {
        this._strIdUtilisateur = strIdUtilisateur;
    }

    /**
     * Retourne l'adresse mail de référence de l'utilisateur
     * @return l'adresse mail de référence de l'utilisateur
     */
    public String getMail(  )
    {
        return _strMail;
    }

    /**
     * Fixe l'adresse mail de référence de l'utilisateur
     * @param strMail l'adresse mail de référence de l'utilisateur
     */
    public void setMail( String strMail )
    {
        _strMail = strMail;
    }

    /**
     * Retourne l'addresse mail de copie n°1
     * @return l'addresse mail de copie n°1
     */
    public String getMailCopie1(  )
    {
        return _strMailCopie1;
    }

    /**
     * Fixe l'adresse mail de copie n°1
     * @param strMail l'adresse mail de copie n°1
     */
    public void setMailCopie1( String strMail )
    {
        _strMailCopie1 = strMail;
    }

    /**
     * Retourne l'addresse mail de copie n°2
     * @return l'addresse mail de copie n°2
     */
    public String getMailCopie2(  )
    {
        return _strMailCopie2;
    }

    /**
     * Fixe l'adresse mail de copie n°2
     * @param strMail l'adresse mail de copie n°2
     */
    public void setMailCopie2( String strMail )
    {
        _strMailCopie2 = strMail;
    }

    /**
     * Retourne le nom de l'utilisateur
     * @return le nom de l'utilisateur
     */
    public String getNom(  )
    {
        return _strNom;
    }

    /**
     * Fixe le nom de l'utilisateur
     * @param strNom le nom de l'utilisateur
     */
    public void setNom( String strNom )
    {
        _strNom = strNom;
    }

    /**
     * retourne le prénom de l'utilisateur
     * @return le prénom de l'utilisateur
     */
    public String getPrenom(  )
    {
        return _strPrenom;
    }

    /**
     * Fixe le prénom de l'utilisateur
     * @param strPrenom le prénom de l'utilisateur
     */
    public void setPrenom( String strPrenom )
    {
        _strPrenom = strPrenom;
    }

    /**
     * retourne la date de la derniere connexion
     * @return la date de la derniere connexion
     */
    public Timestamp getDerniereConnexion(  )
    {
        return _tsDerniereConnexion;
    }

    /**
     * Fixe la date de la derniere connexion
     * @param tsDerniereConnexion la date de la derniere connexion
     */
    public void setDerniereConnexion( Timestamp tsDerniereConnexion )
    {
        _tsDerniereConnexion = tsDerniereConnexion;
    }

    /**
     * retourne l'identifiant de session à la dernière connexion
     * @return l'identifiant de session à la dernière connexion
     */
    public String getDerniereIdSession(  )
    {
        return _strDerniereIdSession;
    }

    /**
     * Fixe l'identifiant de session à la dernière connexion
     * @param strDerniereIdSession l'identifiant de session à la dernière connexion
     */
    public void setDerniereIdSession( String strDerniereIdSession )
    {
        _strDerniereIdSession = strDerniereIdSession;
    }
}
