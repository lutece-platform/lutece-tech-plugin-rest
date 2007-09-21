<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="whatsNewPortlet" scope="session" class="fr.paris.lutece.plugins.whatsnew.web.portlet.WhatsNewPortletJspBean" />

<%
    whatsNewPortlet.init( request, whatsNewPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( whatsNewPortlet.doCreate( request ) );
%>
