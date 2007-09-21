/*
 * DocumentResourceServlet.java
 *
 * Created on 10 mai 2006, 19:00
 */
package fr.paris.lutece.plugins.document.web;

import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.DocumentResource;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Servlet serving document file resources
 */
public class DocumentResourceServlet extends HttpServlet
{
    private static final String PARAMETER_DOCUMENT_ID = "id";
    private static final String PARAMETER_ATTRIBUTE_ID = "id_attribute";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
         * @throws ServletException the servlet Exception
         * @throws IOException the io exception
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );
        String strAttributeId = request.getParameter( PARAMETER_ATTRIBUTE_ID );
        DocumentResource resource;

        if ( strAttributeId != null )
        {
            int nAttributeId = Integer.parseInt( strAttributeId );
            resource = DocumentHome.getResource( nDocumentId, nAttributeId );
        }
        else
        {
            resource = DocumentHome.getResource( nDocumentId );
        }

        response.setHeader( "Content-Disposition", "attachment;filename=\"Document\"" );
        response.setContentType( resource.getContentType(  ) );

        OutputStream out = response.getOutputStream(  );
        out.write( resource.getContent(  ) );
        out.flush(  );
        out.close(  );
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
         * @throws ServletException the servlet Exception
         * @throws IOException the io exception
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
         * @throws ServletException the servlet Exception
         * @throws IOException the io exception
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /** Returns a short description of the servlet.
         * @return message
     */
    public String getServletInfo(  )
    {
        return "Servlet serving file resources of documents";
    }
}
