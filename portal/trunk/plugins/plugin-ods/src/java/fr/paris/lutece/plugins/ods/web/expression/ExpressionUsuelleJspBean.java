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
package fr.paris.lutece.plugins.ods.web.expression;

import fr.paris.lutece.plugins.ods.business.expression.ExpressionUsuelle;
import fr.paris.lutece.plugins.ods.business.expression.ExpressionUsuelleHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * ExpressionUsuelleJspBean
 */
public class ExpressionUsuelleJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_EXPRESSION_USUELLE = "ODS_EXPRESSIONS";
    private static final String TEMPLATE_LISTE_EXPRESSIONS = "admin/plugins/ods/expression/liste_expression_usuelle.html";
    private static final String TEMPLATE_SELECTION_EXPRESSIONS = "admin/plugins/ods/expression/selection_expression_usuelle.html";
    private static final String TEMPLATE_CREATION_EXPRESSION = "admin/plugins/ods/expression/creation_expression_usuelle.html";
    private static final String TEMPLATE_MODIFICATION_EXPRESSION = "admin/plugins/ods/expression/modification_expression_usuelle.html";
    private static final String PROPERTY_EXPRESSIONS_PER_PAGE = "ods.listeexpressionusuelle.label.line_per_page";
    private static final String PROPERTY_PAGE_CREATION_EXPRESSION = "ods.expressionusuelle.creation.pageTitle";
    private static final String PROPERTY_PAGE_MODIFICATION_EXPRESSION = "ods.expressionusuelle.modification.pageTitle";
    private static final String MARK_LISTE_EXPRESSIONS = "liste_expressions";
    private static final String MARK_LISTE_EXPRESSION_COUNT = "liste_expression_count";
    private static final String MARK_EXPRESSION = "expression";
    private static final String MESSAGE_CONFIRM_DELETE_EXPRESSION = "ods.message.confirmDeleteExpressionUsuelle";
    private static final String MESSAGE_CANNOT_DELETE_EXPRESSION = "ods.message.cannotDeleteExpression";
    private static final String JSP_URL_DO_SUPPRESSION_EXPRESSION = "jsp/admin/plugins/ods/expression/DoSuppressionExpressionUsuelle.jsp";
    private static final String FIELD_EXPRESSION = "ods.creationexpressionusuelle.label.libelle";

    // Attributs du Paginator
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_EXPRESSIONS_PER_PAGE, 10 );
    private String _strCurrentPageIndex;

    /**
     * Retourne l'interface de gestion des expressions usuelles.
     * @param request le requête HTTP
     * @return l'interface de gestion des expressions usuelles
     */
    public String getExpressionList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        List<ExpressionUsuelle> listExpression = ExpressionUsuelleHome.findExpressionList( getPlugin(  ) );

        Paginator paginator = new Paginator( listExpression, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_EXPRESSIONS, paginator.getPageItems(  ) );
        model.put( MARK_LISTE_EXPRESSION_COUNT, listExpression.size(  ) );
        model.put( OdsMarks.MARK_PAGINATOR, paginator );
        model.put( OdsMarks.MARK_NB_ITEMS_PER_PAGE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nItemsPerPage );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_EXPRESSIONS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Retourne l'interface de selection des expressions usuelles.
     * @param request le requête HTTP
     * @return l'interface de selection des expressions usuelles.
     */
    public String getSelectionExpression( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        List<ExpressionUsuelle> listExpression = ExpressionUsuelleHome.findExpressionList( getPlugin(  ) );

        Paginator paginator = new Paginator( listExpression, _nItemsPerPage,
                OrdreDuJourJspBean.JSP_SELECTION_PROPOSITION, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_EXPRESSIONS, paginator.getPageItems(  ) );
        model.put( OdsMarks.MARK_NOMBRE_RESULTATS, listExpression.size(  ) );
        model.put( OdsMarks.MARK_PAGINATOR, paginator );
        model.put( OdsMarks.MARK_NB_ITEMS_PER_PAGE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nItemsPerPage );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECTION_EXPRESSIONS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Retourne l'interface de création d'une expression usuelle.
     * @param request le requête HTTP
     * @return l'interface de création d'une expression usuelle
     */
    public String getCreationExpression( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_EXPRESSION );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATION_EXPRESSION, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Crée une expression usuelle.
     * Si le champ obligatoire est renseigné,
     * la méthode retourne l'url de gestion des expressions usuelles.
     * Sinon la méthode retourne l'url affichant les messages d'erreur
     * @param request la requête HTTP
     * @return une url
     */
    public String doCreationExpression( HttpServletRequest request )
    {
        if ( !requiredField( request ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( requiredField( request ), getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        ExpressionUsuelle expression = new ExpressionUsuelle(  );

        String strExpression = request.getParameter( OdsParameters.EXPRESSION ).trim(  );

        if ( strExpression.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        expression.setExpression( strExpression );

        ExpressionUsuelleHome.create( expression, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Retourne l'interface de modification d'une expression usuelle.
     * @param request le requête HTTP
     * @return l'interface de modification d'une expression usuelle
     */
    public String getModificationExpression( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_EXPRESSION );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( request.getParameter( OdsParameters.ID_EXPRESSION ) == null )
        {
            return getExpressionList( request );
        }

        int nIdExpression = -1;

        try
        {
            nIdExpression = Integer.parseInt( request.getParameter( OdsParameters.ID_EXPRESSION ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        ExpressionUsuelle expression = ExpressionUsuelleHome.findByPrimaryKey( nIdExpression, getPlugin(  ) );

        model.put( MARK_EXPRESSION, expression );

        template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_EXPRESSION, getLocale(  ), model );

        return ( getAdminPage( template.getHtml(  ) ) );
    }

    /**
     * Modifie une expression usuelle.
     * Si le champ obligatoire est renseigné,
     * la méthode retourne l'url de gestion des expressions usuelles.
     * Sinon la méthode retourne l'url affichant les messages d'erreur
     * @param request la requête HTTP
     * @return une url
     */
    public String doModificationExpression( HttpServletRequest request )
    {
        if ( !requiredField( request ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( requiredField( request ), getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        ExpressionUsuelle expression = new ExpressionUsuelle(  );

        int nIdExpression = -1;

        try
        {
            nIdExpression = Integer.parseInt( request.getParameter( OdsParameters.ID_EXPRESSION ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        expression.setIdExpression( nIdExpression );

        String strExpression = request.getParameter( OdsParameters.EXPRESSION ).trim(  );

        if ( strExpression.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        expression.setExpression( strExpression );

        ExpressionUsuelleHome.update( expression, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Retourne l'interface de suppression d'une expression usuelle.
     * @param request le requête HTTP
     * @return l'interface de création d'une expression usuelle
     */
    public String getSuppressionExpression( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_EXPRESSION ) == null )
        {
            return getHomeUrl( request );
        }

        String strIdExpression = request.getParameter( OdsParameters.ID_EXPRESSION );
        UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_EXPRESSION );
        url.addParameter( OdsParameters.ID_EXPRESSION, strIdExpression );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_EXPRESSION, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Supprime une expression usuelle.
     * @param request la requête HTTP
     * @return url de gestion des expressions usuelles
     */
    public String doSuppressionExpression( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_EXPRESSION ) == null )
        {
            return getHomeUrl( request );
        }

        int nIdExpression;

        try
        {
            nIdExpression = Integer.parseInt( request.getParameter( OdsParameters.ID_EXPRESSION ) );
        }
        catch ( NumberFormatException nbe )
        {
            return getHomeUrl( request );
        }

        try
        {
            ExpressionUsuelle expression = ExpressionUsuelleHome.findByPrimaryKey( nIdExpression, getPlugin(  ) );
            ExpressionUsuelleHome.remove( expression, getPlugin(  ) );
        }
        catch ( AppException ae )
        {
            if ( ae.getInitialException(  ) instanceof SQLException )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_EXPRESSION,
                    AdminMessage.TYPE_STOP );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * Indique si la requête est valide, c'est à dire que tous les champs obligatoires ont été remplis.
     * @param request la requête HTTP
     * @return true si la requête est valide, false sinon
     */
    private String requiredField( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.EXPRESSION ).equals( null ) ||
                request.getParameter( OdsParameters.EXPRESSION ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            return FIELD_EXPRESSION;
        }

        return OdsConstants.CONSTANTE_CHAINE_VIDE;
    }
}
