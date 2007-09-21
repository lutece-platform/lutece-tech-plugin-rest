<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="direction" scope="session" class="fr.paris.lutece.plugins.ods.web.direction.DirectionJspBean" />

<%
direction.init( request, fr.paris.lutece.plugins.ods.web.direction.DirectionJspBean.RIGHT_ODS_DIRECTION);
%>

<%= direction.getModificationDirection( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>