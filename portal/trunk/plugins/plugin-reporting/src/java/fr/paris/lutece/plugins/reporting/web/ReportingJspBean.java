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
package fr.paris.lutece.plugins.reporting.web;

import fr.paris.lutece.plugins.reporting.business.ReportingFiche;
import fr.paris.lutece.plugins.reporting.business.ReportingFicheHome;
import fr.paris.lutece.plugins.reporting.business.ReportingMonthly;
import fr.paris.lutece.plugins.reporting.business.ReportingMonthlyHome;
import fr.paris.lutece.plugins.reporting.business.ReportingPeriod;
import fr.paris.lutece.plugins.reporting.business.ReportingPeriodHome;
import fr.paris.lutece.plugins.reporting.business.ReportingProject;
import fr.paris.lutece.plugins.reporting.business.ReportingProjectHome;
import fr.paris.lutece.plugins.reporting.utils.ReportingUtils;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage reporting features ( manage,
 * create, modify, remove)
 */
public class ReportingJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_REPORTING_MANAGEMENT = "REPORTING_MANAGEMENT";
    public static final String RIGHT_PROJECT_MANAGEMENT = "REPORTING_PROJECT_MANAGEMENT";
    public static final String RIGHT_PERIOD_MANAGEMENT = "REPORTING_PERIOD_MANAGEMENT";

    //templates
    private static final String TEMPLATE_REPORTING_PERIOD = "admin/plugins/reporting/manage_reporting_period.html";
    private static final String TEMPLATE_MANAGE_REPORTING_PROJECT = "admin/plugins/reporting/manage_reporting_project.html";
    private static final String TEMPLATE_CREATE_REPROTING_PROJECT = "admin/plugins/reporting/create_reporting_project.html";
    private static final String TEMPLATE_MODIFY_REPROTING_PROJECT = "admin/plugins/reporting/modify_reporting_project.html";
    private static final String TEMPLATE_MANAGE_REPORTING = "admin/plugins/reporting/manage_reporting.html";
    private static final String TEMPLATE_PUBLISHING_REPROTING_MONTHLY = "admin/plugins/reporting/publishing_reporting_monthly.html";
    private static final String TEMPLATE_PUBLISHING_REPROTING_FICHE = "admin/plugins/reporting/publishing_reporting_fiche.html";
    private static final String TEMPLATE_HISTORY_MONTHLY_PROJECT = "admin/plugins/reporting/manage_history_monthly_project.html";
    private static final String TEMPLATE_HISTORY_FICHE_PROJECT = "admin/plugins/reporting/manage_history_fiche_project.html";
    private static final String TEMPLATE_DETAIL_FICHE_PROJECT = "admin/plugins/reporting/manage_detail_fiche_project.html";

    //message
    private static final String MESSAGE_CONFIRM_REMOVE_PROJECT = "reporting.message.confirmRemoveReportingProject";
    private static final String MESSAGE_CONFIRM_CREATE_PERIOD = "reporting.message.confirmCretaReportingPeriod";
    private static final String MESSAGE_CONFIRM_UPDATE_FICHE = "reporting.message.confirmUpdateReportingFiche";
    private static final String MESSAGE_CONFIRM_UPDATE_MONTHLY = "reporting.message.confirmUpdateReportingMonthly";
    private static final String MESSAGE_ERROR_NO_PERIOD_CREATED = "reporting.message.error.noPeriodCreated";
    private static final String MESSAGE_FIELDS_CATIA_NOT_NUMBER = "reporting.message.error.catiaNotNumber";

    //properties
    private static final String PROPERTY_REPORTING_PROJECT_PER_PAGE = "reporting.itemsPerPage";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PROJECT = "reporting.create_project.title";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PROJECT = "reporting.modify_project.title";
    private static final String PROPERTY_PAGE_TITLE_PUBLISHING = "reporting.manageReporting.Title";
    private static final String PROPERTY_RISK_ITEMS_NUMBER = "reporting.riskItemsNumber";
    private static final String PROPERTY_TENDENCY_ITEMS_NUMBER = "reporting.tendencyItemsNumber";
    private static final String PROPERTY_LABEL_RISK = "reporting.publishingReportingFiche.labelRisk";
    private static final String PROPERTY_LABEL_TENDENCY = "reporting.publishingReportingFiche.labelTendency";
    private static final String PROPERTY_RISK_DEFAULT_VALUE = "reporting.riskDefaultValue";
    private static final String PROPERTY_TENDENCY_DEFAULT_VALUE = "reporting.tendencyDefaultValue";

    //Markers
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_PUSH_REPORTING_PROJECT_LIST = "reporting_project_list";
    private static final String MARK_REPORTING_PERIOD_NAME = "reporting_period_name";
    private static final String MARK_REPORTING_PERIOD_LIST = "reporting_period_list";
    private static final String MARK_LAST_REPORTING_FICHE = "last_fiche";
    private static final String MARK_LAST_REPORTING_MONTHLY = "last_Monthly";
    private static final String MARK_REPORTING_PROJET = "reporting_project";
    private static final String MARK_REPORTING_FICHE = "reporting_fiche";
    private static final String MARK_REPORTING_FICHE_LIST = "reporting_fiche_list";
    private static final String MARK_REPORTING_MONTHLY_LIST = "reporting_monthly_list";
    private static final String MARK_PROJECT_ID = "project_id";
    private static final String MARK_PROJECT_NAME = "project_name";
    private static final String MARK_WORKGROUPS_LIST = "workgroups_list";
    private static final Object MARK_TENDENCY_REF_LIST = "tendency_list";
    private static final Object MARK_RISK_REF_LIST = "risk_list";

    //Jsp Definition
    private static final String JSP_DO_REMOVE_REPORTING_PROJECT = "jsp/admin/plugins/reporting/DoRemoveProject.jsp";
    private static final String JSP_DO_UPDATE_REPORTING_FICHE = "jsp/admin/plugins/reporting/DoUpdateFiche.jsp";
    private static final String JSP_DO_UPDATE_REPORTING_MONTHLY = "jsp/admin/plugins/reporting/DoUpdateMonthly.jsp";
    private static final String JSP_DO_CREATE_PERIOD = "jsp/admin/plugins/reporting/DoCreatePeriod.jsp";

    //parameters
    private static final String PARAMETER_PERIOD_ID = "period_id";
    private static final String PARAMETER_PROJECT_ID = "project_id";
    private static final String PARAMETER_FICHE_ID = "fiche_id";
    private static final String PARAMETER_MONTHLY_ID = "monthly_id";
    private static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";
    private static final String PARAMETER_REPORTING_PERIOD_NAME = "period_name";
    private static final String PARAMETER_REPORTING_PROJECT_NAME = "project_name";
    private static final String PARAMETER_REPORTING_PROJECT_ID_CATIA = "project_idCatia";
    private static final String PARAMETER_PROJECT_MANAGER_NAME = "project_manager_name";
    private static final String PARAMETER_FOLLOW_UP = "project_follow_up";
    private static final String PARAMETER_FOLLOW_UP_VALUE_ON = "on";
    private static final String PARAMETER_RISK = "risk";
    private static final String PARAMETER_TENDENCY = "tendency";
    private static final String PARAMETER_COMING_TASKS = "coming_tasks";
    private static final String PARAMETER_COMPLETED_TASKS = "completed_tasks";
    private static final String PARAMETER_CURRENT_TASKS = "current_tasks";
    private static final String PARAMETER_FACT = "monthly_fact";
    private static final String PARAMETER_EVENT = "monthly_event";
    private static final String PARAMETER_WORKGROUP = "workgroupKey";
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // other constants
    private static final String EMPTY_STRING = "";
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;

    /*-------------------------------MANAGEMENT  OF REPORTING-----------------------------*/

    /**
     * Return management reporting ( list of project ) form
     *@param request
     * The Http request
     * @return Html form
     */
    public String getManageReporting( HttpServletRequest request )
    {
        setPageTitleProperty( EMPTY_STRING );

        AdminUser adminUser = getUser(  );

        List<ReportingProject> listProject = ReportingProjectHome.getReportingProjectsList( getPlugin(  ) );

        HashMap model = new HashMap(  );
        model.put( MARK_PUSH_REPORTING_PROJECT_LIST,
            AdminWorkgroupService.getAuthorizedCollection( listProject, adminUser ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_REPORTING, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Return publiishing reporting form
     *@param request
     * The Http request
     * @return Html form
     */
    public String getPublishingReporting( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_PUBLISHING );

        int nProjectId = Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) );

        ReportingProject reportingProject = ReportingProjectHome.findByPrimaryKey( nProjectId, getPlugin(  ) );

        HashMap model = new HashMap(  );
        model.put( MARK_PROJECT_ID, nProjectId );
        model.put( MARK_PROJECT_NAME, reportingProject.getName(  ) );

        ReportingMonthly lastReportingMonthly = null;
        HtmlTemplate templateMonthly = new HtmlTemplate( EMPTY_STRING );

        //If a follow up monthly was ask for the project
        if ( reportingProject.getFollowUp(  ) )
        {
            ReportingPeriod reportingCurrentPeriod = ReportingPeriodHome.getReportingCurrentPeriod( getPlugin(  ) );

            lastReportingMonthly = ReportingMonthlyHome.getLastReportingMonthlyOfProject( nProjectId,
                    reportingCurrentPeriod.getIdPeriod(  ), getPlugin(  ) );

            ReportingPeriod reportingPeriod = ReportingPeriodHome.getReportingCurrentPeriod( getPlugin(  ) );
            model.put( MARK_LAST_REPORTING_MONTHLY, lastReportingMonthly );
            model.put( MARK_REPORTING_PERIOD_NAME, reportingPeriod.getName(  ) );
            model.put( MARK_RISK_REF_LIST, buildRiskRefList( request ) );
            model.put( MARK_TENDENCY_REF_LIST, buildTendencyRefList( request ) );

            templateMonthly = AppTemplateService.getTemplate( TEMPLATE_PUBLISHING_REPROTING_MONTHLY, getLocale(  ),
                    model );
        }

        ReportingFiche lastReportingFiche = ReportingFicheHome.getLastUpdateOfProject( nProjectId, getPlugin(  ) );

        model.put( MARK_LAST_REPORTING_FICHE, lastReportingFiche );
        model.put( MARK_RISK_REF_LIST, buildRiskRefList( request ) );
        model.put( MARK_TENDENCY_REF_LIST, buildTendencyRefList( request ) );

        HtmlTemplate templateFiche = AppTemplateService.getTemplate( TEMPLATE_PUBLISHING_REPROTING_FICHE,
                getLocale(  ), model );

        HtmlTemplate template = new HtmlTemplate( templateFiche.getHtml(  ) + templateMonthly.getHtml(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Return comfirm update reporting fiche form
     *@param request
     * The Http request
     * @return Html form
     */
    public String getConfirmUpdateReportingFiche( HttpServletRequest request )
    {
        String strCurrentTasks = request.getParameter( PARAMETER_CURRENT_TASKS );
        String strCompletedTasks = request.getParameter( PARAMETER_COMPLETED_TASKS );
        String strPrevuesTasks = request.getParameter( PARAMETER_COMING_TASKS );
        String strTendency = request.getParameter( PARAMETER_TENDENCY );
        String strRisk = request.getParameter( PARAMETER_RISK );

        String strCurrentTasksWithBr = ReportingUtils.convertNewLineToHtmlTag( strCurrentTasks );
        String strCompletedTasksWithBr = ReportingUtils.convertNewLineToHtmlTag( strCompletedTasks );
        String strComingTasksWithBr = ReportingUtils.convertNewLineToHtmlTag( strPrevuesTasks );

        if ( strTendency == null )
        {
            strTendency = AppPropertiesService.getProperty( PROPERTY_TENDENCY_DEFAULT_VALUE );
        }

        if ( strRisk == null )
        {
            strRisk = AppPropertiesService.getProperty( PROPERTY_RISK_DEFAULT_VALUE );
        }

        int nTendency = Integer.parseInt( strTendency );
        int nRisk = Integer.parseInt( strRisk );

        UrlItem url = new UrlItem( JSP_DO_UPDATE_REPORTING_FICHE );
        url.addParameter( PARAMETER_PROJECT_ID, Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) ) );
        url.addParameter( PARAMETER_TENDENCY, nTendency );
        url.addParameter( PARAMETER_RISK, nRisk );
        
        Map<String,String> formParameters = new HashMap<String,String>();
        formParameters.put(PARAMETER_CURRENT_TASKS, strCurrentTasksWithBr);
        formParameters.put(PARAMETER_COMPLETED_TASKS, strCompletedTasksWithBr );
        formParameters.put(PARAMETER_COMING_TASKS, strComingTasksWithBr);
        
        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_UPDATE_FICHE, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION, formParameters );

    }

    /**
     * Process update reporting fiche
     * @param request
     * The Http request
     * @return Html form
     */
    public String doUpdateReportingFiche( HttpServletRequest request )
    {
        int nIdProject = Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) );
        String strCurrentTasks = request.getParameter( PARAMETER_CURRENT_TASKS );
        String strCompletedTasks = request.getParameter( PARAMETER_COMPLETED_TASKS );
        String strComingTasks = request.getParameter( PARAMETER_COMING_TASKS );
        String strTendency = request.getParameter( PARAMETER_TENDENCY );
        String strRisk = request.getParameter( PARAMETER_RISK );

        int nTendency = Integer.parseInt( strTendency );
        int nRisk = Integer.parseInt( strRisk );
        
        ReportingFiche reportingFiche = new ReportingFiche(  );

        reportingFiche.setIdProject( nIdProject );
        reportingFiche.setCurrentTasks( strCurrentTasks );
        reportingFiche.setCompletedTasks( strCompletedTasks );
        reportingFiche.setComingTasks( strComingTasks );
        reportingFiche.setTendency( nTendency );
        reportingFiche.setRisk( nRisk );

        ReportingFicheHome.create( reportingFiche, getPlugin(  ) );

        //Go to the parent page
        return getHomeUrl( request );
    }

    /**
     * Return comfirm update reporting monthly form
     *@param request
     * The Http request
     * @return Html form
     */
    public String getConfirmUpdateReportingMonthly( HttpServletRequest request )
    {
        String strFact = request.getParameter( PARAMETER_FACT );
        String strEvent = request.getParameter( PARAMETER_EVENT );
        String strTendency = request.getParameter( PARAMETER_TENDENCY );
        String strRisk = request.getParameter( PARAMETER_RISK );

        if ( strTendency == null )
        {
            strTendency = AppPropertiesService.getProperty( PROPERTY_TENDENCY_DEFAULT_VALUE );
        }

        if ( strRisk == null )
        {
            strRisk = AppPropertiesService.getProperty( PROPERTY_RISK_DEFAULT_VALUE );
        }

        int nTendency = Integer.parseInt( strTendency );
        int nRisk = Integer.parseInt( strRisk );

        String strFactWithBr = ReportingUtils.convertNewLineToHtmlTag( strFact );
        String strEventWithBr = ReportingUtils.convertNewLineToHtmlTag( strEvent );


        UrlItem url = new UrlItem( JSP_DO_UPDATE_REPORTING_MONTHLY );
        url.addParameter( PARAMETER_PROJECT_ID, Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) ) );
        url.addParameter( PARAMETER_TENDENCY, nTendency );
        url.addParameter( PARAMETER_RISK, nRisk );
        
        Map<String,String> formParameters = new HashMap<String,String>();
        formParameters.put( PARAMETER_FACT, strFactWithBr );
        formParameters.put( PARAMETER_EVENT, strEventWithBr );      
        
        String strIdMonthly = request.getParameter( PARAMETER_MONTHLY_ID );

        if ( ( strIdMonthly == null ) || strIdMonthly.equals( EMPTY_STRING ) )
        {
            url.addParameter( PARAMETER_MONTHLY_ID, EMPTY_STRING );
        }
        else
        {
            url.addParameter( PARAMETER_MONTHLY_ID, strIdMonthly );
            url.addParameter( PARAMETER_PERIOD_ID, request.getParameter( PARAMETER_PERIOD_ID ) );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_UPDATE_MONTHLY,url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION, formParameters );

    }

    /**
     * Process update reporting Monthly
     * @param request
     * The Http request
     * @return Html form
     */
    public String doUpdateReportingMonthly( HttpServletRequest request )
    {
        String strIdMonthly = request.getParameter( PARAMETER_MONTHLY_ID );
        String strFact = request.getParameter( PARAMETER_FACT );
        String strEvent = request.getParameter( PARAMETER_EVENT );
        String strIdPeriod = request.getParameter( PARAMETER_PERIOD_ID );
        String strTendency = request.getParameter( PARAMETER_TENDENCY );
        String strRisk = request.getParameter( PARAMETER_RISK );

        int nTendency = Integer.parseInt( strTendency );
        int nRisk = Integer.parseInt( strRisk );

        int nIdProject = Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) );

        int nIdPeriod = 0;

        if ( strIdPeriod != null )
        {
            nIdPeriod = Integer.parseInt( strIdPeriod );
        }

        ReportingPeriod reportingCurrentPeriod = ReportingPeriodHome.getReportingCurrentPeriod( getPlugin(  ) );

        ReportingMonthly reportingMonthly = null;

        if ( ( strIdMonthly == null ) || strIdMonthly.equals( EMPTY_STRING ) )
        {
            reportingMonthly = new ReportingMonthly(  );

            reportingMonthly.setIdProject( nIdProject );
            reportingMonthly.setIdPeriod( reportingCurrentPeriod.getIdPeriod(  ) );
            reportingMonthly.setFact( strFact );
            reportingMonthly.setEvent( strEvent );
            reportingMonthly.setTendency( nTendency );
            reportingMonthly.setRisk( nRisk );

            ReportingMonthlyHome.create( reportingMonthly, getPlugin(  ) );
        }
        else if ( nIdPeriod != reportingCurrentPeriod.getIdPeriod(  ) )
        {
            reportingMonthly = new ReportingMonthly(  );

            reportingMonthly.setIdProject( nIdProject );
            reportingMonthly.setIdPeriod( reportingCurrentPeriod.getIdPeriod(  ) );
            reportingMonthly.setFact( strFact );
            reportingMonthly.setEvent( strEvent );
            reportingMonthly.setTendency( nTendency );
            reportingMonthly.setRisk( nRisk );

            ReportingMonthlyHome.create( reportingMonthly, getPlugin(  ) );
        }
        else
        {
            reportingMonthly = ReportingMonthlyHome.findByPrimaryKey( Integer.parseInt( request.getParameter( 
                            PARAMETER_MONTHLY_ID ) ), getPlugin(  ) );

            reportingMonthly.setFact( strFact );
            reportingMonthly.setEvent( strEvent );
            reportingMonthly.setTendency( nTendency );
            reportingMonthly.setRisk( nRisk );

            ReportingMonthlyHome.update( reportingMonthly, getPlugin(  ) );
        }

        //Go to the parent page
        return getHomeUrl( request );
    }

    /*-------------------------------HISTORIQUES OF PROJECT-----------------------------*/

    /**
     * Return History of project fiche form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getHistoryFicheProject( HttpServletRequest request )
    {
        int nIdProject = Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) );

        List<ReportingFiche> listReportingFiche = ReportingFicheHome.getAllReportingFicheOfProject( nIdProject,
                getPlugin(  ) );

        HashMap model = new HashMap(  );
        model.put( MARK_REPORTING_FICHE_LIST, listReportingFiche );
        model.put( MARK_PROJECT_ID, nIdProject );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_HISTORY_FICHE_PROJECT, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Return details of project fiche form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getDetailFicheProject( HttpServletRequest request )
    {
        int nIdFiche = Integer.parseInt( request.getParameter( PARAMETER_FICHE_ID ) );
        int nIdProject = Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) );

        ReportingFiche reportingFiche = ReportingFicheHome.findByPrimaryKey( nIdFiche, getPlugin(  ) );

        HashMap model = new HashMap(  );
        model.put( MARK_REPORTING_FICHE, reportingFiche );
        model.put( MARK_PROJECT_ID, nIdProject );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_DETAIL_FICHE_PROJECT, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Return history of project monthly form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getHistoryMonthlyProject( HttpServletRequest request )
    {
        int nIdProject = Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) );

        List<ReportingMonthly> listReportingMonthly = ReportingMonthlyHome.getAllReportingMonthlyOfProject( nIdProject,
                getPlugin(  ) );

        HashMap<String, List<ReportingMonthly>> model = new HashMap<String, List<ReportingMonthly>>(  );
        model.put( MARK_REPORTING_MONTHLY_LIST, listReportingMonthly );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_HISTORY_MONTHLY_PROJECT, getLocale(  ),
                model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /*-------------------------------MANAGEMENT OF PROJECT-----------------------------*/

    /**
     * Return management project (list of project) form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getManageReportingProject( HttpServletRequest request ) //Implement paginator
    {
        setPageTitleProperty( EMPTY_STRING );

        _nItemsPerPage = getItemsPerPage( request );
        _strCurrentPageIndex = getPageIndex( request );

        List<ReportingProject> listProject = ReportingProjectHome.getReportingProjectsList( getPlugin(  ) );

        listProject = (List<ReportingProject>) AdminWorkgroupService.getAuthorizedCollection( listProject, getUser(  ) );

        Paginator paginator = new Paginator( listProject, _nItemsPerPage, getHomeUrl( request ), PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex );
        HashMap model = new HashMap(  );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, EMPTY_STRING + _nItemsPerPage );
        model.put( MARK_PUSH_REPORTING_PROJECT_LIST, paginator.getPageItems(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_REPORTING_PROJECT, getLocale(  ),
                model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Return create project  form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getCreateReportingProject( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_PROJECT );

        HashMap<String, ReferenceList> model = new HashMap<String, ReferenceList>(  );

        model.put( MARK_WORKGROUPS_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_REPROTING_PROJECT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process create project  form
     * @param request
     * The Http request
     * @return Html form
     */
    public String doCreateReportingProject( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_REPORTING_PROJECT_NAME );
        String strIdCatia = request.getParameter( PARAMETER_REPORTING_PROJECT_ID_CATIA );
        String strProjectManager = request.getParameter( PARAMETER_PROJECT_MANAGER_NAME );
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP );
        String strFollowUp = request.getParameter( PARAMETER_FOLLOW_UP );

        boolean bFollowUp = false;

        if ( ( strFollowUp != null ) && strFollowUp.equals( PARAMETER_FOLLOW_UP_VALUE_ON ) )
        {
            ReportingPeriod reportingCurrentPeriod = ReportingPeriodHome.getReportingCurrentPeriod( getPlugin(  ) );

            if ( reportingCurrentPeriod == null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_NO_PERIOD_CREATED,
                    AdminMessage.TYPE_STOP );
            }

            bFollowUp = true;
        }

        // Mandatory field
        if ( ( strName == null ) || strName.equals( EMPTY_STRING ) || ( strProjectManager == null ) ||
                strProjectManager.equals( EMPTY_STRING ) || ( strIdCatia == null ) ||
                strIdCatia.equals( EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdCatia;

        try
        {
            nIdCatia = Integer.parseInt( strIdCatia );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FIELDS_CATIA_NOT_NUMBER, AdminMessage.TYPE_STOP );
        }

        ReportingProject reportingProject = new ReportingProject(  );
        reportingProject.setName( strName );
        reportingProject.setIdCatia( nIdCatia );
        reportingProject.setProjectManager( strProjectManager );
        reportingProject.setFollowUp( bFollowUp );
        reportingProject.setWorkgroupKey( strWorkgroupKey );

        ReportingProjectHome.create( reportingProject, getPlugin(  ) );

        //Go to the parent page
        return getHomeUrl( request );
    }

    /**
     * Return modify project  form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getModifyReportingProject( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_PROJECT );

        ReportingProject reportingProject = ReportingProjectHome.findByPrimaryKey( Integer.parseInt( 
                    request.getParameter( PARAMETER_PROJECT_ID ) ), getPlugin(  ) );

        HashMap model = new HashMap(  );

        model.put( MARK_WORKGROUPS_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );
        model.put( MARK_REPORTING_PROJET, reportingProject );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_REPROTING_PROJECT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process create project  form
     * @param request
     * The Http request
     * @return Html form
     */
    public String doModifyReportingProject( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_REPORTING_PROJECT_NAME );
        String strIdCatia = request.getParameter( PARAMETER_REPORTING_PROJECT_ID_CATIA );
        String strProjectManager = request.getParameter( PARAMETER_PROJECT_MANAGER_NAME );
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP );
        String strFollowUp = request.getParameter( PARAMETER_FOLLOW_UP );

        boolean bFollowUp = false;

        if ( ( strFollowUp != null ) && strFollowUp.equals( PARAMETER_FOLLOW_UP_VALUE_ON ) )
        {
            ReportingPeriod reportingCurrentPeriod = ReportingPeriodHome.getReportingCurrentPeriod( getPlugin(  ) );

            if ( reportingCurrentPeriod == null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_NO_PERIOD_CREATED,
                    AdminMessage.TYPE_STOP );
            }

            bFollowUp = true;
        }

        //      Mandatory field
        if ( ( strName == null ) || strName.equals( EMPTY_STRING ) || ( strProjectManager == null ) ||
                strProjectManager.equals( EMPTY_STRING ) || ( strIdCatia == null ) ||
                strIdCatia.equals( EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        ReportingProject reportingProject = ReportingProjectHome.findByPrimaryKey( Integer.parseInt( 
                    request.getParameter( PARAMETER_PROJECT_ID ) ), getPlugin(  ) );

        int nIdCatia;

        try
        {
            nIdCatia = Integer.parseInt( strIdCatia );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FIELDS_CATIA_NOT_NUMBER, AdminMessage.TYPE_STOP );
        }

        reportingProject.setName( strName );
        reportingProject.setIdCatia( nIdCatia );
        reportingProject.setProjectManager( strProjectManager );
        reportingProject.setFollowUp( bFollowUp );
        reportingProject.setWorkgroupKey( strWorkgroupKey );

        ReportingProjectHome.update( reportingProject, getPlugin(  ) );

        //Go to the parent page
        return getHomeUrl( request );
    }

    /**
     * Return confirm remove project form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getConfirmRemoveReportingProject( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_REPORTING_PROJECT );
        url.addParameter( PARAMETER_PROJECT_ID, Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PROJECT, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Process Remove a project
     * @param request The Http request
     * @return Html form
     */
    public String doRemoveReportingProject( HttpServletRequest request )
    {
        int nIdProject = Integer.parseInt( request.getParameter( PARAMETER_PROJECT_ID ) );
        ReportingProject reportingProject = ReportingProjectHome.findByPrimaryKey( nIdProject, getPlugin(  ) );

        List<ReportingFiche> listReportingFiche = ReportingProjectHome.getAllReportingFiche( nIdProject, getPlugin(  ) );

        // remove all fiche of project
        for ( ReportingFiche currentFiche : listReportingFiche )
        {
            ReportingFicheHome.remove( currentFiche.getIdFiche(  ), getPlugin(  ) );
        }

        List<ReportingMonthly> listReportingMonthly = ReportingProjectHome.getAllReportingMonthly( nIdProject,
                getPlugin(  ) );

        // remove all monthly reporting of project
        for ( ReportingMonthly currentMonthly : listReportingMonthly )
        {
            ReportingMonthlyHome.remove( currentMonthly.getIdMonthly(  ), getPlugin(  ) );
        }

        ReportingProjectHome.remove( reportingProject.getIdProject(  ), getPlugin(  ) );

        // Go to the parent page
        return getHomeUrl( request );
    }

    /*-------------------------------MANAGEMENT  OF PERIOD-----------------------------*/

    /**
     * Return management of project form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getManageReportingPeriod( HttpServletRequest request )
    {
        setPageTitleProperty( EMPTY_STRING );

        List<ReportingPeriod> listReportingPeriod = ReportingPeriodHome.getReportingPeriodsList( getPlugin(  ) );

        Collections.sort( listReportingPeriod, Collections.reverseOrder(  ) );

        HashMap<String, List<ReportingPeriod>> model = new HashMap<String, List<ReportingPeriod>>(  );

        model.put( MARK_REPORTING_PERIOD_LIST, listReportingPeriod );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_REPORTING_PERIOD, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Return create period form
     * @param request
     * The Http request
     * @return Html form
     */
    public String getConfirmCreatePeriod( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_REPORTING_PERIOD_NAME );

        //Mandatory field
        if ( ( strName == null ) || strName.equals( EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        String strNameEncode = ReportingUtils.encodeForURL( strName );

        UrlItem url = new UrlItem( JSP_DO_CREATE_PERIOD );
        url.addParameter( PARAMETER_REPORTING_PERIOD_NAME, strNameEncode );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CREATE_PERIOD, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Process create period form
     * @param request
     * The Http request
     * @return Html form
     */
    public String doCreatePeriod( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_REPORTING_PERIOD_NAME );

        List<ReportingPeriod> listReportingPeriod = ReportingPeriodHome.getReportingPeriodsList( getPlugin(  ) );

        for ( ReportingPeriod currentPeriod : listReportingPeriod )
        {
            currentPeriod.setCurrent( false );
            ReportingPeriodHome.update( currentPeriod, getPlugin(  ) );
        }

        ReportingPeriod period = new ReportingPeriod(  );
        period.setName( strName );
        period.setCurrent( true );

        ReportingPeriodHome.create( period, getPlugin(  ) );

        //Go to the parent page
        return getHomeUrl( request );
    }

    /*-------------------------------METHODS FOR PAGINATOR-----------------------------*/

    /**
    * Used by the paginator to fetch a number of items
    * @param request The HttpRequest
    * @return The number of items
    */
    private int getItemsPerPage( HttpServletRequest request )
    {
        int nItemsPerPage;
        String strItemsPerPage = request.getParameter( PARAMETER_ITEMS_PER_PAGE );

        if ( strItemsPerPage != null )
        {
            nItemsPerPage = Integer.parseInt( strItemsPerPage );
        }
        else
        {
            if ( _nItemsPerPage != 0 )
            {
                nItemsPerPage = _nItemsPerPage;
            }
            else
            {
                nItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_REPORTING_PROJECT_PER_PAGE, 10 );
            }
        }

        return nItemsPerPage;
    }

    /**
     * Fetches the page index
     * @param request The HttpRequest
     * @return The PageIndex
     */
    private String getPageIndex( HttpServletRequest request )
    {
        String strPageIndex = request.getParameter( PARAMETER_PAGE_INDEX );
        strPageIndex = ( strPageIndex != null ) ? strPageIndex : _strCurrentPageIndex;

        return strPageIndex;
    }

    /**
     *
     * @param request the request
     * @return the list of risk label
     */
    private ReferenceList buildRiskRefList( HttpServletRequest request )
    {
        ReferenceList riskList = new ReferenceList(  );
        int nNubersItemsRisk = Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_RISK_ITEMS_NUMBER ) );

        for ( int i = 0; i < nNubersItemsRisk; i++ )
        {
            ReferenceItem item = new ReferenceItem(  );
            String strCode = String.valueOf( i + 1 );
            item.setCode( strCode );
            item.setName( I18nService.getLocalizedString( PROPERTY_LABEL_RISK + strCode, request.getLocale(  ) ) );
            riskList.add( item );
        }

        return riskList;
    }

    /**
     *
     * @param request the request
     * @return the list of tendency label
     */
    private ReferenceList buildTendencyRefList( HttpServletRequest request )
    {
        ReferenceList tendencyList = new ReferenceList(  );
        int nNubersItemsTendency = Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_TENDENCY_ITEMS_NUMBER ) );

        for ( int i = 0; i < nNubersItemsTendency; i++ )
        {
            ReferenceItem item = new ReferenceItem(  );
            String strCode = String.valueOf( i + 1 );
            item.setCode( strCode );
            item.setName( I18nService.getLocalizedString( PROPERTY_LABEL_TENDENCY + strCode, request.getLocale(  ) ) );
            tendencyList.add( item );
        }

        return tendencyList;
    }
}
