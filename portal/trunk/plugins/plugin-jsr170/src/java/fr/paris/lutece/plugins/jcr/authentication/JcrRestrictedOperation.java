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
package fr.paris.lutece.plugins.jcr.authentication;

import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;
import javax.security.auth.Subject;


/**
 * Wrapper class to use restricted operations
 */
public class JcrRestrictedOperation
{
    private transient String _strLogin;
    private transient String _strPassword;

    /**
     * @param strLogin the login to connect
     * @param strPassword the password
     */
    public JcrRestrictedOperation( String strLogin, String strPassword )
    {
        _strLogin = strLogin;
        _strPassword = strPassword;
    }

    /**
     * Runs a PrivilegedAction by instantiation of a new Subject with
     * _strLogin/_strPassword
     *
     * @param action the PrivilegedAction to run
     * @return the result of the action
     * @see PrivilegedAction
     */
    public Object doRestrictedOperation( PrivilegedAction action )
    {
        Principal user = new JsrUser( _strLogin );
        Set<Principal> setUser = new HashSet<Principal>(  );
        setUser.add( user );

        SimpleCredentials simpleCredentials = new SimpleCredentials( _strLogin, _strPassword.toCharArray(  ) );
        Set<Credentials> setPubCred = new HashSet<Credentials>(  );
        setPubCred.add( simpleCredentials );

        //Set<Credentials> setPrivCred = new HashSet<Credentials>(  );
        Subject subject = new Subject( true, setUser, setPubCred, setPubCred );

        return Subject.doAs( subject, action );
    }
}
