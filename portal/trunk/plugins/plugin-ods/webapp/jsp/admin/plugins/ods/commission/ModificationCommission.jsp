<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="commission" scope="session" class="fr.paris.lutece.plugins.ods.web.commission.CommissionJspBean" />

<% 
commission.init( request, fr.paris.lutece.plugins.ods.web.commission.CommissionJspBean.RIGHT_ODS_COMMISSION );
%>

<%= commission.getModificationCommission( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>
