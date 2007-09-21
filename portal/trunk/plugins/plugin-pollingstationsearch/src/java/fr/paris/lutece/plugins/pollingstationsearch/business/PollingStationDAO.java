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
package fr.paris.lutece.plugins.pollingstationsearch.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides data access methods for polling stations.
 */
public final class PollingStationDAO implements IPollingStationDAO
{
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = "SELECT polling_station_id, polling_station_urban_district, polling_station_number, polling_station_name, polling_station_location_complement, polling_station_addr_number, polling_station_addr, polling_station_addr_urban_district " +
        "FROM polling_station WHERE polling_station_id = ?";

    /**
     * Find a polling station by its primary key.
     *
     * @param plugin the plugin.
     * @param nPollingStationkey the primary key of the polling station.
     * @return The polling station.
     *
     */
    public PollingStation findByPrimaryKey( Plugin plugin, int nPollingStationkey )
    {
        PollingStation pollingStation = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nPollingStationkey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            pollingStation = new PollingStation(  );
            pollingStation.setId( daoUtil.getInt( 1 ) );
            pollingStation.setPollingStationUrbanDistrict( daoUtil.getString( 2 ) );
            pollingStation.setPollingStationNumber( daoUtil.getString( 3 ) );
            pollingStation.setPollingStationName( daoUtil.getString( 4 ) );
            pollingStation.setPollingStationLocationComplement( daoUtil.getString( 5 ) );
            pollingStation.setPollingStationAddrNumber( daoUtil.getString( 6 ) );
            pollingStation.setPollingStationAddr( daoUtil.getString( 7 ) );

            // transform the PollingStationAddrUrbanDistrict
            String strUrbanDistrict = daoUtil.getString( 8 ).trim(  );

            if ( strUrbanDistrict.length(  ) >= 5 )
            {
                if ( strUrbanDistrict.charAt( 3 ) == '0' )
                {
                    strUrbanDistrict = strUrbanDistrict.substring( 4 );
                }
                else
                {
                    strUrbanDistrict = strUrbanDistrict.substring( 3 );
                }
            }

            pollingStation.setPollingStationAddrUrbanDistrict( strUrbanDistrict );
        }

        daoUtil.free(  );

        return pollingStation;
    }
}
