<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="odj" scope="session" class="fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean" />

<%
odj.init( request,fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean.RIGHT_ODS_ODJ);
response.sendRedirect(  odj.doModificationOrdreDuJour( request )); 
%>
