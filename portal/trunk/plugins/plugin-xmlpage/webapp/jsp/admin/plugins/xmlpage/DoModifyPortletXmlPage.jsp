<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../CheckSession.jsp" />

<jsp:useBean id="xmlpagePortlet" scope="session" class="fr.paris.lutece.plugins.xmlpage.web.portlet.XmlPagePortletJspBean" />
<jsp:useBean id="user" scope="session" class="fr.paris.lutece.portal.web.user.UserJspBean" />

<%
    if( user.check( "DEF_ADMIN" ) )
    {
        response.sendRedirect( xmlpagePortlet.doModify( request ) );
    }
    else
    {
        response.sendRedirect( user.getPluginAccessDeniedUrl() );
    }
%>
