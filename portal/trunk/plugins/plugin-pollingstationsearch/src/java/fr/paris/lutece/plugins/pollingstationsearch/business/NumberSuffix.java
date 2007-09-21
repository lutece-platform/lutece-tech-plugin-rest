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


/**
 * This class represents the business object NumberSuffix.
 */
public class NumberSuffix
{
    private String _strNumberSuffix;
    private String _strNumberSuffixLabel;

    /**
     * Returns the number suffix.
     * @return the number suffix.
     */
    public String getNumberSuffix(  )
    {
        return _strNumberSuffix;
    }

    /**
     * Sets the number suffix.
     * @param numberSuffix the new value.
     */
    public void setNumberSuffix( String numberSuffix )
    {
        _strNumberSuffix = numberSuffix;
    }

    /**
     * Returns the number suffix label.
     * @return the number suffix label.
     */
    public String getNumberSuffixLabel(  )
    {
        return _strNumberSuffixLabel;
    }

    /**
     * Sets the number suffix label.
     * @param strNumberSuffixLabel the new value.
     */
    public void setNumberSuffixLabel( String strNumberSuffixLabel )
    {
        _strNumberSuffixLabel = strNumberSuffixLabel;
    }
}
