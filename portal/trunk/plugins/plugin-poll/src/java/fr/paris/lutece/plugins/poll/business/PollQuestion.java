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

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents business object PollQuestion
 */
public class PollQuestion
{
    private int _nIdQuestion;
    private int _nIdPoll;
    private String _strLabelQuestion;
    private ArrayList<PollChoice> _choices = new ArrayList<PollChoice>(  );
    private int _nVotesCount;

    /**
     * Returns the identifier of this question of the poll
     * @return the identifier as an integer
     */
    public int getId(  )
    {
        return _nIdQuestion;
    }

    /**
     * Sets the identifier with the specified integer
     * @param nIdQuestion The identifier of the question
     */
    public void setId( int nIdQuestion )
    {
        _nIdQuestion = nIdQuestion;
    }

    /**
     * Returns the identifier of the poll
     * @return The identifier of the poll as an integer.
     */
    public int getIdPoll(  )
    {
        return _nIdPoll;
    }

    /**
     * Sets the identifier of the poll with the specified integer
     * @param nIdPoll The identifier
     */
    public void setIdPoll( int nIdPoll )
    {
        _nIdPoll = nIdPoll;
    }

    /**
     * Returns the label of the question
     * @return _strLabelQuestion The label of the question
     */
    public String getLabel(  )
    {
        return _strLabelQuestion;
    }

    /**
     * Sets the label of the question with the specified string
     * @param strLabelQuestion The String label
     */
    public void setLabel( String strLabelQuestion )
    {
        _strLabelQuestion = strLabelQuestion;
    }

    /**
     * Returns the list of avaiable choices
     * @return _choices The list of choices
     */
    public List<PollChoice> getChoices(  )
    {
        return _choices;
    }

    /**
     * Returns the count of votes for the question
     * @return _nVotesCount the votes count
     */
    public int getVotesCount(  )
    {
        return _nVotesCount;
    }

    /**
     * Sets the count of votes for the question
     * @param nVotesCount the votes count
     */
    public void setVotesCount( int nVotesCount )
    {
        _nVotesCount = nVotesCount;
    }
}
