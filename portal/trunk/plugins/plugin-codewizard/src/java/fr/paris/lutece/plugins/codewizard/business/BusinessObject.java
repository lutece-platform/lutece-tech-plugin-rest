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
package fr.paris.lutece.plugins.codewizard.business;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provide the Business Object object
 */
public class BusinessObject
{
    //Constants
    private String _strPackageName;
    private String _strClassName;
    private String _strTable;
    private String _strIdColumnName;
    private String _strPluginName;
    private ArrayList _attributes = new ArrayList(  );

    /**
     * Sets the PackageName
     *
     * @param strPackageName The PackageName
     */
    public void setPackageName( String strPackageName )
    {
        _strPackageName = strPackageName;
    }

    /**
     * Returns the PackageName
     *
     * @return The PackageName
     */
    public String getPackageName(  )
    {
        return _strPackageName;
    }

    /**
     * Sets the ClassName
     *
     * @param strClassName The ClassName
     */
    public void setClassName( String strClassName )
    {
        _strClassName = strClassName;
    }

    /**
     * Returns the ClassName
     *
     * @return The ClassName
     */
    public String getClassName(  )
    {
        return _strClassName;
    }

    /**
     * Sets the Table
     *
     * @param strTable The Table
     */
    public void setTable( String strTable )
    {
        _strTable = strTable;
    }

    /**
     * Returns the Table
     *
     * @return The Table
     */
    public String getTable(  )
    {
        return _strTable;
    }

    /**
     * Sets the IdColumnName
     *
     * @param strIdColumnName The IdColumnName
     */
    public void setIdColumnName( String strIdColumnName )
    {
        _strIdColumnName = strIdColumnName;
    }

    /**
     * Returns the IdColumnName
     *
     * @return The IdColumnName
     */
    public String getIdColumnName(  )
    {
        return _strIdColumnName;
    }

    /*
     * Add Attributes
     * @param attribute The ObjectAttribute
     */
    public void addAttribute( ObjectAttribute attribute )
    {
        _attributes.add( attribute );
    }

    /**
     * Returns the select Sql request
     * @return null
     */
    public String getSelectSQLRequest(  )
    {
        return null;
    }

    /**
     * Returns a collection of attributes
     * @return _attributes
     */
    public Collection getAttributes(  )
    {
        return _attributes;
    }

    /**
     * Returns the InstanceName
     *
     * @return The InstanceName
     */
    public String getInstanceName(  )
    {
        String strInstanceName = _strClassName.substring( 0, 1 ).toLowerCase(  ) +
            _strClassName.substring( 1, _strClassName.length(  ) );

        return strInstanceName;
    }

    /**
     * Returns the PluginName
     * @return The PluginName
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the PluginName
     * @param strPluginName The PluginName
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }
}
