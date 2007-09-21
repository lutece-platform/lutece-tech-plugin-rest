<%@page errorPage="../../../ErrorPage.jsp" %>
<jsp:useBean id="horodatage" scope="session" class="fr.paris.lutece.plugins.ods.web.horodatage.HorodatageJspBean" />
<%
horodatage.init( request, fr.paris.lutece.plugins.ods.web.horodatage.HorodatageJspBean.RIGHT_ODS_HORODATAGE );
String returnUrl = horodatage.doDownloadCSV( request, response ); 
if( ! response.isCommitted() ) 
{
  response.sendRedirect( returnUrl );
}
%>
