<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<%
if(  pdd.isFonctionnaliteProjetDeDeliberation() )
{
	pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROJET_DE_DELIB );
}
else
{
	pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROP_DE_DELIB );
}
%>

<% 
pdd.setFonctionnalite( false );
pdd.initFilter( request );
%>

<%= pdd.getPDDListMultiSelect( request,
		VoeuAmendementJspBean.CONSTANTE_URL_JSP_APPELANT_PROPOSITION,
		null,
		false ) %>

<%@ include file="../../../AdminFooter.jsp" %>
