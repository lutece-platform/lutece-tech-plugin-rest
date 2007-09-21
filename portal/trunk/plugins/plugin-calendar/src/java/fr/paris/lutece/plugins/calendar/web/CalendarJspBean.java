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
package fr.paris.lutece.plugins.calendar.web;

import fr.paris.lutece.plugins.calendar.business.CalendarHome;
import fr.paris.lutece.plugins.calendar.business.SimpleEvent;
import fr.paris.lutece.plugins.calendar.service.AgendaResource;
import fr.paris.lutece.plugins.calendar.service.Utils;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.filesystem.DirectoryNotFoundException;
import fr.paris.lutece.util.filesystem.FileSystemUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingFormatArgumentException;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage calendars from the dataBase features ( manage, create, modify, remove)
 */
public class CalendarJspBean extends PluginAdminPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_CALENDAR = "CALENDAR_MANAGEMENT";

    //Templates
    private static final String TEMPLATE_MANAGE_CALENDARS = "admin/plugins/calendar/manage_calendars.html";
    private static final String TEMPLATE_CREATE_CALENDAR = "admin/plugins/calendar/create_calendar.html";
    private static final String TEMPLATE_MODIFY_CALENDAR = "admin/plugins/calendar/modify_calendar.html";
    private static final String TEMPLATE_CREATE_EVENT = "admin/plugins/calendar/create_event.html";
    private static final String TEMPLATE_MODIFY_EVENT = "admin/plugins/calendar/modify_event.html";

    // Jsp Definition
    private static final String JSP_DO_REMOVE_CALENDAR = "jsp/admin/plugins/calendar/DoRemoveCalendar.jsp";
    private static final String JSP_DO_REMOVE_EVENT = "jsp/admin/plugins/calendar/DoRemoveEvent.jsp";
    private static final String JSP_MODIFY_CALENDAR = "jsp/admin/plugins/calendar/ModifyCalendar.jsp?plugin_name=calendar&calendar_id=";
    private static final String JSP_EVENT_LIST = "ModifyCalendar.jsp?plugin_name=calendar&calendar_id=";

    // Message keys
    private static final String MESSAGE_CONFIRM_REMOVE_CALENDAR = "calendar.message.confirmRemoveCalendar";
    private static final String MESSAGE_CONFIRM_REMOVE_EVENT = "calendar.message.confirmRemoveEvent";
    private static final String EXT_IMAGE_FILES = ".png";
    private Plugin _plugin;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;

    /**
     * This class is used to handle back office management of database calendars.
     */
    public CalendarJspBean(  )
    {
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( Constants.PROPERTY_EVENTS_PER_PAGE, 5 );
    }

    /**
     * Returns calendars management form
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageCalendars( HttpServletRequest request )
    {
        setPageTitleProperty( Constants.PROPERTY_PAGE_TITLE_MANAGE_CALENDARS );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( Constants.PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        HashMap model = new HashMap(  );
        List<AgendaResource> listCalendar = CalendarHome.findAgendaResourcesList( _plugin );
        listCalendar = (List) AdminWorkgroupService.getAuthorizedCollection( listCalendar, getUser(  ) );

        Paginator paginator = new Paginator( listCalendar, _nItemsPerPage, getHomeUrl( request ),
                Constants.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( Constants.MARK_CALENDARS_LIST, listCalendar );
        model.put( Constants.MARK_PAGINATOR, paginator );
        model.put( Constants.MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CALENDARS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the Calendar creation form
     *
     * @param request The Http request
     * @return Html creation form
     */
    public String getCreateCalendar( HttpServletRequest request )
    {
        setPageTitleProperty( Constants.PROPERTY_PAGE_TITLE_CREATE_CALENDAR );

        HashMap model = new HashMap(  );
        ReferenceList ref = null;

        try
        {
            AdminUser adminUser = getUser(  );
            Locale locale = getLocale(  );
            ref = AdminWorkgroupService.getUserWorkgroups( adminUser, locale );
        }
        catch ( RuntimeException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }

        model.put( Constants.MARK_DOTS_LIST, getDotsList(  ) );
        model.put( Constants.MARK_WORKGROUPS_LIST, ref );
        model.put( Constants.MARK_ROLES_LIST, RoleHome.getRolesList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_CALENDAR, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process Calendar creation
     *
     * @param request The Http request
     * @return URL
     */
    public String doCreateCalendar( HttpServletRequest request )
    {
        String strName = request.getParameter( Constants.PARAMETER_CALENDAR_NAME );
        String strImage = request.getParameter( Constants.PARAMETER_CALENDAR_IMAGE );
        String strWorkgroup = request.getParameter( Constants.PARAMETER_WORKGROUP );

        // Mandatory field
        if ( strName.equals( "" ) || ( strImage == null ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        AgendaResource calendar = new AgendaResource(  );
        calendar.setName( strName );
        calendar.setEventImage( strImage );
        calendar.setEventPrefix( request.getParameter( Constants.PARAMETER_CALENDAR_PREFIX ) );
        calendar.setRole( request.getParameter( Constants.PARAMETER_CALENDAR_ROLE ) );
        calendar.setWorkgroup( strWorkgroup );
        calendar.setRoleManager( request.getParameter( Constants.PARAMETER_CALENDAR_ROLE_MANAGER ) );
        CalendarHome.createAgenda( calendar, _plugin );

        return getHomeUrl( request );
    }

    /**
     * Returns the form for calendar modification
     *
     * @param request The Http request
     * @return Html form
     */
    public String getModifyCalendar( HttpServletRequest request )
    {
        setPageTitleProperty( Constants.PROPERTY_PAGE_TITLE_MODIFY_CALENDAR );

        int nCalendarId = Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );
        String strSortEvents = request.getParameter( Constants.PARAMETER_SORT_EVENTS );
        strSortEvents = ( strSortEvents != null ) ? strSortEvents : "1";

        HashMap model = new HashMap(  );
        model.put( Constants.MARK_CALENDAR, CalendarHome.findAgendaResource( nCalendarId, _plugin ) );
        model.put( Constants.MARK_WORKGROUPS_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );
        model.put( Constants.MARK_DOTS_LIST, getDotsList(  ) );

        List listEvents = CalendarHome.findEventsList( nCalendarId, Integer.parseInt( strSortEvents ), _plugin );

        //paginator parameters
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Paginator paginator = new Paginator( listEvents, _nItemsPerPage,
                JSP_MODIFY_CALENDAR + nCalendarId + "&sort_events=" + Integer.parseInt( strSortEvents ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( Constants.MARK_PAGINATOR, paginator );
        model.put( Constants.MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( Constants.MARK_EVENTS_LIST, paginator.getPageItems(  ) );
        model.put( Constants.MARK_EVENTS_SORT_LIST, getSortEventList(  ) );
        model.put( Constants.MARK_DEFAULT_SORT_EVENT, Integer.parseInt( strSortEvents ) );
        model.put( Constants.MARK_ROLES_LIST, RoleHome.getRolesList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_CALENDAR, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the Calendar modifications
     *
     * @param request The Http request
     * @return Html form
     */
    public String doModifyCalendar( HttpServletRequest request )
    {
        String strName = request.getParameter( Constants.PARAMETER_CALENDAR_NAME );
        String strWorkgroup = request.getParameter( Constants.PARAMETER_WORKGROUP );
        String strImage = request.getParameter( Constants.PARAMETER_CALENDAR_IMAGE );

        // Mandatory field
        if ( strName.equals( "" ) || strImage.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        AgendaResource calendar = CalendarHome.findAgendaResource( Integer.parseInt( request.getParameter( 
                        Constants.PARAMETER_CALENDAR_ID ) ), _plugin );

        calendar.setName( strName );
        calendar.setEventImage( strImage );
        calendar.setEventPrefix( request.getParameter( Constants.PARAMETER_CALENDAR_PREFIX ) );
        calendar.setRole( request.getParameter( Constants.PARAMETER_CALENDAR_ROLE ) );
        calendar.setRoleManager( request.getParameter( Constants.PARAMETER_CALENDAR_ROLE_MANAGER ) );
        calendar.setWorkgroup( strWorkgroup );
        CalendarHome.updateAgenda( calendar, _plugin );

        return getHomeUrl( request );
    }

    /**
     * Returns the confirmation to remove the calendar
     *
     * @param request The Http request
     * @return the confirmation page
     */
    public String getConfirmRemoveCalendar( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_CALENDAR );
        url.addParameter( Constants.PARAMETER_CALENDAR_ID,
            Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CALENDAR, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Remove a calendar
     *
     * @param request The Http request
     * @return Html form
     */
    public String doRemoveCalendar( HttpServletRequest request )
    {
        int nCalendarId = Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );
        List<SimpleEvent> listEvents = CalendarHome.findEventsList( nCalendarId, 1, _plugin );

        for ( SimpleEvent event : listEvents )
        {
            CalendarHome.removeEvent( nCalendarId, event.getId(  ), _plugin );
        }

        CalendarHome.removeAgenda( nCalendarId, _plugin );

        // Go to the parent page
        return getHomeUrl( request );
    }

    /**
     * Returns the Event creation form
     *
     * @param request The Http request
     * @return Html creation form
     */
    public String getCreateEvent( HttpServletRequest request )
    {
        setPageTitleProperty( Constants.PROPERTY_PAGE_TITLE_CREATE_EVENT );

        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( Constants.PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        HashMap model = new HashMap(  );
        model.put( Constants.MARK_CALENDAR_ID,
            Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) ) );
        model.put( Constants.MARK_LOCALE, getLocale(  ).getLanguage(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_EVENT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process Event creation
     *
     * @param request The Http request
     * @return URL
     */
    public String doCreateEvent( HttpServletRequest request )
    {
        String strDate = request.getParameter( Constants.PARAMETER_EVENT_DATE );

        int nCalendarId = Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );

        // Mandatory field
        if ( strDate.equals( "" ) || request.getParameter( Constants.PARAMETER_EVENT_TITLE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        try
        {
            //put the date in form yyyMMdd
            strDate = String.format( "%3$2s%2$2s%1$2s", (Object[]) strDate.split( "/" ) ); //TODO Date handling must be unified
        }
        catch ( MissingFormatArgumentException e )
        {
            return AdminMessageService.getMessageUrl( request, Constants.PROPERTY_MESSAGE_DATEFORMAT,
                AdminMessage.TYPE_STOP );
        }

        SimpleEvent event = new SimpleEvent(  );
        event.setDate( strDate );

        String strTimeStart = request.getParameter( Constants.PARAMETER_EVENT_TIME_START );
        String strTimeEnd = request.getParameter( Constants.PARAMETER_EVENT_TIME_END );

        if ( !Utils.checkTime( strTimeStart ) || !Utils.checkTime( strTimeEnd ) )
        {
            return AdminMessageService.getMessageUrl( request, Constants.PROPERTY_MESSAGE_TIMEFORMAT,
                AdminMessage.TYPE_STOP );
        }

        event.setDateTimeStart( strTimeStart );
        event.setDateTimeEnd( strTimeEnd );
        event.setTitle( request.getParameter( Constants.PARAMETER_EVENT_TITLE ) );
        CalendarHome.createEvent( nCalendarId, event, _plugin );

        return JSP_EVENT_LIST + nCalendarId;
    }

    /**
     * Returns the form for event modification
     *
     * @param request The Http request
     * @return Html form
     */
    public String getModifyEvent( HttpServletRequest request )
    {
        setPageTitleProperty( Constants.PROPERTY_PAGE_TITLE_MODIFY_EVENT );

        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( Constants.PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        int nCalendarId = Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );
        String strEventId = request.getParameter( Constants.PARAMETER_EVENT_ID );
        int nIdEvent = Integer.parseInt( strEventId );

        HashMap model = new HashMap(  );
        model.put( Constants.MARK_EVENT, CalendarHome.findEvent( nIdEvent, _plugin ) );
        model.put( Constants.MARK_CALENDAR_ID, nCalendarId );
        model.put( Constants.MARK_DEFAULT_SORT_EVENT, request.getParameter( Constants.PARAMETER_SORT_EVENTS ) );
        model.put( Constants.MARK_LOCALE, getLocale(  ).getLanguage(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_EVENT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the Event modifications
     *
     * @param request The Http request
     * @return Html form
     */
    public String doModifyEvent( HttpServletRequest request )
    {
        int nCalendarId = Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );
        String strEventDate = request.getParameter( Constants.PARAMETER_EVENT_DATE );

        // Mandatory field
        if ( strEventDate.equals( "" ) || request.getParameter( Constants.PARAMETER_EVENT_TITLE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        try
        {
            //put the date in form yyyyMMdd
            strEventDate = String.format( "%3$2s%2$2s%1$2s", (Object[]) strEventDate.split( "/" ) ); //TODO Refactor the format
        }
        catch ( MissingFormatArgumentException e )
        {
            return AdminMessageService.getMessageUrl( request, Constants.PROPERTY_MESSAGE_DATEFORMAT,
                AdminMessage.TYPE_STOP );
        }

        SimpleEvent event = CalendarHome.findEvent( Integer.parseInt( request.getParameter( 
                        Constants.PARAMETER_EVENT_ID ) ), _plugin );
        event.setDate( strEventDate );

        String strTimeStart = request.getParameter( Constants.PARAMETER_EVENT_TIME_START );
        String strTimeEnd = request.getParameter( Constants.PARAMETER_EVENT_TIME_END );

        if ( !Utils.checkTime( strTimeStart ) || !Utils.checkTime( strTimeEnd ) )
        {
            return AdminMessageService.getMessageUrl( request, Constants.PROPERTY_MESSAGE_TIMEFORMAT,
                AdminMessage.TYPE_STOP );
        }

        event.setDateTimeStart( strTimeStart );
        event.setDateTimeEnd( strTimeEnd );
        event.setTitle( request.getParameter( Constants.PARAMETER_EVENT_TITLE ) );

        CalendarHome.updateEvent( nCalendarId, event, _plugin );

        return JSP_EVENT_LIST + nCalendarId + "&" + Constants.PARAMETER_SORT_EVENTS + "=" +
        request.getParameter( Constants.PARAMETER_SORT_EVENTS );
    }

    /**
     * Returns the confirmation to remove the event
     *
     * @param request The Http request
     * @return the confirmation page
     */
    public String getConfirmRemoveEvent( HttpServletRequest request )
    {
        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( Constants.PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        UrlItem url = new UrlItem( JSP_DO_REMOVE_EVENT );
        url.addParameter( Constants.PARAMETER_CALENDAR_ID,
            Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) ) );
        url.addParameter( Constants.PARAMETER_EVENT_ID,
            Integer.parseInt( request.getParameter( Constants.PARAMETER_EVENT_ID ) ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_EVENT, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Remove a event
     *
     * @param request The Http request
     * @return Html form
     */
    public String doRemoveEvent( HttpServletRequest request )
    {
        int nCalendarId = Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );
        CalendarHome.removeEvent( nCalendarId,
            Integer.parseInt( request.getParameter( Constants.PARAMETER_EVENT_ID ) ), _plugin );

        // Go to the parent page
        return getHomeUrl( request );
    }

    /**
     * Return a list of calendar dots
     *
     * @return A list of icons
     */
    private ReferenceList getDotsList(  )
    {
        String strDotsPath = AppPropertiesService.getProperty( Constants.PROPERTY_CALENDAR_DOTS_PATH );
        String strRootDirectory = AppPathService.getWebAppPath(  );
        ReferenceList listDots = new ReferenceList(  );

        try
        {
            List<File> listFiles = FileSystemUtil.getFiles( strRootDirectory, "/" + strDotsPath );

            for ( File file : listFiles )
            {
                String strFileName = file.getName(  );

                if ( strFileName.endsWith( EXT_IMAGE_FILES ) )
                {
                    String strPathFile = strDotsPath + strFileName;
                    listDots.addItem( strPathFile, strPathFile );
                }
            }
        }
        catch ( DirectoryNotFoundException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }

        return listDots;
    }

    /**
     * Return the list [(1, ascendant),(2,descendant)] that is used to sort the events date
     *
     * @return a refenceList
     */
    private ReferenceList getSortEventList(  )
    {
        ReferenceList list = new ReferenceList(  );
        list.addItem( 1, I18nService.getLocalizedString( Constants.PROPERTY_SORT_EVENTS + 1, getLocale(  ) ) );
        list.addItem( 2, I18nService.getLocalizedString( Constants.PROPERTY_SORT_EVENTS + 2, getLocale(  ) ) );

        return list;
    }
}
