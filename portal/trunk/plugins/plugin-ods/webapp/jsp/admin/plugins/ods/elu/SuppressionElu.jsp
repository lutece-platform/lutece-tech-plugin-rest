<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="elu" scope="session" class="fr.paris.lutece.plugins.ods.web.elu.EluJspBean" />

<%
elu.init( request, fr.paris.lutece.plugins.ods.web.elu.EluJspBean.RIGHT_ODS_ELU);
response.sendRedirect( elu.getSuppressionElu( request ));
%>


