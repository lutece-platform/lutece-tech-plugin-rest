<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="htmlPortlet" scope="session" class="fr.paris.lutece.plugins.html.web.portlet.HtmlPortletJspBean" />


<% htmlPortlet.init( request, htmlPortlet.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= htmlPortlet.getCreate ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>