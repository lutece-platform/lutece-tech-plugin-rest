<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="fichier" scope="session" class="fr.paris.lutece.plugins.ods.web.fichier.FichierJspBean" />

<%
fichier.init( request, fr.paris.lutece.plugins.ods.web.fichier.FichierJspBean.RIGHT_ODS_FICHIERS );
response.sendRedirect( fichier.getSuppressionFichier( request, false )); 
%>
