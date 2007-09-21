<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="expression" scope="session" class="fr.paris.lutece.plugins.ods.web.expression.ExpressionUsuelleJspBean" />

<% 
    expression.init( request, fr.paris.lutece.plugins.ods.web.expression.ExpressionUsuelleJspBean.RIGHT_ODS_EXPRESSION_USUELLE);
    response.sendRedirect( expression.getSuppressionExpression( request ) ); 
%>