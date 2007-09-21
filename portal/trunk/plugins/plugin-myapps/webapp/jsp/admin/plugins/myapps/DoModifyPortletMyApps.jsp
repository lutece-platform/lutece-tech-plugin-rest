<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="portletMyApps" scope="session" class="fr.paris.lutece.plugins.myapps.web.portlet.MyAppsPortletJspBean" />

<%
    portletMyApps.init( request, portletMyApps.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( portletMyApps.doModify( request )   );
%>