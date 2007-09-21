<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="folderlisting" scope="session" class="fr.paris.lutece.plugins.folderlisting.web.FolderListingDatabaseJspBean" />

<%
    folderlisting.init( request, folderlisting.RIGHT_FOLDERLISTING_MANAGEMENT );
    response.sendRedirect( folderlisting.doCreateFolder( request ) );
%>

