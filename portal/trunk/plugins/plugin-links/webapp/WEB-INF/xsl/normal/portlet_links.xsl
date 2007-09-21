<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="portlet">
	<div class="portlet">
        <xsl:if test="not(string(display-portlet-title)='1')">
			<h3 class="portlet-header">
				<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
			</h3>
        </xsl:if>

		<div class="portlet-content">
			<xsl:apply-templates select="links-portlet" />
		</div>
	</div>
</xsl:template>


<xsl:template match="links-portlet">
	<ul>
		<xsl:apply-templates select="link" />
    </ul>
</xsl:template>


<xsl:template match="link">
    <li>
		<p>
			<b>
		    	<a href="{link-url}" target="_blank">
        			<xsl:apply-templates select="link-name" />
				</a>
			</b>
           <xsl:if test="normalize-space(link-description)" >
        	   <br />
		       <small>
    		       <xsl:value-of select="link-description" />
	           </small>
           </xsl:if>
        </p>
	</li>
</xsl:template>

</xsl:stylesheet>
