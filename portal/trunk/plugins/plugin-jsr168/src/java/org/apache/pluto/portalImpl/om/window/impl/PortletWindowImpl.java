/*
 * Copyright 2003,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pluto.portalImpl.om.window.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.pluto.om.common.ObjectID;
import org.apache.pluto.om.entity.PortletEntity;
import org.apache.pluto.om.window.PortletWindow;
import org.apache.pluto.om.window.PortletWindowCtrl;
import org.apache.pluto.portalImpl.services.log.Log;

//import fr.paris.lutece.plugins.jsr168.pluto.LutecePlutoConstant;

public class PortletWindowImpl implements PortletWindow, PortletWindowCtrl {

    private ObjectID _objectId;
    private String _id;
    private PortletEntity _portletEntity;
    
	final private Map _renderParameters;
    private WindowState _windowState;
    private PortletMode _portletMode;

	final private Map _prevRenderParameters;
	private WindowState _prevWindowState;
	private PortletMode _prevPortletMode;
    
    public PortletWindowImpl(final String id) 
    {
		_renderParameters=new HashMap();
		_prevRenderParameters=new HashMap();
        _id = id;
		_portletMode=PortletMode.VIEW;
		_prevPortletMode=PortletMode.VIEW;
		_windowState=WindowState.NORMAL;
		_prevWindowState=WindowState.NORMAL;
    }
    
	public Map getRenderParameters() 
	{
		return Collections.unmodifiableMap(_renderParameters);
	}

	public String getParameter(final String paramName) 
	{
		final String[] values=(String[])_renderParameters.get(paramName);
		if (values!=null) {
			return values[0];	
		}
		return null;
	}

	public String[] getParameterValues(final String paramName) 
	{
		final String[] values=(String[])_renderParameters.get(paramName);
		return values;	
	}

	synchronized public void setRenderParameter(final Map parameters) 
	{
		if (parameters==null) 
		{
			throw new IllegalArgumentException("Parameter 'parameters' can't be null.");
		}
		
		Log.debug(/*LutecePlutoConstant.LOG_CATEGORY,*/ "portlet " + _id + " old render params " + _renderParameters.size() + " / " + _renderParameters);
		Log.debug(/*LutecePlutoConstant.LOG_CATEGORY,*/ "portlet " + _id + " new render params " + parameters.size() + " / " + parameters);

		_renderParameters.clear();
		final Iterator itEntries=parameters.entrySet().iterator();
		while(itEntries.hasNext()) 
		{
			final Map.Entry entry=(Map.Entry)itEntries.next();
			final Object name=entry.getKey();
			final Object value=entry.getValue();
			
			if (!(name instanceof String))
			{
				throw new IllegalArgumentException("Key name must be a String (key '" + name + "' (" + name.getClass().getName() + ")).");
			}
			if (value instanceof String) {
				_renderParameters.put(name, new String[] { (String)value });
			} else if (value instanceof String[]) {
				_renderParameters.put(name, value);
			} else {
				throw new IllegalArgumentException("Values must be a String or String[] type String ou String[] (key '" + name + "' value is " + value.getClass().getName() + ").");
			}
		}
	}

	public void setPortletMode(final PortletMode portletMode) 
	{
		if (_portletMode!=null) { 
			_portletMode=portletMode;
		} else {
			_portletMode=PortletMode.VIEW;
		}
	}
	
	public PortletMode getPortletMode() 
	{
		return _portletMode;	
	}

	public void setWindowState(final WindowState windowState) 
	{
		if (windowState!=null) {
			_windowState=windowState;
		} else {
			_windowState=WindowState.NORMAL;	
		}	
	}

	public WindowState getWindowState() 
	{
		return _windowState;
	}

    // PortletWindow implementation.

     /**
     * Returns the identifier of this portlet instance window as object id
     *
     * @return the object identifier
     **/
    public ObjectID getId()
    {
        if (_objectId==null)
        {
            _objectId = org.apache.pluto.portalImpl.util.ObjectID.createFromString(_id);
        }
        return _objectId;
    }
    /**
     * Returns the portlet entity
     *
     * @return the portlet entity
     **/
    public PortletEntity getPortletEntity()
    {
        return _portletEntity;
    }

    // PortletWindowCtrl implementation.
    /**
     * binds an identifier to this portlet window
     *
     * @param id the new identifier
     */
    public void setId(String id)
    {
        _id = id;
        _objectId = null;
    }
    
    /**
     * binds a portlet instance to this portlet window
     * 
     * @param portletEntity a portlet entity object
     **/
    public void setPortletEntity(PortletEntity portletEntity) {
        _portletEntity = portletEntity;
    }

	public void saveValues() {
		_prevPortletMode=_portletMode;
		_prevWindowState=_windowState;
		_prevRenderParameters.clear();
		_prevRenderParameters.putAll(_renderParameters);
	}
	public void restoreValues() {
		_portletMode=_prevPortletMode;
		_windowState=_prevWindowState;
		_renderParameters.clear();
		_renderParameters.putAll(_prevRenderParameters);
	}
	
	/**
	 * Return the previous portlet mode
	 * 
	 * @return the previous portlet mode
	 */
	public PortletMode getPrevPortletMode() {
		return _prevPortletMode;
	}

	/**
	 * Return the previous window state
	 * 
	 * @return the previous window state
	 */
	public WindowState getPrevWindowState() {
		return _prevWindowState;
	}

}