<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="dbPagePortlet" scope="session" class="fr.paris.lutece.plugins.dbpage.web.portlet.DbPagePortletJspBean" />

<%
    dbPagePortlet.init( request, dbPagePortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( dbPagePortlet.doCreate( request ) );
%>