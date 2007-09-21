<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_STATUTVA_GESTION_AVAL, true);
response.sendRedirect( amendement.doModificationStatutsNonDefinis( request ));
%>


