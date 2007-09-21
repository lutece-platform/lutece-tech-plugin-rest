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
package fr.paris.lutece.plugins.formengine.service.output;

import fr.paris.lutece.plugins.formengine.web.Form;


/**
 * Transaction Id generator used by default by formengine
 */
public class TransactionFileIdGenerator extends FileIdGenerator
{
    private static final String PATH_TRANSACTION_IDS = "formengine.path.transaction.ids";
    private static final String ID_SEPARATOR = "_";

    /**
     *
     */
    public TransactionFileIdGenerator(  )
    {
    }

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.IdGenerator#init(fr.paris.lutece.plugins.formengine.web.Form)
     */
    protected void init( Form form )
    {
        setDirectoryPathProperty( PATH_TRANSACTION_IDS );
        setFileName( form.getTransactionCode(  ) + ID_SEPARATOR + form.getInstanceName(  ) + ".id" );
    }

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.IdGenerator#updateId(fr.paris.lutece.plugins.formengine.web.Form, java.lang.String)
     */
    protected String updateId( Form form, String strCurrentId )
    {
        String strTransactionCode = form.getTransactionCode(  );
        String strTransactionInstance = form.getInstanceName(  );
        String strPrefixId = strTransactionCode + ID_SEPARATOR + strTransactionInstance + ID_SEPARATOR;

        String strId;

        if ( ( strCurrentId == null ) || ( strCurrentId.trim(  ).equals( "" ) ) )
        {
            strId = strPrefixId + "1";
        }
        else
        {
            strId = strCurrentId.replaceAll( strPrefixId, "" );

            int nId = Integer.parseInt( strId );
            nId++;
            strId = String.valueOf( nId );
            strId = strPrefixId + strId;
        }

        return strId;
    }
}
