<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html" indent="yes"/>

<xsl:template match="portlet">
    <xsl:if test="not(string(display-portlet-title)='1')">
	<h3 class="portlet-header">
	    <xsl:value-of disable-output-escaping="yes" select="portlet-name" />
	</h3>
    </xsl:if>
    <div>
       <div class="portlet-content">
          <xsl:apply-templates select="html-portlet" />
       </div>
    </div>
</xsl:template>

<xsl:template match="html-portlet">
         <xsl:apply-templates select="html-portlet-content" />
</xsl:template>

<xsl:template match="html-portlet-content">
            <xsl:value-of disable-output-escaping="yes" select="." />
</xsl:template>

</xsl:stylesheet>