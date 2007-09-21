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
            <a href="{$site-path}?document_id={document-id}&#38;portlet_id={$portlet-id}" target="_top">      
                <xsl:apply-templates select="document-xml-content/image/image-file/file-resource" />
            </a>
            <br />           
                <xsl:value-of disable-output-escaping="yes" select="document-xml-content/image/image-description" />
            <br />
                <xsl:value-of disable-output-escaping="yes" select="document-xml-content/image/image-credits" />
            <br />
        </xsl:if>
    </li>        
</xsl:template>              
	

<xsl:template match="file-resource">
	<xsl:choose>
		<xsl:when test="(resource-content-type='image/jpeg' or resource-content-type='image/jpg' or  resource-content-type='image/pjpeg' or resource-content-type='image/gif' or resource-content-type='image/png')" >
			<img src="document?id={resource-document-id}&amp;id_attribute={resource-attribute-id}" alt="" border="0" width="120" height="120"/>
		</xsl:when>
		<xsl:otherwise>
             <a href="document?id={resource-document-id}&amp;id_attribute={resource-attribute-id}"> 
			   <img src="images/admin/skin/plugins/document/filetypes/file.png" border="0" alt="" width="120" height="120"/>
             </a>
		</xsl:otherwise>        
	</xsl:choose>
</xsl:template>

</xsl:stylesheet>
