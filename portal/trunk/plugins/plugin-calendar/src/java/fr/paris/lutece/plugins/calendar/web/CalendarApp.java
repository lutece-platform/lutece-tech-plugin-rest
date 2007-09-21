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

import fr.paris.lutece.plugins.calendar.business.Agenda;
import fr.paris.lutece.plugins.calendar.business.CalendarHome;
import fr.paris.lutece.plugins.calendar.business.MultiAgenda;
import fr.paris.lutece.plugins.calendar.business.SimpleEvent;
import fr.paris.lutece.plugins.calendar.service.AgendaResource;
import fr.paris.lutece.plugins.calendar.service.AgendaService;
import fr.paris.lutece.plugins.calendar.service.EventListService;
import fr.paris.lutece.plugins.calendar.service.Utils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.MissingFormatArgumentException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class is the main class of the XPage application of the plugin calendar.
 *
 */
public class CalendarApp implements XPageApplication
{
    //Templates
    private static final String TEMPLATE_CALENDAR = "skin/plugins/calendar/calendar.html";
    private static final String TEMPLATE_CALENDAR_LEGEND = "skin/plugins/calendar/calendar_legend.html";
    private static final String TEMPLATE_CALENDAR_MANAGE_EVENTS = "skin/plugins/calendar/calendar_manage_events.html";
    private static final String TEMPLATE_CREATE_EVENT_FRONT = "skin/plugins/calendar/create_event_front.html";
    private static final String TEMPLATE_MODIFY_EVENT_FRONT = "skin/plugins/calendar/modify_event_front.html";
    private static final String ACTION_MANAGE_EVENTS = "manage_events";
    private static final String ACTION_ADD_EVENT = "add_event";
    private static final String ACTION_MODIFY_EVENT = "modify_event";
    private static final String ACTION_REMOVE_EVENT = "remove_event";
    private static final String ACTION_DO_CREATE_EVENT = "do_create_event";
    private static final String ACTION_DO_MODIFY_EVENT = "do_modify_event";
    private static final String ACTION_DO_REMOVE_EVENT = "do_remove_event";
    private static final String PROPERTY_PLUGIN_NAME = "calendar";

    //Front Messages
    private static final String PROPERTY_CONFIRM_REMOVE_TITLE_MESSAGE = "calendar.siteMessage.confirmRemove.title";
    private static final String PROPERTY_CONFIRM_REMOVE_ALERT_MESSAGE = "calendar.siteMessage.confirmRemove.alertMessage";
    private static final String PROPERTY_INVALID_DATE_TITLE_MESSAGE = "calendar.siteMessage.invalidDate.title";
    private static final String PROPERTY_INVALID_DATE_MESSAGE = "calendar.siteMessage.invalidDate.alertMessage";
    private static final String PROPERTY_INVALID_TIME_TITLE_MESSAGE = "calendar.siteMessage.invalidTime.title";
    private static final String PROPERTY_INVALID_TIME_MESSAGE = "calendar.siteMessage.invalidTime.alertMessage";
    private static final String JSP_PAGE_PORTAL = "jsp/site/Portal.jsp";

    /**
     * Returns the content of the page Contact. It is composed by a form which to capture the data to send a message to
     * a contact of the portal.
     *
     * @return the Content of the page Contact
     * @param request The http request
     * @param nMode The current mode
     * @param plugin The plugin object
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException A message exception treated on front
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws SiteMessageException
    {
        XPage page = new XPage(  );

        String strAction = request.getParameter( Constants.PARAMETER_ACTION );
        String strPluginName = request.getParameter( Constants.PARAMETER_PAGE );
        String strCalendarId = request.getParameter( Constants.PARAMETER_CALENDAR_ID );
        plugin = PluginService.getPlugin( strPluginName );

        if ( ( strAction != null ) && strAction.equalsIgnoreCase( ACTION_MANAGE_EVENTS ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            //return unregistered form if unregistered user wants to acces application management form
            if ( ( user != null ) && ( user.getName(  ) != null ) && !user.getName(  ).equals( "" ) )
            {
                page.setContent( getManageEvents( strCalendarId, request, plugin ).getHtml(  ) );
                page.setTitle( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MANAGE_EVENTS,
                        request.getLocale(  ) ) );
                page.setPathLabel( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MANAGE_EVENTS,
                        request.getLocale(  ) ) );

                return page;
            }
        }

        if ( ( strAction != null ) && strAction.equalsIgnoreCase( ACTION_ADD_EVENT ) )
        { //return unregistered form if unregistered user wants to acces application management form

            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( ( user != null ) && ( user.getName(  ) != null ) && !user.getName(  ).equals( "" ) )
            {
                page.setContent( getCreateEvent( strCalendarId, request, plugin ).getHtml(  ) );
                page.setTitle( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_CREATE_EVENT,
                        request.getLocale(  ) ) );
                page.setPathLabel( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_CREATE_EVENT,
                        request.getLocale(  ) ) );

                return page;
            }
        }

        if ( ( strAction != null ) && strAction.equalsIgnoreCase( ACTION_DO_CREATE_EVENT ) )
        {
            //return unregistered form if unregistered user wants to acces application management form
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( ( user != null ) && ( user.getName(  ) != null ) && !user.getName(  ).equals( "" ) )
            {
                doCreateEvent( strCalendarId, request, plugin );
                page.setContent( getManageEvents( strCalendarId, request, plugin ).getHtml(  ) );
                page.setTitle( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MANAGE_EVENTS,
                        request.getLocale(  ) ) );
                page.setPathLabel( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MANAGE_EVENTS,
                        request.getLocale(  ) ) );

                return page;
            }
        }

        if ( ( strAction != null ) && strAction.equalsIgnoreCase( ACTION_MODIFY_EVENT ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( ( user != null ) && ( user.getName(  ) != null ) && !user.getName(  ).equals( "" ) )
            {
                page.setContent( getModifyEvent( request, plugin ) );
                page.setTitle( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MODIFY_EVENT,
                        request.getLocale(  ) ) );
                page.setPathLabel( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MODIFY_EVENT,
                        request.getLocale(  ) ) );

                return page;
            }
        }

        if ( ( strAction != null ) && strAction.equalsIgnoreCase( ACTION_DO_MODIFY_EVENT ) )
        {
            //return unregistered form if unregistered user wants to acces application management form
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( ( user != null ) && ( user.getName(  ) != null ) && !user.getName(  ).equals( "" ) )
            {
                doModifyEvent( request, plugin );
                page.setContent( getManageEvents( strCalendarId, request, plugin ).getHtml(  ) );
                page.setTitle( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MANAGE_EVENTS,
                        request.getLocale(  ) ) );
                page.setPathLabel( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MANAGE_EVENTS,
                        request.getLocale(  ) ) );

                return page;
            }
        }

        if ( ( strAction != null ) && strAction.equalsIgnoreCase( ACTION_DO_REMOVE_EVENT ) )
        {
            //return unregistered form if unregistered user wants to acces application management form
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( ( user != null ) && ( user.getName(  ) != null ) && !user.getName(  ).equals( "" ) )
            {
                int nCalendarId = Integer.parseInt( strCalendarId );
                CalendarHome.removeEvent( nCalendarId,
                    Integer.parseInt( request.getParameter( Constants.PARAMETER_EVENT_ID ) ), plugin );

                page.setContent( getManageEvents( strCalendarId, request, plugin ).getHtml(  ) );
                page.setTitle( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MANAGE_EVENTS,
                        request.getLocale(  ) ) );
                page.setPathLabel( I18nService.getLocalizedString( Constants.PROPERTY_PAGE_TITLE_MANAGE_EVENTS,
                        request.getLocale(  ) ) );

                return page;
            }
        }

        if ( ( strAction != null ) && strAction.equalsIgnoreCase( ACTION_REMOVE_EVENT ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            //return unregistered form if unregistered user wants to acces application management form
            if ( ( user != null ) && ( user.getName(  ) != null ) && !user.getName(  ).equals( "" ) )
            {
                getRemoveEvent( request );
            }
        }

        // Gets calendar infos from the request parameters and session
        CalendarView view = getView( request );
        EventList eventlist = getEventList( view.getType(  ) );
        MultiAgenda agenda = getAgenda( request );
        String strDate = getDate( request );
        CalendarUserOptions options = getUserOptions( request );

        // Load and fill the page template
        HashMap model = new HashMap(  );
        model.put( Constants.MARK_PREVIOUS, view.getPrevious( strDate ) );
        model.put( Constants.MARK_NEXT, view.getNext( strDate ) );
        model.put( Constants.MARK_TITLE, view.getTitle( strDate, options ) );
        model.put( Constants.MARK_DATE, strDate );
        model.put( Constants.MARK_LEGEND, getLegend( request, agenda, options ) );
        model.put( Constants.MARK_VIEW_CALENDAR, view.getCalendarView( strDate, agenda, options ) );
        model.put( Constants.MARK_SMALL_MONTH_CALENDAR1,
            SmallMonthCalendar.getSmallMonthCalendar( Utils.getPreviousMonth( strDate ), agenda, options ) );
        model.put( Constants.MARK_SMALL_MONTH_CALENDAR2,
            SmallMonthCalendar.getSmallMonthCalendar( strDate, agenda, options ) );
        model.put( Constants.MARK_SMALL_MONTH_CALENDAR3,
            SmallMonthCalendar.getSmallMonthCalendar( Utils.getNextMonth( strDate ), agenda, options ) );

        // Display event list if there is some events to display
        String strEventList = "";

        if ( ( eventlist != null ) && ( agenda.getAgendas(  ).size(  ) != 0 ) )
        {
            strEventList = eventlist.getEventList( strDate, agenda, options.getLocale(  ) );
        }

        model.put( Constants.MARK_EVENT_LIST, strEventList );

        String strRunAppJspUrl = AppPropertiesService.getProperty( Constants.PROPERTY_RUNAPP_JSP_URL );
        model.put( Constants.MARK_JSP_URL, strRunAppJspUrl );

        // Set XPage data
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CALENDAR, options.getLocale(  ), model );
        page.setContent( template.getHtml(  ) );
        page.setTitle( I18nService.getLocalizedString( Constants.PROPERTY_TITLE, options.getLocale(  ) ) +
            view.getTitle( strDate, options ) );
        page.setPathLabel( I18nService.getLocalizedString( Constants.PROPERTY_PATH, options.getLocale(  ) ) +
            view.getPath( strDate, options ) );

        return page;
    }

    /**
     * Get the date from the request parameter
     * @param request The HTTP request
     * @return The string date code
     */
    private String getDate( HttpServletRequest request )
    {
        String strDate = request.getParameter( Constants.PARAM_DATE );

        if ( !Utils.isValid( strDate ) )
        {
            strDate = Utils.getDateToday(  );
        }

        return strDate;
    }

    /**
     * Get the calendar view from the request parameter or stored in the session
     * @param request The HTTP request
     * @return A Calendar View object
     */
    private CalendarView getView( HttpServletRequest request )
    {
        CalendarView view = null;
        String strView = request.getParameter( Constants.PARAM_VIEW );

        if ( strView != null )
        {
            if ( strView.equals( Constants.VIEW_DAY ) )
            {
                view = new DayCalendarView(  );
            }
            else if ( strView.equals( Constants.VIEW_WEEK ) )
            {
                view = new WeekCalendarView(  );
            }
            else if ( strView.equals( Constants.VIEW_MONTH ) )
            {
                view = new MonthCalendarView(  );
            }
            else
            {
                // Default view
                view = new MonthCalendarView(  );
            }

            HttpSession session = request.getSession( true );
            session.setAttribute( Constants.ATTRIBUTE_CALENDAR_VIEW, view );
        }
        else
        {
            HttpSession session = request.getSession(  );
            CalendarView viewCurrentSession = (CalendarView) session.getAttribute( Constants.ATTRIBUTE_CALENDAR_VIEW );

            if ( viewCurrentSession != null )
            {
                view = viewCurrentSession;
            }
            else
            {
                // Default view
                view = new MonthCalendarView(  );
            }
        }

        return view;
    }

    /**
     * Gets the eventlist associated to the current view type
     * @param nViewType The View type
     * @return Return the eventlist defined in the calendar.properties
     */
    private EventList getEventList( int nViewType )
    {
        EventList eventlist = null;
        String strEventListKeyName = null;

        switch ( nViewType )
        {
            case CalendarView.TYPE_DAY:
                strEventListKeyName = AppPropertiesService.getProperty( Constants.PROPERTY_EVENTLIST_VIEW_DAY );

                break;

            case CalendarView.TYPE_WEEK:
                strEventListKeyName = AppPropertiesService.getProperty( Constants.PROPERTY_EVENTLIST_VIEW_WEEK );

                break;

            case CalendarView.TYPE_MONTH:
                strEventListKeyName = AppPropertiesService.getProperty( Constants.PROPERTY_EVENTLIST_VIEW_MONTH );

                break;

            default:
        }

        if ( strEventListKeyName != null )
        {
            eventlist = EventListService.getInstance(  ).getEventList( strEventListKeyName );
        }

        return eventlist;
    }

    /**
     * Get the agenda from the request parameter or stored in the session
     * @param request The HTTP request
     * @return An agenda object
     */
    private MultiAgenda getAgenda( HttpServletRequest request )
    {
        MultiAgenda agenda = null;
        String[] strAgendas = request.getParameterValues( Constants.PARAM_AGENDA );

        if ( strAgendas != null )
        {
            MultiAgenda agendaCombined = new MultiAgenda(  );

            for ( int i = 0; i < strAgendas.length; i++ )
            {
                String strAgendaKeyName = strAgendas[i];
                AgendaResource agendaResource = AgendaService.getInstance(  ).getAgendaResource( strAgendaKeyName );
                Agenda a = null;

                if ( agendaResource != null )
                {
                    a = agendaResource.getAgenda(  );
                }

                if ( a != null )
                {
                    // Check security access
                    String strRole = AgendaService.getInstance(  ).getAgendaResource( strAgendaKeyName ).getRole(  );

                    if ( ( strRole != null ) && ( !strRole.equals( "" ) ) && (!strRole.equals(Constants.PROPERTY_ROLE_NONE)) )
                    {
                        if ( SecurityService.getInstance(  ).isAuthenticationEnable(  ) )
                        {
                            if ( SecurityService.getInstance(  ).isUserInRole( request, strRole ) )
                            {
                                agendaCombined.addAgenda( a );
                            }
                        }
                    }
                    else
                    {
                        agendaCombined.addAgenda( a );
                    }
                }
            }

            agenda = agendaCombined;

            HttpSession session = request.getSession( true );
            session.setAttribute( Constants.ATTRIBUTE_CALENDAR_AGENDA, agenda );
        }
        else
        {
            HttpSession session = request.getSession(  );
            MultiAgenda agendaCurrentSession = (MultiAgenda) session.getAttribute( Constants.ATTRIBUTE_CALENDAR_AGENDA );

            if ( agendaCurrentSession != null )
            {
                agenda = agendaCurrentSession;
            }
            else
            {
                agenda = new MultiAgenda(  );
            }
        }

        return agenda;
    }

    /**
     * Get user optionsfrom the request parameter or stored in the session or in cookies
     * @param request The HTTP request
     * @return A CalendarUserOptions
     */
    private CalendarUserOptions getUserOptions( HttpServletRequest request )
    {
        CalendarUserOptions options = new CalendarUserOptions(  );
        options.setLocale( request.getLocale(  ) );
        options.setDayOffDisplayed( true );

        return options;
    }

    /**
     * Build the legend of all agenda selected
     * @param multiAgenda A multi agenda
     * @param options Options storing display settings
     * @param request The http request
     * @return The HTML code of the Legend
     */
    private String getLegend( HttpServletRequest request, MultiAgenda multiAgenda, CalendarUserOptions options )
    {
        String strLegend = "";

        if ( ( multiAgenda != null ) && !multiAgenda.getAgendas(  ).isEmpty(  ) )
        {
            HashMap model = new HashMap(  );

            List<Agenda> listAgendas = multiAgenda.getAgendas(  );
            List<AgendaResource> listAgendaResource = new ArrayList(  );

            String strReadWrite = AppPropertiesService.getProperty( Constants.PROPERTY_READ_WRITE );
            String strReadOnly = AppPropertiesService.getProperty( Constants.PROPERTY_READ_ONLY );

            for ( Agenda agenda : listAgendas )
            {
                AgendaResource agendaResource = AgendaService.getInstance(  ).getAgendaResource( agenda.getKeyName(  ) );
                String strRessourceType = agendaResource.getResourceType(  );
                String strManagerRole = agendaResource.getRoleManager(  );

                if ( strRessourceType.equalsIgnoreCase( strReadWrite ) )
                {
                    // Check security access
                    if ( ( strManagerRole != null ) && ( !strManagerRole.equals( "" ) )&& (!strManagerRole.equals(Constants.PROPERTY_ROLE_NONE))  )
                    {
                        if ( SecurityService.getInstance(  ).isAuthenticationEnable(  ) )
                        {
                            if ( !SecurityService.getInstance(  ).isUserInRole( request, strManagerRole ) )
                            {
                                agendaResource.setResourceType( strReadOnly );
                            }
                        }
                    }
                }

                listAgendaResource.add( agendaResource );
            }

            model.put( Constants.MARK_AGENDA_RESOURCE_LIST, listAgendaResource );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CALENDAR_LEGEND, options.getLocale(  ),
                    model );
            strLegend = template.getHtml(  );
        }

        return strLegend;
    }

    /**
     * Returns events management popup
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageEvents( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        //Filtering by management role and resource type
        //List all the agendas
        CalendarUserOptions options = getUserOptions( request );
        MultiAgenda multiAgenda = getAgenda( request );
        List<Agenda> listAgendas = multiAgenda.getAgendas(  );
        List<AgendaResource> listAgendaResource = new ArrayList(  );
        String strReadWrite = AppPropertiesService.getProperty( Constants.PROPERTY_READ_WRITE );

        for ( Agenda agenda : listAgendas )
        {
            AgendaResource agendaResource = AgendaService.getInstance(  ).getAgendaResource( agenda.getKeyName(  ) );
            String strRessourceType = agendaResource.getResourceType(  );
            String strManagerRole = agendaResource.getRoleManager(  );

            if ( strRessourceType.equals( strReadWrite ) )
            {
                // Check security access
                if ( ( strManagerRole != null ) && ( !strManagerRole.equals( "" ) )&& (!strManagerRole.equals(Constants.PROPERTY_ROLE_NONE))  )
                {
                    if ( SecurityService.getInstance(  ).isAuthenticationEnable(  ) )
                    {
                        if ( SecurityService.getInstance(  ).isUserInRole( request, strManagerRole ) )
                        {
                            listAgendaResource.add( agendaResource );
                        }
                    }
                }
            }
        }

        model.put( Constants.MARK_AGENDA_RESOURCE_LIST, listAgendaResource );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CALENDAR_MANAGE_EVENTS, options.getLocale(  ),
                model );
        template.getHtml(  );

        return template.getHtml(  );
    }

    /**
     * Returns the list of agenda resources which can be managed by a
     * an identified user
     *
     * @param request The Http request
     * @return A list of agenda resources
     */
    public List<AgendaResource> getListEvents( HttpServletRequest request )
    {
        MultiAgenda multiAgenda = getAgenda( request );
        List<Agenda> listAgendas = multiAgenda.getAgendas(  );
        List<AgendaResource> listAgendaResource = new ArrayList(  );
        String strReadWrite = AppPropertiesService.getProperty( Constants.PROPERTY_READ_WRITE );

        for ( Agenda agenda : listAgendas )
        {
            String strRessourceType = null;
            AgendaResource agendaResource = AgendaService.getInstance(  ).getAgendaResource( agenda.getKeyName(  ) );

            if ( agendaResource.getResourceType(  ) != null )
            {
                strRessourceType = agendaResource.getResourceType(  );
            }

            String strManagerRole = agendaResource.getRoleManager(  );

            if ( strRessourceType.equals( strReadWrite ) )
            {
                // Check security access
                if ( ( strManagerRole != null ) && ( !strManagerRole.equals( "" ) ) )
                {
                    if ( SecurityService.getInstance(  ).isAuthenticationEnable(  ) )
                    {
                        if ( SecurityService.getInstance(  ).isUserInRole( request, strManagerRole ) )
                        {
                            listAgendaResource.add( agendaResource );
                        }
                    }
                }
            }
        }

        return listAgendaResource;
    }

    /**
     * Returns the management of events related to a specific calendar
     * @param strCalendarId The identifier of the calendar
     * @param request The HttpRequest
     * @param _plugin The Plugin
     * @return The managemant form of events
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException The exception will be treated and handled by the front Message Service
     */
    private HtmlTemplate getManageEvents( String strCalendarId, HttpServletRequest request, Plugin _plugin )
        throws SiteMessageException
    {
        String strSortEvents = request.getParameter( Constants.PARAMETER_SORT_EVENTS );
        strSortEvents = ( strSortEvents != null ) ? strSortEvents : "1";

        List listEvents = CalendarHome.findEventsList( Integer.parseInt( strCalendarId ),
                Integer.parseInt( strSortEvents ), _plugin );
        HashMap model = new HashMap(  );
        AgendaResource agenda = CalendarHome.findAgendaResource( Integer.parseInt( strCalendarId ), _plugin );

        //Verify if user has rights
        verifyUserIsManager( request, agenda );

        //Fetch the name of the calendar to be modified
        model.put( Constants.MARK_CALENDAR,
            CalendarHome.findAgendaResource( Integer.parseInt( strCalendarId ), _plugin ) );
        model.put( Constants.MARK_EVENT_LIST, listEvents );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CALENDAR_MANAGE_EVENTS, request.getLocale(  ),
                model );

        return template;
    }

    /**
     * The form used to add an event to a calendar
     * @param strCalendarId The identifier of the calendar
     * @param request The HttpRequest
     * @param _plugin The Plugin
     * @return The addition form
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException The exception which is treated by the front Message mechanism
     */
    private HtmlTemplate getCreateEvent( String strCalendarId, HttpServletRequest request, Plugin _plugin )
        throws SiteMessageException
    {
        AgendaResource agenda = CalendarHome.findAgendaResource( Integer.parseInt( strCalendarId ), _plugin );
        HashMap model = new HashMap(  );

        //Verify if user has rights
        verifyUserIsManager( request, agenda );

        model.put( Constants.MARK_CALENDAR_ID, strCalendarId );
        model.put( Constants.MARK_LOCALE, request.getLocale(  ).getLanguage(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_EVENT_FRONT, request.getLocale(  ),
                model );

        return template;
    }

    /**
     * Method calling the modification form of an event
     * @return
     * @param request The HttpRequest object
     * @param _plugin The plugin
     */
    private String getModifyEvent( HttpServletRequest request, Plugin _plugin )
    {
        int nCalendarId = Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );
        String strEventId = request.getParameter( Constants.PARAMETER_EVENT_ID );
        int nEventId = Integer.parseInt( strEventId );
        HashMap model = new HashMap(  );
        model.put( Constants.MARK_EVENT, CalendarHome.findEvent( nEventId, _plugin ) );
        model.put( Constants.MARK_CALENDAR_ID, nCalendarId );
        model.put( Constants.MARK_DEFAULT_SORT_EVENT, request.getParameter( Constants.PARAMETER_SORT_EVENTS ) );
        model.put( Constants.MARK_LOCALE, request.getLocale(  ).getLanguage(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_EVENT_FRONT, request.getLocale(  ),
                model );

        return template.getHtml(  );
    }

    /**
     * The Creation action of an event
     * @param strCalendarId The identifier of the Calendar
     * @param request The HttpRequest
     * @param _plugin The Plugin
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException Exception used by the front Message mechanism
     */
    private void doCreateEvent( String strCalendarId, HttpServletRequest request, Plugin _plugin )
        throws SiteMessageException
    {
        AgendaResource agenda = CalendarHome.findAgendaResource( Integer.parseInt( strCalendarId ), _plugin );

        //Verify if user has rights
        verifyUserIsManager( request, agenda );

        //Creation of the event
        String strDate = request.getParameter( Constants.PARAMETER_EVENT_DATE );
        String strEventTitle = request.getParameter( Constants.PARAMETER_EVENT_TITLE );

        int nCalendarId = Integer.parseInt( strCalendarId );
        //Mandatory fields
        verifyFieldFilled( request, strDate );
        verifyFieldFilled( request, strEventTitle );

        try
        {
            //put the date in form yyyMMdd
            strDate = String.format( "%3$2s%2$2s%1$2s", (Object[]) strDate.split( "/" ) );
        }
        catch ( MissingFormatArgumentException e )
        {
            errorDateFormat( request );
        }

        SimpleEvent event = new SimpleEvent(  );
        event.setDate( strDate );

        String strTimeStart = request.getParameter( Constants.PARAMETER_EVENT_TIME_START );
        String strTimeEnd = request.getParameter( Constants.PARAMETER_EVENT_TIME_END );

        if ( !Utils.checkTime( strTimeStart ) || !Utils.checkTime( strTimeEnd ) )
        {
            errorTimeFormat( request );
        }

        event.setDateTimeStart( strTimeStart );
        event.setDateTimeEnd( strTimeEnd );
        event.setTitle( request.getParameter( Constants.PARAMETER_EVENT_TITLE ) );
        CalendarHome.createEvent( nCalendarId, event, _plugin );
    }

    /**
     * Method modifying the event
     *
     * @param request The request
     * @param _plugin The plugin
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException Exception used by the front Message mechanism
     */
    private void doModifyEvent( HttpServletRequest request, Plugin _plugin )
        throws SiteMessageException
    {
        int nCalendarId = Integer.parseInt( request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );
        String strEventId = request.getParameter( Constants.PARAMETER_EVENT_ID );
        String strEventDate = request.getParameter( Constants.PARAMETER_EVENT_DATE );
        int nEventId = Integer.parseInt( strEventId );

        verifyFieldFilled( request, strEventDate );

        try
        {
            //put the date in form yyyyMMdd
            strEventDate = String.format( "%3$2s%2$2s%1$2s", (Object[]) strEventDate.split( "/" ) );
        }
        catch ( MissingFormatArgumentException e )
        {
            errorDateFormat( request );
        }

        SimpleEvent event = CalendarHome.findEvent( nEventId, _plugin );
        event.setDate( strEventDate );

        String strTimeStart = request.getParameter( Constants.PARAMETER_EVENT_TIME_START );
        String strTimeEnd = request.getParameter( Constants.PARAMETER_EVENT_TIME_END );

        if ( !Utils.checkTime( strTimeStart ) || !Utils.checkTime( strTimeEnd ) )
        {
            errorTimeFormat( request );
        }

        event.setDateTimeStart( strTimeStart );
        event.setDateTimeEnd( strTimeEnd );
        event.setTitle( request.getParameter( Constants.PARAMETER_EVENT_TITLE ) );

        CalendarHome.updateEvent( nCalendarId, event, _plugin );
    }

    /**
     * Method verifying whether authenticated user is the manager of the calendar
     * @param request The Http Request
     * @param agenda The Agenda
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException Exception used by the front Message mechanism
     */
    private void verifyUserIsManager( HttpServletRequest request, AgendaResource agenda )
        throws SiteMessageException
    {
        if ( !SecurityService.getInstance(  ).isUserInRole( request, agenda.getRoleManager(  ) )  )
        {
            SiteMessageService.setMessage( request, Messages.USER_ACCESS_DENIED, new String[] { "" }, "", null, "",
                SiteMessage.TYPE_STOP );
        }
    }

    /**
     * Method verifies whether a mandatory field is filled
     * @param request The HttpRequest
     * @param strField The field to be checked
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException Exception used by the front Message mechanism
     */
    private void verifyFieldFilled( HttpServletRequest request, String strField )
        throws SiteMessageException
    {
        if ( ( strField == null ) || strField.equals( "" ) )
        {
            SiteMessageService.setMessage( request, Messages.MANDATORY_FIELDS, new String[] { "" }, "", null, "",
                SiteMessage.TYPE_STOP );
        }
    }

    /**
     * Verifies the date format
     * @param request The HttpRequest
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException Exception used by the front Message mechanism
     */
    private void errorDateFormat( HttpServletRequest request )
        throws SiteMessageException
    {
        SiteMessageService.setMessage( request, PROPERTY_INVALID_DATE_MESSAGE, null,
            PROPERTY_INVALID_DATE_TITLE_MESSAGE, null, null, SiteMessage.TYPE_STOP );
    }

    /**
     * Verifies the time format
     * @param request The HttpRequest
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException Exception used by the front Message mechanism
     */
    private void errorTimeFormat( HttpServletRequest request )
        throws SiteMessageException
    {
        SiteMessageService.setMessage( request, PROPERTY_INVALID_TIME_MESSAGE, null,
            PROPERTY_INVALID_TIME_TITLE_MESSAGE, null, null, SiteMessage.TYPE_STOP );
    }

    /**
     * The method calling the remove action
     *
     * @param request The HttpRequest
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException Exception used by the front Message mechanism
     */
    private void getRemoveEvent( HttpServletRequest request )
        throws SiteMessageException
    {
        UrlItem url = new UrlItem( JSP_PAGE_PORTAL );
        url.addParameter( Constants.PARAMETER_PAGE, PROPERTY_PLUGIN_NAME );
        url.addParameter( Constants.PARAMETER_ACTION, ACTION_DO_REMOVE_EVENT );
        url.addParameter( Constants.PARAMETER_CALENDAR_ID, request.getParameter( Constants.PARAMETER_CALENDAR_ID ) );
        url.addParameter( Constants.PARAMETER_EVENT_ID, request.getParameter( Constants.PARAMETER_EVENT_ID ) );
        SiteMessageService.setMessage( request, PROPERTY_CONFIRM_REMOVE_ALERT_MESSAGE, null,
            PROPERTY_CONFIRM_REMOVE_TITLE_MESSAGE, url.getUrl(  ), null, SiteMessage.TYPE_CONFIRMATION );
    }
}
