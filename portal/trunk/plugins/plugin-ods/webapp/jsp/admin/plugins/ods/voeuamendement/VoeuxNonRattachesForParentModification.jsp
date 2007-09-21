<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VOEU_NON_RATTACHE_GESTION_AVAL, true);
%>

<%=amendement.getVoeuAmendementList(
		request,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_TYPE_VNR,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_URL_JSP_APPELANT_VOEU_NON_RATTACHE_PARENT_MODIFICATION,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_URL_JSP_RETOUR_PARENT_MODIFICATION_VOEU_NON_RATTACHE) %>

<%@ include file="../../../AdminFooter.jsp" %>
