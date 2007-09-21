<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<% 
pdd.setFonctionnalite( false );
pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROP_DE_DELIB );%>
pdd.initFilter( request );
%>

<%= pdd.getPDDListSelect( 
		request,
		fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean.JSP_SELECTION_PROPOSITION,
		fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean.JSP_SELECTION_PDD_RETOUR) %>
		
<%@ include file="../../../AdminFooter.jsp" %>
