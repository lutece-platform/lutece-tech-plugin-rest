
<jsp:useBean id="folderlistingFile" scope="request" class="fr.paris.lutece.plugins.folderlisting.web.FolderListingFileJspBean" />


<%
	try
	{
			boolean bOk = folderlistingFile.checkRights( request );

			if ( bOk )
			{
				byte[] fileContent = folderlistingFile.getFileContent( request );
				if ( fileContent != null )
				{
					String strFileName = folderlistingFile.getFilename(request);
					//the header and also the names set by which user will be prompted to save
					response.setHeader ("Content-Disposition", "attachment;filename=\""+strFileName+"\"");
					response.setContentType(application.getMimeType(strFileName));

					response.getOutputStream(  ).write( fileContent );
				}
				else
				{
                    String strUrl = folderlistingFile.getFileErrorUrl(request, folderlistingFile.FILE_NOT_FOUND );
                    response.sendRedirect(strUrl);
				}
			}
			else
			{
                String strUrl = folderlistingFile.getFileErrorUrl(request,folderlistingFile.FILE_NOT_ALLOWED);
                response.sendRedirect(strUrl);        
			}
	}
	catch(Exception e)
	{
		System.out.println(e);
	}
	out.clear();
	out = pageContext.pushBody();
%>


