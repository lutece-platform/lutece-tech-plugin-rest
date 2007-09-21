<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="whatsNewPortlet" scope="session" class="fr.paris.lutece.plugins.whatsnew.web.portlet.WhatsNewPortletJspBean" />
<% whatsNewPortlet.init( request, whatsNewPortlet.RIGHT_MANAGE_ADMIN_SITE); %>

<%= whatsNewPortlet.getCreate ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
