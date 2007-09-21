/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.plugins.projeau.service.output;

import fr.paris.lutece.plugins.formengine.service.output.FileIdGenerator;
import fr.paris.lutece.plugins.formengine.web.Form;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * Transaction Id generator used by default by formengine
 */
public class ProjeauFileIdGenerator extends FileIdGenerator
{
    //properties
    private static final String PROPERTY_FRAGMENT_PATH_TRANSACTION_ID = ".path.transaction.ids";
    private static final String PROPERTY_FRAGMENT_PREFIX_ID = ".id.prefix";
        
    /**
     *
     */
    public ProjeauFileIdGenerator(  )
    {
    }

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.IdGenerator#init(fr.paris.lutece.plugins.formengine.web.Form)
     */
    protected void init( Form form )
    {
        setDirectoryPathProperty( form.getName(  ) + PROPERTY_FRAGMENT_PATH_TRANSACTION_ID );
        setFileName( form.getInstanceName(  ) + ".id" );
    }

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.IdGenerator#updateId(fr.paris.lutece.plugins.formengine.web.Form, java.lang.String)
     */
    protected String updateId( Form form, String strCurrentId )
    {
        String strPrefix = AppPropertiesService.getProperty( form.getName(  ) + PROPERTY_FRAGMENT_PREFIX_ID );        
        String strNum;

        if ( ( strCurrentId == null ) || ( strCurrentId.trim(  ).equals( "" ) ) )
        {
            strNum = "00001";
        }
        else
        {            
            strNum = strCurrentId.replaceAll( strPrefix, "" );

            int nNum = Integer.parseInt( strNum );
            nNum++;
            strNum = String.valueOf( nNum );
                
            while ( strNum.length(  ) != 5 )               
            {
                strNum = "0" + strNum;
            }         
        }       
        
        String strNewId = strPrefix + strNum;
        
        return strNewId;
    }
}
