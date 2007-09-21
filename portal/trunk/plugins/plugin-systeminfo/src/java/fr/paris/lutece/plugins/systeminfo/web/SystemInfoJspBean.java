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
package fr.paris.lutece.plugins.systeminfo.web;

import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


public class SystemInfoJspBean extends PluginAdminPageJspBean
{
    /////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_SYSTEMINFO_MANAGEMENT = "SYSTEMINFO_MANAGEMENT";

    // Markers
    private static final String MARK_JAVA_VERSION = "java_version";
    private static final String MARK_JAVA_VENDOR = "java_vendor";
    private static final String MARK_JAVA_VM_SPECIFICATION_VERSION = "java_vm_specification_version";
    private static final String MARK_JAVA_VM_SPECIFICATION_VENDOR = "java_vm_specification_vendor";
    private static final String MARK_JAVA_VM_SPECIFICATION_NAME = "java_vm_specification_name";
    private static final String MARK_JAVA_VM_VERSION = "java_vm_version";
    private static final String MARK_JAVA_VM_VENDOR = "java_vm_vendor";
    private static final String MARK_JAVA_VM_NAME = "java_vm_name";
    private static final String MARK_JAVA_SPECIFICATION_VERSION = "java_specification_version";
    private static final String MARK_JAVA_SPECIFICATION_VENDOR = "java_specification_vendor";
    private static final String MARK_JAVA_SPECIFICATION_NAME = "java_specification_name";
    private static final String MARK_OS_NAME = "os_name";
    private static final String MARK_OS_VERSION = "os_version";
    private static final String MARK_TOTAL_MEMORY = "runtime_totalMemory";
    private static final String MARK_FREE_MEMORY = "runtime_freeMemory";
    private static final String MARK_MAX_MEMORY = "runtime_maxMemory";

    //Properties
    private static final String PROPERTY_JAVA_VERSION = "java.version";
    private static final String PROPERTY_JAVA_VENDOR = "java.vendor";
    private static final String PROPERTY_JAVA_VM_SPECIFICATION_VERSION = "java.vm.specification.version";
    private static final String PROPERTY_JAVA_VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";
    private static final String PROPERTY_JAVA_VM_SPECIFICATION_NAME = "java.vm.specification.name";
    private static final String PROPERTY_JAVA_VM_VERSION = "java.vm.version";
    private static final String PROPERTY_JAVA_VM_VENDOR = "java.vm.vendor";
    private static final String PROPERTY_JAVA_VM_NAME = "java.vm.name";
    private static final String PROPERTY_JAVA_SPECIFICATION_VERSION = "java.specification.version";
    private static final String PROPERTY_JAVA_SPECIFICATION_VENDOR = "java.specification.vendor";
    private static final String PROPERTY_JAVA_SPECIFICATION_NAME = "java.specification.name";
    private static final String PROPERTY_OS_NAME = "os.name";
    private static final String PROPERTY_OS_VERSION = "os.version";

    // Templates
    private static final String TEMPLATE_MANAGE_SYSTEMINFO = "admin/plugins/systeminfo/manage_systeminfo.html";

    /**
     * Get informations from system
     *
     * @param request The Http request
     * @return the html code of information system
     */
    public String getSystemInfo( HttpServletRequest request )
    {
        Properties properties = System.getProperties(  );

        HashMap model = new HashMap(  );
        model.put( MARK_JAVA_VERSION, properties.getProperty( PROPERTY_JAVA_VERSION ) );
        model.put( MARK_JAVA_VENDOR, properties.getProperty( PROPERTY_JAVA_VENDOR ) );
        model.put( MARK_JAVA_VM_SPECIFICATION_VERSION, properties.getProperty( PROPERTY_JAVA_VM_SPECIFICATION_VERSION ) );
        model.put( MARK_JAVA_VM_SPECIFICATION_VENDOR, properties.getProperty( PROPERTY_JAVA_VM_SPECIFICATION_VENDOR ) );
        model.put( MARK_JAVA_VM_SPECIFICATION_NAME, properties.getProperty( PROPERTY_JAVA_VM_SPECIFICATION_NAME ) );
        model.put( MARK_JAVA_VM_VERSION, properties.getProperty( PROPERTY_JAVA_VM_VERSION ) );
        model.put( MARK_JAVA_VM_VENDOR, properties.getProperty( PROPERTY_JAVA_VM_VENDOR ) );
        model.put( MARK_JAVA_VM_NAME, properties.getProperty( PROPERTY_JAVA_VM_NAME ) );
        model.put( MARK_JAVA_SPECIFICATION_VERSION, properties.getProperty( PROPERTY_JAVA_SPECIFICATION_VERSION ) );
        model.put( MARK_JAVA_SPECIFICATION_VENDOR, properties.getProperty( PROPERTY_JAVA_SPECIFICATION_VENDOR ) );
        model.put( MARK_JAVA_SPECIFICATION_NAME, properties.getProperty( PROPERTY_JAVA_SPECIFICATION_NAME ) );
        model.put( MARK_OS_NAME, properties.getProperty( PROPERTY_OS_NAME ) );
        model.put( MARK_OS_VERSION, properties.getProperty( PROPERTY_OS_VERSION ) );
        model.put( MARK_TOTAL_MEMORY, Runtime.getRuntime(  ).totalMemory(  ) );
        model.put( MARK_FREE_MEMORY, Runtime.getRuntime(  ).freeMemory(  ) );
        model.put( MARK_MAX_MEMORY, Runtime.getRuntime(  ).maxMemory(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_SYSTEMINFO, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modification of system memory with a garbage collector
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doGarbageCollector( HttpServletRequest request )
    {
        System.gc(  );

        return getHomeUrl( request );
    }
}
