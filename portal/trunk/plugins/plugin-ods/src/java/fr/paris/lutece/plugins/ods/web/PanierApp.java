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
package fr.paris.lutece.plugins.ods.web;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.panier.Panier;
import fr.paris.lutece.plugins.ods.business.panier.PanierHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.manager.GenerateZipManager;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.jms.JMSException;

import javax.naming.NamingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * PanierApp: a pour responsabilité d'afficher les archives téléchargées et les éléments du panier de l'utilisateur connecté
 */
public class PanierApp implements XPageApplication
{
    public static final String MARK_DATE_AJOUT = "dateAjout";
    public static final String MARK_TAILLE_TOTAL_MAX = "tailleTotalMax";
    public static final String PROPERTY_TELECHARGEMENT_HOST = "telechargement.host";
    public static final String PROPERTY_TELECHARGEMENT_WEBAPP = "telechargement.webapp";
    public static final String PROPERTY_TELECHARGEMENT_SERVLET_PANIER = "telechargement.servlet.panier";
    public static final String PROPERTY_TELECHARGEMENT_COOKIE_DOMAIN = "telechargement.cookie.domain";
    public static final String PROPERTY_COOKIE_WSSO_NAME_SOURCE = "cookie.source.wssoUserGuid.name";
    public static final String PROPERTY_COOKIE_WSSO_NAME_DESTINATION = "cookie.redirection.wssoUserGuid.name";
    
    private static final String MARK_FICHIERS_NON_PUBLIES = "fichiers_non_publies";
    private static final String TEMPLATE_PANIER = "skin/plugins/ods/panier.html";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.panier";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.panier.page.title";
    private static final String MARK_LISTE_PRODUIT = "liste_produits";
    private static final String MARK_LISTE_ZIP = "liste_zip";
    private static final String MARK_SELECTION = "selection";
    private static final String MARK_DO_WHAT = "doWhat";
    private static final String MARK_DO_ADD_SELECTION_PANIER = "addSelectionPanier";
    private static final String MARK_DO_DELETE_SELECTION_PANIER = "deleteSelectionPanier";
    private static final String MARK_SELECTION_ELEMENTS_PANIER_TO_DOWNLOAD = "idElementPanier";
    private static final String MARK_URLBASE = "urlBase";
    private static final String MARK_URL_WEBAPP_SERVLET_TELECHARGEMENT = "urlWebappServletTelechargement";
    private static final String MARK_URL_DOWNLOAD_PANIER = "url_download_panier";
    private static final String PROPERTY_TAILLE_TOTAL_MAX = "taille_total_max_telechargement";
    private static final String CONSTANT_XPAGE_CONFIRMATION = "jsp/site/RunStandaloneApp.jsp?page=ods_confirmation_telechargement";
    private static final String CONSTANT_XPAGE_PANIER = "jsp/site/Portal.jsp?page=ods_panier";
    private static final String CONSTANTE_ODS = "ods";
    private static final String CONSTANTE_LISTE_FICHIERS_SEPARATOR = "-";

    private static final int DEFAULT_PROPERTY_TAILLE_TOTAL_MAX = 22020096;

    /* Variables de session */
    private List<Produit> _produits = new ArrayList<Produit>(  );
    private List<Panier> _archives = new ArrayList<Panier>(  );

    /**
     * renvoie la page.
     * @param request le requête http
     * @param nMode le mode
     * @param plugin le Plugin actif
     * @return la page
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        /* On met à jour la liste des utilisateurs et gère la session de l'utilisateur */
        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        /* On instancie une nouvelle XPage */
        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );

        /* Récupère l'id de l'utilisateur connecté. */
        LuteceUser luteceUser = SecurityService.getInstance(  ).getRegisteredUser( request );
        String idUser = luteceUser.getName(  );

        /* Récupère les valeurs des paramètres passé à la requête */
        String selection = request.getParameter( MARK_SELECTION );
        String doWhat = request.getParameter( MARK_DO_WHAT );

        /* Tableau représentant les ids des documents à télécharger */
        String[] ids = ( selection != null ) ? selection.split( CONSTANTE_LISTE_FICHIERS_SEPARATOR ) : null;

        /* Permet de mettre à jour la liste en session contenant les éléments du paniers de l'utilisateur connécté */
        refreshElementsPanier( plugin, idUser );

        /* Test pour savoir si l'on doit ajouter ou enlever les éléments du panier qui ont été séléctionné */
        if ( ( doWhat != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( doWhat ) )
        {
            if ( doWhat.equals( MARK_DO_ADD_SELECTION_PANIER ) )
            {
                addSelectionPanier( request, plugin, ids, idUser );
            }
            else if ( doWhat.equals( MARK_DO_DELETE_SELECTION_PANIER ) )
            {
                removeSelectionPanier( plugin, ids, idUser );
            }
        }

        /* Permet de mettre à jour la liste en session contenant les éléments du paniers de l'utilisateur connécté */
        refreshElementsPanier( plugin, idUser );

        /* Permet de mettre à jour la liste en session contenant les archives téléchargés de l'utilisateur connécté */
        refreshZips( plugin, idUser );
        
        /* Création du cookie pour attester de l'authentification sur le serveur de téléchargement */
        HttpServletResponse response = LocalVariables.getResponse();
        Cookie[] cookies = request.getCookies();
        String strGuid = "";

        if (cookies != null) 
        {
        	for (Cookie c : cookies) 
        	{
        		if ((c != null) && 
        				(c.getName() != null) && 
        				c.getName().equals( AppPropertiesService.getProperty( PanierApp.PROPERTY_COOKIE_WSSO_NAME_SOURCE ) ) )
        		{
        			strGuid = c.getValue();
        			break;
        		}
        	}
        }

        Cookie newCookie = new Cookie( AppPropertiesService.getProperty( PanierApp.PROPERTY_COOKIE_WSSO_NAME_DESTINATION ), strGuid );
        newCookie.setDomain( AppPropertiesService.getProperty( PanierApp.PROPERTY_TELECHARGEMENT_COOKIE_DOMAIN ) );
        newCookie.setPath("/");
        response.addCookie( newCookie );
        
        String url = AppPropertiesService.getProperty( PanierApp.PROPERTY_TELECHARGEMENT_HOST ) + "/" +
			AppPropertiesService.getProperty( PanierApp.PROPERTY_TELECHARGEMENT_WEBAPP ) + "/" +
			AppPropertiesService.getProperty( PanierApp.PROPERTY_TELECHARGEMENT_SERVLET_PANIER ) + 
			"?id=";

        model.put( MARK_URL_WEBAPP_SERVLET_TELECHARGEMENT,
            AppPropertiesService.getProperty( PROPERTY_TELECHARGEMENT_HOST ) + "/" +
            AppPropertiesService.getProperty( PROPERTY_TELECHARGEMENT_WEBAPP ) );
        model.put( MARK_TAILLE_TOTAL_MAX,
            AppPropertiesService.getPropertyInt( PROPERTY_TAILLE_TOTAL_MAX, DEFAULT_PROPERTY_TAILLE_TOTAL_MAX ) );
        model.put( MARK_LISTE_PRODUIT, _produits );
        model.put( MARK_LISTE_ZIP, _archives );
        model.put( MARK_URLBASE, AppPathService.getBaseUrl( request ) + CONSTANT_XPAGE_CONFIRMATION );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );
        model.put( MARK_URL_DOWNLOAD_PANIER, url);

        if ( request.getParameter( MARK_FICHIERS_NON_PUBLIES ) != null )
        {
            model.put( MARK_FICHIERS_NON_PUBLIES, true );
        }

        /* On récupère la liste des VA mis en ligne après la date de dernière connexion de l'utilisateur */
        List<VoeuAmendement> listVa = OdsUtils.getNouveauxVAPublies( request, plugin );

        if ( listVa != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_VAS, listVa );
        }

        // Chargement et affichage de la template
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PANIER, request.getLocale(  ), model );
        String strPagetitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setContent( template.getHtml(  ) );

        // On affecte un titre et un chemin à la XPage
        page.setTitle( strPagetitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }

    /**
     * Cette méthode permet de lancer la génération de l'archive contenant les éléments du panier séléctionnés
     *
     * @param request le requête http
     * @param response le response http
     */
    public void downloadSelectionPanier( HttpServletRequest request, HttpServletResponse response )
    {
        /* On récupère les paramètres envoyés dans la requête */
        String[] ids = request.getParameterValues( MARK_SELECTION_ELEMENTS_PANIER_TO_DOWNLOAD );
        String dateAjout = request.getParameter( MARK_DATE_AJOUT );

        /* On récupère l'utilisateur courant */
        LuteceUser luteceUser = SecurityService.getInstance(  ).getRegisteredUser( request );

        boolean bFichiersNonPublies = false;

        try
        {
            /* Si la liste de fichiers n'est pas nulle ou vide on génère le zip contenant tous ces fichiers */
            if ( ( ids != null ) && ( ids.length > 0 ) )
            {
                Fichier fichier;

                for ( int i = 0; i < ids.length; i++ )
                {
                    fichier = null;

                    if ( ids[i] != null )
                    {
                        int nIdFichier = Integer.parseInt( ids[i] );
                        fichier = FichierHome.findByPrimaryKey( nIdFichier, PluginService.getPlugin( "ods" ) );

                        if ( ( fichier != null ) && !fichier.getEnLigne(  ) )
                        {
                            bFichiersNonPublies = true;

                            break;
                        }
                    }
                }

                GenerateZipManager.generateZIP( request, ids, PluginService.getPlugin( CONSTANTE_ODS ),
                    luteceUser.getName(  ), dateAjout, 1 );
            }
        }
        catch ( NamingException e1 )
        {
            AppLogService.error( e1 );
        }
        catch ( JMSException e1 )
        {
            AppLogService.error( e1 );
        }

        //idElementPanier
        try
        {
            // On renvoie l'utilisateur vers la page du panier
            String strUrl = AppPathService.getBaseUrl( request ) + CONSTANT_XPAGE_PANIER;

            if ( bFichiersNonPublies )
            {
                strUrl += ( "&" + MARK_FICHIERS_NON_PUBLIES + "=" + OdsConstants.CONSTANTE_CHAINE_VIDE );
            }

            response.sendRedirect( strUrl );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
    }

    /**
    * Cette méthode permet de récupérer les zip de l'utilisateur connecté
    *
    * @param plugin le Plugin actif
    * @param idUser Id utilisateur connecté
    */
    private void refreshZips( Plugin plugin, String idUser )
    {
        _archives = PanierHome.findZipByIdUser( idUser, plugin );
    }

    /**
     *
     * Cette méthode permet de récupérer les éléments du panier de l'utilisateur connecté
     *
     * @param plugin le Plugin actif
     * @param idUser Id utilisateur connecté
     */
    private void refreshElementsPanier( Plugin plugin, String idUser )
    {
        _produits = new ArrayList<Produit>(  );

        // On récupère la liste et le nombre des identifiants de fichiers de l'utilisateur
        String[] ids = PanierHome.findElementPanierByIdUser( idUser, plugin );
        int lIds = ( ids != null ) ? ids.length : 0;

        for ( int i = 0; i < lIds; i++ )
        {
            if ( ( ids[i] != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( ids[i] ) &&
                    ( getProduitByIdFichier( Integer.parseInt( ids[i] ) ) == null ) )
            {
                Produit produit = new Produit(  );
                List<PDD> refPdds = new ArrayList<PDD>(  );

                // On récupère le fichier identifié
                int idFichier = Integer.parseInt( ids[i] );
                Fichier fichier = FichierHome.findByPrimaryKey( idFichier, plugin );

                if ( fichier != null )
                {
                    produit.setFichier( fichier );

                    int idTypeDocument = fichier.getTypdeDocument(  ).getId(  );

                    // Si le fichier est un voeu ou un amendement, on recuèpre la liste des PDDs 
                    // que l'on ajoute au panier en tant que référence
                    if ( ( idTypeDocument == TypeDocumentEnum.AMENDEMENT.getId(  ) ) ||
                            ( idTypeDocument == TypeDocumentEnum.VOEU.getId(  ) ) ||
                            ( ( fichier.getPDD(  ) == null ) &&
                            ( idTypeDocument == TypeDocumentEnum.DELIBERATION.getId(  ) ) ) )
                    {
                        produit.setVoeu( true );

                        VoeuAmendement voeuAmendement;

                        if ( idTypeDocument == TypeDocumentEnum.DELIBERATION.getId(  ) )
                        {
                            voeuAmendement = VoeuAmendementHome.findByDeliberation( fichier, plugin );
                        }
                        else
                        {
                            voeuAmendement = VoeuAmendementHome.findByTexteInitial( fichier, plugin );
                        }

                        if ( voeuAmendement != null )
                        {
                            produit.setVoeuAmendement( voeuAmendement );

                            List<PDD> pdds = voeuAmendement.getPdds(  );
                            int lPdds = ( pdds != null ) ? pdds.size(  ) : 0;

                            for ( int j = 0; j < lPdds; j++ )
                            {
                                refPdds.add( pdds.get( j ) );
                            }
                        }
                    }

                    // Sinon on ajoute juste le fichier
                    else
                    {
                        produit.setVoeu( false );
                        refPdds.add( fichier.getPDD(  ) );
                    }

                    produit.setRefPdds( refPdds );
                    _produits.add( produit );
                }
            }
        }
    }

    /**
     * Permet de supprimer les éléments du panier sélectionné par l'utilisateur
     *
     * @param plugin le Plugin actif
     * @param ids tableau des id des documents qui doivent être téléchargé
     * @param idUser id de l'utilisateur
     */
    private void removeSelectionPanier( Plugin plugin, String[] ids, String idUser )
    {
        int lIds = ( ids != null ) ? ids.length : 0;

        // Pour chaque fichier sélectionné, on le supprime dans la base
        for ( int i = 0; i < lIds; i++ )
        {
            int nIdFichier = OdsUtils.string2id( ids[i] );
            if ( nIdFichier != -1 )
            {
                Produit p = getProduitByIdFichier( nIdFichier );

                if ( p != null )
                {
                    PanierHome.removeElementPanier( idUser, p.getFichier(  ).getId(  ), plugin );
                }
            }
        }
    }

    /**
     * Retourne l'objet Produit qui correspond au fichier passé en paramètre
     *
     * @param idFichier id du fichier
     * @return l'objet Produit ou null si aucun Produit ne correspond au fichier
     */
    private Produit getProduitByIdFichier( int idFichier )
    {
        // Pour chaque Produit du panier, si l'identifiant dfdu fichier correspond à l'identifiant
        // du fichier que l'on recherche, on retourne le Produit
        for ( Produit p : _produits )
        {
            if ( ( p.getFichier(  ) != null ) && ( p.getFichier(  ).getId(  ) == idFichier ) )
            {
                return p;
            }
        }

        return null;
    }

    /**
     * Permet d'ajouter les éléments du panier sélectionné par l'utilisateur
     *
     * @param request le requête http
    * @param plugin le Plugin actif
     * @param ids tableau des id des documents qui doivent être téléchargé
     * @param idUser id de l'utilisateur
     */
    private void addSelectionPanier( HttpServletRequest request, Plugin plugin, String[] ids, String idUser )
    {
        int lIds = ( ids != null ) ? ids.length : 0;

        // Pour chaque élément sélectionné, si le fichier n'est pas déjà dans le panier,
        // alors on ajoute l'élément sélectionné au panier dans la base
        for ( int i = 0; i < lIds; i++ )
        {
            if ( ( ids[i] != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( ids[i] ) &&
                    ( getProduitByIdFichier( Integer.parseInt( ids[i] ) ) == null ) )
            {
                int idFichier = Integer.parseInt( ids[i] );
                Fichier fichier = FichierHome.findByPrimaryKey( idFichier, plugin );

                if ( fichier != null )
                {
                    LuteceUserManager.getUtilisateurCourant( request, plugin );
                    PanierHome.addElementPanier( idUser, idFichier, plugin );
                }
            }
        }
    }

    /**
     * Classe permettant de stocker les éléments du panier à afficher
     *
     */
    public class Produit
    {
        private Fichier _fichier;
        private boolean _bVoeu;
        private VoeuAmendement _voeuAmendement;
        private List<PDD> _refPdds;

        /**
         * @return le fichier
         */
        public Fichier getFichier(  )
        {
            return _fichier;
        }

        /**
         * @param fichier le fichier que l'on souhaite affecté au Produit
         */
        public void setFichier( Fichier fichier )
        {
            this._fichier = fichier;
        }

        /**
         * @return la liste des PDDs
         */
        public List<PDD> getRefPdds(  )
        {
            return _refPdds;
        }

        /**
         * @param refPdds la liste des PDDs que doit contenir le Produit
         */
        public void setRefPdds( List<PDD> refPdds )
        {
            this._refPdds = refPdds;
        }

        /**
         * @return le voeu ou amendement
         */
        public VoeuAmendement getVoeuAmendement(  )
        {
            return _voeuAmendement;
        }

        /**
         * @param voeuAmendement le voeu ou amendement que l'on souhaite affecté au Produit
         */
        public void setVoeuAmendement( VoeuAmendement voeuAmendement )
        {
            this._voeuAmendement = voeuAmendement;
        }

        /**
         * @return TRUE si le produit est un voeu ou una mendement, FALSE sinon
         */
        public boolean getVoeu(  )
        {
            return _bVoeu;
        }

        /**
         * @param voeu vaut TRUE si le produit est un voeu ou una mendement, FALSE sinon
         */
        public void setVoeu( boolean voeu )
        {
            _bVoeu = voeu;
        }
    }
}
