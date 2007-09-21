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

import fr.paris.lutece.plugins.reporting.business.ReportingProject;
import fr.paris.lutece.plugins.reporting.business.ReportingProjectHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class manages reporting page.
 */
public class ReportingApp implements XPageApplication
{
    // properties for page titles and path label
    private static final String PROPERTY_XPAGE_PAGETITLE = "reporting.xpage.pagetitle";
    private static final String PROPERTY_XPAGE_PATHLABEL = "reporting.xpage.pathlabel";
    private static final String PROPERTY_XPAGE_PATHLABEL_STANDARD = "reporting.xpage.pathlabelStandard";
    private static final String PROPERTY_XPAGE_PATHLABEL_MONTHLY = "reporting.xpage.pathlabelMonthly";

    //Marks
    private static final String MARK_REPORTING_PROJECT_LIST = "reporting_project_list";
    private static final String MARK_WORKGROUPS_LIST = "workgroups_list";

    //templates TEMPLATE_XPAGE_REPORTING_PROJECT_LIST_WITH_FICHE
    private static final String TEMPLATE_XPAGE_REPORTING_PROJECT_LIST_WITH_FICHE = "skin/plugins/reporting/reporting_project_list_with_fiche.html";
    private static final String TEMPLATE_XPAGE_REPORTING_PROJECT_LIST_WITH_MONTHLY = "skin/plugins/reporting/reporting_project_list_with_monthly.html";
    private static final String TEMPLATE_XPAGE_REPORTING = "skin/plugins/reporting/reporting.html";

    // request parameters
    private static final String PARAMETER_TABLE = "table";
    private static final String PARAMETER_TABLE_STANDARD = "standard";
    private static final String PARAMETER_TABLE_MONTHLY = "monthly";
    private static final String PARAMETER_WORKGROUP = "workgroup";

    /**
     * Returns the Reporting XPage content depending on the request parameters and the current mode.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
     * @param plugin The Plugin
     * @return The page content.
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );

        String strTable = request.getParameter( PARAMETER_TABLE );
        String[] listWorkgroup = request.getParameterValues( PARAMETER_WORKGROUP );

        page.setTitle( I18nService.getLocalizedString( PROPERTY_XPAGE_PAGETITLE, request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_XPAGE_PATHLABEL, request.getLocale(  ) ) );

        //	   display of xpage.
        if ( ( strTable == null ) || strTable.equals( "" ) )
        {
            page.setContent( getReportingXpageHome( request ) );
        }
        else if ( strTable.equals( PARAMETER_TABLE_STANDARD ) )
        {
            page.setPathLabel( I18nService.getLocalizedString( PROPERTY_XPAGE_PATHLABEL_STANDARD, request.getLocale(  ) ) );

            if ( listWorkgroup == null )
            {
                page.setContent( getReportingProjectListWithFiche( request, plugin, null ) );
            }
            else
            {
                page.setContent( getReportingProjectListWithFiche( request, plugin, listWorkgroup ) );
            }
        }

        else if ( strTable.equals( PARAMETER_TABLE_MONTHLY ) )
        {
            page.setPathLabel( I18nService.getLocalizedString( PROPERTY_XPAGE_PATHLABEL_MONTHLY, request.getLocale(  ) ) );
            page.setContent( getReportingProjectListWithMonthly( request, plugin ) );
        }

        return page;
    }

    /**
     *
     * @param request the request
     * @return the xpage home in html form.
     */
    private String getReportingXpageHome( HttpServletRequest request )
    {
        HashMap<String, ReferenceList> model = new HashMap<String, ReferenceList>(  );

        Collection<AdminWorkgroup> listWorkgroup = AdminWorkgroupHome.findAll(  );
        ReferenceList ref = new ReferenceList(  );

        for ( AdminWorkgroup current : listWorkgroup )
        {
            ref.addItem( current.getKey(  ), current.getDescription(  ) );
        }

        model.put( MARK_WORKGROUPS_LIST, ref );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_REPORTING, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     *
     * @param request The HTTP request.
     * @param plugin The Plugin
     * @param listWorkgroup The list of work group
     * @return the reporting Project list in html form.
     */
    private String getReportingProjectListWithFiche( HttpServletRequest request, Plugin plugin, String[] listWorkgroup )
    {
        HashMap<String, List<ReportingProject>> model = new HashMap<String, List<ReportingProject>>(  );

        List<ReportingProject> reportingProjectList = new ArrayList<ReportingProject>(  );

        if ( listWorkgroup == null )
        {
            reportingProjectList = ReportingProjectHome.getReportingProjectsListWithLastReportingFiche( plugin );
        }
        else
        {
            for ( String strCurrentWorkGroup : listWorkgroup )
            {
                reportingProjectList.addAll( ReportingProjectHome.getReportingProjectsListByWorkgroupWithLastReportingFiche( 
                        strCurrentWorkGroup, plugin ) );
            }
        }

        model.put( MARK_REPORTING_PROJECT_LIST, reportingProjectList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_REPORTING_PROJECT_LIST_WITH_FICHE,
                request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     *
     * @param request The HTTP request.
     * @param plugin The Plugin
     * @return the reporting Project list in html form.
     */
    private String getReportingProjectListWithMonthly( HttpServletRequest request, Plugin plugin )
    {
        HashMap<String, List<ReportingProject>> model = new HashMap<String, List<ReportingProject>>(  );
        List<ReportingProject> reportingProjectList = ReportingProjectHome.getReportingProjectsListWithLastReportingMonthly( plugin );

        model.put( MARK_REPORTING_PROJECT_LIST, reportingProjectList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_REPORTING_PROJECT_LIST_WITH_MONTHLY,
                request.getLocale(  ), model );

        return template.getHtml(  );
    }
}
