<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="xmlpagePortlet" scope="session" class="fr.paris.lutece.plugins.xmlpage.web.portlet.XmlPagePortletJspBean" />

<% xmlpagePortlet.init( request, xmlpagePortlet.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= xmlpagePortlet.getCreate( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

