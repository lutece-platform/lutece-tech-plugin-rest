<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
if( amendement.isGestionAval( ) )
{
	amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VOEU_RATTACHE_GESTION_AVAL, true);
}
else
{
	amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VOEU_RATTACHE, false);
}
%>

<% 
response.sendRedirect( amendement.doAddRemoveParent(request,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_ACTION_ADD_PARENT,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_RETOUR_MODIFICATION) );
%>
