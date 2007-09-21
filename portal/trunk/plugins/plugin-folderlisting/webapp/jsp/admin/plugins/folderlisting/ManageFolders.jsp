<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="folderlisting" scope="session" class="fr.paris.lutece.plugins.folderlisting.web.FolderListingDatabaseJspBean" />

<% folderlisting.init( request, folderlisting.RIGHT_FOLDERLISTING_MANAGEMENT ); %>
<%= folderlisting.getManageFolders ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>