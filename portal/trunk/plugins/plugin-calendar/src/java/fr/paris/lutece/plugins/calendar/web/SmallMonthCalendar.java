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
import fr.paris.lutece.plugins.calendar.service.Utils;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


/**
 * This class provides a Small Html Month calendar.
 */
public class SmallMonthCalendar
{
    // Templates
    private static final String TEMPLATE_VIEW_MONTH = "skin/plugins/calendar/small_month_calendar.html";
    private static final String TEMPLATE_WEEK = "skin/plugins/calendar/small_month_calendar_week.html";
    private static final String TEMPLATE_DAY = "skin/plugins/calendar/small_month_calendar_day.html";
    private static final String TEMPLATE_EMPTY_DAY = "skin/plugins/calendar/small_month_calendar_empty_day.html";

    /**
     * Provides a small HTML month calendar displaying days with links
     * @return The HTML code of the month.
     * @param options The options which contains displaying settings
     * @param strDate The code date defining the month to display
     * @param agenda An agenda to hilight some days.
     */
    public static String getSmallMonthCalendar( String strDate, Agenda agenda, CalendarUserOptions options )
    {
        HashMap model = new HashMap(  );
        Calendar calendar = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), 1 );

        int nDayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );

        if ( nDayOfWeek == 1 )
        {
            nDayOfWeek = 8;
        }

        StringBuffer sbWeeks = new StringBuffer(  );

        boolean bDone = false;
        boolean bStarted = false;

        while ( !bDone )
        {
            HashMap weekModel = new HashMap(  );

            //HtmlTemplate tWeek = new HtmlTemplate( templateWeek );
            StringBuffer sbDays = new StringBuffer(  );

            for ( int i = 0; i < 7; i++ )
            {
                if ( ( ( ( i + 2 ) != nDayOfWeek ) && !bStarted ) || bDone )
                {
                    sbDays.append( AppTemplateService.getTemplate( TEMPLATE_EMPTY_DAY ).getHtml(  ) );

                    continue;
                }
                else
                {
                    bStarted = true;
                }

                sbDays.append( getDay( calendar, agenda, options ) );

                int nDay = calendar.get( Calendar.DAY_OF_MONTH );
                calendar.roll( Calendar.DAY_OF_MONTH, true );

                int nNewDay = calendar.get( Calendar.DAY_OF_MONTH );

                if ( nNewDay < nDay )
                {
                    bDone = true;
                }
            }

            weekModel.put( Constants.MARK_DAYS, sbDays.toString(  ) );
            sbWeeks.append( AppTemplateService.getTemplate( TEMPLATE_WEEK, options.getLocale(  ), weekModel ).getHtml(  ) );
        }

        model.put( Constants.MARK_MONTH_LABEL, Utils.getMonthLabel( strDate, options.getLocale(  ) ) );
        model.put( Constants.MARK_DATE, strDate );
        model.put( Constants.MARK_WEEKS, sbWeeks.toString(  ) );
        model.put( Constants.MARK_JSP_URL, AppPropertiesService.getProperty( Constants.PROPERTY_RUNAPP_JSP_URL ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_MONTH, options.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Build the day view of a given
     * @return The HTML of a day
     * @param options The options which stores the display settings
     * @param calendar A calendar object positioned on a given day
     * @param agenda The agenda
     */
    private static String getDay( Calendar calendar, Agenda agenda, CalendarUserOptions options )
    {
        HashMap model = new HashMap(  );
        String strDate = Utils.getDate( calendar );
        String strLinkClass = AppPropertiesService.getProperty( Constants.PROPERTY_SMALLCALENDAR_LINKCLASS_NO_EVENT );

        if ( agenda.hasEvents( strDate ) )
        {
            strLinkClass = AppPropertiesService.getProperty( Constants.PROPERTY_SMALLCALENDAR_LINKCLASS_HAS_EVENTS );
        }

        model.put( Constants.MARK_LINK_CLASS, strLinkClass );
        model.put( Constants.MARK_DATE, strDate );
        model.put( Constants.MARK_DAY, calendar.get( Calendar.DAY_OF_MONTH ) );
        model.put( Constants.MARK_DAY_CLASS, getDayClass( calendar ) );
        model.put( Constants.MARK_JSP_URL, AppPropertiesService.getProperty( Constants.PROPERTY_RUNAPP_JSP_URL ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DAY, options.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Calculate the style class to render the day
     * @param calendar A calendar object positionned on the day to render
     * @return A CSS style
     */
    private static String getDayClass( Calendar calendar )
    {
        String strClass = Constants.STYLE_CLASS_SMALLMONTH_DAY;
        String strDate = Utils.getDate( calendar );
        String strToday = Utils.getDateToday(  );

        if ( Utils.isDayOff( calendar ) )
        {
            strClass += Constants.STYLE_CLASS_SUFFIX_OFF;
        }
        else if ( strDate.compareTo( strToday ) < 0 )
        {
            strClass += Constants.STYLE_CLASS_SUFFIX_OLD;
        }
        else if ( strDate.equals( strToday ) )
        {
            strClass += Constants.STYLE_CLASS_SUFFIX_TODAY;
        }

        return strClass;
    }
}
