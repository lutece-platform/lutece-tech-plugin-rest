<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="linksPortlet" scope="session" class="fr.paris.lutece.plugins.links.web.portlet.LinksPortletJspBean" />

<% linksPortlet.init( request, linksPortlet.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= linksPortlet.getModify( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
