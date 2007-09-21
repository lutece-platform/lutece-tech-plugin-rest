<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
if( amendement.isGestionAval( ) )
{
	amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_STATUTVA_GESTION_AVAL, true);
}
else
{
	amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VA_NUMEROTES, false);
}
%>

<%
amendement.doDownloadLiasse( request, response );

%>
