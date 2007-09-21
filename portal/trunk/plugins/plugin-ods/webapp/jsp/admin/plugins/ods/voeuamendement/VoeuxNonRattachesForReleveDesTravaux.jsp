<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.ods.web.relevetravaux.ReleveDesTravauxJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VOEU_NON_RATTACHE, false);
%>

<%=amendement.getVoeuAmendementList(
		request,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_TYPE_VNR,
		ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_APPELANT_VOEU_NON_RATTACHE,
		ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_RETOUR)%>

<%@ include file="../../../AdminFooter.jsp" %>
