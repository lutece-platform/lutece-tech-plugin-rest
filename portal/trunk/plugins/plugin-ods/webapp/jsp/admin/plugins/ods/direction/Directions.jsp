<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="direction" scope="session" class="fr.paris.lutece.plugins.ods.web.direction.DirectionJspBean" />

<%
direction.init( request, fr.paris.lutece.plugins.ods.web.direction.DirectionJspBean.RIGHT_ODS_DIRECTION);
%>

<%= direction.getDirectionList( request )%>

<%@ include file="../../../AdminFooter.jsp" %>