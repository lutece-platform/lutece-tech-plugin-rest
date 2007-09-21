<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="htmlPortlet" scope="request" class="fr.paris.lutece.plugins.html.web.portlet.HtmlPortletJspBean" />

<%=
        htmlPortlet.getListener( request )
%>
