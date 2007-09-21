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
package fr.paris.lutece.plugins.searchstats.service;

import fr.paris.lutece.plugins.searchstats.business.QueryRecord;
import fr.paris.lutece.plugins.searchstats.business.QueryRecordHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.search.QueryEvent;
import fr.paris.lutece.portal.service.search.QueryEventListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * QueryListener
 */
public class QueryListener implements QueryEventListener
{
    public void processQueryEvent(QueryEvent event)
    {
        QueryRecord record = new QueryRecord();
        Calendar calendar = new GregorianCalendar();
        record.setYear( calendar.get( Calendar.YEAR ));
        record.setMonth( calendar.get( Calendar.MONTH ) + 1);
        record.setDay( calendar.get( Calendar.DAY_OF_MONTH ));
        record.setHour( calendar.get( Calendar.HOUR ) + ( (calendar.get( Calendar.AM_PM ) == Calendar.AM ) ? 0 : 12 ));
        record.setQuery( event.getQuery());
        record.setResultsCount( event.getResultsCount() );
        
        Plugin plugin = PluginService.getPlugin(  SearchStatsPlugin.PLUGIN_NAME );
        QueryRecordHome.create( record , plugin );
                
    }
    
}
