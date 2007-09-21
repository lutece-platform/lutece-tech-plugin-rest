<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
if( amendement.isGestionAval( ) )
{
	amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_AMENDEMENT_GESTION_AVAL, true);
}
else
{
	amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_AMENDEMENT, false);
}
%>

<% 
response.sendRedirect( amendement.doAddRemoveParent(request,  
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_ACTION_REMOVE_PARENT,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_RETOUR_MODIFICATION ) );
%>
