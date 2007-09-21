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
package fr.paris.lutece.plugins.ods.web.horodatage;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import fr.paris.lutece.plugins.ods.service.horodatage.HorodatageHome;
import fr.paris.lutece.plugins.ods.service.horodatage.Trace;
import fr.paris.lutece.plugins.ods.service.horodatage.TraceNotification;
import fr.paris.lutece.plugins.ods.service.horodatage.TracePublication;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 *
 * HorodatageJspBean
 *
 */
public class HorodatageJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_HORODATAGE = "ODS_HORODATAGE";
    public static final String CONSTANTE_PUBLICATION = "Publication";
    public static final String CONSTANTE_NOTIFICATION = "Notification";
    private static final String TEMPLATE_LISTE_HORODATAGE = "admin/plugins/ods/horodatage/horodatage.html";
    private static final String MARK_DATE_START = "date_start";
    private static final String MARK_DATE_END = "date_end";
    private static final String MARK_TYPE_HORODATAGE = "type_horodatage";
    private static final String MESSAGE_ERROR_DATE = "ods.horodatage.message.errorDate";
    private static final String MESSAGE_ERROR_DOWNLOAD = "ods.horodatage.message.errorDownload";

    /* Variables de session */
    private Timestamp _dateStart;
    private Timestamp _dateEnd;

    /**
     * Retourne l'interface de gestion de l'horodatage
     *
     * @param request le requête HTTP
     * @return l'interface de gestion des directions
     */
    public String getHorodatage( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        model.put( MARK_DATE_START, _dateStart );
        model.put( MARK_DATE_END, _dateEnd );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_HORODATAGE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Permet de renvoyer le flux de données du fichier a telecharger
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return Le flux de données du fichier a telecharger
     */
    public String doDownloadCSV( HttpServletRequest request, HttpServletResponse response )
    {
        String dateStart = request.getParameter( MARK_DATE_START );
        String dateEnd = request.getParameter( MARK_DATE_END );
        String typeHorodatage = request.getParameter( MARK_TYPE_HORODATAGE );
        _dateStart = OdsUtils.getDate( dateStart, true );
        _dateEnd = OdsUtils.getDate( dateEnd, false );
        
        
        
        // Cas d'erreur
        if ( ( _dateStart == null ) || 
        		( _dateEnd == null ) ||
        		( _dateStart.after( _dateEnd ) ) )
        {
        		return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DATE, AdminMessage.TYPE_STOP );        	
        } 

        if ( typeHorodatage != null )
        {
            try
            {
                download( response, typeHorodatage, _dateStart, _dateEnd );
            }
            catch ( IOException e )
            {
                AppLogService.error( e );

                try
                {
                	response.sendRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_DOWNLOAD,
                			AdminMessage.TYPE_STOP ) );
                }
                catch ( IOException ioe )
                {
                    AppLogService.error( ioe );
                }
            }
        }
   

        return getHorodatage( request );
    }

    /**
     * Permet de créer le flux de données du fichier a télécharger
     *
     * @param response HttpServletResponse
     * @param typeHorodatage Type PUBLICATION ou NOTIFICATION
     * @param dateStart Filtre sur la date de début des traces
     * @param dateEnd Filtre sur la date de fin des traces
     * @throws IOException IOException
     */
    private void download( HttpServletResponse response, String typeHorodatage,
        Timestamp dateStart, Timestamp dateEnd ) throws IOException
    {
        byte[] fichier = getFichier( typeHorodatage, dateStart, dateEnd );

        if ( fichier != null )
        {
            String strFileName = "export.csv";

            response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
            response.setContentType( "application/text" );
            response.setHeader( "Pragma", "public" );
            response.setHeader( "Expires", "0" );
            response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );
            response.setContentLength( (int) fichier.length );

            OutputStream os = response.getOutputStream(  );
            os.write( fichier );
            os.close(  );
        }
    }

    /**
     * Permet de construire le fichier CSV
     *
     * @param typeHorodatage Type PUBLICATION ou NOTIFICATION
     * @param dateStart Filtre sur la date de début des traces
    * @param dateEnd Filtre sur la date de fin des traces
     * @return Tableau de byte représentant le fichier à télécharger
     */
    private byte[] getFichier( String typeHorodatage, Timestamp dateStart, Timestamp dateEnd )
    {
        StringBuffer fichier = new StringBuffer(  );

        List<Trace> traces = HorodatageHome.findAllTrace( getPlugin(  ), typeHorodatage, dateStart, dateEnd );

        int lTraces = ( traces != null ) ? traces.size(  ) : 0;
        writeColonnes( fichier, typeHorodatage );

        for ( int i = 0; i < lTraces; i++ )
        {
            Trace trace = traces.get( i );
            writeTrace( fichier, trace );

            if ( typeHorodatage.equals( CONSTANTE_PUBLICATION ) )
            {
                writeTracePublication( fichier, (TracePublication) trace );
            }
            else if ( typeHorodatage.equals( CONSTANTE_NOTIFICATION ) )
            {
                writeTraceNotification( fichier, (TraceNotification) trace );
            }

            fichier.append( "\n" );
        }

        return fichier.toString(  ).getBytes(  );
    }

    /**
     * Ecrit les données commune à toutes les traces dans le fichier à télécharger
     *
     * @param fichier StringBuffer
     * @param trace Trace
     */
    private void writeTrace( StringBuffer fichier, Trace trace )
    {
        fichier.append("\"").append( trace.getIdTrace(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getDateTrace(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getSignatures(  ) ).append("\"").append( ";" );
    }

    /**
     * Ecrit les données spécifique aux traces de type Notification dans le fichier à télécharger
     *
     * @param fichier StringBuffer
     * @param trace Trace
     */
    private void writeTraceNotification( StringBuffer fichier, TraceNotification trace )
    {
        fichier.append("\"").append( trace.getObjet(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( Base64.encodeBase64( trace.getContenu(  ).getBytes(  ) ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getIdUtilisateur(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getDateEnvoi(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getDestinataires(  ) ).append("\"").append( ";" );
    }

    /**
     * Ecrit les données spécifique aux traces de type Publication dans le fichier à télécharger
     *
     * @param fichier StringBuffer
     * @param trace Trace
     */
    private void writeTracePublication( StringBuffer fichier, TracePublication trace )
    {
        fichier.append("\"").append( trace.getFamilleDoc(  ).getId(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getIdDbOds(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getReference(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getDatePublication(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getIntitule(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getVersion(  ) ).append("\"").append( ";" );
        fichier.append("\"").append( trace.getAction(  ) ).append("\"").append( ";" );
    }

    /**
     * Ecrit les titres des colonnes pour un type de trace
     *
     * @param fichier StringBuffer
     * @param typeHorodatage Type PUBLICATION ou NOTIFICATION
     */
    private void writeColonnes( StringBuffer fichier, String typeHorodatage )
    {
        fichier.append( "\"id_trace\"" ).append( ";" );
        fichier.append( "\"date_trace\"" ).append( ";" );
        fichier.append( "\"signature\"" ).append( ";" );

        if ( typeHorodatage.equals( CONSTANTE_PUBLICATION ) )
        {
            fichier.append( "\"famille_doc\"" ).append( ";" );
            fichier.append( "\"id_db_ods\"" ).append( ";" );
            fichier.append( "\"reference\"" ).append( ";" );
            fichier.append( "\"date_publication\"" ).append( ";" );
            fichier.append( "\"intitule\"" ).append( ";" );
            fichier.append( "\"version\"" ).append( ";" );
            fichier.append( "\"action\"" ).append( ";" );
        }
        else if ( typeHorodatage.equals( CONSTANTE_NOTIFICATION ) )
        {
            fichier.append( "\"objet\"" ).append( ";" );
            fichier.append( "\"contenu\"" ).append( ";" );
            fichier.append( "\"id_utilisateur\"" ).append( ";" );
            fichier.append( "\"date_envoi\"" ).append( ";" );
            fichier.append( "\"destinataires\"" ).append( ";" );
        }

        fichier.append( "\n" );
    }
}
