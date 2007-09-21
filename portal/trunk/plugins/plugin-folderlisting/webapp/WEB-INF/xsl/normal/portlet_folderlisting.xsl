<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="site-path" select="site-path" />

<xsl:template match="portlet">
    
	<div class="portlet" id="portlet_id_{portlet-id}" name="portlet_id_{portlet-id}" >
        <xsl:if test="not(string(display-portlet-title)='1')">
			<h3 class="portlet-header">
				<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
			</h3>
        </xsl:if>

		<div class="portlet-content">
		    <xsl:apply-templates select="folderlisting-portlet" />
		</div>
	</div>
</xsl:template>


<xsl:template match="folderlisting-portlet">
    
    <h3>
    	Répertoire : 
    	<i>
	    	<xsl:call-template name="link-path">
			  	<xsl:with-param name="path" select="concat(directory-root-alias,directory-path)"/>
			</xsl:call-template>
		</i>	
    </h3>
    
    
	<table cellpadding="1" cellspacing="1" width="100%">
	    <tr>
    	      <th></th>
            <th>Nom</th>
            <th>Taille</th>
            <th>Date</th>
        </tr>
        <xsl:apply-templates select="directory" />
	    <xsl:apply-templates select="file" />
     </table>
</xsl:template>

<xsl:template match="file">
	<tr>
		<td>
			<xsl:choose>
			  	<xsl:when test="(file-extension = 'jpg') or (file-extension = 'JPG')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_image.png" height="16" width="16" border="0"/>
			 	</xsl:when>
		        <xsl:when test="(file-extension = 'gif') or (file-extension = 'GIF')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_image.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'png') or (file-extension = 'PNG')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_image.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'bmp') or (file-extension = 'BMP')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_image.png" height="16" width="16" border="0"/>
				</xsl:when>
		        <xsl:when test="(file-extension = 'doc') or (file-extension = 'rtf')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_word.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'xls') or (file-extension = 'csv')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_excel.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'ppt')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_ppt.png" height="16" width="16" border="0"/>
			  	</xsl:when>
			    <xsl:when test="(file-extension = 'pdf')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_pdf.png" height="16" width="16" border="0"/>
				</xsl:when>
		        <xsl:when test="(file-extension = 'txt') or (file-extension = 'css')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_txt.png" height="16" width="16" border="0"/>
				</xsl:when>
		        <xsl:when test="(file-extension = 'zip') or (file-extension = 'rar')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_zip.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'js')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_js.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'java') or (file-extension = 'jsp')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_java.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'html') or (file-extension = 'htm')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_html.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'xml')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_xml.png" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:when test="(file-extension = 'xsl')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_xsl.png" height="16" width="16" border="0"/>
			  	</xsl:when>
				<xsl:when test="(file-extension = 'ods')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_ods.jpg" height="16" width="16" border="0"/>
			  	</xsl:when>
				<xsl:when test="(file-extension = 'odt')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_odt.jpg" height="16" width="16" border="0"/>
			  	</xsl:when>
				<xsl:when test="(file-extension = 'odp')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_odp.jpg" height="16" width="16" border="0"/>
			  	</xsl:when>
				<xsl:when test="(file-extension = 'sxc')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_sxc.jpg" height="16" width="16" border="0"/>
			  	</xsl:when>
				<xsl:when test="(file-extension = 'sxi')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_sxi.jpg" height="16" width="16" border="0"/>
			  	</xsl:when>
				<xsl:when test="(file-extension = 'sxw')">
				        <img src="images/local/skin/plugins/folderlisting/icons/icon_sxw.jpg" height="16" width="16" border="0"/>
			  	</xsl:when>
		        <xsl:otherwise>
        			<img src="images/local/skin/plugins/folderlisting/icons/icon_unknown.png" height="16" width="16" border="0"/>
		        </xsl:otherwise>
			</xsl:choose>
		</td>
  		<td>
			<a href="jsp/site/plugins/folderlisting/DisplayFile.jsp?portlet_id={../../portlet-id}&#38;folder={../directory-path}&#38;file={file-name}">
			    <xsl:apply-templates select="file-name" />
			</a>
		</td>
		<td>
			<xsl:value-of select="file-size"/> Ko
		</td>
		<td>
			<xsl:value-of select="file-date"/>
		</td>
	</tr>
</xsl:template>



<xsl:template match="directory">
	<tr>
		<td>
			<img src="images/local/skin/plugins/folderlisting/icons/icon_directory.png" height="16" width="16" border="0"/>  
		</td>
  		<td>
	        <a href="{$site-path}?page_id={../../page-id}&#38;folder_{../../portlet-id}={../directory-path}/{directory-name}#portlet_id_{../../portlet-id}" target="_top" >
		        <xsl:apply-templates select="directory-name" />
			</a>
		</td>
		<td></td>
		<td>
			<xsl:value-of select="directory-date"/>
		</td>
	</tr>
</xsl:template>




<xsl:template name="link-path">
	
	<xsl:param name="path"/>
    <xsl:variable name="path-before" select="substring-before($path,'/')"/>
	<xsl:variable name="path-after" select="substring-after($path,'/')"/>
	
	<xsl:variable name="link-path-before" select="substring-before(/portlet/folderlisting-portlet/directory-path,concat('/',$path-after))"/>
	
	<xsl:choose>
	      <xsl:when test="string-length($path-after)>0">
	      		
	      		<a href="{$site-path}?page_id={/portlet/page-id}&#38;folder_{/portlet/portlet-id}={$link-path-before}#portlet_id_{/portlet/portlet-id}" >
	      			<xsl:value-of select="$path-before"/>
	      		</a>
	      		<xsl:text>/</xsl:text>
				<xsl:call-template name="link-path">
					<xsl:with-param name="path" select="$path-after"/>
				</xsl:call-template>
		 </xsl:when>
	      <xsl:when test="string-length($path-after)=0">
	      	<xsl:value-of select="$path"/>
	      </xsl:when>
	 </xsl:choose>

</xsl:template>


</xsl:stylesheet>

