<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<%
pdd.init( request );
pdd.initFilter( request );
pdd.initUrlRetour(OrdreDuJourJspBean.JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR, request);
%>

<%= pdd.getModificationCompleteProjetDeDeliberation( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>
