<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum"%>

<jsp:useBean id="odj" scope="session" class="fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean" />

<%
odj.init( request,fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean.RIGHT_ODS_ODJ);
response.sendRedirect(  odj.doGenereAdditifOrRectificatif(request,TypeOrdreDuJourEnum.ADDITIF.getId())); 
%>
