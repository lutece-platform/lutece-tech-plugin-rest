<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="portletMyApps" scope="session" class="fr.paris.lutece.plugins.myapps.web.portlet.MyAppsPortletJspBean" />


<% portletMyApps.init( request, portletMyApps.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= portletMyApps.getModify( request ) %>