<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="dbpagePortlet" scope="session" class="fr.paris.lutece.plugins.dbpage.web.portlet.DbPagePortletJspBean" />

<% dbpagePortlet.init( request, dbpagePortlet.RIGHT_MANAGE_ADMIN_SITE); %>
<%= dbpagePortlet.getModify( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

