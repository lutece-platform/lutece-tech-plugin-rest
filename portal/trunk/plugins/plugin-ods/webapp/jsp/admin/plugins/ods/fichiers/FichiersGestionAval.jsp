<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="fichier" scope="session" class="fr.paris.lutece.plugins.ods.web.fichier.FichierJspBean" />

<%
fichier.init( request, fr.paris.lutece.plugins.ods.web.fichier.FichierJspBean.RIGHT_ODS_FICHIERS_GESTION_AVAL );
fichier.setAllSeances( request );
fichier.setAllTypeDocuments( request, true ); 
fichier.setAllPublication( request ); 
%>

<%= fichier.getFichiersList( request, true ) %>

<%@ include file="../../../AdminFooter.jsp" %>
