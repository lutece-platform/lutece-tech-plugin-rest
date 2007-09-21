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
<xsl:if test="not(string(document-xml-content)='null')">
    <li>
        <a href="{$site-path}?document_id={document-id}&amp;portlet_id={$portlet-id}" target="_top">      
             <xsl:value-of select="document-xml-content/pdf/document-title" />   
        </a>
		<br />
		<xsl:value-of select="document-xml-content/pdf/document-summary" />   
	
    </li>
</xsl:if>
</xsl:template>              


</xsl:stylesheet>

