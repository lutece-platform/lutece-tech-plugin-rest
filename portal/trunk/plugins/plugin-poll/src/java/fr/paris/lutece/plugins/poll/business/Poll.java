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
package fr.paris.lutece.plugins.poll.business;

import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents business object Poll
 */
public class Poll implements RBACResource, AdminWorkgroupResource
{
    public static final String RESOURCE_TYPE = "POLL";
    private int _nIdPoll;
    private String _strName;
    private boolean _bStatus;
    private String _strAdminWorkgroup;
    private ArrayList<PollQuestion> _questions = new ArrayList<PollQuestion>(  );

    /**
     * Return the identifier of this poll
     * @return the identifier as an int
     */
    public int getId(  )
    {
        return _nIdPoll;
    }

    /**
     * Sets the identifier of the poll with the specified int
     * @param nIdPoll the identifier as an int
     */
    public void setId( int nIdPoll )
    {
        _nIdPoll = nIdPoll;
    }

    /**
     * Returns the name of this poll as a String
     * @return The poll name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the poll with the specified String
     * @param strName The String poll name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Return the status of the poll. True means that the poll is active, false inactive.
     * @return The boolean wich describes the status of the poll.
     */
    public boolean getStatus(  )
    {
        return _bStatus;
    }

    /**
     * Sets the status of the poll
     * @param status The status of the poll
     */
    public void setStatus( boolean bStatus )
    {
        _bStatus = bStatus;
    }

    /**
     * Return the list of the questions
     * @return the list of the questions
     */
    public List<PollQuestion> getQuestions(  )
    {
        return _questions;
    }

    /**
     * RBAC resource implmentation
     * @return The resource type code
     */
    public String getResourceTypeCode(  )
    {
        return RESOURCE_TYPE;
    }

    /**
     * RBAC resource implmentation
     * @return The resourceId
     */
    public String getResourceId(  )
    {
        return "" + _nIdPoll;
    }

    /**
     * Returns the workgroup
     * @return The workgroup
     */
    public String getWorkgroup(  )
    {
        return _strAdminWorkgroup;
    }

    /**
     * Sets the workgroup
     * @param strWorkgroup The workgroup
     */
    public void setWorkgroup( String strAdminWorkgroup )
    {
        _strAdminWorkgroup = AdminWorkgroupService.normalizeWorkgroupKey( strAdminWorkgroup );
    }
}
