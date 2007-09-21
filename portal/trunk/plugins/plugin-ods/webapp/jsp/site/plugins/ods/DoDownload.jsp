<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:useBean id="downloadFile" scope="request" class="fr.paris.lutece.plugins.ods.web.DownloadFile" />

<% downloadFile.doDowloadFichier( request, response ); %>
