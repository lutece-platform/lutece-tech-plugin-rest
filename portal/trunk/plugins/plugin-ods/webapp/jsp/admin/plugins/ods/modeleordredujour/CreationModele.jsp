<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="modele" scope="session" class="fr.paris.lutece.plugins.ods.web.modeleordredujour.ModeleOrdreDuJourJspBean" />

<%
modele.init( request, fr.paris.lutece.plugins.ods.web.modeleordredujour.ModeleOrdreDuJourJspBean.RIGHT_ODS_MODELES_ODJ);
%>

<%= modele.getCreationModeleOdj( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>
