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
package fr.paris.lutece.plugins.ods.service.search.action;


/**
 * Cette clase permet de gérer un index Lucène au moyen d'action.<br>
 * Une action concerne un serveur de consultation et un objet.<br>
 * Les actions possibles pour un document sont:
 *          1: ajout <br>
 *          2: modification <br>
 *          3: suppression <br>
 * Les types d'objet sont:
 *          1: fichier <br>
 *          2: projet ou proposition de délibération
 *          3: voeu ou amendement
 */
public class IndexerAction
{
    public static final int ACTION_CREATE = 1;
    public static final int ACTION_MODIFY = 2;
    public static final int ACTION_DELETE = 3;
    public static final int TYPE_FICHIER = 1;
    public static final int TYPE_PDD = 2;
    public static final int TYPE_VA = 3;
    private int _nIdTask;
    private int _nIdIndexer;
    private int _nType;
    private int _nIdObjet;
    private int _nCodeTask;
    private boolean _bArchive;

    /**
     * Retourne l'identifiant de l'action
     * @return l'identifiant de l'action
     */
    public int getIdAction(  )
    {
        return _nIdTask;
    }

    /**
     * Fixe l'identifiant de l'action
     * @param nIdTask l'identifiant de l'action
     */
    public void setIdAction( int nIdTask )
    {
        _nIdTask = nIdTask;
    }

    /**
     * Retourne l'id du serveur concerné
     * @return l'id du serveur
     */
    public int getIdIndexer(  )
    {
        return _nIdIndexer;
    }

    /**
     * Fixe l'id du serveur concerné
     * @param nIdIndexer objet serveur
     */
    public void setIdIndexer( int nIdIndexer )
    {
        _nIdIndexer = nIdIndexer;
    }

    /**
     * Retourne le type d'objet concerné: <br>
     * 1 pour un Fichier <br>
     * 2 pour un Fichier image (avec texte) <br>
     * 3 pour le titre d'un Fichier <br>
     * 4 pour l'objet d'un PDD <br>
     * 5 pour une référence de PDD
     * @return le type d'objet concerné
     */
    public int getType(  )
    {
        return _nType;
    }

    /**
     * Fixe le type d'objet concerné; utiliser les constantes TYPE_XXX
     * @param nType le type de l'objet
     */
    public void setType( int nType )
    {
        _nType = nType;
    }

    /**
     * Retourne l'objet concerné
     * @return l'objet concerné
     */
    public int getIdObjet(  )
    {
        return _nIdObjet;
    }

    /**
     * Fixe l'objet concerné
     * @param nIdObjet l'objet concerné
     */
    public void setIdObjet( int nIdObjet )
    {
        _nIdObjet = nIdObjet;
    }

    /**
     * Retourne l'action à réaliser: <br>
     *  1 pour ajout <br>
     *  2 pour modification <br>
     *  3 pour suppression
     * @return l'action à réaliser
     */
    public int getCodeTask(  )
    {
        return _nCodeTask;
    }

    /**
     * Fixe l'action à réaliser; utiliser les constantes ACTION_XXX
     * @param nCodeTask l'action à réaliser
     */
    public void setCodeTask( int nCodeTask )
    {
        _nCodeTask = nCodeTask;
    }

    /**
     * Indique si l'on insère dans l'index archive
     * @return <b>true</b> si l'action concerne l'index archive, <b>false</b> sinon
     */
    public boolean isArchive(  )
    {
        return _bArchive;
    }

    /**
     * Fixe la valeur pour insérer dans l'index archive ou prochaine séance
     * @param bArchive la valeur du paramètre
     */
    public void setArchive( boolean bArchive )
    {
        _bArchive = bArchive;
    }
}
