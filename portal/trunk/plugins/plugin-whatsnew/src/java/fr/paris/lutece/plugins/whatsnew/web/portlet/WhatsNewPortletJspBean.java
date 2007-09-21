/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.plugins.whatsnew.web.portlet;

import fr.paris.lutece.plugins.whatsnew.business.portlet.WhatsNewPortlet;
import fr.paris.lutece.plugins.whatsnew.business.portlet.WhatsNewPortletHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Actor Portlet
 */
public class WhatsNewPortletJspBean extends PortletJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    // Messages
    private static final String MESSAGE_MANDATORY_PORTLET_NB_ELEMENTS_MAX = "whatsnew.message.portlet.nbelementsmax.mandatory";
    private static final String MESSAGE_NOT_VALID_PORTLET_NB_ELEMENTS_MAX = "whatsnew.message.portlet.nbelementsmax.not.valid";
    private static final String MESSAGE_NEGATIVE_PORTLET_NB_ELEMENTS_MAX = "whatsnew.message.portlet.nbelementsmax.negative";
    
    
    // parameters
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_SHOW_DOCUMENTS = "checkbox_documents";
    private static final String PARAMETER_SHOW_PORTLETS = "checkbox_portlets";
    private static final String PARAMETER_SHOW_PAGES = "checkbox_pages";
    private static final String PARAMETER_PERIOD = "text_period";
    private static final String PARAMETER_NB_ELEMENTS_MAX = "text_nbElements";
    private static final String PARAMETER_ELEMENTS_ORDER = "display_order";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";

    // Marks
    private static final String MARK_COMBO_PERIOD = "combo_period";
    private static final String MARK_DEFAULT_PERIOD = "default_period";
    private static final String MARK_VALUE_DESC = "value_desc";
    private static final String MARK_VALUE_ASC = "value_asc";
    private static final String MARK_VALUE_ALPHA = "value_alpha";
    private static final String MARK_CHECKED_DOCUMENTS = "checked_documents";
    private static final String MARK_CHECKED_PORTLETS = "checked_portlets";
    private static final String MARK_CHECKED_PAGES = "checked_pages";
    private static final String MARK_NB_ELEMENTS_MAX = "nbElements";
    private static final String MARK_CHECKED_ORDER_DESC = "checked_desc";
    private static final String MARK_CHECKED_ORDER_ASC = "checked_asc";
    private static final String MARK_CHECKED_ORDER_ALPHA = "checked_alpha";
    private static final String MARK_TYPE_ARTICLE = "type_article";
    private static final String MARK_TYPE_FICHE = "type_permanent_article";
    private static final String MARK_TYPE_PORTLET = "type_portlet";
    private static final String MARK_TYPE_PAGE = "type_page";

    // properties
    private static final String PROPERTY_FRAGMENT_DAYS_COMBO_LIST = ".days.combo.list";
    private static final String PROPERTY_FRAGMENT_DAYS_COMBO_DEFAULT_VALUE = ".days.combo.default.value";

    // constants
    private static final String CONSTANT_CHECKED = "checked";
    private static final String CONSTANT_EMPTY_STRING = "";
    private static final String CONSTANT_DELIMITER_SEMI_COLON = ";";
    private static final String CONSTANT_DELIMITER_COMA = ",";

	

    /**
     * Returns portlet "what's new" 's creation form
     *
     * @param request request
     * @return Html form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletTypeId = request.getParameter( PARAMETER_PORTLET_TYPE_ID );

        HashMap model = new HashMap(  );
        String strPeriodByDefault = AppPropertiesService.getProperty( getPropertiesPrefix(  ) +
                PROPERTY_FRAGMENT_DAYS_COMBO_DEFAULT_VALUE );
        model.put( MARK_COMBO_PERIOD, getComboDays(  ) );
        model.put( MARK_DEFAULT_PERIOD, String.valueOf( Integer.parseInt( strPeriodByDefault ) ) );
        model = initializePortlet( model );

        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId, model );

        return template.getHtml(  );
    }

    /**
     * Returns portlet "what's new" 's modification form
     *
     * @param request request
     * @return Html form
     */
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        WhatsNewPortlet portlet = (WhatsNewPortlet) PortletHome.findByPrimaryKey( nPortletId );

        HashMap model = new HashMap(  );
        model.put( MARK_COMBO_PERIOD, getComboDays(  ) );
        model.put( MARK_DEFAULT_PERIOD, String.valueOf( portlet.getPeriod(  ) ) );
        model = initializePortlet( model );

        if ( portlet.getShowDocuments(  ) )
        {
            model.put( MARK_CHECKED_DOCUMENTS, CONSTANT_CHECKED );
        }
        else
        {
            model.put( MARK_CHECKED_DOCUMENTS, CONSTANT_EMPTY_STRING );
        }

        if ( portlet.getShowPortlets(  ) )
        {
            model.put( MARK_CHECKED_PORTLETS, CONSTANT_CHECKED );
        }
        else
        {
            model.put( MARK_CHECKED_PORTLETS, CONSTANT_EMPTY_STRING );
        }

        if ( portlet.getShowPages(  ) )
        {
            model.put( MARK_CHECKED_PAGES, CONSTANT_CHECKED );
        }
        else
        {
            model.put( MARK_CHECKED_PAGES, CONSTANT_EMPTY_STRING );
        }

        model.put( MARK_NB_ELEMENTS_MAX, String.valueOf( portlet.getNbElementsMax(  ) ) );

        if ( portlet.getElementsOrder(  ) == WhatsNewPortlet.ELEMENT_ORDER_DATE_DESC )
        {
            model.put( MARK_CHECKED_ORDER_DESC, CONSTANT_CHECKED );
            model.put( MARK_CHECKED_ORDER_ASC, CONSTANT_EMPTY_STRING );
            model.put( MARK_CHECKED_ORDER_ALPHA, CONSTANT_EMPTY_STRING );
        }
        else if ( portlet.getElementsOrder(  ) == WhatsNewPortlet.ELEMENT_ORDER_DATE_ASC )
        {
            model.put( MARK_CHECKED_ORDER_DESC, CONSTANT_EMPTY_STRING );
            model.put( MARK_CHECKED_ORDER_ASC, CONSTANT_CHECKED );
            model.put( MARK_CHECKED_ORDER_ALPHA, CONSTANT_EMPTY_STRING );
        }
        else if ( portlet.getElementsOrder(  ) == WhatsNewPortlet.ELEMENT_ORDER_ALPHA )
        {
            model.put( MARK_CHECKED_ORDER_DESC, CONSTANT_EMPTY_STRING );
            model.put( MARK_CHECKED_ORDER_ASC, CONSTANT_EMPTY_STRING );
            model.put( MARK_CHECKED_ORDER_ALPHA, CONSTANT_CHECKED );
        }
        else
        {
            model.put( MARK_CHECKED_ORDER_DESC, CONSTANT_CHECKED );
            model.put( MARK_CHECKED_ORDER_ASC, CONSTANT_EMPTY_STRING );
            model.put( MARK_CHECKED_ORDER_ALPHA, CONSTANT_EMPTY_STRING );
        }

        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml(  );
    }

    /**
     * Process whatsNewPortlet's creation
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doCreate( HttpServletRequest request )
    {
        WhatsNewPortlet portlet = new WhatsNewPortlet(  );

        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        int nIdPage = Integer.parseInt( strIdPage );

        String strNbElementsMax = request.getParameter( PARAMETER_NB_ELEMENTS_MAX );

        if ( ( strNbElementsMax == null ) || strNbElementsMax.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_PORTLET_NB_ELEMENTS_MAX,
                AdminMessage.TYPE_ERROR );
        }

        // check NbelementMax is of a valid format
        try
        {
            int nNbElementsMax = Integer.parseInt( strNbElementsMax );
            if ( nNbElementsMax <= 0 )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NEGATIVE_PORTLET_NB_ELEMENTS_MAX,
                    AdminMessage.TYPE_ERROR );
            }
        }
        catch ( NumberFormatException nb )
        {
            //the format of the identifier of the page is not valid
            return AdminMessageService.getMessageUrl( request, MESSAGE_NOT_VALID_PORTLET_NB_ELEMENTS_MAX,
                AdminMessage.TYPE_ERROR );
        }

        portlet = validatePortlet( portlet, request );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        // Creating portlet
        WhatsNewPortletHome.getInstance(  ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Process portlet's modification
     *
     * @param request request
     * @return The Jsp management URL of the process result
     */
    public String doModify( HttpServletRequest request )
    {
        // Getting portlet
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        WhatsNewPortlet portlet = (WhatsNewPortlet) PortletHome.findByPrimaryKey( nPortletId );

        String strNbElementsMax = request.getParameter( PARAMETER_NB_ELEMENTS_MAX );

        if ( ( strNbElementsMax == null ) || strNbElementsMax.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_PORTLET_NB_ELEMENTS_MAX,
                AdminMessage.TYPE_ERROR );
        }

        // check NbelementMax is of a valid format
        try
        {
        	int nNbElementsMax = Integer.parseInt( strNbElementsMax );
            if ( nNbElementsMax <= 0 )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NEGATIVE_PORTLET_NB_ELEMENTS_MAX,
                    AdminMessage.TYPE_ERROR );
            }
        }
        catch ( NumberFormatException nb )
        {
            //the format of the identifier of the page is not valid
            return AdminMessageService.getMessageUrl( request, MESSAGE_NOT_VALID_PORTLET_NB_ELEMENTS_MAX,
                AdminMessage.TYPE_ERROR );
        }

        portlet = validatePortlet( portlet, request );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // Modificating portlet
        portlet.update(  );

        // Returns page with new created portlet
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.whatsnew";
    }

    /**
     * Initialize the number of days' combo
     *
     * @return the html code of the combo
     */
    private ReferenceList getComboDays(  )
    {
        // Returns the list stored in the property file and the default value
        String strPropertyNameList = getPropertiesPrefix(  ) + PROPERTY_FRAGMENT_DAYS_COMBO_LIST;
        String strListe = AppPropertiesService.getProperty( strPropertyNameList );

        ReferenceList comboDaysList = new ReferenceList(  );

        StringTokenizer strTokSemiColon = new StringTokenizer( strListe, CONSTANT_DELIMITER_SEMI_COLON );

        while ( strTokSemiColon.hasMoreTokens(  ) )
        {
            StringTokenizer strTokComa = new StringTokenizer( strTokSemiColon.nextToken(  ), CONSTANT_DELIMITER_COMA );

            while ( strTokComa.hasMoreTokens(  ) )
            {
                comboDaysList.addItem( Integer.parseInt( strTokComa.nextToken(  ) ), strTokComa.nextToken(  ) );
            }
        }

        return comboDaysList;
    }

    private HashMap initializePortlet( HashMap model )
    {
        model.put( MARK_VALUE_DESC, String.valueOf( WhatsNewPortlet.ELEMENT_ORDER_DATE_DESC ) );
        model.put( MARK_VALUE_ASC, String.valueOf( WhatsNewPortlet.ELEMENT_ORDER_DATE_ASC ) );
        model.put( MARK_VALUE_ALPHA, String.valueOf( WhatsNewPortlet.ELEMENT_ORDER_ALPHA ) );

        return model;
    }

    /**
     * process the validation of the portlet
     *
     * @param portlet the portlet to modify and validate
     * @param request the request containing the informations to store in the portlet
     * @return WhatsNewPortlet the new portlet
     */
    private WhatsNewPortlet validatePortlet( WhatsNewPortlet portlet, HttpServletRequest request )
    {
        // Getting portlet's common attibuts
        setPortletCommonData( request, portlet );

        // Getting portlet's specific attibuts
        if ( request.getParameter( PARAMETER_SHOW_DOCUMENTS ) == null )
        {
            portlet.setShowDocuments( false );
        }
        else
        {
            portlet.setShowDocuments( true );
        }

        if ( request.getParameter( PARAMETER_SHOW_PORTLETS ) == null )
        {
            portlet.setShowPortlets( false );
        }
        else
        {
            portlet.setShowPortlets( true );
        }

        if ( request.getParameter( PARAMETER_SHOW_PAGES ) == null )
        {
            portlet.setShowPages( false );
        }
        else
        {
            portlet.setShowPages( true );
        }

        portlet.setPeriod( Integer.parseInt( request.getParameter( PARAMETER_PERIOD ) ) );
        portlet.setNbElementsMax( Integer.parseInt( request.getParameter( PARAMETER_NB_ELEMENTS_MAX ) ) );
        portlet.setElementsOrder( Integer.parseInt( request.getParameter( PARAMETER_ELEMENTS_ORDER ) ) );

        return portlet;
    }
}
