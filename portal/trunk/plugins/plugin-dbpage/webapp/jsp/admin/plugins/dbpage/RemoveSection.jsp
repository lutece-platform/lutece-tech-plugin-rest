<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="dbpage" scope="session" class="fr.paris.lutece.plugins.dbpage.web.DbPageJspBean" />
<jsp:useBean id="user" scope="session" class="fr.paris.lutece.portal.web.user.UserJspBean" />

<%
    if( user.check( "DBPAGE_MANAGEMENT" ) )
    {
%>
        <%= dbpage.getRemoveSection(request) %>
<%
    }
    else
    {
        response.sendRedirect( user.getPluginAccessDeniedUrl() );
    }
%>