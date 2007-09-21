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
package fr.paris.lutece.plugins.jsr168.pluto;


/**
 * Button definition for {@link fr.paris.lutece.plugins.jsr168.pluto.Buttons}
 *
 * @see fr.paris.lutece.plugins.jsr168.pluto.Buttons
 */
public class Button
{
    private final String _strUrlRender;
    private final String _strImagePath;

    /**
     * Initialize a <code>Button</code> with url and image path
     *
     * @param strUrlRender URL associated with the button
     * @param strImagePath Image relative path to the icon
     */
    Button( final String strUrlRender, final String strImagePath )
    {
        _strUrlRender = strUrlRender;
        _strImagePath = strImagePath;
    }

    /**
     * Return the url associated to this button
     *
     * @return the url associated to this button
     */
    public String getUrlRender(  )
    {
        return _strUrlRender;
    }

    /**
     * Return the image path associated to this button
     *
     * @return the image path associated to this button
     */
    public String getImagePath(  )
    {
        return _strImagePath;
    }
}
