<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="site-path" select="site-path" />
<xsl:variable name="portlet-id" select="portlet/portlet-id" />

<xsl:template match="portlet">
	<div class="portlet-background-colored" >
        <xsl:if test="not(string(display-portlet-title)='1')">
			<h3 class="portlet-background-colored-header">
				<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
			</h3>
        </xsl:if>
		<div class="portlet-background-colored-content" >	
		     <ul>   
         	       <xsl:apply-templates select="document-list-portlet/document" />
             </ul>  
		</div>        
	</div>
</xsl:template>


<xsl:template match="document">      
    <li style="list-style:none;"> 
        <xsl:if test="not(string(document-xml-content)='null')">
             <table width="90%" summary="document"> 
                 <tr>
                     <td  width="65%">   
                         <a href="{$site-path}?document_id={document-id}&#38;portlet_id={$portlet-id}" target="_top">      
                             <xsl:value-of select="document-xml-content/dvd/dvd-title" />
                         </a>
                     </td>
                     <td  width="35%">
                         <xsl:apply-templates select="document-xml-content/dvd/dvd-cover/file-resource" />
                     </td>
                </tr>    
             </table>
        </xsl:if>
    </li>
</xsl:template>              
	

<xsl:template match="file-resource">
	<xsl:choose>
		<xsl:when test="(resource-content-type='image/jpeg' or resource-content-type='image/jpg' or  resource-content-type='image/pjpeg' or resource-content-type='image/gif' or resource-content-type='image/png')" >
			<img src="document?id={resource-document-id}&amp;id_attribute={resource-attribute-id}" align="right" width="40" height="40"/>
		</xsl:when>
		<xsl:otherwise>
             <a href="document?id={resource-document-id}&amp;id_attribute={resource-attribute-id}"> 
			   <img src="images/admin/skin/plugins/document/filetypes/file.png" border="0" align="right" width="40" height="40"/>
             </a>
		</xsl:otherwise>        
	</xsl:choose>
</xsl:template>

</xsl:stylesheet>

