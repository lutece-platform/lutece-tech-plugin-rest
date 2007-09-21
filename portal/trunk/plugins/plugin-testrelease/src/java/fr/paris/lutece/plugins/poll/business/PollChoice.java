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

/**
 * This class represents business object PollChoice
 */
public class PollChoice
{
    private int _nIdChoice;
    private int _nIdQuestion;
    private String _strChoice;
    private int _nScore;
    
       
    /**
     * Returns the identifier of this choice.
     *
     * @return _nIdChoice the choice identifier
     */
    public int getId()
    {
        return _nIdChoice;
    }
    
    /**
     * Sets the identifier of the choice to the specified integer.
     *
     * @param nIdChoice the new identifier
     */
    public void setId( int nIdChoice )
    {
        _nIdChoice = nIdChoice;
    }
    
    /**
     * Returns the identifier of this question.
     * @return _nIdQuestion The identifier of the question
     */
    public int getQuestionId()
    {
        return _nIdQuestion;
    }
    
    /**
     * Sets the identifier of the question to the specified integer.
     * @param nIdQuestion The question identifier
     */
    public void setQuestionId( int nIdQuestion )
    {
        _nIdQuestion = nIdQuestion;
    }
    
    /**
     * Returns the Choice label.
     * @return _strChoice The choice
     */
    public String getLabel()
    {
        return _strChoice;
    }
    
    /**
     * Sets the label of the Choice to the specified string.
     * @param strChoice The choice label
     */
    public void setLabel( String strChoice )
    {
        _strChoice = strChoice;
    }
    
    /**
     * Returns The Score
     * @return _nScore the score
     */
    public int getScore()
    {
        return _nScore;
    }
    
    /**
     * Sets the score to the specified integer.
     * @param nScore The score
     */
    public void setScore( int nScore )
    {
        _nScore = nScore;
    }
    
}
