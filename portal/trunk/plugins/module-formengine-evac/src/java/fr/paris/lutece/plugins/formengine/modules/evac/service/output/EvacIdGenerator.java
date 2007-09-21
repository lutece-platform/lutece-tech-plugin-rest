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
package fr.paris.lutece.plugins.formengine.modules.evac.service.output;

import fr.paris.lutece.plugins.formengine.service.output.FileIdGenerator;
import fr.paris.lutece.plugins.formengine.web.Form;


/**
 *
 */
public class EvacIdGenerator extends FileIdGenerator
{
    private static final String PATH_TRANSACTION_IDS = "formengine.path.transaction.ids";
    private static final String BASE_TRANSACTION_NUM = "00000";
    private static final String PREFIX_TRANSACTION_NUM = "W";
    private static final String CHAR_ZERO = "0";

    /**
     *
     */
    public EvacIdGenerator(  )
    {
    }

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.IdGenerator#init(fr.paris.lutece.plugins.formengine.web.Form)
     * @param form the form
     */
    protected void init( Form form )
    {
        setDirectoryPathProperty( PATH_TRANSACTION_IDS );
        setFileName( form.getInstanceName(  ) + ".id" );
    }

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.IdGenerator#generateId(fr.paris.lutece.plugins.formengine.web.Form)
     * @param form the form
     * @param strCurrentId the current id
     * @return the id updated
     */
    public synchronized String updateId( Form form, String strCurrentId )
    {
        String strNum;
        String strPrefix = "";

        if ( ( strCurrentId == null ) || ( strCurrentId.trim(  ).equals( "" ) ) )
        {
            strNum = BASE_TRANSACTION_NUM;
        }
        else
        {
            strNum = strCurrentId.replace( PREFIX_TRANSACTION_NUM, "" );

            int nNum = Integer.parseInt( strNum );
            nNum++;
            strNum = String.valueOf( nNum );

            if ( strNum.length(  ) < 5 )
            {
                int nPrefix = 5 - strNum.length(  );

                for ( int i = 0; i < nPrefix; i++ )
                {
                    strPrefix = strPrefix + CHAR_ZERO;
                }
            }
        }

        String strNewId = PREFIX_TRANSACTION_NUM + strPrefix + strNum;

        return strNewId;
    }
}
