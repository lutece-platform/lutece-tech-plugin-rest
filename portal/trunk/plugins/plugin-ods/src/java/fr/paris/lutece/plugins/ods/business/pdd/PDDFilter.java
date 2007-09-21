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
package fr.paris.lutece.plugins.ods.business.pdd;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;

import java.sql.Timestamp;


/**
 *
 * Reponsable du filtre pour la fonctionnalité PDD
 */
public class PDDFilter
{
    public static final String ALL_STRING = "all";
    public static final int ALL_INT = -1;

    // Variables declarations
    private int _nIdFormationConseil = ALL_INT;
    private int _nIdOdjReferenceFormationConseilMunicipal = ALL_INT;
    private int _nIdOdjReferenceFormationConseilGeneral = ALL_INT;
    private int _nInscritODJ = ALL_INT;
    private int _nPublication = ALL_INT;
    private String _strModeIntroduction = ALL_STRING;
    private int _nCategorieDeliberation = ALL_INT;
    private int _nStatut = ALL_INT;
    private String _strDirection = ALL_STRING;
    private String _strGroupeDepositaire = ALL_STRING;
    private String _strType = ALL_STRING;
    private int _nSeance = ALL_INT;
    private int _nProchaineSeance = ALL_INT;
    private int _nArchiveAvecPS = ALL_INT;
    private boolean _bArchiveSansPS;
    private String _strReference = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private int _nDelegationService = ALL_INT;
    private int _nTypeOrdreDuJour = ALL_INT;
    private Timestamp _tsDatePublication;
    private Timestamp _tsDateRetourControleDeLegalite;

    /**
     * @return _tsDateRetourControleDeLegalite Timestamp
     */
    public Timestamp getDateRetourControleDeLegalite(  )
    {
        return _tsDateRetourControleDeLegalite;
    }

    /**
     * @param tsDateRetourControleDeLegalite _tsDateRetourControleDeLegalite to set
     */
    public void setDateRetourControleDeLegalite( Timestamp tsDateRetourControleDeLegalite )
    {
        _tsDateRetourControleDeLegalite = tsDateRetourControleDeLegalite;
    }

    /**
     * @return boolean TRUE si _tsDateRetourControleDeLegalite est actif, FALSE sinon
     */
    public boolean containsDateRetourControleDeLegaliteCriteria(  )
    {
        return ( _tsDateRetourControleDeLegalite != null );
    }

    /**
     * @return _tsDatePublication Timestamp
     */
    public Timestamp getDatePublication(  )
    {
        return _tsDatePublication;
    }

    /**
     * @param tsDatePubliocation _tsDatePublication to set
     */
    public void setDatePublication( Timestamp tsDatePubliocation )
    {
        _tsDatePublication = tsDatePubliocation;
    }

    /**
     * @return boolean TRUE si _tsDatePublication est actif, FALSE sinon
     */
    public boolean containsDatePublicationCriteria(  )
    {
        return ( _tsDatePublication != null );
    }

    /**
     * @return _nCategorieDeliberation int
     */
    public int getCategorieDeliberation(  )
    {
        return _nCategorieDeliberation;
    }

    /**
     * @param categorieDeliberation _nCategorieDeliberation to set
     */
    public void setCategorieDeliberation( int categorieDeliberation )
    {
        _nCategorieDeliberation = categorieDeliberation;
    }

    /**
     *
     * @return boolean TRUE si _nCategorieDeliberation est actif, FALSE sinon
     */
    public boolean containsCategorieDeliberationCriteria(  )
    {
        return ( _nCategorieDeliberation != ALL_INT );
    }

    /**
     * @return _nStatut int
     */
    public int getStatut(  )
    {
        return _nStatut;
    }

    /**
     * @param statut _nStatut to set
     */
    public void setStatut( int statut )
    {
        _nStatut = statut;
    }

    /**
     * @return boolean TRUE si _nStatut est actif, FALSE sinon
     */
    public boolean containsStatutCriteria(  )
    {
        return ( _nStatut != ALL_INT );
    }

    /**
     * @return _nIdFormationConseil int
     */
    public int getIdFormationConseil(  )
    {
        return _nIdFormationConseil;
    }

    /**
     * @param idFormationConseil _nIdFormationConseil to set
     */
    public void setIdFormationConseil( int idFormationConseil )
    {
        _nIdFormationConseil = idFormationConseil;
    }

    /**
     * @return boolean TRUE si _nIdFormationConseil est actif, FALSE sinon
     */
    public boolean containsFormationConseilCriteria(  )
    {
        return ( _nIdFormationConseil != ALL_INT );
    }

    /**
     * @return _nIdOdjReferenceFormationConseilMunicipal
     * l'id de de l'ordre du jour de reference pour le conseil general
     * auquel doit etre rattaché le pdd
     */
    public int getIdOdjReferenceFormationConseilMunicipal(  )
    {
        return _nIdOdjReferenceFormationConseilMunicipal;
    }

    /**
     * @return _nIdOdjReferenceFormationConseilGeneral
     * l'id de de l'ordre du jour de reference pour le conseil general
     * auquel doit etre rattaché le pdd
     */
    public int getIdOdjReferenceFormationConseilGeneral(  )
    {
        return _nIdOdjReferenceFormationConseilGeneral;
    }

    /**
     * @param nId
     * l'id de de l'ordre du jour de reference pour le conseil general
     * auquel doit etre rattaché le pdd
     */
    public void setIdOdjReferenceFormationConseilMunicipal( int nId )
    {
        _nIdOdjReferenceFormationConseilMunicipal = nId;
    }

    /**
     * @param nId l'id de de l'ordre du jour de reference pour le conseil general
     * auquel doit etre rattaché le pdd
     */
    public void setIdOdjReferenceFormationConseilGeneral( int nId )
    {
        _nIdOdjReferenceFormationConseilGeneral = nId;
    }

    /**
     * @return retourne 0 si le pdd ne doit pas etre inscrit a un ordre du jour de reference
     *                                  1 si le pdd  doit etre inscrit a un ordre du jour de reference
     */
    public int getInscritODJ(  )
    {
        return _nInscritODJ;
    }

    /**
     * @param inscritODJ 0 si le pdd ne doit pas etre inscrit a un ordre du jour de reference
     *                                   1 si le pdd  doit etre inscrit a un ordre du jour de reference
     */
    public void setInscritODJ( int inscritODJ )
    {
        _nInscritODJ = inscritODJ;
    }

    /**
     * @return boolean TRUE si le pdd doit  etre inscrit a l'odj de reference
     */
    public boolean containsInscritODJCriteria(  )
    {
        return ( ( _nInscritODJ != ALL_INT ) );
    }

    /**
     * @return boolean TRUE si le pdd doit  etre inscrit a l'odj de reference du conseil general
     */
    public boolean containsIdOdjReferenceFormationConseilGeneralCriteria(  )
    {
        return ( ( _nIdOdjReferenceFormationConseilGeneral != ALL_INT ) );
    }

    /**
     * @return boolean TRUE si le pdd doit  etre inscrit a l'odj de reference du conseil municipal
     */
    public boolean containsIdOdjReferenceFormationConseilMunicipalCriteria(  )
    {
        return ( ( _nIdOdjReferenceFormationConseilMunicipal != ALL_INT ) );
    }

    /**
     * @return the _nModeIntroduction
     */
    public String getModeIntroduction(  )
    {
        return _strModeIntroduction;
    }

    /**
     * @param modeIntroduction the _nModeIntroduction to set
     */
    public void setModeIntroduction( String modeIntroduction )
    {
        _strModeIntroduction = modeIntroduction;
    }

    /**
     * @return boolean TRUE si _strModeIntroduction est actif, FALSE sinon
     */
    public boolean containsModeIntroductionCriteria(  )
    {
        return ( !_strModeIntroduction.equals( ALL_STRING ) );
    }

    /**
     * @return the _nSeance
     */
    public int getSeance(  )
    {
        return _nSeance;
    }

    /**
     * @param seance _nSeance to set
     */
    public void setSeance( int seance )
    {
        _nSeance = seance;
    }

    /**
     * @return boolean TRUE si _nProchaineSeance est actif, FALSE sinon
     */
    public boolean containsProchaineSeanceCriteria(  )
    {
        return ( _nProchaineSeance != ALL_INT );
    }

    /**
     * @return the _nProchaineSeance
     */
    public int getProchaineSeance(  )
    {
        return _nProchaineSeance;
    }

    /**
     * @param prochaineSeance _nProchaineSeance to set
     */
    public void setProchaineSeance( int prochaineSeance )
    {
        _nProchaineSeance = prochaineSeance;
    }

    /**
     * @return boolean TRUE si _nArchive est actif, FALSE sinon
     */
    public boolean containsArchiveAvecPSCriteria(  )
    {
        return ( _nArchiveAvecPS != ALL_INT );
    }

    /**
     * Retourne le filtre Archive
     * @return le filtre Archive
     */
    public int getArchiveAvecPS(  )
    {
        return _nArchiveAvecPS;
    }

    /**
     * Fixe le filtre archive avec l'id de la prochaine séance
     * @param prochaineSeance l'id de la prochaine séance
     */
    public void setArchiveAvecPS( int prochaineSeance )
    {
        _nArchiveAvecPS = prochaineSeance;
    }

    /**
     * Indique si la recherche porte sur les archives sans prochaine séance dans la base
     * @return <b>true</b> si la recherche porte sur les archives sans prochaine séance dans la base, <b>false</b> sinon
     */
    public boolean getArchiveSansPS(  )
    {
        return _bArchiveSansPS;
    }

    /**
     * Fixe le filtre archive sans prochaine séance
     * @param bArchiveSansPS <b>true</b> si la recherche porte sur les archives sans prochaine séance dans la base, <b>false</b> sinon
     */
    public void setArchiveSansPS( boolean bArchiveSansPS )
    {
        _bArchiveSansPS = bArchiveSansPS;
    }

    /**
     * @return boolean TRUE si _nSeance est actif, FALSE sinon
     */
    public boolean containsSeanceCriteria(  )
    {
        return ( _nSeance != ALL_INT );
    }

    /**
     * @return _strDirection String
     */
    public String getDirection(  )
    {
        return _strDirection;
    }

    /**
     * @param direction the _strDirection to set
     */
    public void setDirection( String direction )
    {
        _strDirection = direction;
    }

    /**
     * @return boolean TRUE si _strDirection est actif, FALSE sinon
     */
    public boolean containsDirectionCriteria(  )
    {
        return ( !_strDirection.equals( ALL_STRING ) );
    }

    /**
     * @return _nPublication int
     */
    public int getPublication(  )
    {
        return _nPublication;
    }

    /**
     * @param nPublication _nPublication to set
     */
    public void setPublication( int nPublication )
    {
        _nPublication = nPublication;
    }

    /**
     * @return boolean TRUE si _nPublication est actif, FALSE sinon
     */
    public boolean containsPublicationCriteria(  )
    {
        return ( _nPublication != ALL_INT );
    }

    /**
     * @return _strGroupeDepositaire
     */
    public String getGroupeDepositaire(  )
    {
        return _strGroupeDepositaire;
    }

    /**
     * @param groupeDepositaire _strGroupeDepositaire to set
     */
    public void setGroupeDepositaire( String groupeDepositaire )
    {
        _strGroupeDepositaire = groupeDepositaire;
    }

    /**
     * @return boolean TRUE si _strGroupeDepositaire est actif, FALSE sinon
     */
    public boolean containsGroupeDepositaireCriteria(  )
    {
        return ( !_strGroupeDepositaire.equals( ALL_STRING ) );
    }

    /**
     * @return _strType String
     */
    public String getType(  )
    {
        return _strType;
    }

    /**
     * @param type _strType to set
     */
    public void setType( String type )
    {
        _strType = type;
    }

    /**
     * @return boolean TRUE si _strType est actif, FALSE sinon
     */
    public boolean containsTypeCriteria(  )
    {
        return ( !_strType.equals( ALL_STRING ) );
    }

    /**
     *
     * @return _strReference String
     */
    public String getReference(  )
    {
        return _strReference;
    }

    /**
     *
     * @param reference String
     */
    public void setReference( String reference )
    {
        _strReference = reference;
    }

    /**
     *
     * @return boolean TRUE si _strReference est actif, FALSE sinon
     */
    public boolean containsReferenceCriteria(  )
    {
        return ( !_strReference.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) );
    }

    /**
     * @return _nDelegationService
     */
    public int getDelegationService(  )
    {
        return _nDelegationService;
    }

    /**
     * @param delegationService _nDelegationService to set
     */
    public void setDelegationService( int delegationService )
    {
        _nDelegationService = delegationService;
    }

    /**
     * @return boolean TRUE si _nDelegationService est actif, FALSE sinon
     */
    public boolean containsDelegationServiceCriteria(  )
    {
        return ( _nDelegationService != ALL_INT );
    }

    /**
     * @return _nTypeOrdreDuJour
     */
    public int getTypeOrdreDuJour(  )
    {
        return _nTypeOrdreDuJour;
    }

    /**
     * @param typeOrdreDuJour _nTypeOrdreDuJour to set
     */
    public void setTypeOrdreDuJour( int typeOrdreDuJour )
    {
        _nTypeOrdreDuJour = typeOrdreDuJour;
    }

    /**
     * @return boolean TRUE si _nTypeOrdreDuJour est actif, FALSE sinon
     */
    public boolean containsTypeOrdreDuJourCriteria(  )
    {
        return ( _nTypeOrdreDuJour != ALL_INT );
    }
}
