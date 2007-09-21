<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="myappsPortlet" scope="session" class="fr.paris.lutece.plugins.myapps.web.portlet.MyAppsPortletJspBean" />

<% myappsPortlet.init( request, myappsPortlet.RIGHT_MANAGE_ADMIN_SITE); %>
<%= myappsPortlet.getCreate ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>


