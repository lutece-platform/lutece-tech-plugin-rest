<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VOEU_NON_RATTACHE, false);
%>

<%=amendement.getVoeuAmendementList(
		request,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_TYPE_VNR,
		OrdreDuJourJspBean.JSP_SELECTION_VOEU,
		OrdreDuJourJspBean.JSP_SELECTION_VOEU_RETOUR)%>

<%@ include file="../../../AdminFooter.jsp" %>
