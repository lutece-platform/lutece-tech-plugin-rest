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
package fr.paris.lutece.plugins.calendar.business.portlet;

import fr.paris.lutece.plugins.calendar.business.Event;
import fr.paris.lutece.plugins.calendar.service.AgendaResource;
import fr.paris.lutece.plugins.calendar.service.AgendaService;
import fr.paris.lutece.plugins.calendar.service.Utils;
import fr.paris.lutece.plugins.calendar.web.Constants;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents the business object CalendarPortlet.
 */
public class CalendarPortlet extends Portlet
{
    // The names of the XML tags
    private static final String TAG_CALENDAR_FILTERED_LIST = "calendar-filtered-list";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_AGENDA_ID = "agenda-id";
    private static final String TAG_AGENDA_NAME = "agenda-name";
    private static final String TAG_AGENDA_EVENT = "event";
    private static final String TAG_EVENT_DATE_LOCAL = "date-local";
    private static final String TAG_AGENDA_EVENT_TITLE = "event-title";
    private static final String TAG_AGENDA_EVENT_DATE = "event-date";

    /**
     * Sets the name of the plugin associated with this portlet.
     *
     * @param strPluginName The plugin name.
     */
    public void setPluginName( String strPluginName )
    {
        super.setPluginName( strPluginName );
    }

    /**
     * Returns the Xml code of the Archive portlet with XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the Archive portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Returns the Xml code of the Calendar portlet without XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the Archive portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );

        XmlUtil.beginElement( strXml, TAG_CALENDAR_FILTERED_LIST );

        // fetch all the calendars related to the specified portlet
        List<AgendaResource> listAgendasInPortlet = CalendarPortletHome.findAgendasInPortlet( this.getId(  ) );
        List<AgendaResource> listAuthorizedAgenda = new ArrayList<AgendaResource>(  );

        AgendaResource a = null;

        // Filter to find whether user is identified and authorized to view the agenda on the front office
        for ( AgendaResource agendaResource : listAgendasInPortlet )
        {
            if ( agendaResource != null )
            {
                a = agendaResource;
            }

            if ( a != null )
            {
                // Check security access
                String strRole = AgendaService.getInstance(  ).getAgendaResource( agendaResource.getId(  ) ).getRole(  );

                if ( ( strRole != null ) && ( !strRole.equals( "" ) ) && (!strRole.equals(Constants.PROPERTY_ROLE_NONE)))
                {
                    if ( SecurityService.getInstance(  ).isAuthenticationEnable(  ) )
                    {
                        if ( SecurityService.getInstance(  ).isUserInRole( request, strRole ) )
                        {
                            listAuthorizedAgenda.add( agendaResource );
                        }
                    }
                }
                else
                {
                    listAuthorizedAgenda.add( agendaResource );
                }
            }
        }

        //Add the events of the authorized agendas
        for ( AgendaResource agendaResource : listAuthorizedAgenda )
        {
            // Generate the XML code for the agendas :
            XmlUtil.beginElement( strXml, TAG_EVENTS );

            // ;
            String strAgendaId = agendaResource.getAgenda(  ).getKeyName(  );
            String strAgendaDesc =agendaResource.getAgenda().getName(  );

            //If nDays=0
            //Loop from today to afterwards

            //Else filter date from begin->end
            List<Event> listEvents;
            int nDays = CalendarPortletHome.getRepetitionDays( this.getId(  ) );

            if ( nDays <= 0 )
            {
                String strBeginDate;
                String strEndDate;
                strBeginDate = CalendarPortletHome.getBeginDate( this.getId(  ) );
                strEndDate = CalendarPortletHome.getEndDate( this.getId(  ) );
                listEvents = agendaResource.getAgenda(  ).getEventsByDate( strBeginDate, strEndDate );
            }
            else
            {
                String strToday = Utils.getDateToday(  );
                listEvents = agendaResource.getAgenda(  ).getEventsByDate( strToday,
                        Utils.getDayAfter( strToday, nDays ) );
            }

            for ( Event event : listEvents )
            {
                if ( ( event.getTitle(  ) != null ) && !event.getTitle(  ).equals( "" ) )
                {
                    XmlUtil.beginElement( strXml, TAG_AGENDA_EVENT );
                    XmlUtil.addElement( strXml, TAG_AGENDA_ID, strAgendaId );
                    XmlUtil.addElement( strXml,TAG_AGENDA_NAME, strAgendaDesc);
                    XmlUtil.addElement( strXml, TAG_AGENDA_EVENT_TITLE, event.getTitle(  ) );
                    XmlUtil.addElement( strXml, TAG_AGENDA_EVENT_DATE, event.getDate(  ) );

                    String strEventDate = event.getDate(  );
                    Calendar calendar = new GregorianCalendar(  );
                    calendar.set( Utils.getYear( strEventDate ), Utils.getMonth( strEventDate ),
                        Utils.getDay( strEventDate ) );

                    String strFormat = AppPropertiesService.getProperty( Constants.PROPERTY_LABEL_FORMAT_DATE_OF_DAY );
                    DateFormat formatDate = new SimpleDateFormat( strFormat, request.getLocale(  ) );
                    String strLocalizedDate = formatDate.format( calendar.getTime(  ) );
                    XmlUtil.addElement( strXml, TAG_EVENT_DATE_LOCAL, strLocalizedDate );
                    XmlUtil.endElement( strXml, TAG_AGENDA_EVENT );
                }
            }

            XmlUtil.endElement( strXml, TAG_EVENTS );
        }

        XmlUtil.endElement( strXml, TAG_CALENDAR_FILTERED_LIST );

        String str = addPortletTags( strXml );

        return str;
    }

    /**
     * Updates the current instance of the CalendarPortlet object
     */
    public void update(  )
    {
        CalendarPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the CalendarPortlet object
     */
    public void remove(  )
    {
        CalendarPortletHome.getInstance(  ).remove( this );
    }
}
