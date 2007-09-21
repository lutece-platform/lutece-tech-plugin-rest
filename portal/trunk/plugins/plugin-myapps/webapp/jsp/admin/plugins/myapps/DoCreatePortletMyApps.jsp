<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="myappsPortlet" scope="session" class="fr.paris.lutece.plugins.myapps.web.portlet.MyAppsPortletJspBean" />

<%
    myappsPortlet.init( request, myappsPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( myappsPortlet.doCreate( request ) );
%>


