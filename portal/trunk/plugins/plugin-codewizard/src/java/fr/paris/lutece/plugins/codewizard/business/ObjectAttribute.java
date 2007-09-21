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


/**
 * This class provide the ObjectAttribute object
 */
public class ObjectAttribute
{
    //Constants
    private String _strColumnName;
    private String _strJavaType;
    private String _strName;
    private String _strVariableName;

    /**
     * Creates a new ObjectAttribute object.
     * @param strColumnName The name of the column
     * @param strJavaType the java type
     */
    public ObjectAttribute( String strColumnName, String strJavaType )
    {
        _strColumnName = strColumnName;
        _strJavaType = strJavaType;
        _strName = getProperName( _strColumnName );
        _strVariableName = getPrefix( _strJavaType ) + _strName;
    }

    /**
     * Returns the Name
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Returns the ColumnName
     *
     * @return The ColumnName
     */
    public String getColumnName(  )
    {
        return _strColumnName;
    }

    /**
     * Returns the Type
     *
     * @return The Type
     */
    public String getType(  )
    {
        return _strJavaType;
    }

    /**
     * Returns the VariableName
     * @return  _strVariableName The VariableName
     */
    public String getVariableName(  )
    {
        return _strVariableName;
    }

    /**
     * Returns the Prefix of variable
     * @param strType the type of variable
     * @return prefix
     */
    private String getPrefix( String strType )
    {
        if ( strType.equalsIgnoreCase( "int" ) )
        {
            return "n";
        }

        if ( strType.equalsIgnoreCase( "String" ) )
        {
            return "str";
        }

        if ( strType.equalsIgnoreCase( "Date" ) )
        {
            return "date";
        }

        return "";
    }

    /**
     * Returns the Proper Name
     * @param strSource the source
     * @return source
     */
    public static String getProperName( String strSource )
    {
        int nIndex = 0;
        boolean bUpper = true;
        StringBuffer strBuffer = new StringBuffer(  );

        while ( nIndex < strSource.length(  ) )
        {
            char c = strSource.charAt( nIndex );

            if ( c == '_' )
            {
                // skip by reading the next char
                nIndex++;
                bUpper = true;
            }

            if ( bUpper )
            {
                String strChar = strSource.substring( nIndex, nIndex + 1 );
                c = strChar.toUpperCase(  ).charAt( 0 );
                bUpper = false;
            }

            strBuffer.append( c );
            nIndex++;
        }

        return strBuffer.toString(  );
    }
}
