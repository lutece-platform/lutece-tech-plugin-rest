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
package fr.paris.lutece.plugins.ods.web.certificataffichage;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.service.manager.DocumentManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * Cette classe permet de gérer l'interface de gestion des certificat d'affichage.
 */
public class CertificatAffichageJspBean extends PluginAdminPageJspBean
{
	public static final String RIGHT_ODS_CERTIFICAT_AFFICHAGE = "ODS_CERTIFICAT_AFFICHAGE";
    
	private static final String CONSTANTE_ENTETE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String XML_CERTIFICAT_OPEN = "<certificat>";
    private static final String XML_CERTIFICAT_CLOSE = "</certificat>";
	private static final String XML_SECRETAIRE_GENERALE_OPEN = "<secretaireGenerale>";
	private static final String XML_SECRETAIRE_GENERALE_CLOSE = "</secretaireGenerale>";
	private static final String XML_DATE_DU_JOUR_OPEN = "<dateDuJour>";
	private static final String XML_DATE_DU_JOUR_CLOSE = "</dateDuJour>";
	
    private static final String MARK_LISTE_PDDS = "liste_pdds";
    private static final String MARK_LISTE_SEANCES = "liste_seances";
    private static final String MARK_ID_SEANCE_SELECTED = "id_seance_selected";
    private static final String MARK_DATE_RETOUR_CONTROLE_LEGALITE = "date_retour_controle_legalite";
    private static final String MARK_LISTE_FORMATION_CONSEIL = "liste_formation_conseil";
    private static final String MARK_ID_FORMATION_CONSEIL_SELECTED = "id_formation_conseil_selected";
    private static final String MARK_ERROR = "error";
    
    private static final String PROPERTY_NOM_SECRETAIRE_GENERALE = "certificatAffichage.nom";

    private static final String TEMPLATE_CERTIFICAT_AFFICHAGE = "admin/plugins/ods/certificatAffichage/certificat.html";
   
    /**
     * Retourne l'interface de gestion des certificats d'affichage
     * @param request le requête HTTP
     * @return l'interface de gestion des certificats d'affichage
     */
    public String getCertificatList( HttpServletRequest request )
    {
		Plugin plugin = getPlugin(  );
		HashMap<Object, Object> model = new HashMap<Object, Object>( );
		
		setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );
		
		int nIdSeance = OdsUtils.getIntegerParameter( OdsParameters.ID_SEANCE, request );
		int nIdFormationConseil = OdsUtils.getIntegerParameter( OdsParameters.ID_FORMATION_CONSEIL, request );
		String strDateRetourControleDeLegalite = request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE );
		Timestamp tsDateRetourControleDeLegalite = OdsUtils.getDate( strDateRetourControleDeLegalite, true );
		
		if ( ( nIdSeance != -1 ) && ( nIdFormationConseil != -1 ) && ( tsDateRetourControleDeLegalite == null ) )
		{
			model.put( MARK_ERROR, true );			
		}
				
		if ( ( nIdSeance == -1 ) &&
				( SeanceHome.getDerniereSeance( plugin ) != null ) )
		{
			nIdSeance = SeanceHome.getDerniereSeance( plugin ).getIdSeance();
		}

		List<PDD> pdds = null;		
		if ( ( tsDateRetourControleDeLegalite != null ) )
		{
			PDDFilter filter = new PDDFilter( );
			filter.setSeance( nIdSeance );
			filter.setIdFormationConseil( nIdFormationConseil );
			filter.setTypeOrdreDuJour( TypeOrdreDuJourEnum.DEFINITIF.getId( ) );
			filter.setDateRetourControleDeLegalite( tsDateRetourControleDeLegalite );
			
			pdds = PDDHome.findByFilter( filter, plugin );
		}
		
		List<Seance> listeSeances = SeanceHome.findOldSeance( plugin );
		List<FormationConseil> listeFormationsConseil = FormationConseilHome.findFormationConseilList( plugin );
		
		model.put( MARK_LISTE_SEANCES, listeSeances );
		model.put( MARK_ID_SEANCE_SELECTED, nIdSeance );
		model.put( MARK_DATE_RETOUR_CONTROLE_LEGALITE, tsDateRetourControleDeLegalite );
		model.put( MARK_LISTE_FORMATION_CONSEIL, listeFormationsConseil );
		model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, nIdFormationConseil );
		model.put( MARK_LISTE_PDDS, pdds );
		model.put( OdsMarks.MARK_NOMBRE_RESULTATS, ( pdds == null ) ? 0 : pdds.size( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CERTIFICAT_AFFICHAGE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );

//        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
//                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
//        {
//            template = AppTemplateService.getTemplate( TEMPLATE_CERTIFICAT_LIST, getLocale(  ) );
//        }
//        else
//        {
//            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
//        }
//
//        return getAdminPage( template.getHtml(  ) );
		
    }

	/**
	 * Retourne l'ordre du jour sous format PDF ou Word
	 * @param request la requete Http
	 * @param response la reponse
	 */
	public void doVisualisationCertificat( HttpServletRequest request, HttpServletResponse response )
	{
		Plugin plugin = getPlugin(  );
		
		int nIdSeance = OdsUtils.getIntegerParameter( OdsParameters.ID_SEANCE, request );
		int nIdFormationConseil = OdsUtils.getIntegerParameter( OdsParameters.ID_FORMATION_CONSEIL, request );
		String strDateRetourControleDeLegalite = request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE );
		Timestamp tsDateRetourControleDeLegalite = OdsUtils.getDate( strDateRetourControleDeLegalite, true );
		
		Seance seance = SeanceHome.findByPrimaryKey( nIdSeance, plugin );
		FormationConseil formationConseil = FormationConseilHome.findByPrimaryKey( nIdFormationConseil, plugin );

		PDDFilter filter = new PDDFilter( );
		filter.setSeance( nIdSeance );
		filter.setIdFormationConseil( nIdFormationConseil );
		filter.setTypeOrdreDuJour( TypeOrdreDuJourEnum.DEFINITIF.getId( ) );
		filter.setDateRetourControleDeLegalite( tsDateRetourControleDeLegalite );
		
		List<PDD> pdds = PDDHome.findByFilter( filter, plugin );

		Timestamp currentTime = new Timestamp( System.currentTimeMillis(  ) );
		String strNomSecretaireGenerale = AppPropertiesService.getProperty( PROPERTY_NOM_SECRETAIRE_GENERALE );
		
		try
		{
			//DocumentManager.deployTemplateService("D:\\certificat.odt", "certificat");
			
			String strXml = XML_CERTIFICAT_OPEN; 
			strXml += XML_SECRETAIRE_GENERALE_OPEN + strNomSecretaireGenerale + XML_SECRETAIRE_GENERALE_CLOSE; 
			strXml += seance.getXml( request );
			strXml += XML_DATE_DU_JOUR_OPEN;
			strXml += currentTime.toString( );
			strXml += XML_DATE_DU_JOUR_CLOSE;
			strXml += formationConseil.getXml( request );
			for( PDD pdd : pdds )
			{
				strXml += pdd.getXml( request ) ;
			}
			strXml+=XML_CERTIFICAT_CLOSE;
			byte[] xml = ( CONSTANTE_ENTETE_XML + strXml ).getBytes( OdsConstants.UTF8 );
			byte[] bConvertedDoc = DocumentManager.convert( request, DocumentManager.PDF_CONVERSION, "certificat",	xml );
			
			if( bConvertedDoc != null && bConvertedDoc.length > 0)
            {
				OutputStream os = response.getOutputStream(  );
					String strFileName = "certificat.pdf";
					response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
					response.setContentType( "application/pdf" );
					response.setHeader( "Pragma", "public" );
					response.setHeader( "Expires", "0" );
					response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );
					os.write(bConvertedDoc);
					os.close(  );	
            }

		}
		catch ( Exception e )
		{
			AppLogService.error( e );
		}
	}
	
}
