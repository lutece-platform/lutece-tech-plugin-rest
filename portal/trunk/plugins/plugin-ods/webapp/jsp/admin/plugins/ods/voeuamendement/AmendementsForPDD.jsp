<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_AMENDEMENT, false);
%>

<%=amendement.getVoeuAmendementList( 
		request,
		fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.CONSTANTE_TYPE_A,
		ProjetDeDeliberationJspBean.CONSTANTE_URL_JSP_APPELANT_AMENDEMENT,
		ProjetDeDeliberationJspBean.CONSTANTE_URL_JSP_RETOUR)%>
		
<%@ include file="../../../AdminFooter.jsp" %>
