<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="dbPagePortlet" scope="session" class="fr.paris.lutece.plugins.dbpage.web.portlet.DbPagePortletJspBean" />

<% dbPagePortlet.init( request, dbPagePortlet.RIGHT_MANAGE_ADMIN_SITE); %>

<%= dbPagePortlet.getCreate ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

