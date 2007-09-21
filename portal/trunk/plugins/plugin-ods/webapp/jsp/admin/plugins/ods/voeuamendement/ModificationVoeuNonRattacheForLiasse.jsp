<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="amendement" scope="session" class="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean" />

<%
amendement.init( request, fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.RIGHT_ODS_VOEU_NON_RATTACHE, false);
amendement.initUrlRetour(fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean.JSP_URL_LIASSE, request);
%>

<%= amendement.getModificationVoeuAmendement( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>
