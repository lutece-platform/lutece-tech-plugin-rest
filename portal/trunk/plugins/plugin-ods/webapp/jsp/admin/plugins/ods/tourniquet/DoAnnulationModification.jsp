<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="tourniquet" scope="session" class="fr.paris.lutece.plugins.ods.web.tourniquet.TourniquetJspBean" />

<%
tourniquet.init( request, fr.paris.lutece.plugins.ods.web.tourniquet.TourniquetJspBean.RIGHT_ODS_TOURNIQUET); 
response.sendRedirect(tourniquet.doAnnulationModification( request ));
%>