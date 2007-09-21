<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="htmlPortlet" scope="session" class="fr.paris.lutece.plugins.html.web.portlet.HtmlPortletJspBean" />

<%
    htmlPortlet.init( request, htmlPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect(  htmlPortlet.doCreate( request )  );
%>




