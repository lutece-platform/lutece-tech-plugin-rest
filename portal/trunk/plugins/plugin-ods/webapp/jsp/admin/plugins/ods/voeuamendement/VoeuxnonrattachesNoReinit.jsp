<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>

<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
if( amendement.isUrLRetour() )
{	
	String strUrlRetour = AppPathService.getBaseUrl(request) + amendement.getUrlRetour();
	amendement.setBurlRetour(false);
	amendement.setUrlRetour(null);
	response.sendRedirect(strUrlRetour ); 	
}
%>

<jsp:include page="../../../AdminHeader.jsp" />

<%
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VOEU_NON_RATTACHE, false);
%>

<%= amendement.getVoeuAmendementList( request,fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_TYPE_VNR )%>

<%@ include file="../../../AdminFooter.jsp" %>
