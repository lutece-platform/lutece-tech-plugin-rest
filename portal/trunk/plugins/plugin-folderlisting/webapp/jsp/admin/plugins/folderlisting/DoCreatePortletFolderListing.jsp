<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="folderlistingPortlet" scope="session" class="fr.paris.lutece.plugins.folderlisting.web.portlet.FolderListingPortletJspBean" />

<%
    folderlistingPortlet.init( request, folderlistingPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( folderlistingPortlet.doCreate( request ) );
%>


