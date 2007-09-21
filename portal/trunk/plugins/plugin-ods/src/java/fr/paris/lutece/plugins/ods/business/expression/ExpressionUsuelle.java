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
package fr.paris.lutece.plugins.ods.business.expression;

import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * ExpressionUsuelle
 */
public class ExpressionUsuelle
{
    private static final String TAG_EXPRESSION = "expression";
    private static final String TAG_ID = "id";
    private static final String TAG_EXPRESSION_USUELLE = "expressionUsuelle";
    private int _nIdExpression;
    private String _strExpression;

    /**
     * Retourne l'identifiant de l'expression usuelle dans la table ods_expression_usuelle.
     * @return l'identifiant de l'expression usuelle dans la table ods_expression_usuelle
     */
    public int getIdExpression(  )
    {
        return _nIdExpression;
    }

    /**
     * Fixe l'identifiant de la direction
     * @param nIdExpression le nouvel identifiant de l'expression
     */
    public void setIdExpression( int nIdExpression )
    {
        _nIdExpression = nIdExpression;
    }

    /**
     * Retourne l'intitulé de l'expression usuelle.
     * @return l'intitulé de l'expression usuelle
     */
    public String getExpression(  )
    {
        return _strExpression;
    }

    /**
     * Fixe l'intitulé de l'expression usuelle.
     * @param strExpression le nouvel intitulé de l'expression usuelle
     */
    public void setExpression( String strExpression )
    {
        _strExpression = strExpression;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant une expression usuelle
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdExpression );
        XmlUtil.beginElement( buffer, TAG_EXPRESSION_USUELLE, attributes );

        if ( _strExpression != null )
        {
            OdsUtils.addElement( buffer, TAG_EXPRESSION, _strExpression );
        }

        XmlUtil.endElement( buffer, TAG_EXPRESSION_USUELLE );

        return buffer.toString(  );
    }
}
