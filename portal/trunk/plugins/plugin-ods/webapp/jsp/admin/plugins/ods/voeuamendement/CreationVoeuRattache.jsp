<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
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

<%=amendement.getCreationVoeuAmendement( request,fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_TYPE_VR, true )%>

<%@ include file="../../../AdminFooter.jsp" %>
