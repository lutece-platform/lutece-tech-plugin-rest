<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="documentComment" scope="session" class="fr.paris.lutece.plugins.document.web.DocumentCommentJspBean" />

<% 
    documentComment.init( request, documentComment.RIGHT_DOCUMENT_MANAGEMENT ); 
    response.sendRedirect( documentComment.doModifyCommentStatus( request ) );
%>
