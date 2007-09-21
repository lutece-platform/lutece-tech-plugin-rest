<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<% 
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_AMENDEMENT, false);
amendement.initUrlRetour(OrdreDuJourJspBean.JSP_MODIFICATION_ORDRE_DU_JOUR,request); 
%>

<%= amendement.getModificationVoeuAmendement( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>
