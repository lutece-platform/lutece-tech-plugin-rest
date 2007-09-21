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

import fr.paris.lutece.plugins.calendar.business.MultiAgenda;
import fr.paris.lutece.plugins.calendar.business.MultiAgendaEvent;
import fr.paris.lutece.plugins.calendar.service.Utils;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;


/**
 * This class provides a calendar view by Day.
 */
public class DayCalendarView implements CalendarView
{
    private static final String TEMPLATE_VIEW_DAY = "skin/plugins/calendar/calendar_view_day.html";
    private static final String TEMPLATE_VIEW_DAY_EVENT = "skin/plugins/calendar/calendar_view_day_event.html";

    /**
     * Returns the HTML view of the Month corresponding to the given date and displaying
     * events of a given agenda
     * @return The view in HTML
     * @param options The options
     * @param strDate The date code
     * @param agenda An agenda
     */
    public String getCalendarView( String strDate, MultiAgenda agenda, CalendarUserOptions options )
    {
        HashMap dayModel = new HashMap(  );
        StringBuffer sbEvents = new StringBuffer(  );

        if ( agenda.hasEvents( strDate ) )
        {
            List<MultiAgendaEvent> events = agenda.getEventsByDate( strDate );

            for ( MultiAgendaEvent event : events )
            {
                HashMap eventModel = new HashMap(  );
                HtmlUtils.fillEventTemplate( eventModel, event, strDate );

                HtmlTemplate tEvent = AppTemplateService.getTemplate( TEMPLATE_VIEW_DAY_EVENT, options.getLocale(  ),
                        eventModel );
                sbEvents.append( tEvent.getHtml(  ) );
            }
        }

        dayModel.put( Constants.MARK_EVENTS, sbEvents.toString(  ) );

        HtmlTemplate tDay = AppTemplateService.getTemplate( TEMPLATE_VIEW_DAY, options.getLocale(  ), dayModel );

        return tDay.getHtml(  );
    }

    /**
     * Returns the next code date corresponding to the current view and the current date
     * @param strDate The current date code
     * @return The next code date
     */
    public String getNext( String strDate )
    {
        return Utils.getNextDay( strDate );
    }

    /**
     * Returns the previous code date corresponding to the current view and the current date
     * @param strDate The current date code
     * @return The previous code date
     */
    public String getPrevious( String strDate )
    {
        return Utils.getPreviousDay( strDate );
    }

    /**
     * Returns the view title
     * @return The view title
     * @param options The options
     * @param strDate The current date code
     */
    public String getTitle( String strDate, CalendarUserOptions options )
    {
        return Utils.getDayLabel( strDate, options.getLocale(  ) );
    }

    /**
     * Returns the view path
     * @return The view path
     * @param options The options
     * @param strDate The current date code
     */
    public String getPath( String strDate, CalendarUserOptions options )
    {
        return getTitle( strDate, options );
    }

    /**
     * Returns the view type
     * @return The view type
     */
    public int getType(  )
    {
        return CalendarView.TYPE_DAY;
    }
}
