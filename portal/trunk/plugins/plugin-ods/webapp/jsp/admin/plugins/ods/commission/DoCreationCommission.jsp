<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="commission" scope="session" class="fr.paris.lutece.plugins.ods.web.commission.CommissionJspBean" />

<%
commission.init( request, fr.paris.lutece.plugins.ods.web.commission.CommissionJspBean.RIGHT_ODS_COMMISSION );
response.sendRedirect( commission.doCreationCommission( request )); 
%>
