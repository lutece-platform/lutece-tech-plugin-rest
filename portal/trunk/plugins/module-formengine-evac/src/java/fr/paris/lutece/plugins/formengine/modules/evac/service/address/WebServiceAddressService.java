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
package fr.paris.lutece.plugins.formengine.modules.evac.service.address;

import fr.paris.lutece.plugins.formengine.modules.evac.business.axis.AdresseService;
import fr.paris.lutece.plugins.formengine.modules.evac.business.axis.AdresseServiceLocator;
import fr.paris.lutece.plugins.formengine.modules.evac.business.axis.AdresseServicePortType;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Adresse;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsSearchAdresse.Adresses;
import fr.paris.lutece.plugins.formengine.modules.evac.utils.EvacUtils;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.axis.client.Stub;

import java.io.StringReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.rmi.RemoteException;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.stream.StreamSource;


/**
 *
 */
public class WebServiceAddressService implements IAddressService
{
    //jaxb context
    private static final String JAXB_CONTEXT_WS_FICHE_ADDRESS = "fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsFicheAdresse";
    private static final String JAXB_CONTEXT_WS_SEARCH_ADDRESS = "fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsSearchAdresse";
    private String _strUrlWS;
    private String _strDefaultCity;
    private String _strDateSearch;
    private String _strUserName;
    private String _strPassword;
    private String _strTimeOut;
    private Adresses _listAdresses;

    /**
    * @throws RemoteException the RemoteExecption
    * @param id the adress id
    * @param bIsTest if true test connect at web service, if false search an adress
    * @return the XML flux of an adress
    *
    */
    public Adresse getAdresseInfo( long id, boolean bIsTest )
        throws RemoteException
    {
        String responseWebService = null;
        AdresseService adresseService = new AdresseServiceLocator(  );

        try
        {
            URL urlWS = null;

            Stub portType = null;

            if ( ( getUrlWS(  ) == null ) || getUrlWS(  ).equals( "" ) )
            {
                portType = (Stub) adresseService.getAdresseServiceHttpPort(  );
            }
            else
            {
                try
                {
                    urlWS = new URL( getUrlWS(  ) );
                }
                catch ( MalformedURLException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }

                portType = (Stub) adresseService.getAdresseServiceHttpPort( urlWS );
            }

            portType.setUsername( getUserName(  ) );
            portType.setPassword( getPassword(  ) );

            try
            {
                portType.setTimeout( Integer.parseInt( getTimeOut(  ) ) );
            }
            catch ( NumberFormatException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }

            responseWebService = ( (AdresseServicePortType) portType ).getAdresseInfo( getDefaultCity(  ), id );
        }
        catch ( ServiceException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        //      traitement du flux xml
        fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsFicheAdresse.Adresse adresse = null;

        JAXBContext jc;

        try
        {
            jc = JAXBContext.newInstance( JAXB_CONTEXT_WS_FICHE_ADDRESS );

            Unmarshaller u = jc.createUnmarshaller(  );
            StringBuffer xmlStr = new StringBuffer( responseWebService );
            adresse = (fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsFicheAdresse.Adresse) u.unmarshal( new StreamSource( 
                        new StringReader( xmlStr.toString(  ) ) ) );
        }
        catch ( JAXBException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        Adresse adresseReturn = new Adresse(  );

        adresseReturn.setIadresse( adresse.getIdentifiant(  ) );
        adresseReturn.setDunumero( adresse.getNumero(  ) );
        adresseReturn.setDubis( adresse.getSuffixe1(  ) );
        adresseReturn.setCodeCommune( adresse.getCodeInsee(  ).toString(  ) );

        if ( !bIsTest )
        {
            List<fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsSearchAdresse.Adresse> listAddress = _listAdresses.getAdresse(  );

            for ( fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsSearchAdresse.Adresse currentAdresse : listAddress )
            {
                if ( String.valueOf( currentAdresse.getIdentifiant(  ) ).equals( String.valueOf( id ) ) )
                {
                    adresseReturn.setTypeVoie( currentAdresse.getTypeVoie(  ) );
                    adresseReturn.setLibelleVoie( currentAdresse.getNomVoie(  ) );

                    break;
                }
            }
        }

        return adresseReturn;
    }

    /**
    * @throws RemoteException the RemoteExecption
    * @param labeladresse the  label adress
    * @return the XML flux of all adress corresponding
    *
    */
    public ReferenceList searchAddress( String labeladresse )
        throws RemoteException
    {
        String responseWebService = null;
        AdresseService adresseService = new AdresseServiceLocator(  );

        try
        {
            URL urlWS = null;

            Stub portType = null;

            if ( ( getUrlWS(  ) == null ) || getUrlWS(  ).equals( "" ) )
            {
                portType = (Stub) adresseService.getAdresseServiceHttpPort(  );
            }
            else
            {
                try
                {
                    urlWS = new URL( getUrlWS(  ) );
                }
                catch ( MalformedURLException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }

                portType = (Stub) adresseService.getAdresseServiceHttpPort( urlWS );
            }

            portType.setUsername( getUserName(  ) );
            portType.setPassword( getPassword(  ) );

            try
            {
                portType.setTimeout( Integer.parseInt( getTimeOut(  ) ) );
            }
            catch ( NumberFormatException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }

            responseWebService = ( (AdresseServicePortType) portType ).searchAddress( getDefaultCity(  ), labeladresse,
                    null, getDateSearch(  ) );

            // check null result and then return null list
            if ( responseWebService == null )
            {
                return null;
            }
        }
        catch ( ServiceException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        //      traitement du flux xml		
        Adresses adresses = null;

        JAXBContext jc;

        try
        {
            jc = JAXBContext.newInstance( JAXB_CONTEXT_WS_SEARCH_ADDRESS );

            Unmarshaller u = jc.createUnmarshaller(  );
            StringBuffer xmlStr = new StringBuffer( responseWebService );
            adresses = (Adresses) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString(  ) ) ) );
        }
        catch ( JAXBException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        List<fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsSearchAdresse.Adresse> listAdresses = adresses.getAdresse(  );

        ReferenceList refList = null;

        //build the list choice
        if ( ( listAdresses != null ) && !listAdresses.isEmpty(  ) )
        {
            refList = new ReferenceList(  );

            for ( fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.wsSearchAdresse.Adresse currentAdresse : listAdresses )
            {
                String suffixe = "";

                if ( currentAdresse.getSuffixe(  ) != null )
                {
                    suffixe = currentAdresse.getSuffixe(  );
                }

                String strCurrentAdresse = "";

                if ( EvacUtils.isTerminateByApostrophe( currentAdresse.getTypeVoie(  ) ) )
                {
                    strCurrentAdresse = currentAdresse.getNumero(  ) + " " + suffixe + " " +
                        currentAdresse.getTypeVoie(  ) + currentAdresse.getNomVoie(  ) + " " +
                        currentAdresse.getCommune(  );
                }
                else
                {
                    strCurrentAdresse = currentAdresse.getNumero(  ) + " " + suffixe + " " +
                        currentAdresse.getTypeVoie(  ) + " " + currentAdresse.getNomVoie(  ) + " " +
                        currentAdresse.getCommune(  );
                }

                String strIdAdresse = currentAdresse.getIdentifiant(  ).toString(  );

                refList.addItem( strIdAdresse, strCurrentAdresse );
            }

            _listAdresses = adresses;
        }

        return refList;
    }

    /**
    *
    * @return the date for parameter methodes of web service
    */
    public String getDateSearch(  )
    {
        return _strDateSearch;
    }

    /**
     *
     * @param strDateSearch the new date search
     */
    public void setDateSearch( String strDateSearch )
    {
        _strDateSearch = strDateSearch;
    }

    /**
     *
     * @return the default city for parameter methodes of web service
     */
    public String getDefaultCity(  )
    {
        return _strDefaultCity;
    }

    /**
     *
     * @param strDefaultCity the new default city
     */
    public void setDefaultCity( String strDefaultCity )
    {
        _strDefaultCity = strDefaultCity;
    }

    /**
     *
     * @return the url of the web service
     */
    public String getUrlWS(  )
    {
        return _strUrlWS;
    }

    /**
     *
     * @param strUrlWS the new web service url
     */
    public void setUrlWS( String strUrlWS )
    {
        _strUrlWS = strUrlWS;
    }

    /**
     *
     * @return the password
     */
    public String getPassword(  )
    {
        return _strPassword;
    }

    /**
     *
     * @param password the password
     */
    public void setPassword( String password )
    {
        _strPassword = password;
    }

    /**
     *
     * @return the user name
     */
    public String getUserName(  )
    {
        return _strUserName;
    }

    /**
     *
     * @param userName the user name
     */
    public void setUserName( String userName )
    {
        _strUserName = userName;
    }

    /**
    *
    * @return the timeout
    */
    public String getTimeOut(  )
    {
        return _strTimeOut;
    }

    /**
     *
     * @param timeOut the timeout
     */
    public void setTimeOut( String timeOut )
    {
        _strTimeOut = timeOut;
    }
}
