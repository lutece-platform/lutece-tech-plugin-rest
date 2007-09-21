<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VOEU_RATTACHE_GESTION_AVAL, true);
%>

<%= amendement.getVoeuAmendementList( 
		request,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_TYPE_VR,
		ProjetDeDeliberationJspBean.CONSTANTE_URL_JSP_APPELANT_VOEUX_GESTION_AVAL,
		ProjetDeDeliberationJspBean.CONSTANTE_URL_JSP_RETOUR_GESTION_AVAL) 
%>

<%@ include file="../../../AdminFooter.jsp" %>
